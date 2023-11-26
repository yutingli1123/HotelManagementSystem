package ca.mcgill.ecse.hotelmanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterData {
    private String name;
    private String username;
    private String email;
    private String password;
}
