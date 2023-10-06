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
public class Hotel {
    @Id
    @GeneratedValue
    private Long id;
    private String openTime;
    private String closeTime;
    @OneToMany(mappedBy = "hotel")
    private List<User> users;

    public Hotel(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.users = new ArrayList<>();
    }
}
