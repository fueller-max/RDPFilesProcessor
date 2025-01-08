package UnitTests;

import FileDataParser.RDPFileLineData.RDPFileLineData;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestRDPFileLineData {


    @Test
    public void compareTimeStampsEqual(){
        RDPFileLineData entry1 =
                new RDPFileLineData("05-12-2024", "00:08:01", null, null, null, null);
        RDPFileLineData entry2 =
                new RDPFileLineData("05-12-2024", "00:08:01", null, null, null, null);

        assertEquals(0, entry1.compareTo(entry2));
    }

    @Test
    public void compareTimeStampsBefore(){
        RDPFileLineData entry1 =
                new RDPFileLineData("05-12-2024", "00:08:01", null, null, null, null);
        RDPFileLineData entry2 =
                new RDPFileLineData("05-12-2024", "00:08:05", null, null, null, null);

        assertEquals(-1, entry1.compareTo(entry2));
    }

    @Test
    public void compareTimeStampsAfter(){
        RDPFileLineData entry1 =
                new RDPFileLineData("05-12-2024", "00:08:11", null, null, null, null);
        RDPFileLineData entry2 =
                new RDPFileLineData("05-12-2024", "00:08:05", null, null, null, null);

        assertEquals(1, entry1.compareTo(entry2));
    }
}
