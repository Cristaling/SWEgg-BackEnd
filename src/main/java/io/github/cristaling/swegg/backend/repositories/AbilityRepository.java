package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AbilityRepository extends JpaRepository<Ability, UUID> {

	List<Ability> getAbilitiesByCategory(String category);
	Ability getAbilityByName(String name);

}
