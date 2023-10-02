package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue
    private Long id;
    private Time openTime;
    private Time closeTime;

    public Hotel(Time openTime, Time closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
