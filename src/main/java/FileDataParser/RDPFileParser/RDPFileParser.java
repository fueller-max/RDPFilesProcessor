package FileDataParser.RDPFileParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import DataStorage.DataStorage;
import FileDataParser.RDPFileLineData.RDPFileLineData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RDPFileParser {

    private static final Logger logger = LogManager.getLogger(RDPFileParser.class);

    private static final Integer ARRAY_SIZE_NO_READ = 12;
    private static final Integer ARRAY_SIZE_GOOD_READ = 23;

    // Regex Pattern for Time
    private static final Pattern TIME_PATTERN
        = Pattern.compile("^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

    public ArrayList<RDPFileLineData> parseToFormat(String date, Stream<String> stream) {
    /***
     * input arg: stream of lines from a file
     * output: List of DPFileLineData
    */
        ArrayList<RDPFileLineData> result = new ArrayList<>();

        stream.forEach(str -> {

            String[] strContent = str.split(";");

            //in case the line has correct time format create a data entry
            if(TIME_PATTERN.matcher(strContent[0]).matches()){

                result.add(parseStringArrayToRDPFileLineData(date, strContent));

            }else{
                //logger.error("The line has invalid format: {}", Arrays.toString(strContent));
            }

        });
        return result;
    }

    private RDPFileLineData parseStringArrayToRDPFileLineData(String _date, String[] array){

        //NO Read
        if(array.length == ARRAY_SIZE_NO_READ){
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[8];
            String code = "NO READ";
            return new RDPFileLineData(_date, time, id, deviceList, readList, code);
        }
        //Good Read
        if(array.length > ARRAY_SIZE_GOOD_READ){
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[14];
            String code = array[23];
            return new RDPFileLineData(_date, time, id, deviceList, readList, code);
        }
        
        return new RDPFileLineData("00-00-0000", "00:00:00", null, null, null, null);
    }
    
}


