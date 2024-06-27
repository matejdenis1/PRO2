package com.example.delivery.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    private String id;
    private String name;
    private List<Menu> menu;
    private String address;
    private String picture;
    public Double rating;
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public String getAddress() {
        return address;
    }

    public String getPicture() {
        return picture;
    }

    public Double getRating() {
        return rating;
    }

}
