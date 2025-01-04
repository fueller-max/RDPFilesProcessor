package UnitTests;

import org.junit.Test;
import SourceDirChecker.SourceDirChecker;
import static org.junit.Assert.assertEquals;


import java.util.Set;



public class TestSourceDirChecker {

    final String srcPathToFile = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\src\\UnitTests\\testData";

    @Test
    public void testReadDataCountAmountOfItems() throws InterruptedException {
        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFile);
        srcDirChecker.trackTheFileDirectory();
        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();
        Thread.sleep(1L * 1000L);
        assertEquals(10, listOfFiles.size());
    }
    @Test
    public void  testReadDataAndFlush() throws InterruptedException {
        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFile);
        srcDirChecker.trackTheFileDirectory();
        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();
        Thread.sleep(1L * 1000L);
        assertEquals(10, listOfFiles.size());
        srcDirChecker.flushFilesToBeRead();
        assertEquals(0, listOfFiles.size());

    }

    @Test
    public void  testReadDataAndFlushAndReadLater() throws InterruptedException {
        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFile);
        srcDirChecker.trackTheFileDirectory();
        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();
        Thread.sleep(1L * 1000L);
        assertEquals(10, listOfFiles.size());
        srcDirChecker.flushFilesToBeRead();
        assertEquals(0, listOfFiles.size());
        Thread.sleep(12L * 1000L);
        assertEquals(0, listOfFiles.size());
    }
    @Test
    public void  testReadDataAndFlushAndModify() throws InterruptedException {
        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFile);
        srcDirChecker.trackTheFileDirectory();
        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();
        Thread.sleep(1L * 1000L);
        assertEquals(10, listOfFiles.size());
        srcDirChecker.flushFilesToBeRead();
        // Modify ONE File in 50 sec!!
        System.out.println("Modify ONE File in 50 sec!!");
        assertEquals(0, listOfFiles.size());
        Thread.sleep(50L * 1000L);
        assertEquals(1, listOfFiles.size());
    }
}
