package pl.storex.storex.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class LoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Invalid email: Empty email")
    @NotNull(message = "Invalid email: Null email")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email: Invalid email format")
    private String email;

    @NotBlank(message = "Invalid password: Empty password")
    @NotNull(message = "Invalid password: Null password")
//    @Min(value = , message = "Invalid password: Password too short")
    private String password;

    public LoginDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
