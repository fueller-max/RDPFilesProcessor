import Schedulers.RDPFileConcurrentProcessCtr;

public class Main {
    public static void main(String[] args) throws Exception {

        final String srcPathToFiles = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\data_files\\rawfiles\\";

        RDPFileConcurrentProcessCtr fileProcessingController
                = new RDPFileConcurrentProcessCtr(srcPathToFiles, 20);
        fileProcessingController.startControl();

//        SourceDirChecker srcDirChecker = new SourceDirChecker(srcPathToFiles);
//        srcDirChecker.trackTheFileDirectory();                                //Start tracking files in directory
//        Set<String> listOfFiles = srcDirChecker.getFilesToBeRead();           //Get list of files to be read
//        Thread.sleep(10L * 1000L);
//        for (String file : listOfFiles){
//            Stream<String> fileRDPContent = RDPFileDataReader
//                                          .readCompleteDataFromFile(srcPathToFiles + file);
//            fileRDPContent.forEach(System.out::println);
//        }


    }
}
