package RDPFileDataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RDPFileDataReader{
    private static final Logger logger = LogManager.getLogger(RDPFileDataReader.class);

    public static Stream<String> readCompleteDataFromFile(String srcPath){
        Stream<String> stream = Stream.of("no data");
        try {
            stream = Files.lines(Paths.get(srcPath));
            logger.info("File has been successfully read from path: {}", srcPath);
        
            } catch (IOException ex) {
            logger.error("File has NOT been read from path: {}", ex.getMessage());
        }

    return stream;
}

}
