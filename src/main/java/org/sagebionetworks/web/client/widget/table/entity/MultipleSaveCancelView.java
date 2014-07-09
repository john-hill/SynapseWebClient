package org.sagebionetworks.web.client.widget.table.entity;

/**
 * This is an abstraction for a view that contains multiple sub-views. It must
 * guarantees that only one of the sub-view is visible at any given time. The
 * visible view is the "current" view. This means if any view other than the
 * current view is toggled, then the current view will be hidden and the toggled
 * view will become visible and be the new current view.
 * 
 * Additionally, this view provides a Save/Cancel control for the current sub-view only.
 * Once editing starts on the current sub-view, "Save" and "Cancel" buttons will become visible.
 * While in the editing state, the view will block the user from toggling to another sub-view.
 * The user will instead be directed to save or cancel their outstanding changes before another
 * sub-view can be toggled.
 * 
 * @author John
 * 
 */
public interface MultipleSaveCancelView {

	
	/**
	 * The presenter controls all business logic for this view.
	 *
	 */
	public interface Presenter {
		/**
		 * Register a SaveCanceWidget providing the ID used to control it.
		 * @param subViewId The ID of a new sub-view.  The ID must be unique within this view.
		 * @param widget
		 */
		public void registerSaveCancelWidget(String subViewId, SaveCancelWidget widget);
		
		/**
		 * Attempt to toggle a sub-view.  The toggle will proceed as long as all conditions are met.
		 * @param subViewId
		 */
		public void attemptToggleSubView(String subViewId);
		
		/**
		 * Abort any changes and make the last view the current view.
		 */
		public void resetToggle();
		
		/**
		 * Should be called when the user selects save.
		 * @param subViewId
		 */
		public void onSave(String subViewId);
		
		/**
		 * Should be called when the user selects cancel.
		 * @param subViewId
		 */
		public void onCancel(String subViewId);
	}
}
