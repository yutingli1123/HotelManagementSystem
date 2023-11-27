package ca.mcgill.ecse.hotelmanagementbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
    private String oldPass;
    private String newPass;
}
