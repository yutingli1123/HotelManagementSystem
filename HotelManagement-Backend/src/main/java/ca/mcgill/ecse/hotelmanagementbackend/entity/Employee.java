package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long accountId;
    private Integer salary;
    private Long timeTableId;

    public Employee(String name, Long accountId, Integer salary, Long timeTableId) {
        this.name = name;
        this.accountId = accountId;
        this.salary = salary;
        this.timeTableId = timeTableId;
    }
}
