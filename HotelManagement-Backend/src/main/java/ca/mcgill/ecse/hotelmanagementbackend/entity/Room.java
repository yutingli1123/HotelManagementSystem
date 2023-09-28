package ca.mcgill.ecse.hotelmanagementbackend.entity;

import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    private RoomType type;
    private Integer fee;
    private Integer totalNumber;

    public Room(RoomType type, Integer fee, Integer totalNumber) {
        this.type = type;
        this.fee = fee;
        this.totalNumber = totalNumber;
    }
}
