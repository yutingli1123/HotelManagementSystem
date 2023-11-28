package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
public class Customer extends User {
    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservationsForCustomer = new ArrayList<>();

    public Customer(String name, String username, String email, String password) {
        super(name, username, email, password);
    }
}
