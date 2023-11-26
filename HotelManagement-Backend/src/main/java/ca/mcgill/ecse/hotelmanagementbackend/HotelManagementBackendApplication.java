package ca.mcgill.ecse.hotelmanagementbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class HotelManagementBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementBackendApplication.class, args);
    }
}
