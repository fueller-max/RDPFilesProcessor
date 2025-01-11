package ConfigReader;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Config {

    String srcPathToFiles;
    String dstPathToFiles;
    Integer updateInterval;
}
