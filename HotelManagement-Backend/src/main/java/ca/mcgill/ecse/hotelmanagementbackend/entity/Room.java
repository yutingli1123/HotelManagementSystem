package ca.mcgill.ecse.hotelmanagementbackend.entity;

import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Amenity;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private List<Amenity> amenities;
    private boolean isAvailable;
    @ManyToOne
    private Hotel hotel;

    public Room(RoomType type, Integer fee) {
        this.type = type;
        this.fee = fee;
        this.isAvailable = true;
    }
}
