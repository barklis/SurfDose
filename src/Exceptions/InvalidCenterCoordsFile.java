package Exceptions;

import application.Preferences;

public class InvalidCenterCoordsFile extends Exception {
	public String getMessage() {
		return Preferences.getLabel("InvalidFile");
	}
}
