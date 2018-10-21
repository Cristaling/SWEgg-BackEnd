package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDataRepository extends JpaRepository<UserData, UUID> {
}
