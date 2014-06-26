package org.sagebionetworks.web.client.widget.table.entity;

import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.client.widget.SynapseWidgetPresenter;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Abstraction for the managing table columns.
 * 
 * @author John
 *
 */
public interface TableColumnWidgetView extends IsWidget, SynapseView{
	
	public interface Presenter extends SynapseWidgetPresenter{
		/**
		 * Apply the column models to a table.
		 * @param columnIds
		 */
		public void applyColumns(List<ColumnModel> columnIds);
		
		/**
		 * If the user clicks cancel, then reload the widget.
		 */
		public void cancelEdit();
	}
	
	/**
	 * Set the current columns of a table.
	 * 
	 * @param columns
	 */
	public void setColumns(List<ColumnModel> columns);
	
	/**
	 * This will be called after an apply has failed.
	 */
	public void applyFailed();
	
	
	/**
	 * Set the presenter.
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);

}
