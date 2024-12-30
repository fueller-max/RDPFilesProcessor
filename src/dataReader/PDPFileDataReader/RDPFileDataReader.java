package dataReader.PDPFileDataReader;

import dataReader.DataReader;

public class RDPFileDataReader extends DataReader{

    private String srcPath;

  public RDPFileDataReader(String srcPath){
    this.srcPath = srcPath;
  }  

  public  String[] readCompleteDataFromFile(){
        
        return new String[0];
    }

}
