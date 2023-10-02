package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class TimeTable {
    @Id
    @GeneratedValue
    private Long id;
    private String timeTableName;
    @OneToMany
    private List<Task> tasks;

    public TimeTable(String timeTableName) {
        this.timeTableName = timeTableName;
    }
}
