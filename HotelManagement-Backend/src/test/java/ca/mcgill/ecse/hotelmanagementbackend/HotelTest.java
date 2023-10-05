package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import ca.mcgill.ecse.hotelmanagementbackend.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HotelTest {
    @Autowired
    private OwnerService ownerService;
    private HotelService hotelService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveHotel() {
        List<Hotel> hotelList = hotelService.findAll();
        Hotel hotel = new Hotel();
        Long hotelId = hotel.getId();

    }
}
