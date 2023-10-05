package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue
    private Long id;
    private String openTime;
    private String closeTime;

    public Hotel(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
