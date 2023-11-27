package ca.mcgill.ecse.hotelmanagementbackend.dto;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Employee;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDto {
    private Long id;
    private String timeTableName;
    private List<Long> taskIds;
    private List<Long> employeeIds;
}
