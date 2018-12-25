package io.github.cristaling.swegg.backend.core.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.cristaling.swegg.backend.core.member.Member;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "member_uuid", nullable = false)
	@JsonIgnore
	private Member member;

	@NotNull
	private Date date;

	@NotNull
	private String text;

	private String onClickURL;

	private String pictureURL;

	public Notification(Member member, @NotNull Date date, @NotNull String text) {
		this.member = member;
		this.date = date;
		this.text = text;
	}

	public Notification() {
	}

	public UUID getUuid() {
		return uuid;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOnClickURL() {
		return onClickURL;
	}

	public void setOnClickURL(String onClickURL) {
		this.onClickURL = onClickURL;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
}
