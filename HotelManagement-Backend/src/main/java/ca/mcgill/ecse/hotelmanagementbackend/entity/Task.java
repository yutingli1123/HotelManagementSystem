package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private Long timeTableId;
    private Time startTime;
    private Time endTime;
    private String taskName;
    private String taskDescription;

    public Task(Long timeTableId, Time startTime, Time endTime, String taskName, String taskDescription) {
        this.timeTableId = timeTableId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }
}
