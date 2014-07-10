package org.sagebionetworks.web.client.widget.table.entity;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Abstraction for a widget that can save and cancel changes to its contents.
 * @author jmhill
 *
 */
public interface SaveCancelWidget extends IsWidget {
	
	/**
	 * Called when the user presses the save button.
	 */
	public void onSave();
	
	/**
	 * Called when the user presses the cancel button or closes the view of this widget.
	 */
	public void onCancel();
	
	/**
	 * The title that should be displayed for this panel.
	 */
	public String getDispalyName();
	
	/**
	 * 
	 * The provided ChangeListener is used to communicate when there are changes that can be saved (or canceled).
	 * @param listener
	 */
	public void addEditListener(ChangeListener listener);
	
	/**
	 * The provided EditListener is used to communicate when there are changes that can be saved (or canceled).
	 *
	 */
	public interface ChangeListener {
		
		/**
		 * When hasChanges is set to true, the save and cancel buttons will become active.
		 * When hasChanges is set to false, the save and cancel buttons will be hidden.
		 * 
		 * @param isEditing
		 */
		public void setHasChanges(boolean hasChanges);
	}

}
