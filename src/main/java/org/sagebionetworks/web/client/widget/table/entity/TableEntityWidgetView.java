package org.sagebionetworks.web.client.widget.table.entity;

import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.client.widget.WidgetRendererPresenter;
import org.sagebionetworks.web.shared.table.v2.RowChange;
import org.sagebionetworks.web.shared.table.v2.Sort;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * A Simple table widget can control all aspects of a Synapse table entity including:
 * Table ColumnModle CRUD, executing queries, viewing and editing query results.
 * This is the second version of the table.
 * 
 * @author John
 *
 */
public interface TableEntityWidgetView extends IsWidget, SynapseView {

	/**
	 * The contract used by the view to interact with the presenter.
	 *
	 */
	public interface Presenter extends WidgetRendererPresenter {
		/**
		 * Execute the given query.
		 * @param query
		 */
		public void executeQuery();
		
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
		public void applyTableChange();
		
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
		
		/**
		 * Load the next page.
		 */
		public void nextPage();
		/**
		 * Load the previous page.
		 */
		public void previousPage();
		
		/**
		 * Load the last page.
		 */
		public void lastPage();
		
		/**
		 * Load the first page.
		 */
		public void firstPage();
		/**
		 * Set the new primary sort.
		 * @param sort
		 */
		public void setPrimarySort(Sort sort);
	}
	
	/**
	 * Get the current query string.
	 * @return
	 */
	public String getQueryString();
	
	/**
	 * Show a query error message.
	 * @param message
	 */
	public void showQueryErrorMessage(String message);
	
	/**
	 * 
	 */
	public void clearQueryErrorMessage();
	
	/**
	 * Get the current row changes.
	 * 
	 * @return
	 */
	public List<RowChange> getRowChanges();
	
	/**
	 * Revert any table changes.
	 */
	public void cancelRowChanges();

	/**
	 * Notify the view of the table schema.
	 * @param schema
	 */
	public void setSchema(String tableId, List<ColumnModel> schema);
	
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
	
	
}
