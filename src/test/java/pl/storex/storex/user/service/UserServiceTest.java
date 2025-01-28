package pl.storex.storex.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.storex.storex.group.model.UsersGroup;
import pl.storex.storex.user.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //before
    List<User> users = setUsers();

    public static List<User> setUsers() {
        List<User> uList = new ArrayList<>();
        uList.add(User.builder().id(2L).email("test2@wp.pl").name("Dupa").group_id(5L).created_at(new Date(2025, Calendar.JANUARY, 20)).build());
        uList.add(User.builder().id(3L).email("test3@wp.pl").name("Dupa2").group_id(5L).created_at(new Date(2025, Calendar.JANUARY, 13)).build());

        return uList;
    }

    @Test
    void checkIfUserExistsReturnsTrueWhenUserExists() {
        // Arrange
        String email = "existing@user.com";
        String name = "Existing User";
        when(userRepository.existsUserByEmailAndName(email, name)).thenReturn(true);
        // Act
        boolean result = userService.checkIfUserExists(email, name);
        // Assert
        assertTrue(result);
    }

    @Test
    void checkIfUserExistsReturnsFalseWhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@user.com";
        String name = "Nonexistent User";
        when(userRepository.existsUserByEmailAndName(email, name)).thenReturn(false);
        // Act
        boolean result = userService.checkIfUserExists(email, name);
        // Assert
        assertFalse(result);
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
        User principal = User.builder().id(1L).email("test@wp.pl").name("Test").group_id(5L).created_at(new Date(2025, Calendar.JANUARY, 27)).build();
        UsersGroup group = UsersGroup.builder().id(1L).groupOwnerEmail("test@wp.pl").updated_by(1L).name("Test").build();
        if (group != null) {
            if (group.getGroupOwnerEmail().equals(principal.getEmail())) {
                //check for other group members
                List<User> list = users;
                if (list == null || list.size() == 1) {
                    group = null;
                } else {
                    User user = list.stream().reduce((a, b) -> {
                        if (a.getCreated_at().before(b.getCreated_at())) {
                            return a;
                        } else {
                            return b;
                        }
                    }).orElse(null);
                    if (user != null) {
                        group.setGroupOwnerEmail(user.getEmail());
                        group.setName(user.getName());
                        group.setUpdated_by(user.getId());
                    }
                }
            }
        }
        assertNotNull(group, "group is not null");
        assertEquals("test3@wp.pl", group.getGroupOwnerEmail(), "group owner email is equal");
    }

    @Test
    void checkIfUserExists() {
    }

    @Test
    void findByNameAndCheckPass() {
    }

    @Test
    void usersInGroup() {
    }

    @Test
    void register() {
    }
}