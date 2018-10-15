package Exceptions;

import application.Preferences;

public class NotEnoughContoursException extends Exception {

	public String getMessage() {
		return Preferences.getLabel("notEnoughContours");
	}
}
