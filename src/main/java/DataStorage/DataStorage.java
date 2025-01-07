package DataStorage;

import FileDataParser.RDPFileLineData.RDPFileLineData;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DataStorage {
    private final Map<String, ArrayList<RDPFileLineData>>  scannerDatabase =
            new TreeMap<>();

    public void storeInternally(RDPFileLineData rdpLine){
        String  id = rdpLine.getId();
        if(scannerDatabase.containsKey(id)){
           scannerDatabase.get(id).add(rdpLine);
        }else{
            scannerDatabase.put(id,new ArrayList<RDPFileLineData>());
            scannerDatabase.get(id).add(rdpLine);
        }
    }
}
