package pl.storex.storex.user.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "test@test.com")
    private String email;
    @Schema(example = "Janusz")
    private String name;
    @Schema(example = "154dfEfjk%ggSD")
    private String password;
    @Schema(description = "Optional", example = "Optional: '1'")
    private Long groupId;
    @Schema(description = "Optional", example = "Optional: test@test.com or my group name")
    private String groupName;
    @Hidden
    private Role role;
/*
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int getGroupId() {
        var group = this.groupUUID;
        if (group) {
            group = this.groupUUID;
        }
        return group;
    }

 */
}
