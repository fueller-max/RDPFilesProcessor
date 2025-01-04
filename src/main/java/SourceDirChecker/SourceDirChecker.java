package SourceDirChecker;

//Basic idea here is to keep track of all files in the given directory
//by the time modified. If the actual time differs from storied the file should be read again
//Checking should be made on a regular basis - for example, each 5 min

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceDirChecker {

    private final Integer TRACK_INTERVAL = 10;  // track interval in secs.
    private final File srcDirectory;
    private final Map<String, BasicFileAttributes> filesAttributes = new HashMap<>();
    private final Set<String> filesToBeRead = new TreeSet<>();
    private volatile boolean stoppedTracking = false;
    private final Object mutex = new Object();
    public SourceDirChecker(String _srcDir){
        srcDirectory = new File(_srcDir);
    }


     //Start tracking endless by certain interval.
    // it works but not the best implementation -> better to rewrite later
    public void trackTheFileDirectory(){

        Runnable trackingThread = () -> {
            while(!stoppedTracking){
                System.out.println("Start checking the directory");
                try {
                    readDirAndCheckTheFiles();
                    System.out.println("Checking the directory completed");
                    Thread.sleep(TRACK_INTERVAL * 1000L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        };

        Thread thread = new Thread(trackingThread);
        thread.start();
    }

    public void stopTracking(){
        stoppedTracking = true;
    }

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
                        synchronized (mutex){
                            filesToBeRead.add(_file);
                        }

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

    }

public Set<String> getFilesToBeRead(){
        synchronized (mutex){
            return filesToBeRead;
        }
}

public void flushFilesToBeRead(){
        synchronized (mutex){
            filesToBeRead.clear();
        }
    }



}
