package ca.mcgill.ecse.hotelmanagementbackend.dto;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Admin;
import ca.mcgill.ecse.hotelmanagementbackend.entity.TimeTable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
}
