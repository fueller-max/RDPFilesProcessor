package DataStorage;

import FileDataParser.RDPFileLineData.RDPFileLineData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DataStorage {

    private static final Logger logger = LogManager.getLogger(DataStorage.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Runnable storeOnDiskRunnable =this::saveCompleteInternalStorageOnDisk;

    private final String dstToOutputFiles;

    private final Object mutex = new Object();

    private final Map<String, ArrayList<RDPFileLineData>>  scannerDatabase =
            new TreeMap<>();

    public DataStorage(String dstToOutputFiles) {
        this.dstToOutputFiles = dstToOutputFiles;

    }

    private static class CreateFile {
        public static void checkAndCreateNewFile(String src) {
            try {
                File outputFile = new File(src);
                if (outputFile.createNewFile()) {
                    logger.info("File created, adding data: {}", outputFile.getName());
                } else {
                    logger.info("Adding data to the file: {}", outputFile.getName());
                }
            } catch (IOException e) {
                logger.error("An error occurred: {}",e.getMessage());
            }
        }
    }

    private static class WriteDataToFile {

        public static void writeBatchDataIntoFile(ArrayList<RDPFileLineData> batch,String src, boolean append){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(src,append))){
                for (RDPFileLineData rdpLine : batch){
                    writer.write(rdpLine.toString());
                    writer.newLine();
                }
            }
            catch(IOException e){
               logger.error("Error during handling the output file {}", e.getMessage());
            }
        }
    }

    public void store(RDPFileLineData rdpLine){
        storeInternally(rdpLine);
    }

    public void saveCompleteInternalStorageOnDisk(){
        synchronized (mutex) {
            for(String id : scannerDatabase.keySet()){
                String dstFile = dstToOutputFiles + id + ".txt";
                CreateFile.checkAndCreateNewFile(dstFile);
                WriteDataToFile.writeBatchDataIntoFile(scannerDatabase.get(id),dstFile,true);
            }
            scannerDatabase.clear();
        }
    }

    private void storeInternally(RDPFileLineData rdpLine){
        String  id = rdpLine.getId();
        synchronized (mutex){
            if(scannerDatabase.containsKey(id)){
                scannerDatabase.get(id).add(rdpLine);
            }else{
                scannerDatabase.put(id, new ArrayList<>());
                scannerDatabase.get(id).add(rdpLine);
            }
        }
    }

    final ScheduledFuture<?> saveOnDiskHandler =
            scheduler.scheduleAtFixedRate(storeOnDiskRunnable, 10, 10, SECONDS);

}
