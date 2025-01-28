package pl.storex.storex.group.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.group.model.UsersGroupDTO;
import pl.storex.storex.group.service.UserGroupService;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;

import java.util.Optional;

@Tag(name = "Store-X Group Controller")
@RestController
@RequestMapping("group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping("/removeUser")
    ResponseEntity<UserDTO> removeUserFromGroup(@RequestBody UserDTO userDTO) {
        Optional<User> user = userGroupService.removeUserFromGroup(userDTO);
        return ResponseEntity.of(user.map(User::toDTO));
    }

    @PostMapping(value = "/update", consumes = "application/json")
    ResponseEntity<UsersGroup> updateGroupName(@RequestBody UsersGroupDTO usersGroupDto) {
        return ResponseEntity.ok(userGroupService.updateGroup(usersGroupDto));
    }

    @DeleteMapping("/removeGroup")
    ResponseEntity.BodyBuilder removeGroup(@RequestBody String groupId) {
        userGroupService.removeGroup(groupId);
        return ResponseEntity.ok();
    }

    @Operation(summary = "Search group by any field")
    @PostMapping("/findGroup")
    ResponseEntity<UsersGroup> findGroup(@RequestBody UsersGroupDTO groupDTO) {
        Optional<UsersGroup> usersGroup = userGroupService.findGroup(groupDTO);
        return ResponseEntity.of(usersGroup);
    }

    @PostMapping("/addUser")
    ResponseEntity<UserDTO> addGroupToUser(@RequestBody UserDTO userDTO) {
       return ResponseEntity.ok(userGroupService.findUsersWithoutGroup(userDTO));
    }

    @PostMapping("/create")
    ResponseEntity<UsersGroupDTO> createGroup(@RequestBody UsersGroupDTO usersGroupDto) {
        return ResponseEntity.ok(userGroupService.createGroup(usersGroupDto));
    }

}
