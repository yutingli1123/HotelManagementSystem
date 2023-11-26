package ca.mcgill.ecse.hotelmanagementbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginData {
    private String username;
    private String password;
}
