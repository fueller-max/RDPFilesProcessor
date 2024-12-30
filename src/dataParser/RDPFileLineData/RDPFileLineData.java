package dataParser.RDPFileLineData;

import dataParser.dataFormat.DataEntryFormat;

public class RDPFileLineData extends DataEntryFormat {

    private String  date;
    private String  time;
    private Integer id;
    private boolean[] deviceList = new boolean[32];
    private boolean[] readList = new boolean[32];
    private String code;

    public RDPFileLineData(String date, String time, Integer id, boolean[] deviceList, boolean[] readList,
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

    public Integer getId() {
        return id;
    }

    public boolean[] getDeviceList() {
        return deviceList;
    }

    public boolean[] getReadList() {
        return readList;
    }

    public String getCode() {
        return code;
    }
}
