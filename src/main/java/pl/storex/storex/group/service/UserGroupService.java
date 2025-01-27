package pl.storex.storex.group.service;

import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.storex.storex.group.model.UsersGroupDTO;
import pl.storex.storex.user.model.User;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.user.model.UserDTO;
import pl.storex.storex.user.service.UserRepository;
import pl.storex.storex.user.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UsersGroupRepository groupRepo;
    private final UserRepository user;
    private final static Logger log = LoggerFactory.getLogger(UserGroupService.class);

    public UsersGroup getGroupById(Long id) {
        Optional<UsersGroup> group = groupRepo.findById(id);
        return group.orElseThrow(() -> new NullPointerException("Group was not found"));
    }


    public UsersGroup updateGroup(UsersGroupDTO usersGroupDTO) {
        Optional<UsersGroup> groupById = groupRepo.findById(usersGroupDTO.getId());
        Optional<User> owner = user.findUserByEmail(groupById.get().getGroupOwnerEmail());
        User userObj;
        userObj = owner.orElse(null);
        return groupRepo.findById(usersGroupDTO.getId()).map(
                group -> {
                    group.setName(usersGroupDTO.getName());
                    assert userObj != null;
                    group.setGroupOwnerEmail(userObj.getEmail());
                    return groupRepo.save(group);
                }).orElseGet(() -> {
                    assert userObj != null;
                    return groupRepo.save(
                            UsersGroup.builder()
                                    .name(usersGroupDTO.getName())
                                    .groupOwnerEmail(userObj.getEmail())
                                    .build());
                }
        );
    }

    UsersGroup findGroup(Long groupId) {
        return groupRepo.findById(groupId).orElseThrow(
                ()-> new NonUniqueResultException("Group not found"));
    }

    public void removeGroup(String groupId) {
        Optional<UsersGroup> group = Optional.ofNullable(findGroup(Long.getLong(groupId)));
        //todo check user from token is group owner
        group.ifPresent(groupRepo::delete);
    }

    public Optional<User> removeUserFromGroup(UserDTO userDTO) {
        Optional<User> optionalUser = user.findById(userDTO.getId());
        return optionalUser.map(user1 -> {
            user1.setGroup_id(null);
           return user.save(user1);
        });
    }

    public Optional<UsersGroup> findGroup(UsersGroupDTO groupDTO) {
        Optional<UsersGroup> group = Optional.empty();
        if (groupDTO.getId() != null) {
            group = groupRepo.findById(groupDTO.getId());
        }
        if (groupDTO.getName() != null) {
            group = Optional.ofNullable(groupRepo.findByName(groupDTO.getName()));
        }
        if (groupDTO.getGroup_owner_email() != null) {
            group = groupRepo.findByGroupOwnerEmail(groupDTO.getGroup_owner_email());
        }
        return group;
    }

     public UserDTO findUsersWithoutGroup(UserDTO userDTO) {
         Optional<User> optionalUser = user.findUserByEmail(userDTO.getEmail());
         UserDTO dto = null;
         if (optionalUser.isPresent()) {
             dto = User.toDTO(optionalUser.get());
         }
         return dto;
    }

    public UsersGroupDTO createGroup(UsersGroupDTO usersGroupDto) {
        UsersGroup saved = groupRepo.save(UsersGroup.toUser(usersGroupDto));
        return UsersGroup.toDTO(saved);
    }
}
