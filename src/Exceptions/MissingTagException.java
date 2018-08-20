package Exceptions;

import com.pixelmed.dicom.AttributeTag;

public class MissingTagException extends Exception {
	private AttributeTag tag;
	
	public MissingTagException(AttributeTag tag) {
		this.tag = tag;
	}

	public AttributeTag getTag() {
		return tag;
	}
	
}
