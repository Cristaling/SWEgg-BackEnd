package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User getUserByEmailAndPassword(String email, String password);
    User getUserByEmail(String email);

}
