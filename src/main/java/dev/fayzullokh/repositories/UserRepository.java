package dev.fayzullokh.repositories;

import dev.fayzullokh.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    List<User> findAllByUsernameContains(String username);

    @NotNull List<User> findAll();

    User findByUsername(String username);

    @Query("select a.id from User a where a.username = ?1")
    int findAuthIdByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) or LOWER(u.firstName) LIKE LOWER(CONCAT('%', :username, '%')) or LOWER(u.lastName) LIKE LOWER(CONCAT('%', :username, '%'))")
    @NotNull List<User> findAll(String username);


    @Transactional
    @Modifying
    @Query("delete from User u where u.username = ?1")
    void deleteByUsername(String username);

}