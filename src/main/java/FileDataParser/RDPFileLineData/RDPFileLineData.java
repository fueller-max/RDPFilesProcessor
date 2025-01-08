package FileDataParser.RDPFileLineData;

import FileDataParser.dataFormat.DataEntryFormat;
import Utils.StringToDateParser;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
public class RDPFileLineData extends DataEntryFormat implements Comparable<RDPFileLineData> {

    private final String date;
    private final String time;
    private final String id;
    private final String deviceList;
    private final String readList;
    private final String code;
    private final LocalDateTime timeStamp;

    public RDPFileLineData(String date, String time,
                           String  id, String  deviceList,
                           String  readList, String code) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.deviceList = deviceList;
        this.readList = readList;
        this.code = code;
        this.timeStamp = StringToDateParser.parseStringToDate(date + " " + time);
    }

    @Override
    public String toString() {
        return "date=" + date + ", time=" + time + ", id=" + id + ", deviceList=" + deviceList
                + ", readList=" + readList + ", code=" + code;
    }

    @Override
    public int compareTo(@NotNull RDPFileLineData other) {
        return StringToDateParser.compareTimeStamps(this.timeStamp, other.getTimeStamp());
    }
}
