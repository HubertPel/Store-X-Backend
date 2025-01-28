package pl.storex.storex.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.storex.storex.config.AuthUser;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.group.service.UsersGroupRepository;
import pl.storex.storex.model.LoginDTO;
import pl.storex.storex.model.RequestAuth;
import pl.storex.storex.security.JwtService;
import pl.storex.storex.user.exception.UserNotFoundException;
import pl.storex.storex.user.model.Role;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UsersGroupRepository groupRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUser authUser;
    private final JwtService jwtService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(UserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .role(Role.USER)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        if (dto.getGroupId() != null) {
            savedUser.setGroup_id(getGroupIdOrCreateNew(dto.getGroupId(), dto.getGroupName(), dto.getName(), savedUser.getId()));
        } else {
            UsersGroup group = groupRepository.save(UsersGroup.builder()
                    .updated_by(savedUser.getId())
                    .name(savedUser.getName())
                    .groupOwnerEmail(savedUser.getEmail())
                    .build()
            );
            savedUser.setGroup_id(group.getId());
        }
        return userRepository.save(savedUser);
    }

    public User update(UserDTO newUser, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    user.setGroup_id(newUser.getGroupId());
                    return userRepository.save(user);
                }).orElseGet(() -> userRepository.save(
                        User.builder()
                                .name(newUser.getName())
                                .password(newUser.getPassword())
                                .email(newUser.getPassword())
                                .group_id(getGroupIdOrCreateNew(
                                        newUser.getGroupId(),
                                        (newUser.getGroupName() != null) ? newUser.getGroupName() : null,
                                        newUser.getEmail(), null))
                                .build()));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
  
  public void deleteById() {
        User principal = authUser.currentUser().orElse(null);
        assert principal != null;

        UsersGroup group = groupRepository.findById(principal.getGroup_id()).orElse(null);
        if (group == null) return;

        log.info("Group was found: {}", group.getName());

        if (!group.getGroupOwnerEmail().equals(principal.getEmail())) return;

        ArrayList<User> users = userRepository.findUsersByGroupId(principal.getGroup_id()).orElse(null);
        if (users == null || users.size() == 1) {
            groupRepository.delete(group);
            userRepository.delete(principal);
            return;
        }

        User newOwner = users.stream()
                .min(Comparator.comparing(User::getCreated_at))
                .orElse(null);

        if (newOwner != null) {
            group.setGroupOwnerEmail(newOwner.getEmail());
            group.setName(newOwner.getName());
            group.setUpdated_by(newOwner.getId());
            groupRepository.save(group);
        }
    }

    public boolean checkIfUserExists(String email, String name) {
        return userRepository.existsUserByEmailAndName(email, name);
    }

    private Long getGroupIdOrCreateNew(Long groupId, String groupName, String ownerEmail, @Nullable Long userId) {
        assert userId != null;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null; //todo throw exception

        UsersGroup usersGroupById = (groupId != null) ? groupRepository.findById(groupId).orElse(null) : null;
        if (usersGroupById != null) {
            return usersGroupById.getId();
        }

        UsersGroup newUserGroup = groupRepository.save(
                UsersGroup.builder()
                        .name(groupName)
                        .groupOwnerEmail(ownerEmail)
                        .updated_by(user.getId())
                        .build()
        );
        return newUserGroup.getId();
    }

    public ResponseEntity<RequestAuth> findByNameAndCheckPass(@NotNull LoginDTO loginDto) {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userByName = userRepository.findUserByEmail(loginDto.getEmail());
        if (userByName.isEmpty() || !passwordEncoder.matches(loginDto.getPassword(), userByName.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        log.info("User found: {}", userByName.get().getName());

        return ResponseEntity.ok().body(RequestAuth.builder()
                .token(jwtService.generateToken(userByName.get()))
                .refreshToken(jwtService.generateRefreshToken(userByName.get()))
                .build());
    }

    public ArrayList<User> usersInGroup(String ownerEmail) {
        return groupRepository.findByGroupOwnerEmail(ownerEmail)
                .map(group -> userRepository.findUsersByGroupId(group.getId())
                        .orElseThrow(() -> new UserNotFoundException("No Users to return")))
                .orElseThrow(() -> new UserNotFoundException("No Users to return"));
    }

    public UserDTO register(UserDTO userDTO) {
        User user = User.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        return User.toDTO(userRepository.save(user));
    }

    public UserDTO registerAdmin(@NotNull UserDTO userDTO) {
        Optional<User> userByEmail = userRepository.findUserByEmail(userDTO.getEmail());
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            user.setGroup_id(getGroupIdOrCreateNew(user.getGroup_id(), user.getName(), user.getEmail(), user.getId()));
            user.setRole(Role.ADMIN);
            return User.toDTO(userRepository.save(user));
        } else {
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .name(userDTO.getName())
                    .role(Role.ADMIN)
                    .build();
            User savedUser = userRepository.save(user);
            savedUser.setGroup_id(getGroupIdOrCreateNew(userDTO.getGroupId(), userDTO.getGroupName(), userDTO.getEmail(), savedUser.getId()));
            return User.toDTO(userRepository.save(savedUser));
        }
    }
}
