package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.abilities.AbilityUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbilityUseRepository extends JpaRepository<AbilityUse, UUID> {


}
