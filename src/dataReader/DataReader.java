package dataReader;

import java.io.IOException;
import java.util.stream.Stream;

abstract public class DataReader {

    abstract public Stream<String> readCompleteDataFromFile() throws IOException;

}
