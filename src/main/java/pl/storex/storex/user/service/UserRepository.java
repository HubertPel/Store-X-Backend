package pl.storex.storex.user.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.storex.storex.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmailAndName(String email, String name);

    User findUserByName(String name);

    @Query(value = "SELECT u from appuser u where u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("select u from appuser u where u.group_id = :groupId")
    Optional<ArrayList<User>> findUsersByGroupId(@Param("groupId") Long groupId);

    List<User> getUsersById(Long id);
}