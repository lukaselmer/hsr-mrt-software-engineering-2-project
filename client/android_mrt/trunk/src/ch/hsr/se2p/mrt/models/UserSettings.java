package ch.hsr.se2p.mrt.models;

import com.j256.ormlite.field.DatabaseField;

public class UserSettings {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String email, password;

	UserSettings() {
		// Needed for ormlite
	}

	public UserSettings(Integer id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public Integer getUserId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
