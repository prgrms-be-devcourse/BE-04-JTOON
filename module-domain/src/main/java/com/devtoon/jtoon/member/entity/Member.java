package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@NotNull
	@Column(name = "email", nullable = false, unique = true, length = 40, updatable = false)
	private String email;

	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull
	@Column(name = "name", nullable = false, length = 10, updatable = false)
	private String name;

	@NotNull
	@Column(name = "nickname", nullable = false, unique = true, length = 30)
	private String nickname;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false, updatable = false)
	private Gender gender;

	@NotNull
	@Column(name = "phone", nullable = false, unique = true, length = 11)
	private String phone;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LoginType loginType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status = Status.NORMAL;

	@Column(name = "last_login_date")
	private LocalDateTime lastLoginDate;

	@Builder
	public Member(String email, String password, String name, String nickname, Gender gender, String phone, Role role,
		LoginType loginType) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.gender = gender;
		this.phone = phone;
		this.role = role;
		this.loginType = loginType;
	}
}