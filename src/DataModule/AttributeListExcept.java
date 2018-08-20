package DataModule;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;

import Exceptions.MissingTagException;

public class AttributeListExcept extends AttributeList {
	
	private AttributeList innerList;
	private boolean inner = false;
	
	public AttributeListExcept() {
		super();
	}

	private AttributeListExcept(AttributeList innerList) {
		this.innerList = innerList;
		inner = true;
	}

	public Attribute getSafe(AttributeTag tag) throws MissingTagException {
		Attribute attr = null;
		if(inner)
			attr = innerList.get(tag);
		else
			attr = get(tag);
		
		if(attr == null)
			throw new MissingTagException(tag);
		else
			return attr;
	}
	
	public static AttributeListExcept getInnerInstance(AttributeList innerList) {
		return new AttributeListExcept(innerList);
	}
}
