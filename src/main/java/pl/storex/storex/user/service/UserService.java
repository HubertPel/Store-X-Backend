package pl.storex.storex.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.group.service.UsersGroupRepository;
import pl.storex.storex.model.LoginDTO;
import pl.storex.storex.user.exception.UserNotFoundException;
import pl.storex.storex.user.model.Role;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
                })
                .orElseGet(() -> userRepository.save(
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

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean checkIfUserExists(String email, String name) {
        return userRepository.existsUserByEmailAndName(email, name);
    }

    private Long getGroupIdOrCreateNew(Long groupId, String groupName, String ownerEmail, @Nullable Long userId) {
        assert userId != null;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        UsersGroup usersGroupById = null;
        if (groupId != null) {
            usersGroupById = groupRepository.findById(groupId).orElse(null);
        }
        Long userGroupId;
        if (usersGroupById != null) {
            userGroupId = usersGroupById.getId();
        } else {
            UsersGroup newUserGroup = groupRepository.save(
                    UsersGroup.builder()
                            .name(groupName)
                            .groupOwnerEmail(ownerEmail)
                            .updated_by(user.getId())
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
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        return User.toDTO(userRepository.save(user));
    }

    public UserDTO registerAdmin(UserDTO userDTO) {
        boolean userExists = checkIfUserExists(userDTO.getEmail(), userDTO.getName());
        AtomicReference<User> userToReturn = new AtomicReference<>();
        if (userExists) {
            Optional<User> userByEmail = userRepository.findUserByEmail(userDTO.getEmail());
            userByEmail.ifPresent(user -> {
                user.setGroup_id(getGroupIdOrCreateNew(user.getGroup_id(), user.getName(), user.getEmail(), user.getId()));
                user.setRole(Role.ADMIN);
               userToReturn.set(userRepository.save(user));
            });
            return User.toDTO(userToReturn.get());
        } else {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setName(userDTO.getName());
            user.setRole(Role.ADMIN);

            User savedUser = userRepository.save(user);
            if ((userDTO.getGroupId()) == null) {
                savedUser.setGroup_id(getGroupIdOrCreateNew(null, userDTO.getGroupName(), userDTO.getEmail(), savedUser.getId()));
            } else {
                savedUser.setGroup_id(userDTO.getGroupId());
            }
            return User.toDTO(userRepository.save(savedUser));
        }
    }
}
