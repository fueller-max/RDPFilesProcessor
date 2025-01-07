import Schedulers.RDPFileConcurrentProcessCtr;

public class Main {
    public static void main(String[] args) throws Exception {

        final String srcPathToFiles = "C:\\Users\\Maksim\\IdeaProjects\\RDPFilesProcessor\\data_files\\rawfiles\\";

        RDPFileConcurrentProcessCtr fileProcessingController
                = new RDPFileConcurrentProcessCtr(srcPathToFiles, 20);
        fileProcessingController.startControl();


    }
}
