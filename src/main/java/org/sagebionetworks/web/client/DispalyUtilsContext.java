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
	 * Handles the exception. Returns true if the user has been alerted to the exception already
	 * @param caught
	 * @param view
	 * @return
	 */
	public boolean handleServiceException(Throwable caught, SynapseView view){
		return DisplayUtils.handleServiceException(caught, globalApplicationState, authenticationController.isLoggedIn(), view);
	}
	
}
