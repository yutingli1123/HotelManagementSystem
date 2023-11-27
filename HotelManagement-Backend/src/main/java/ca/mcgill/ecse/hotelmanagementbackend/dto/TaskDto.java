package ca.mcgill.ecse.hotelmanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfTheWeek;
    private String taskName;
    private String taskDescription;
}
