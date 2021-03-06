package org.sagebionetworks.web.client.widget.login;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.shared.exceptions.TermsOfUseException;
import org.sagebionetworks.web.shared.users.UserData;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginWidget implements LoginWidgetView.Presenter {

	private LoginWidgetView view;
	private AuthenticationController authenticationController;	
	private List<UserListener> listeners = new ArrayList<UserListener>();	
	private String openIdActionUrl;
	private String openIdReturnUrl;
	
	@Inject
	public LoginWidget(LoginWidgetView view, AuthenticationController controller) {
		this.view = view;
		view.setPresenter(this);
		this.authenticationController = controller;		
	}

	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();
	}
	
	public void addUserListener(UserListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void setUsernameAndPassword(final String username, final String password, final boolean explicitlyAcceptsTermsOfUse) {		
		authenticationController.loginUser(username, password, explicitlyAcceptsTermsOfUse, new AsyncCallback<UserData>() {
			@Override
			public void onSuccess(UserData result) {
				fireUserChage(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof TermsOfUseException) {
					authenticationController.getTermsOfUse(new AsyncCallback<String>() {
						public void onSuccess(String termsOfUseContent) {	
							view.showTermsOfUse(termsOfUseContent, 
									new AcceptTermsOfUseCallback() {
										public void accepted() {setUsernameAndPassword(username, password, true);}
									});							
						}
						public void onFailure(Throwable t) {
							view.showTermsOfUseDownloadFailed();							
						}
					});

				} else {
					view.showAuthenticationFailed();
				}
			}
		});
	}
	
	public void clear() {
		view.clear();
	}

	// needed?
	private void fireUserChage(UserData user) {
		for(UserListener listener: listeners){
			listener.userChanged(user);
		}
	}

	public void setOpenIdActionUrl(String url) {
		this.openIdActionUrl = url;		
	}
	
	public void setOpenIdReturnUrl(String url) {
		this.openIdReturnUrl = url;
	}
	
	@Override
	public String getOpenIdActionUrl() {
		return openIdActionUrl;
	}

	@Override
	public String getOpenIdReturnUrl() {
		return openIdReturnUrl;
	}

	@Override
	public void acceptTermsOfUse() {
		view.acceptTermsOfUse();
	}
	
	
}
