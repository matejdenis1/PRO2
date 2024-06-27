package com.example.delivery.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
}
