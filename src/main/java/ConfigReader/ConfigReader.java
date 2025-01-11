package ConfigReader;

import java.io.FileReader;
import java.io.IOException;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    public static Config  readConfig(){

        Config config = new Config();

        try(FileReader reader = new FileReader("config.json")) {

            JsonReader jsonReader = Json.createReader(reader);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();



            // Access nested objects
            JsonObject file_storage = jsonObject.getJsonObject("file_storage");
            config.setSrcPathToFiles(file_storage.getString("srcPathToFiles"));
            config.setDstPathToFiles(file_storage.getString("dstPathToFiles"));
            config.setUpdateInterval(Integer.valueOf(file_storage.getString("updateInterval")));

        } catch ( IOException e) {
            logger.error("Error while reading config: {}", e.getMessage());  ;
        }

        return config;
    }


}
