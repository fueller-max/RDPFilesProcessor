package UnitTests;

import RDPFileDataReader.RDPFileDataReader;
import org.javatuples.Pair;
import org.junit.Test;

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
}
