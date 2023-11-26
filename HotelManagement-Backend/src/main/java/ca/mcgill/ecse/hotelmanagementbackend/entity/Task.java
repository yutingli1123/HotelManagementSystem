package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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
