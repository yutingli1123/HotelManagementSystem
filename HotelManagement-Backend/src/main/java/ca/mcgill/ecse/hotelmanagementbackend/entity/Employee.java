package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public class Employee extends Admin {
    private Integer salary;
    @ManyToOne
    private TimeTable timeTable;

    public Employee(String name, String username, String email, String password, Integer salary) {
        super(name, username, email, password);
        this.salary = salary;
    }
}
