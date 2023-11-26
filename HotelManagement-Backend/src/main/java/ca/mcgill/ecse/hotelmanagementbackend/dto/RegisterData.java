package ca.mcgill.ecse.hotelmanagementbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterData {
    private String name;
    private String username;
    private String email;
    private String password;
}
