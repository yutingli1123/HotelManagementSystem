package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class TimeTable {
    @Id
    @GeneratedValue
    private Long id;
    private String timeTableName;
    @OneToMany(mappedBy = "timeTable")
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "timeTable")
    private List<Employee> employees = new ArrayList<>();

    public TimeTable(String timeTableName) {
        this.timeTableName = timeTableName;
    }
}
