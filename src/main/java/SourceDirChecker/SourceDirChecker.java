package SourceDirChecker;

//Basic idea here is to keep track of all files in the given directory
//by the time modified. If the actual time differs from storied the file should be read again
//Checking should be made on a regular basis - for example, each 5 min

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.BlockingQueue;


public class SourceDirChecker {

    private static final Logger logger = LogManager.getLogger(SourceDirChecker.class);
    @Getter
    private final String srcDir;
    private final File srcDirectory;
    private final Map<String, BasicFileAttributes> filesAttributes = new HashMap<>();
    private final BlockingQueue<Set<String>> queue;
    private final Set<String> filesToBeRead = new TreeSet<>();

    public SourceDirChecker(String _srcDir, BlockingQueue<Set<String>> queue){
        this.srcDir = _srcDir;
        this.srcDirectory = new File(_srcDir);
        this.queue = queue;
    }

    public void trackTheFileDirectory() throws Exception {
        readDirAndCheckTheFiles();
    }


    /**
     * Should be called in timely bases to check the directory for new files
     * or modified ones
     */
    private void readDirAndCheckTheFiles() throws Exception {

        String[] subFiles = srcDirectory.list();
        for (String _file : Objects.requireNonNull(subFiles)){
            try {

                Path file = Paths.get(srcDirectory +  "/" +_file);
                BasicFileAttributes attr =
                        Files.readAttributes(file, BasicFileAttributes.class);

                //If a file has been read already -> check the attributes
                //if there is a difference -> add file for reading
                if(filesAttributes.containsKey(_file)){
                    BasicFileAttributes attrExisted = filesAttributes.get(_file);
                    if(!attrExisted.lastModifiedTime().equals(attr.lastModifiedTime())){
                            filesToBeRead.add(_file);
                            //update attr
                            filesAttributes.put(_file, attr);
                    }
                }

                //If a file has not been read and is not present in Map->
                //add to map + add to list for reading
                if(!filesAttributes.containsKey(_file)){
                    filesAttributes.put(_file, attr);
                        filesToBeRead.add(_file);
                }

            } catch (IOException e) {
                throw new Exception("Cannot read the file");
            }

        }

        //Put the produced Set in the external blocking queue to be consumed
        //The queue must be empty before we put produced into it
        //IMPORTANT! filesToBeRead must be cleared in consumer!
        if(!filesToBeRead.isEmpty()){
            logger.info("Waiting the blocking queue to be empty...");
            queue.put(filesToBeRead);
            logger.info("List of files has been added to queue");
        }else{
            logger.info("No new data provided...");
        }

    }

}
