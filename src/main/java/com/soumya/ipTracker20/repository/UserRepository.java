package com.soumya.ipTracker20.repository;

import com.soumya.ipTracker20.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

    User findByEmail(String email);

}
