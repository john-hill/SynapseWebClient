package org.sagebionetworks.web.client;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xhr.client.XMLHttpRequest;

public interface GWTWrapper {
	String getHostPageBaseURL();

	String getModuleBaseURL();

	void assignThisWindowWith(String url);

	String encodeQueryString(String queryString);

	XMLHttpRequest createXMLHttpRequest();

	NumberFormat getNumberFormat(String pattern);
	
	String getHostPrefix();
	
	String getCurrentURL();
}
