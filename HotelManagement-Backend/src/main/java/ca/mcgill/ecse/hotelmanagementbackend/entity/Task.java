package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private Time startTime;
    private Time endTime;
    private String taskName;
    private String taskDescription;
    @ManyToOne
    private TimeTable timeTable;

    public Task(Time startTime, Time endTime, String taskName, String taskDescription) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }
}
