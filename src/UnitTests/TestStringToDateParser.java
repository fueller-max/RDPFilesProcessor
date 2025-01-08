package UnitTests;

import Utils.StringToDateParser;
import org.junit.Test;

public class TestStringToDateParser {

@Test
    public void testParsing(){
    String dateTime = "05-12-2024 00:08:01";
    System.out.println(StringToDateParser.parseStringToDate(dateTime));
}
}
