package org.sagebionetworks.web.client.widget.table.entity;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.client.DispalyUtilsContext;
import org.sagebionetworks.web.client.SynapseClientAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * This widget displays and manages the columns of a table.
 * It was extracted from the SimpleTableWidget.
 * 
 * @author John
 *
 */
public class TableColumnWidget implements TableColumnWidgetView.Presenter {
	
	private static final String FAILED_TO_APPLY_COLUMN_CHANGES_TO_TABLE = "Failed to apply column changes to table: ";
	private static final String FAILED_TO_RELOAD_COLUMNS = "Failed to reload columns: ";
	private DispalyUtilsContext displayUtilsContext;
	private TableColumnWidgetView view;
	private SynapseClientAsync synapseClient;
	private TableModelUtils tableUtils;
	private List<ColumnModel> columnModels;
	private String tableId;
	
	@Inject
	public TableColumnWidget(TableColumnWidgetView view,
			SynapseClientAsync synapseClient, TableModelUtils tableUtils, DispalyUtilsContext displayUtilsContex) {
		super();
		this.view = view;
		this.synapseClient = synapseClient;
		this.tableUtils = tableUtils;
		this.displayUtilsContext = displayUtilsContex;
	}

	@Override
	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();	
	}
	
	/**
	 * Configure this widget with the column models of a table entity.
	 * @param models
	 */
	public void configure(String tableId, List<ColumnModel> models){
		if(models == null){
			models = new ArrayList<ColumnModel>();
		}
		this.columnModels = models;
		this.tableId = tableId;
		// Start with the view non-editable
		view.showLoading();

		// pass this along to the view
		view.setColumns(this.columnModels);
	}
	
	/**
	 * Reload if something goes wrong.
	 */
	private void reload(){
		// disable the view.
		view.showLoading();
		synapseClient.getColumnModelsForTableEntity(this.tableId, new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				try {
					// The new column models.
					columnModels = tableUtils.columnModelFromJSON(result);
					// Pass to the view
					view.setColumns(columnModels);
				} catch (JSONObjectAdapterException e) {
					displayUtilsContext.handleServiceException(e, view, FAILED_TO_RELOAD_COLUMNS+e.getMessage());
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				displayUtilsContext.handleServiceException(caught, view, FAILED_TO_RELOAD_COLUMNS+caught.getMessage());
			}
		});
	}

	@Override
	public void applyColumns(List<ColumnModel> columns) {
		// Set the view to loading
		view.showLoading();
		try {
			
			List<String> jsons = tableUtils.toJSONList(columns);
			synapseClient.setTableSchema(this.tableId, jsons, new AsyncCallback<List<String>>() {
				
				@Override
				public void onSuccess(List<String> results) {
					try {
						columnModels = tableUtils.columnModelFromJSON(results);
					} catch (JSONObjectAdapterException e) {
						displayUtilsContext.handleServiceException(e, view, FAILED_TO_APPLY_COLUMN_CHANGES_TO_TABLE+e.getMessage());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					displayUtilsContext.handleServiceException(caught, view, FAILED_TO_APPLY_COLUMN_CHANGES_TO_TABLE+caught.getMessage());
				}
			});
		} catch (JSONObjectAdapterException e) {
			displayUtilsContext.handleServiceException(e, view, FAILED_TO_APPLY_COLUMN_CHANGES_TO_TABLE+e.getMessage());
		}
	}

	@Override
	public void cancelEdit() {
		// Reload on cancel
		reload();
	}

}
