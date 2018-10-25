package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Member, UUID> {

    Member getUserByEmailAndPassword(String email, String password);
    Member getUserByEmail(String email);

}
