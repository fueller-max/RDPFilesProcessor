import ConfigReader.Config;
import ConfigReader.ConfigReader;

import Schedulers.RDPFileConcurrentProcessCtr;

public class Main {
    public static void main(String[] args) throws Exception {

        Config config = ConfigReader.readConfig();
        String srcPathToFiles = config.getSrcPathToFiles();
        String dstPathToFiles = config.getDstPathToFiles();
        Integer trackInterval = config.getUpdateInterval();



//        RDPFileConcurrentProcessCtr fileProcessingController
//                = new RDPFileConcurrentProcessCtr(srcPathToFiles, dstPathToFiles, trackInterval);
//        fileProcessingController.startControl();

    }
}
