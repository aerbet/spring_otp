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
        User savedUser = repository.save(user);
        sendVerificationEmail(savedUser.getEmail(), otp);
        RegisterResponse response = RegisterResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();

        return response;
    }

    public void verify(String email, String otp) {
        User user = repository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.isVerified()) {
            throw new RuntimeException("User is already verified");
        }
        if (otp.equals(user.getOtp())) {
            user.setVerified(true);
            repository.save(user);
        }
    }

    public User login(String email, String password) {
        User user = repository.findByEmail(email);
        if (user != null && user.isVerified() && user.getPassword().equals(password)) {
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
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
