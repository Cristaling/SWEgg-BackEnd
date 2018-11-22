package io.github.cristaling.swegg.backend.core.abilities;

import io.github.cristaling.swegg.backend.core.member.Member;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="endorsements")
public class Endorsement {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "endorsed_uuid", nullable = false)
	private	Member endorsed;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "endorser_uuid", nullable = false)
	private	Member endorser;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "ability_uuid", nullable = false)
	private	Ability ability;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Member getEndorsed() {
		return endorsed;
	}

	public void setEndorsed(Member endorsed) {
		this.endorsed = endorsed;
	}

	public Member getEndorser() {
		return endorser;
	}

	public void setEndorser(Member endorser) {
		this.endorser = endorser;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}
}
