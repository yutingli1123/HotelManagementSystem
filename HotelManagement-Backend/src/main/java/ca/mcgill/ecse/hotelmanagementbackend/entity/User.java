package ca.mcgill.ecse.hotelmanagementbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "hotel_user")
public abstract class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToOne
    private Hotel hotel;

    public User(String name, String username, String email, String password, Hotel hotel) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.hotel = hotel;
    }
}
