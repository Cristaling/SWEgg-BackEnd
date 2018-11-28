package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.abilities.Endorsement;
import io.github.cristaling.swegg.backend.core.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, UUID> {

	Endorsement getByAbilityAndEndorsedAndEndorser(Ability ability, Member endorsed, Member endorser);
	List<Endorsement> getByEndorsed(Member endorsed);

}
