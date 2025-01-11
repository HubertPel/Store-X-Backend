package pl.storex.storex.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity(name = "appuser")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private Long group_id;
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private Role role;


    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .role(this.role)
//                .role(this.role.stream().map(Role::toDTO).collect(Collectors.toSet())) // INTERESTING
                .groupId(this.group_id)
                .build();
    }

    public static User fromDTO(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .group_id(userDTO.getGroupId())
                .build();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .groupId(user.group_id)
                .name(user.name)
                .groupName(user.email)
                .email(user.email)
                .build();
    }

    public static User toUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .group_id(userDTO.getGroupId())
                .build();
    }
}
