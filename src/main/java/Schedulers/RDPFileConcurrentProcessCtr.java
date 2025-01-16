package Schedulers;

import DataStorage.DataStorage;
import FileDataParser.RDPFileLineData.RDPFileLineData;
import FileDataParser.RDPFileParser.RDPFileParser;
import RDPFileDataReader.RDPFileDataReader;
import SourceDirChecker.SourceDirChecker;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RDPFileConcurrentProcessCtr {

    private static final Logger logger = LogManager.getLogger(RDPFileConcurrentProcessCtr.class);

    private final Integer _trackInterval;  // track interval in secs

    //Scheduler for source directory checker. Normally should run approx. every 1-5 min to check for
    //new or modified files
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //Queue for the list of files to be read and parsed
    private final BlockingQueue<Set<String>> filesToBeReadQueue = new ArrayBlockingQueue<>(1);

    //Queue for the file content encapsulated into ArrayList<RDPFileLineData>
    private final BlockingQueue<ArrayList<RDPFileLineData>> parsedFiles
                                                              = new ArrayBlockingQueue<>(1000);

    //As we want to read not a complete file every time but just a new portion of data
    //we are keeping track of a position after each last read and next time
    //start from the last saved position. The first reading must start from 0
    private final Map<String, Long> trackingPosInFile =new TreeMap<>();

    //Also store the data of a file
    private final Map<String, String> dateInFile =new TreeMap<>();


    //Executor service for two threads:  file data reader and file data parser
    private static final Executor executor  = Executors.newFixedThreadPool(2);

    private final SourceDirChecker srcDirChecker;
    private final DataStorage dataStorage;

    private final Runnable srcDirectoryCheckerRunnable;
    private final Runnable RDPFileDataReaderRunnable;
    private final Runnable RDPDataStorageRunnable;

    public RDPFileConcurrentProcessCtr(String srcDir, String dstDir, Integer trackInterval){
        this._trackInterval = trackInterval;
        this.srcDirChecker =  new SourceDirChecker(srcDir, filesToBeReadQueue);
        this.dataStorage = new DataStorage(dstDir);


        //Runnable for source directory checker: executed by scheduler
        this.srcDirectoryCheckerRunnable = () -> {
            try {
                srcDirChecker.trackTheFileDirectory();
            } catch (Exception e) {
                logger.fatal(e.getMessage());
            }
        };


//Runnable for File Reader + Parser: called in a thread once list of file(s) is available
        this.RDPFileDataReaderRunnable = () -> {
            Set<String> filesToBeRead;
            try {
                while (true){
                    filesToBeRead = filesToBeReadQueue.take();
                    for(String file : filesToBeRead){
                        RDPFileParser rdpFileParser = new RDPFileParser();

                        //Check if we have already read the file before
                        //if not -> init a new entry with pos 0
                        //if yes read from the last position saved
                        //after reading completed -> update the pos
                        //Also check and save date for the file once
                        if(!trackingPosInFile.containsKey(file)){

                            trackingPosInFile.put(file,0L);

                            String date = RDPFileDataReader.readDateFromFile(srcDirChecker.getSrcDir() + file);
                            dateInFile.put(file, date);
                        }

                        Pair<Stream<String>, Long> fileWithTrackingData =RDPFileDataReader
                                .readFileFromPos(srcDirChecker.getSrcDir() + file,trackingPosInFile.get(file));

                        parsedFiles.put(rdpFileParser.parseToFormat(dateInFile.get(file),fileWithTrackingData.getValue0()));
                        trackingPosInFile.replace(file,fileWithTrackingData.getValue1());
                    }
                    filesToBeRead.clear();
                }
            } catch (InterruptedException e) {
                logger.fatal(e.getMessage());
            }
        };

        //Runnable for line data parser + storage
        this.RDPDataStorageRunnable = () ->{
            try {
                while(true){
                    ArrayList<RDPFileLineData> fileContent = parsedFiles.take();
                    for (RDPFileLineData line : fileContent){
                        dataStorage.store(line);
                }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

    }

    public void startControl(){

        //Start directory track on a certain interval using scheduler
        final ScheduledFuture<?> sourceDirCheckerHandle =
                scheduler.scheduleAtFixedRate(srcDirectoryCheckerRunnable, 0, _trackInterval, SECONDS);

          executor.execute(RDPFileDataReaderRunnable);
         executor.execute(RDPDataStorageRunnable);
    }

}
