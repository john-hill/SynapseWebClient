package org.sagebionetworks.web.client;

import org.sagebionetworks.web.client.security.AuthenticationController;

import com.google.inject.Inject;

/**
 * Provides context for DisplayUtils functions.
 * @author John
 *
 */
public class DispalyUtilsContext {

	private AuthenticationController authenticationController;
	private GlobalApplicationState globalApplicationState;
	
	@Inject
	public DispalyUtilsContext(
			AuthenticationController authenticationController,
			GlobalApplicationState globalApplicationState) {
		super();
		this.authenticationController = authenticationController;
		this.globalApplicationState = globalApplicationState;
	}
	
	public AuthenticationController getAuthenticationController() {
		return authenticationController;
	}
	public GlobalApplicationState getGlobalApplicationState() {
		return globalApplicationState;
	}
	
	/**
	 * Handles services exception. If DisplayUtils does not handle the message
	 * the passed fallbackMessage will be shown.
	 * @param caught
	 * @param view
	 * @return
	 */
	public void handleServiceException(Throwable caught, SynapseView view, String fallbackMessage){
		if(! DisplayUtils.handleServiceException(caught, globalApplicationState, authenticationController.isLoggedIn(), view)){
			view.showErrorMessage(fallbackMessage);
		}
	}
	
}
