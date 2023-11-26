package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue
    private String title;
    private String content;
    public Request(String title, String Content){
        this.title = title;
        this.content = Content;
    }

}
