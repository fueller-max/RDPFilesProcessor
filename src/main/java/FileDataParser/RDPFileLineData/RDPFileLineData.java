package FileDataParser.RDPFileLineData;

import FileDataParser.dataFormat.DataEntryFormat;

public class RDPFileLineData extends DataEntryFormat {

    private final String  date;
    private final String  time;
    private final String id;
    private final String deviceList;
    private final String  readList;
    private final String code;

    public RDPFileLineData(String date, String time, String  id, String  deviceList, String  readList,
            String code) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.deviceList = deviceList;
        this.readList = readList;
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String  getId() {
        return id;
    }

    public String  getDeviceList() {
        return deviceList;
    }

    public String  getReadList() {
        return readList;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "RDPFileLineData [date=" + date + ", time=" + time + ", id=" + id + ", deviceList=" + deviceList
                + ", readList=" + readList + ", code=" + code + "]";
    }
}
