package pl.storex.storex.user.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "Invalid email: Empty email")
    @NotNull(message = "Invalid email: Null email")
    @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email: Invalid email format")
    @Schema(example = "test@test.com")
    private String email;
    @Schema(example = "Janusz")
    private String name;
    @Schema(example = "154dfEfjk%ggSD")
    @NotBlank(message = "Invalid password: Empty password")
    @NotNull(message = "Invalid password: Null password")
    @Min(value = 5, message = "Invalid password: Password too short")
    private String password;
    @Schema(description = "Optional", example = "Optional: '1'")
    private Long groupId;
    @Schema(description = "Optional", example = "Optional: test@test.com or my group name")
    private String groupName;
    @Hidden
    private Role role;

}
