package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private Date checkInDate;
    private Date checkOutDate;
    @ManyToMany
    private List<Room> rooms;
    @ManyToOne
    private Customer customer;


    public Reservation(Date checkInDate, Date checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.rooms = new ArrayList<>();
    }
}
