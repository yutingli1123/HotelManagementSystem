package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "time_tables")
public class TimeTable {
    @Id
    @GeneratedValue
    private Long id;
    private String timeTableName;

    public TimeTable(String timeTableName) {
        this.timeTableName = timeTableName;
    }
}
