package FileDataParser.RDPFileParser;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import FileDataParser.RDPFileLineData.RDPFileLineData;


public class RDPFileParser {

    private static final Integer ARRAY_SIZE_NO_READ = 12;
    private static final Integer ARRAY_SIZE_GOOD_READ = 23;

    // Regex Pattern for Time
    private static final Pattern TIME_PATTERN
        = Pattern.compile("^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

    public ArrayList<RDPFileLineData> parseToFormat(Stream<String> stream) {
    /***
     * input arg: stream of lines from a file
     * output: List of DPFileLineData
    */
        ArrayList<RDPFileLineData> result = new ArrayList<>();
        final String[] date = new String[1]; // workaround for the date to be updated in lambda expression.

        stream.forEach(str -> {

            String[] strContent = str.split(";");

            //Extract the date from the file
            // (the date is the same for all entries in the file given)
            //Normally, it is the 4th line in file
            if(strContent[0].contains("Date:")){
                String[] dateStr = strContent[0].split(" ");
                if(dateStr.length > 2){
                    date[0] = dateStr[1];
                }
            }
            //in case the line has correct time format create a data entry
            if(TIME_PATTERN.matcher(strContent[0]).matches()){

                result.add(parseStringArrayToRDPFileLineData(date[0], strContent));

            }

        });
        return result;
    }

    private RDPFileLineData parseStringArrayToRDPFileLineData(String _date, String[] array){

        RDPFileLineData result = new RDPFileLineData(null, null, null, null, null, null);

        //NO Read
        if(array.length == ARRAY_SIZE_NO_READ){
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[8];
            String code = "NO READ";
            result = new RDPFileLineData(_date, time, id, deviceList, readList, code);
        }
        //Good Read
        if(array.length > ARRAY_SIZE_GOOD_READ){
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[14];
            String code = array[23];
            result = new RDPFileLineData(_date, time, id, deviceList, readList, code);
        }
        
        return result;
    }
    
}
