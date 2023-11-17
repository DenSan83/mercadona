package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);

    boolean existsByUserName(String username);
    boolean existsByUserNameAndUserIdNot(String username, Integer userId);
    @Query("SELECT u FROM User u ORDER BY u.userId")
    List<User> findAllByUserId();
}
