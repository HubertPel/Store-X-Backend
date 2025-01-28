package pl.storex.storex.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.storex.storex.model.LoginDTO;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.group.service.UsersGroupRepository;
import pl.storex.storex.user.exception.UserNotFoundException;
import pl.storex.storex.user.model.Role;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UsersGroupRepository groupRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

        if (dto.getGroupId() != null) {
            user.setGroup_id(getGroupIdOrCreateNew(dto.getGroupId(), dto.getGroupName(), dto.getName()));
        } else {
            UsersGroup group = groupRepository.save(UsersGroup.builder()
                    .name(dto.getName())
                    .groupOwnerEmail(user.getEmail())
                    .build()
            );
            user.setGroup_id(group.getId());
        }
        return userRepository.save(user);
    }

    public User update(UserDTO newUser, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(newUser.getPassword());
                    user.setGroup_id(newUser.getGroupId());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    Long groupId = getGroupIdOrCreateNew(newUser.getGroupId(), newUser.getGroupName(), newUser.getEmail());
                    return userRepository.save(User.builder()
                            .name(newUser.getName())
                            .password(newUser.getPassword())
                            .email(newUser.getEmail())
                            .group_id(groupId)
                            .build());
                });
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.getUsersById(id).forEach(user -> {
            user.setEnabled(false);
            user.setDeleted(Date.from(Instant.now()));
            userRepository.save(user);
        });
    }

    public boolean checkIfUserExists(String email, String name) {
        return userRepository.existsUserByEmailAndName(email, name);
    }

    private Long getGroupIdOrCreateNew(Long groupId, String groupName, String ownerEmail) {
        UsersGroup usersGroupById = groupRepository.findUsersGroupById(groupId);
        Long userGroupId;
        if (usersGroupById != null) {
            userGroupId = usersGroupById.getId();
        } else {
            UsersGroup newUserGroup = groupRepository.save(
                    UsersGroup.builder()
                            .name(groupName)
                            .groupOwnerEmail(ownerEmail)
                            .build()
            );
            userGroupId = newUserGroup.getId();
        }
        return userGroupId;
    }

    public User findByNameAndCheckPass(LoginDTO loginDto) {
        Optional<User> userByName = userRepository.findUserByEmail(loginDto.getEmail());
        if (userByName.isPresent()) {
            if (passwordEncoder.matches(loginDto.getPassword(), userByName.get().getPassword())) {
                return userByName.get();
            }
        }
        return null;
    }

    public ArrayList<User> usersInGroup(String ownerEmail) {
        Optional<UsersGroup> usersGroup = groupRepository.findByGroupOwnerEmail(ownerEmail);
        Optional<ArrayList<User>> users = Optional.empty();
        if (usersGroup.isPresent()) {
            users = userRepository.findUsersByGroupId(usersGroup.get().getId());
        }
        return users.orElseThrow(() -> new UserNotFoundException("No Users to return"));
    }

    public UserDTO register(UserDTO userDTO) {
        User user = User.toUser(userDTO);
        user.setRole(Role.USER);
        return User.toDTO(userRepository.save(user));
    }

    public UserDTO registerAdmin(UserDTO userDTO) {
        User user = User.toUser(userDTO);
        user.setRole(Role.ADMIN);
        return User.toDTO(userRepository.save(user));
    }
}
