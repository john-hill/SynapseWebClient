package org.sagebionetworks.web.client.widget.table.entity;

import java.util.HashMap;
import java.util.Map;

import org.sagebionetworks.web.client.widget.table.entity.SaveCancelWidget.ChangeListener;

/**
 * Enforces all business logic around the MultipleSaveCancelView.
 * 
 * @author jmhill
 *
 */
public class MultipleSaveCancelViewPresenter implements MultipleSaveCancelView.Presenter{
	
	private Map<String, SaveCancelWidget> subViews = new HashMap<String, SaveCancelWidget>();
	private String currentViewId;
	private String lastViewId;
	private boolean currentHasChangesToCommit;
	private MultipleSaveCancelView view;
	
	public MultipleSaveCancelViewPresenter(MultipleSaveCancelView view) {
		super();
		this.view = view;
	}

	@Override
	public void registerSaveCancelWidget(final String subViewId, SaveCancelWidget widget) {
		if(this.subViews.containsKey(subViewId)){
			view.showError("A sub-view it the ID: "+subViewId+" has already been registered");
			return;
		}
		this.subViews.put(subViewId, widget);
		this.lastViewId = subViewId;
		widget.addEditListener(new ChangeListener(){
			@Override
			public void setHasChanges(boolean hasChanges) {
				if(!subViewId.equals(currentViewId)){
					view.showError("Only the current view can toggle changes.");
					return;
				}
				if(currentHasChangesToCommit != hasChanges){
					currentHasChangesToCommit = hasChanges;
					view.enableSaveCancel(subViewId, hasChanges);
				}
			}});
	}

	@Override
	public void attemptToggleSubView(String subViewId) {
		if(subViewId.equals(currentViewId)){
			// Do nothing when the user toggles the current view.
			return;
		}
		// Block the user if there are any changes
		if(currentHasChangesToCommit){
			view.showInfo("Uncommitted Changes","Please Save or Cancel your uncommitted changes to continue.");
			return;
		}
		if(currentViewId != null){
			// First shut down the current view.
			view.enableSaveCancel(currentViewId, false);
			view.collapseView(currentViewId);
		}

		// This is now the new view
		this.currentViewId = subViewId;
		this.currentHasChangesToCommit = false;
		view.enableSaveCancel(subViewId, false);
		view.expandView(subViewId);
	}

	@Override
	public void resetToggle() {
		// Enable the last view
		if(lastViewId == null){
			view.showError("Cannot rest the toggle until at least one sub-view has been registered.");
			return;
		}
		attemptToggleSubView(lastViewId);
	}

	@Override
	public void onSave(String subViewId) {
		if(!subViewId.equals(currentViewId)){
			view.showError("Only the current view can save changes.");
			return;
		}
		if(!currentHasChangesToCommit){
			view.showError("There are no changes to save.");
			return;
		}
		// First disable the save and cancel buttons
		view.enableSaveCancel(subViewId, false);
		currentHasChangesToCommit = false;
		SaveCancelWidget wiget = this.subViews.get(subViewId);
		wiget.onSave();
		view.showInfo("Saved", "Your changes were saved!!!!");

	}

	@Override
	public void onCancel(String subViewId) {
		if(!subViewId.equals(currentViewId)){
			view.showError("Only the current view can save changes.");
			return;
		}
		// First disable the save and cancel buttons
		view.enableSaveCancel(subViewId, false);
		currentHasChangesToCommit = false;
		SaveCancelWidget wiget = this.subViews.get(subViewId);
		wiget.onCancel();
	}

}
