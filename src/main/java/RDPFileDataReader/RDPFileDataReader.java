package RDPFileDataReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

public class RDPFileDataReader{
    private static final Logger logger = LogManager.getLogger(RDPFileDataReader.class);

    public static Stream<String> readCompleteDataFromFile(String pathToFile){
        Stream<String> stream = Stream.of("no data");
        try {
            stream = Files.lines(Paths.get(pathToFile));
            logger.info("File has been successfully read from path: {}", pathToFile);
        
            } catch (IOException ex) {
            logger.error("File has NOT been read from path: {}", ex.getMessage());
        }

    return stream;
    }

    public static Pair<Stream<String>, Long> readFileFromPos(String pathToFile, long pos ) {

        List<String> fileLines = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile(pathToFile, "r");){
            if (pos < 0 || pos> raf.length()) {
                throw new RuntimeException("Position is outside of the permissible range");
            }

            while (pos < raf.length()){
                fileLines.add(raf.readLine());
                pos = raf.getFilePointer();
            }

        } catch (IOException ex) {
            logger.error("A Problem occurred during random reading from file: {}", ex.getMessage());
        }
        return new Pair<>(fileLines.stream(), pos);
    }

}
