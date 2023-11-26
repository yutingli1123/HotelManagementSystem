package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class HotelTest {
    @Autowired
    private HotelService hotelService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void testReadAndWriteHotelObjects() {
        Hotel hotel = new Hotel();
        hotelService.save(hotel);
        Long hotelId = hotel.getId();
        Hotel hotelById = hotelService.findById(hotelId);
        assertEquals("checks if hotel is saved", hotel, hotelById);
    }

    @Test
    @Transactional
    void testReadAndWriteHotelAttributes() {
        Hotel hotel = new Hotel(LocalTime.of(8, 0), LocalTime.of(23, 30));
        hotelService.save(hotel);
        // checks read
        LocalTime openTime = hotel.getOpenTime();
        LocalTime closeTime = hotel.getCloseTime();
        assertEquals("checks open time read", "08:00:00", openTime);
        assertEquals("checks close time read", "23:30:00", closeTime);
        // check write
        hotel.setOpenTime(LocalTime.of(6, 0));
        hotel.setCloseTime(LocalTime.of(0, 0));
        openTime = hotel.getOpenTime();
        closeTime = hotel.getCloseTime();
        assertEquals("checks open time written", "06:00:00", openTime);
        assertEquals("checks close time written", "00:00:00", closeTime);
    }
}
