package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public class Owner extends User {
    public Owner(String name, String username, String email, String password, Hotel hotel) {
        super(name, username, email, password, hotel);
    }
}
