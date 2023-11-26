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
    private long id;
    private String title;
    private String content;
    public Request(long id,String title, String Content){
        this.id = id;
        this.title = title;
        this.content = Content;
    }

}
