package DataStorage.DBStorage.POJO;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Setter
@Getter
@NoArgsConstructor
public abstract class RDPEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime timeStamp;
    private String code;
    private String deviceList;
    private String readList;
}

