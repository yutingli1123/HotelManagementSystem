package ca.mcgill.ecse.hotelmanagementbackend.entity;

import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

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
