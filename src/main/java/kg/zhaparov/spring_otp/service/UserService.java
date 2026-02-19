package kg.zhaparov.spring_otp.service;

import kg.zhaparov.spring_otp.model.User;
import kg.zhaparov.spring_otp.payload.request.RegisterRequest;
import kg.zhaparov.spring_otp.payload.response.RegisterResponse;
import kg.zhaparov.spring_otp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final EmailService emailService;

    public UserService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public RegisterResponse register(RegisterRequest request) {
        User existingUser = repository.findByEmail(request.getEmail());

        if (existingUser != null && existingUser.isVerified()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        String otp = generateOtp();
        user.setOtp(otp);
        repository.save(user);
        sendVerificationEmail(user.getEmail(), otp);
        RegisterResponse response = RegisterResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();

        return response;
    }

    private String generateOtp() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(999999);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email, String otp) {
        String subject = "Email verification";
        String body = "your verification code: " + otp;
        emailService.sendEmail(email, subject, body);
    }
}
