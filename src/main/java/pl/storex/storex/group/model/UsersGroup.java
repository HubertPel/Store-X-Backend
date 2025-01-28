package pl.storex.storex.group.model;

import jakarta.persistence.*;
import lombok.*;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "users_groups")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersGroup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "group_owner_email")
    private String groupOwnerEmail;
    private Date created_at;
    private Date updated_at;
    private Long updated_by;


    public static UsersGroupDTO toDTO(UsersGroup usersGroup) {
        return UsersGroupDTO.builder()
                .id(usersGroup.getId())
                .group_owner_email(usersGroup.getGroupOwnerEmail())
                .updated_by(usersGroup.getUpdated_by())
                .name(usersGroup.getName())
                .created_at(usersGroup.getCreated_at())
                .updated_at(usersGroup.getUpdated_at())
                .build();
    }

    public static UsersGroup toUser(UsersGroupDTO dto) {
        return UsersGroup.builder()
                .id(dto.getId())
                .name(dto.getName())
                .updated_by(dto.getUpdated_by())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .groupOwnerEmail(dto.getGroup_owner_email())
                .build();
    }

}
