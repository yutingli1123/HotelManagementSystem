package ca.mcgill.ecse.hotelmanagementbackend.dto;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomBatch {
    private Integer number;
    private RoomType roomType;
    private Integer fee;
}
