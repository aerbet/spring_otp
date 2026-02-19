package kg.zhaparov.spring_otp.controller;

import kg.zhaparov.spring_otp.model.User;
import kg.zhaparov.spring_otp.payload.request.RegisterRequest;
import kg.zhaparov.spring_otp.payload.response.RegisterResponse;
import kg.zhaparov.spring_otp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final UserService service;


    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = service.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        try {
            service.verify(email, otp);
            return new ResponseEntity<>("User verified successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        User user = service.login(email, password);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
