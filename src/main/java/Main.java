import FileDataParser.RDPFileLineData.RDPFileLineData;
import RDPFileDataReader.RDPFileDataReader;
import SourceDirChecker.SourceDirChecker;

import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {

        final String srcPathToFiles = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\data_files\\rawfiles\\";

        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFiles);
        srcDirChecker.trackTheFileDirectory();                                //Start tracking files in directory
        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();           //Get list of files to be read
        Thread.sleep(10L * 1000L);
        for (String file : listOfFiles){
            Stream<String> fileRDPContent = RDPFileDataReader
                                          .readCompleteDataFromFile(srcPathToFiles + file);
            fileRDPContent.forEach(System.out::println);
        }


    }
}
