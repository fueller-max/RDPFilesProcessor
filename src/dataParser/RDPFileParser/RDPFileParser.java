package dataParser.RDPFileParser;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import dataParser.RDPFileLineData.RDPFileLineData;


public class RDPFileParser {

    // Regex Pattern for Time
    private static final Pattern TIME_PATTERN
        = Pattern.compile("^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

    // Regex Pattern for Time
    private static final Pattern DATE_PATTERN
    = Pattern.compile("\\d{2}\\-\\d{2}\\-\\d{4}");

    
    public ArrayList<RDPFileLineData> parseToFormat(Stream<String> stream) {
        ArrayList<RDPFileLineData> result = new ArrayList<>();
        final String[] date = new String[1]; // workaround for the date to be updated in lambda expression.

        stream.forEach(str -> {

            String[] strContent = str.split(";");
            
            //Extract date from the file (it is common fo all entries..)
            if(strContent[0].contains("Date:")){
                String[] dateStr = strContent[0].split(" ");
                if(dateStr.length > 2){
                    date[0] = dateStr[1];
                }
            }
        
            if(TIME_PATTERN.matcher(strContent[0]).matches()){
            
                result.add(parseStringArrayToRDPFileLineData(date[0], strContent));
                
            }

        });
        return result;
    }

    private RDPFileLineData parseStringArrayToRDPFileLineData(String _date, String[] array){

        RDPFileLineData result = new RDPFileLineData(null, null, null, null, null, null);

        //NO Read
        if(array.length == 12){
            String date = _date;
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[8];
            String code = "NO READ";
            result = new RDPFileLineData(date, time, id, deviceList, readList, code);
        }
        //Good Read
        if(array.length > 23){
            String date =  _date;
            String time = array[0];
            String id = array[1];
            String deviceList = array[7];
            String readList = array[14];
            String code = array[23];
            result = new RDPFileLineData(date, time, id, deviceList, readList, code);
        }
        
        return result;
    }
    
}
