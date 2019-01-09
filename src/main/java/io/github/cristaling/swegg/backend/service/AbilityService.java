package io.github.cristaling.swegg.backend.service;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.abilities.Endorsement;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.AbilityRepository;
import io.github.cristaling.swegg.backend.repositories.EndorsementRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class AbilityService {

	private UserRepository userRepository;

	private AbilityRepository abilityRepository;
	private EndorsementRepository endorsementRepository;

	@Autowired
	public AbilityService(UserRepository userRepository, AbilityRepository abilityRepository, EndorsementRepository endorsementRepository) {
		this.userRepository = userRepository;
		this.abilityRepository = abilityRepository;
		this.endorsementRepository = endorsementRepository;
	}

	public List<Ability> getAbilitiesByCategory(String category) {
		return this.abilityRepository.getAbilitiesByCategory(category);
	}

	public List<String> getCategories() {
		List<String> result = new ArrayList<>();
		List<Ability> abilities = this.abilityRepository.findAll();

		for (Ability ability : abilities) {
			if (!result.contains(ability.getCategory())) {
				result.add(ability.getCategory());
			}
		}

		return result;
	}

	public HashMap<Ability, List<String>> getEndorsementsByMember(String email) {
		Member member;
		try {
			member = this.userRepository.getMemberByEmail(email);
		} catch (EntityNotFoundException ex) {
			return new HashMap<>();
		}

		List<Endorsement> endorsements = this.endorsementRepository.getByEndorsed(member);
		HashMap<Ability, List<String>> result = new HashMap<>();

		for (Endorsement endorsement : endorsements) {
			if (!result.containsKey(endorsement.getAbility())) {
				result.put(endorsement.getAbility(), new ArrayList<>());
			}
			result.get(endorsement.getAbility()).add(endorsement.getEndorser().getEmail());
		}

		return result;
	}

	public Ability addAbility(String name, String category) {

		Ability ability;

		try {
			ability = this.abilityRepository.getAbilityByName(name);
			if (ability != null) {
				return null;
			}
		} catch (EntityNotFoundException ex) {

		}

		ability = new Ability();
		ability.setName(name);
		ability.setCategory(category);
		this.abilityRepository.save(ability);
		return ability;
	}

	public void toggleEndorsement(Member endorser, String endorsedEmail, UUID abilityUUID) {

		Member endorsed;
		try {
			endorsed = this.userRepository.getMemberByEmail(endorsedEmail);
		} catch (EntityNotFoundException ex) {
			return;
		}

		if (endorsed == null) {
			return;
		}

		Ability ability = this.abilityRepository.getOne(abilityUUID);

		Endorsement existent = this.endorsementRepository.getByAbilityAndEndorsedAndEndorser(ability, endorsed, endorser);

		if (existent != null) {
			this.endorsementRepository.delete(existent);
			return;
		}

		Endorsement endorsement = new Endorsement();
		endorsement.setAbility(ability);
		endorsement.setEndorsed(endorsed);
		endorsement.setEndorser(endorser);

		this.endorsementRepository.save(endorsement);
	}

	public Ability getAbilityByName(String abilityName) {
		try {
			Ability ability = this.abilityRepository.getAbilityByName(abilityName);
			if (ability != null) {
				return ability;
			}
		} catch (EntityNotFoundException ex) {

		}
		return addAbility(abilityName, "General");
	}

	public List<Ability> getMemberAbilities(Member member) {

		List<Ability> result = new ArrayList<>();
		List<Endorsement> endorsements = this.endorsementRepository.getByEndorsed(member);

		for (Endorsement endorsement : endorsements) {
			if (!result.contains(endorsement.getAbility())) {
				result.add(endorsement.getAbility());
			}
		}

		return result;
	}
}
