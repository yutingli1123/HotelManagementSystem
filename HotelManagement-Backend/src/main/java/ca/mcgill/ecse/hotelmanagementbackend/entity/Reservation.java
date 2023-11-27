package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private Date checkInDate;
    private Date checkOutDate;
    private Integer totalFee;
    @ManyToOne
    private Room room;
    @ManyToOne
    private Customer customer;


    public Reservation(Date checkInDate, Date checkOutDate, Room room, Integer totalFee) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
        this.totalFee = totalFee;
    }
}
