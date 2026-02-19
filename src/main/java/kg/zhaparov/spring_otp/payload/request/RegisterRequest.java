package kg.zhaparov.spring_otp.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterRequest {
    private String userName;
    private String email;
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
