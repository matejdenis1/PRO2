package com.example.delivery.User;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK) ;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable ObjectId id){
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginForm loginForm){
        Optional<User> user = userService.loginUser(loginForm);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterForm registerForm){
        return new ResponseEntity<>(userService.registerUser(registerForm), HttpStatus.OK);
    }

}
