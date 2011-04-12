package ch.hsr.se2p.mrt.models;

import com.j256.ormlite.field.DatabaseField;

public class UserSettings {
	@DatabaseField
	private Integer userId;
	@DatabaseField
	private String firstName, lastName;

	private static UserSettings currentSettings;

	UserSettings() {
		// Needed for ormlite
	}

	public static UserSettings getCurrentSettings() {
		return currentSettings;
	}

	public static void setCurrentSettings(UserSettings currentSettings) {
		UserSettings.currentSettings = currentSettings;
	}

	public UserSettings(Integer userId, String firstName, String lastName) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}
