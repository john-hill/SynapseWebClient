package org.sagebionetworks.web.client.widget.table.entity;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.repo.model.table.TableEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.model.EntityBundle;
import org.sagebionetworks.web.shared.EntityBundleTransport;

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
	
	private TableColumnWidgetView view;
	private SynapseClientAsync synapseClient;
	private TableModelUtils tableJSONUtils;
	private List<ColumnModel> columnModels;
	private TableEntity tableEntity;
	
	@Inject
	public TableColumnWidget(TableColumnWidgetView view,
			SynapseClientAsync synapseClient, TableModelUtils tableJSONUtils) {
		super();
		this.view = view;
		this.synapseClient = synapseClient;
		this.tableJSONUtils = tableJSONUtils;
	}

	@Override
	public void createNewColumn(ColumnModel cm) {
		// First disable the view
		view.setEditable(false);
		try {
			String columnJson = tableJSONUtils.toJSON(cm);
			String tableJson = tableJSONUtils.toJSON(tableEntity);
			synapseClient.createColumnModelAndAddToTable(tableJson, columnJson, new AsyncCallback<EntityBundleTransport>(){

				@Override
				public void onFailure(Throwable caught) {
					view.showErrorMessage("Failed to create a column: "+caught.getMessage());
					reload();
				}

				@Override
				public void onSuccess(EntityBundleTransport transport) {
					try {
						// configure again
						configure(transport);
					} catch (JSONObjectAdapterException e) {
						view.showErrorMessage(DisplayConstants.ERROR_INCOMPATIBLE_CLIENT_VERSION);
					}

					
				}});
		} catch (JSONObjectAdapterException e) {
			view.showErrorMessage(DisplayConstants.ERROR_INCOMPATIBLE_CLIENT_VERSION);
			reload();
		}

	}

	@Override
	public void updateColumnOrder(List<String> columnIds) {
		// First disable the view
		view.setEditable(false);
		
	}

	@Override
	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();	
	}
	
	public void configure(EntityBundleTransport transport) throws JSONObjectAdapterException{
		EntityBundle bundle = tableJSONUtils.bundleFromTransport(transport);
		configure((TableEntity) bundle.getEntity(), bundle.getTableBundle().getColumnModels());
	}
	
	
	/**
	 * Configure this widget with the column models of a table entity.
	 * @param models
	 */
	public void configure(TableEntity tableEntity, List<ColumnModel> models){
		if(models == null){
			models = new ArrayList<ColumnModel>();
		}
		this.columnModels = models;
		this.tableEntity = tableEntity;
		// Start with the view non-editable
		view.setEditable(false);

		// pass this along to the view
		view.setColumns(this.columnModels);
		// Make the view editable
		view.setEditable(true);
	}
	
	/**
	 * Reload if something goes wrong.
	 */
	private void reload(){
		// disable the view.
		view.setEditable(false);
		// Load the columns from the table
		int partsMask = EntityBundleTransport.ENTITY + EntityBundleTransport.TABLE_DATA;
		synapseClient.getEntityBundle(tableEntity.getId(), partsMask, new AsyncCallback<EntityBundleTransport>(){

			@Override
			public void onFailure(Throwable caught) {
				view.showErrorMessage("Failed to reload: "+caught.getMessage());
			}

			@Override
			public void onSuccess(EntityBundleTransport transport) {
				try {
					configure(transport);
				} catch (JSONObjectAdapterException e) {
					view.showErrorMessage(DisplayConstants.ERROR_INCOMPATIBLE_CLIENT_VERSION);
				}
				
			}});
	}

}
