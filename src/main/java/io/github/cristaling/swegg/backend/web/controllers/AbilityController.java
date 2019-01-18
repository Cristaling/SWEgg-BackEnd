package io.github.cristaling.swegg.backend.web.controllers;

import io.github.cristaling.swegg.backend.core.abilities.Ability;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.service.AbilityService;
import io.github.cristaling.swegg.backend.service.SecurityService;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;
import io.github.cristaling.swegg.backend.web.requests.EndorsementRequest;
import io.github.cristaling.swegg.backend.web.responses.EndorsementResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/ability")
public class AbilityController {

	private SecurityService securityService;
	private AbilityService abilityService;

	public AbilityController(SecurityService securityService, AbilityService abilityService) {
		this.securityService = securityService;
		this.abilityService = abilityService;
	}

	@GetMapping
	public ResponseEntity getAllAbilities() {
		return new ResponseEntity(this.abilityService.getAbilitiesByCategory("General")
				.stream()
				.map((Ability::getName))
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	@RequestMapping("/add")
	public ResponseEntity addAbilities(@RequestHeader("Authorization") String token,
	                                   @RequestBody List<String> abilities) {
		if (!this.securityService.canAccessRole(token, MemberRole.PROVIDER)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		for (String ability : abilities) {
			this.abilityService.addAbility(ability, "General");
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping("/endorsements")
	public ResponseEntity getEndorsements(@RequestHeader("Authorization") String token,
	                                      @RequestParam String email) {
		if (!this.securityService.canAccessRole(token, MemberRole.PROVIDER)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		HashMap<Ability, List<String>> endorsementMap = this.abilityService.getEndorsementsByMember(email);
		List<EndorsementResponse> result = new ArrayList<>();

		for (Map.Entry<Ability, List<String>> entry : endorsementMap.entrySet()) {
			result.add(new EndorsementResponse(entry.getKey().getName(), entry.getValue()));
		}

		result.sort(Comparator.comparing(EndorsementResponse::getAbility));

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@RequestMapping("/endorse")
	public ResponseEntity toggleEndorsement(@RequestHeader("Authorization") String token,
	                                     @RequestBody EndorsementRequest request) {
		if (!this.securityService.canAccessRole(token, MemberRole.PROVIDER)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		Member endorser = this.securityService.getUserByToken(token);
		Ability ability = this.abilityService.getAbilityByName(request.getAbilityName());

		// Cannot be null
		if (ability == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		this.abilityService.toggleEndorsement(endorser, request.getEmail(), ability);
		return new ResponseEntity(HttpStatus.OK);
	}

}
