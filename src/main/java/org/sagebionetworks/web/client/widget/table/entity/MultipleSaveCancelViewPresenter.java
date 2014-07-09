package org.sagebionetworks.web.client.widget.table.entity;

import java.util.HashMap;
import java.util.Map;


public class MultipleSaveCancelViewPresenter implements MultipleSaveCancelView.Presenter{
	
	private Map<String, SaveCancelWidget> subViews = new HashMap<String, SaveCancelWidget>();
	private String currentViewId;

	@Override
	public void registerSaveCancelWidget(String subViewId, SaveCancelWidget widget) {
		if(this.subViews.containsKey(subViewId)){
			throw new IllegalArgumentException("A sub-view it the ID: "+subViewId+" has already been registered");
		}
		this.subViews.put(subViewId, widget);
	}

	@Override
	public void attemptToggleSubView(String subViewId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetToggle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSave(String subViewId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(String subViewId) {
		// TODO Auto-generated method stub
		
	}

}
