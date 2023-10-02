package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public abstract class Admin extends User {
    @ManyToMany
    private List<Reservation> reservations;
    public Admin(String name, String username, String email, String password, Hotel hotel) {
        super(name, username, email, password, hotel);
    }
}
