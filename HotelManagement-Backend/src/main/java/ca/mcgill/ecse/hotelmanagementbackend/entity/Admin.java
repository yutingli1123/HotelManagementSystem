package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public abstract class Admin extends User {
    public Admin(String name, String username, String email, String password, Hotel hotel) {
        super(name, username, email, password, hotel);
    }
}
