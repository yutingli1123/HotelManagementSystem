package ca.mcgill.ecse.hotelmanagementbackend;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Hotel;
import ca.mcgill.ecse.hotelmanagementbackend.service.HotelService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HotelManagementBackendApplication {
    @Autowired
    private HotelService hotelService;

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementBackendApplication.class, args);
    }

    @PostConstruct
    public void Init() {
        if (hotelService.findAll().isEmpty()) {
            Hotel hotel = new Hotel();
            hotelService.save(hotel);
        }
    }
}
