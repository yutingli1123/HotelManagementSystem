package ca.mcgill.ecse.hotelmanagementbackend.dto;

import ca.mcgill.ecse.hotelmanagementbackend.enumeration.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationDto {
    private Long id;
    private RoomType roomType;
    private Date checkInDate;
    private Date checkOutDate;
}
