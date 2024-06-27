package com.example.delivery.User;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserService{

    private UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final MongoTemplate mongoTemplate;

    public UserService(MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUser(ObjectId id) {
        return userRepository.findById(String.valueOf(id));
    }

    public Optional<User> loginUser(LoginForm loginForm) {
            String email = loginForm.getEmail();
            String password = loginForm.getPassword();
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isPresent()){
                if(passwordEncoder.matches(password,user.get().getPassword())){
                    return user;
                }
            }
            return Optional.empty();
        }


    public String registerUser(RegisterForm registerForm) {
        if(userRepository.findByEmail(registerForm.getEmail()).isPresent()){
            return "User with this email already exists";
        }
        String email = registerForm.getEmail();
        String password= passwordEncoder.encode(registerForm.getPassword());
        String address = registerForm.getAddress();
        String role = registerForm.getRole();
        List<String> ratedRestaurants = new ArrayList<>();

        User user = new User(email,password,address,role,ratedRestaurants);
        try{
            mongoTemplate.save(user);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return "Error occured while registering user.";
        }
        return "";
    }
}


