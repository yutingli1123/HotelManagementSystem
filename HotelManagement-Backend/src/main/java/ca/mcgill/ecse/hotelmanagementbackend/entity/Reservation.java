package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    @ManyToMany
    private List<Room> rooms;
    @ManyToOne
    private Customer customer;
    @ManyToMany
    private List<Admin> admins;


    public Reservation(Long guestId, Date date) {
        this.date = date;
    }
}
