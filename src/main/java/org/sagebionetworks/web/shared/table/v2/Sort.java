package org.sagebionetworks.web.shared.table.v2;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Sort implements IsSerializable {

	Long columnId;
	SortDirection direction;
	
}
