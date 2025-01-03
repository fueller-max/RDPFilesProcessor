import java.util.ArrayList;
import java.util.stream.Stream;

import dataParser.RDPFileParser.RDPFileParser;
import dataReader.PDPFileDataReader.RDPFileDataReader;
import dataParser.RDPFileLineData.RDPFileLineData;
public class Main {
    public static void main(String[] args) throws Exception {

        final String srcPathToFile = "C://RDP//raw_files//24113017rw_test.dat";

        RDPFileDataReader rdpFileDataReader = new RDPFileDataReader(srcPathToFile);
        RDPFileParser rdpFileParser  = new RDPFileParser(); 

        Stream<String> lines = rdpFileDataReader.readCompleteDataFromFile();

        ArrayList<RDPFileLineData> dataParsed = rdpFileParser.parseToFormat(lines);
        
        //DEBUG output : Lines from file
        for(RDPFileLineData elm : dataParsed){
            System.out.println(elm.toString());
        }

 
    }
}
