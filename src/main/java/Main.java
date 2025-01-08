import Schedulers.RDPFileConcurrentProcessCtr;

public class Main {
    public static void main(String[] args) throws Exception {

        final String srcPathToFiles = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\data_files\\rawfiles\\";
        final String dstPathToFiles = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\data_files\\output_files\\";

        RDPFileConcurrentProcessCtr fileProcessingController
                = new RDPFileConcurrentProcessCtr(srcPathToFiles, dstPathToFiles, 20);
        fileProcessingController.startControl();


    }
}
