package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class StringToDateParser {

    public static LocalDateTime parseStringToDate(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static Integer compareTimeStamps(LocalDateTime timestamp1,LocalDateTime timestamp2 ){
        if (timestamp1.isEqual(timestamp2)){
            return 0;
        }
        return timestamp1.isAfter(timestamp2) ? 1 : -1;
    }
}
