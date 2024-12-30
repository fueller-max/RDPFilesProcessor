package dataParser;

import java.util.ArrayList;
import java.util.stream.Stream;

import dataParser.dataFormat.DataEntryFormat;

public abstract class DataParserToFormat {

   abstract public ArrayList<DataEntryFormat> parseToFormat(Stream<String> stream);
}
