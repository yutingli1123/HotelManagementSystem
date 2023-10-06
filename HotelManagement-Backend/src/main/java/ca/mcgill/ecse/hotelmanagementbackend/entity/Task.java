package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private Date startDate;
    private Date endDate;
    private String taskName;
    private String taskDescription;
    @ManyToOne
    private TimeTable timeTable;

    public Task(Date startDate, Date endDate, String taskName, String taskDescription) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }
}
