package pl.storex.storex.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.storex.storex.model.LoginDTO;
import pl.storex.storex.model.RequestAuth;
import pl.storex.storex.security.JwtService;
import pl.storex.storex.group.service.UserGroupService;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;
import pl.storex.storex.user.exception.UserNotFoundException;
import pl.storex.storex.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Store-X User Controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService repository;
    private final JwtService jwtService;
    private final UserGroupService userGroupService;

    @GetMapping("/getAll")
    List<User> allUsers() {
        return repository.findAll();
    }

    @PostMapping("/register")
    ResponseEntity<UserDTO> registerWithoutGroup(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(repository.register(userDTO));
    }

    @PostMapping(value = "/addUser", produces = "application/json", consumes = "application/json")
    ResponseEntity<UserDTO> newUser(@RequestBody UserDTO userDTO) {
        User newUser = repository.save(userDTO);
        return ResponseEntity.ok(UserDTO.builder()
                        .email(newUser.getEmail())
                        .name(newUser.getName())
                        .groupId(newUser.getGroup_id())
                        .password(newUser.getPassword())
                        .groupName(userGroupService.getGroupById(newUser.getGroup_id()).getName())
                .build());
    }

    @GetMapping("/getUserByID/{id}")
    User user(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    @PostMapping(value = "/groupUsers")
    ResponseEntity<ArrayList<User>> getUsersInGroup(@RequestBody String groupOwnerEmail) {
        return ResponseEntity.ok(repository.usersInGroup(groupOwnerEmail));
    }

    @PutMapping(value = "/updateUser/{id}", consumes = "application/json", produces = "application/json")
    User updateUser(@RequestBody UserDTO newUser, @PathVariable Long id) {
        return repository.update(newUser, id);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/removeUser/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }

//    @CrossOrigin(origins = "localhost:52114")
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    ResponseEntity<RequestAuth> login(@RequestBody LoginDTO loginDto) {
        System.out.println(loginDto.getEmail());
        if (loginDto.getEmail() != null && loginDto.getPassword() != null) {
            //find user and check pass
            User user = repository.findByNameAndCheckPass(loginDto);
            if (user != null) {
                return ResponseEntity
                        .ok()
                        .body(RequestAuth.builder()
                                .token(jwtService.generateToken(user))
                                .refreshToken(jwtService.generateRefreshToken(user))
                                .build());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Register user with Admin role")
    @PostMapping("/register/admin")
    ResponseEntity<UserDTO> registerAdmin(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(repository.registerAdmin(userDTO));
    }

}
