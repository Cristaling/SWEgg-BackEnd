package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Member, UUID> {

    Member getMemberByEmailAndPassword(String email, String password);
    Member getMemberByEmail(String email);
    Member getMemberByGoogleID(String googleID);

    @Query("SELECT m FROM Member m WHERE " +
            "concat(lower(m.memberData.firstName), ' ', lower(m.memberData.lastName)) LIKE %:name% or " +
            "concat(lower(m.memberData.lastName), ' ', lower(m.memberData.firstName)) LIKE %:name% ")
    Page<Member> getMemberByCompleteNameIgnoreCase(Pageable pageRequest, @Param("name") String name);

}
