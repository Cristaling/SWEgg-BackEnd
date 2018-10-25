package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.member.MemberData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDataRepository extends JpaRepository<MemberData, UUID> {
}
