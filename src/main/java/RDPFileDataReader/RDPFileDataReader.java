package RDPFileDataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RDPFileDataReader{

public static Stream<String> readCompleteDataFromFile(String srcPath) throws IOException{
    Stream<String> stream = Stream.of("no data");

    try {
        stream = Files.lines(Paths.get(srcPath));
        System.out.println("File has been successfully read");
        
    } catch (IOException ex) {
        System.err.println("File has NOT been read from path: " + ex.getMessage() );
    }

    return  stream;
}

}
