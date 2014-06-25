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
		 * Create a new ColumnModel
		 * @param cm
		 */
		public void createNewColumn(ColumnModel cm);
		
		/**
		 * Update the order of the columns of the table.
		 * @param columnIds
		 */
		public void updateColumnOrder(List<String> columnIds);
	}
	
	/**
	 * Set the current columns of a table.
	 * 
	 * @param columns
	 */
	public void setColumns(List<ColumnModel> columns);
	
	/**
	 * Enable or disable editing of the widget.
	 * @param enabled
	 */
	public void setEditable(boolean enabled);

	/**
	 * Set the presenter.
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);

}
