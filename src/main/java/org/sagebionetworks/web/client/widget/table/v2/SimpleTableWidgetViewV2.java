package org.sagebionetworks.web.client.widget.table.v2;

import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.repo.model.table.RowSet;
import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.shared.table.v2.RowChange;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * A Simple table widget can control all aspects of a Synapse table entity including:
 * Table ColumnModle CRUD, executing queries, viewing and editing query results.
 * This is the second version of the table.
 * 
 * @author dburdick (V1)
 * @author John (V2)
 *
 */
public interface SimpleTableWidgetViewV2 extends IsWidget, SynapseView {

	/**
	 * Set the presenter.
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);

	/**
	 * Set the query string used by this table.
	 * 
	 * @param query
	 */
	public void setQuery(String query);

	/**
	 * The contract used by the view to interact with the presenter.
	 *
	 */
	public interface Presenter {
		/**
		 * Execute the given query.
		 * @param query
		 */
		public void executeQuery(String query);
		
		/**
		 * Add a column to the current table.
		 * @param cm
		 */
		public void addColumn(ColumnModel cm);
		
		/**
		 * Remove the column identified by passed column Id.
		 * 
		 * @param columnId
		 */
		public void removeColumn(String columnId);
		
		/**
		 * Apply the passed RowSet as a change to the table.
		 * @param change
		 */
		public void applyTableChange(List<RowChange> changes);
		
		/**
		 * Cancel applying changes to a table.
		 */
		public void cancelTableChanges();
		
		/**
		 * Delete the rows identified by the passed rowIds.
		 * 
		 * @param rowIdsToRemove
		 */
		public void deleteRows(List<Long> rowIdsToRemove);
	}
	
	/**
	 * Set the query string to be displayed.
	 * 
	 * @param queryString
	 */
	public void setQueryString(String queryString);
	
}
