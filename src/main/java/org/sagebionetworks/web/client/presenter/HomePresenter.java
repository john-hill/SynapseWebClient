package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.StackConfigServiceAsync;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.place.Home;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.view.HomeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

@SuppressWarnings("unused")
public class HomePresenter extends AbstractActivity implements HomeView.Presenter {
	public static final String KEY_DATASETS_SELECTED_COLUMNS_COOKIE = "org.sagebionetworks.selected.dataset.columns";
	
	private Home place;
	private HomeView view;
	private GlobalApplicationState globalApplicationState;
	private CookieProvider cookieProvider;
	private AuthenticationController authenticationController;
	private StackConfigServiceAsync stackConfigService;
	
	@Inject
	public HomePresenter(HomeView view, 
			CookieProvider cookieProvider, 
			AuthenticationController authenticationController, 
			GlobalApplicationState globalApplicationState,
			StackConfigServiceAsync stackConfigService){
		this.view = view;
		// Set the presenter on the view
		this.cookieProvider = cookieProvider;
		this.authenticationController = authenticationController;
		this.globalApplicationState = globalApplicationState;
		this.stackConfigService = stackConfigService;
		this.view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		// Install the view
		panel.setWidget(view);
	}

	public void setPlace(Home place) {
		this.place = place;		
		view.setPresenter(this);		
		if(place != null && place.toToken() != null) {
			if(place.toToken().equals(DisplayConstants.TURN_DEMO_ON_TOKEN)) {
				DisplayConstants.showDemoHtml = true;
				authenticationController.saveShowDemo();
			} else if(place.toToken().equals(DisplayConstants.TURN_DEMO_OFF_TOKEN)) {
				DisplayConstants.showDemoHtml = false;
				authenticationController.saveShowDemo();
			}
		} 
		view.refresh();
	}

	@Override
    public String mayStop() {
        view.clear();
        return null;
    }

	@Override
	public boolean showLoggedInDetails() {
		return authenticationController.isLoggedIn();
	}

	@Override
	public void showBCCSignup(AsyncCallback<String> callback) {
		stackConfigService.getBCCSignupEnabled(callback);
	}
	

}
