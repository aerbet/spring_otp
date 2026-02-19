package kg.zhaparov.spring_otp.controller;

import kg.zhaparov.spring_otp.payload.request.RegisterRequest;
import kg.zhaparov.spring_otp.payload.response.RegisterResponse;
import kg.zhaparov.spring_otp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
