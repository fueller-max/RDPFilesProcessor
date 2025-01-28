package UnitTests;

import DataStorage.DBStorage.POJO.RDPEntry;
import DataStorage.DBStorage.POJO.Scanner_ID_11;
import DataStorage.DBStorage.POJO.Scanner_ID_12;
import Utils.PostgresSQLDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Test;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDBHibernate {

    private static final Logger logger = LogManager.getLogger(TestDBHibernate.class);

    PostgresSQLDriver  postgresSQLDriver = new PostgresSQLDriver("hibernate.cfg.xml");


    @Test
    public void simpleInsertOneEntry(){

        Scanner_ID_11 scanner_id_11 = new Scanner_ID_11();

        //Prepare the data entries
        scanner_id_11.setCode("0050115298");
        scanner_id_11.setDeviceList("111111111111000000000000");
        scanner_id_11.setReadList("001111001100000000000000");
        LocalDateTime timeStamp = LocalDateTime.now();
        scanner_id_11.setTimeStamp(timeStamp);

        postgresSQLDriver.insertEntry(scanner_id_11);

    }

    @Test
    public void simpleInsertTwoEntries(){

        Scanner_ID_11 scanner_id_11 = new Scanner_ID_11();

        Scanner_ID_12 scanner_id_12 = new Scanner_ID_12();

        //Prepare the data entries id 11
        scanner_id_11.setCode("0050115298");
        scanner_id_11.setDeviceList("111111111111000000000000");
        scanner_id_11.setReadList("001111001100000000000000");
        LocalDateTime timeStamp = LocalDateTime.now();
        scanner_id_11.setTimeStamp(timeStamp);

        //Prepare the data entries id 12
        scanner_id_12.setCode("0050115298");
        scanner_id_12.setDeviceList("111111111111000000000000");
        scanner_id_12.setReadList("001111001100000000000000");
        scanner_id_12.setTimeStamp(timeStamp);

        postgresSQLDriver.insertEntry(scanner_id_11);
        postgresSQLDriver.insertEntry(scanner_id_12);

    }

    @Test
    public void insertBatchOfEntries(){

        List<RDPEntry> entries = new ArrayList<>();

        for (int i = 0; i < 10; ++i){
            RDPEntry entry = new Scanner_ID_11();
            entry.setCode("0050115298");
            entry.setDeviceList("111111111111000000000000");
            entry.setReadList("001111001100000000000000");
            LocalDateTime timeStamp = LocalDateTime.now();
            entry.setTimeStamp(timeStamp);
            entries.add(entry);
        }

        postgresSQLDriver.insertBatchOfEntries(entries);

    }


}
