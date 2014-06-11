package org.sagebionetworks.web.shared.table.v2;

import java.util.List;

/**
 * A set of changes to apply to a table entity.
 * 
 * @author John
 *
 */
public class TableChangeSet {

	String tableId;
	List<RowChange> changes;
	
} 
