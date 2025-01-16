package UnitTests;

import RDPFileDataReader.RDPFileDataReader;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestFileDataReader {

    @Test
    public void readFileCompleteFromInitialPos(){
        String src = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\src\\UnitTests\\testData\\24120511rw.dat";
        Pair<Stream<String>, Long> file = RDPFileDataReader.readFileFromPos(src, 0);
        System.out.println(file.getValue1());
        assertEquals(1720, file.getValue0().count());
    }

    @Test
    public void readFileCompleteFromRandomPos(){
        String src = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\src\\UnitTests\\testData\\24120512rw.dat";
        Pair<Stream<String>, Long> file = RDPFileDataReader.readFileFromPos(src, 1697);
        System.out.println(file.getValue1());
        //assertEquals(1720, file.getValue0().count());
    }

    @Test
    public void readDate() throws FileNotFoundException {
        String src = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\src\\UnitTests\\testData\\24120512rw.dat";
        String date = RDPFileDataReader.readDateFromFile(src);
        System.out.println(date);
    }
}
