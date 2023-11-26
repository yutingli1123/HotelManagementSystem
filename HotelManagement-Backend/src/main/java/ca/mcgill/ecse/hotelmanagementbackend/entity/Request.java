package ca.mcgill.ecse.hotelmanagementbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue
    private String title;
    private String content;
    @ManyToOne
    private Room room;
    public Request(String title, String Content){
        this.title = title;
        this.content = Content;
    }

}
