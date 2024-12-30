package dataReader.PDPFileDataReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import dataReader.DataReader;

public class RDPFileDataReader extends DataReader{

    private String srcPath;

public RDPFileDataReader(String srcPath){
    this.srcPath = srcPath;
}

public Stream<String> readCompleteDataFromFile() throws IOException{
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
