package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfTheWeek;
    private String taskName;
    private String taskDescription;
    @ManyToOne
    private TimeTable timeTable;

    public Task(LocalTime startTime, LocalTime endTime, String dayOfTheWeek, String taskName, String taskDescription) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfTheWeek = dayOfTheWeek;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }
}
