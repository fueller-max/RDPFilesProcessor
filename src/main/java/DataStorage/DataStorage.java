package DataStorage;

import DataStorage.DBStorage.POJO.RDPEntry;
import FileDataParser.RDPFileLineData.RDPFileLineData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


import Utils.PostgresSQLDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DataStorage {

    private static final Logger logger = LogManager.getLogger(DataStorage.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Runnable storeOnDiskRunnable =this::saveCompleteInternalStorageOnDisk;

    private final String dstToOutputFiles;

    private final Object mutex = new Object();

    private final boolean storeToDataBase;
    private final DBPersistance dbPersistance;


    private final Map<String, ArrayList<RDPFileLineData>>  scannerDatabase =
            new TreeMap<>();

    public DataStorage(String dstToOutputFiles, boolean writeToDB, Optional<String> dbCfg) {
        this.dstToOutputFiles = dstToOutputFiles;
        this.storeToDataBase = writeToDB;

        String databaseCfg = dbCfg.orElse(null);
        dbPersistance = new DBPersistance(databaseCfg);
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

        public static void writeBatchDataIntoFile(ArrayList<RDPFileLineData> batch,String dst, boolean append){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(dst,append))){
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


    private static class DBPersistance{

        private final PostgresSQLDriver postgresSQLDriver;

       public DBPersistance(String cfg){
        if(cfg != null){
            postgresSQLDriver = new PostgresSQLDriver(cfg);
        }else{
            logger.error("No cfg for data base provided!");
            throw new IllegalArgumentException("No cfg for data base provided!");
        }

       }

       public void storeBatchInDB(ArrayList<RDPFileLineData> entries) throws ClassNotFoundException,
               InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

           List<RDPEntry> RDPEntries = new ArrayList<>();

           for(var entry : entries){
               RDPEntry rdpEntry= getRDPEntryForDB(entry);
               rdpEntry.setCode(entry.getCode());
               rdpEntry.setDeviceList("not set yet");
               rdpEntry.setReadList("not set yet");
               rdpEntry.setTimeStamp(entry.getTimeStamp());
               RDPEntries.add(rdpEntry);
               //logger.info("RDP Entry for DB storage: {}", rdpEntry);
           }

           postgresSQLDriver.insertBatchOfEntries(RDPEntries);

       }

       private RDPEntry getRDPEntryForDB(RDPFileLineData rdpLine) throws ClassNotFoundException,
               NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
           String id = rdpLine.getId();
           Class<?> scanner_id_x = Class.forName("DataStorage.DBStorage.POJO.Scanner_ID_" + id);
           return (RDPEntry) scanner_id_x.getDeclaredConstructor().newInstance();
       }
    }

    public void store(RDPFileLineData rdpLine){
        storeInternally(rdpLine);
    }

    public void saveCompleteInternalStorageOnDisk() {
        synchronized (mutex) {
            for (String id : scannerDatabase.keySet()) {
                String dstFile = dstToOutputFiles + id + ".txt";
                CreateFile.checkAndCreateNewFile(dstFile);
                WriteDataToFile.writeBatchDataIntoFile(scannerDatabase.get(id), dstFile, true);
                if (storeToDataBase) {
                    try {
                        dbPersistance.storeBatchInDB(scannerDatabase.get(id));
                    } catch (Exception e) {
                        logger.fatal("Problem with DB storage: {}", e.getMessage());
                    }
                }
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
