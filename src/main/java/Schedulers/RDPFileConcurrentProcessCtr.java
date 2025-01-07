package Schedulers;

import RDPFileDataReader.RDPFileDataReader;
import SourceDirChecker.SourceDirChecker;

import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RDPFileConcurrentProcessCtr {

    private static final Logger logger = LogManager.getLogger(RDPFileConcurrentProcessCtr.class);

    private  Integer _trackInterval = 10;  // track interval in secs

    //Scheduler for source directory checker. Normally should run approx. every 1-5 min to check for
    //new or modified files
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //Queue for the list of files to be read and parsed
    private final BlockingQueue<Set<String>> filesToBeReadQueue = new ArrayBlockingQueue<>(1);
    //Queue for the file content encapsulated into Stream<String>
    private final BlockingQueue<Stream<String>> linesFromFiles = new ArrayBlockingQueue<>(10000);

    //Executor service for two threads:  file data reader and file data parser
    private static final Executor executor  = Executors.newFixedThreadPool(2);

    private final SourceDirChecker srcDirChecker;

    private final Runnable srcDirectoryCheckerRunnable;
    private final Runnable RDPFileDataReaderRunnable;
    //private final Runnable RDPFileDataParserRunnable;

    public RDPFileConcurrentProcessCtr(String srcDir, Integer trackInterval){
        this._trackInterval = trackInterval;
        this.srcDirChecker =  new SourceDirChecker(srcDir, filesToBeReadQueue);

        this.srcDirectoryCheckerRunnable = ()->{
            try {
                srcDirChecker.trackTheFileDirectory();
            } catch (Exception e) {
                logger.fatal(e.getMessage());
            }
        };

        this.RDPFileDataReaderRunnable = ()->{
            Set<String> filesToBeRead;
            try {
                while (true){
                    filesToBeRead = filesToBeReadQueue.take();
                    for(String file : filesToBeRead){
                        linesFromFiles.put(RDPFileDataReader
                                .readCompleteDataFromFile(srcDirChecker.getSrcDir() + file));
                    }
                    Stream<String> lines = linesFromFiles.take();

                    lines.forEach(System.out::println);

                    filesToBeRead.clear();
                }
            } catch (InterruptedException e) {
                logger.fatal(e.getMessage());
            }
        };
    }

    public void startControl(){

        //Start directory track on a certain interval using scheduler
        final ScheduledFuture<?> sourceDirCheckerHandle =
                scheduler.scheduleAtFixedRate(srcDirectoryCheckerRunnable, 0, _trackInterval, SECONDS);

        executor.execute(RDPFileDataReaderRunnable);
    }




}
