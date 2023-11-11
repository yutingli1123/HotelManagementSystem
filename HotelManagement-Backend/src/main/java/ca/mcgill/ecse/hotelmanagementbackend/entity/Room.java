package ca.mcgill.ecse.hotelmanagementbackend.entity;

import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Amenity;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private Integer fee;
    @ElementCollection
    private List<Amenity> amenities = new ArrayList<>();
    private boolean isAvailable = true;
    @ManyToOne
    private Hotel hotel;
    @ManyToMany(mappedBy = "rooms")
    private List<Reservation> reservations = new ArrayList<>();

    public Room(RoomType type, Integer fee) {
        this.type = type;
        this.fee = fee;
    }
}
