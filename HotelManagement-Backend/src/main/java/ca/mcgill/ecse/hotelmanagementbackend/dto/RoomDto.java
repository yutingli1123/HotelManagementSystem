package ca.mcgill.ecse.hotelmanagementbackend.dto;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Request;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private RoomType type;
    private Integer fee;
}
