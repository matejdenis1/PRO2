package com.example.delivery.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.util.List;

@Document(collection = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {



    public User(String email, String password, String address, String role, List<String> ratedRestaurants) {
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
        this.ratedRestaurants = ratedRestaurants;
    }
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String role;
    @Getter
    @Setter
    private List<String> ratedRestaurants;

}
