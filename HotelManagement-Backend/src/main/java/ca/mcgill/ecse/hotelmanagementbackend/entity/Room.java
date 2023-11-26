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
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();
    @OneToMany(mappedBy = "room")
    private List<Request> requests = new ArrayList<>();

    public Room(RoomType type, Integer fee) {
        this.type = type;
        this.fee = fee;
    }
}
