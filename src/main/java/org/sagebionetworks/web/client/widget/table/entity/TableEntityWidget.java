package org.sagebionetworks.web.client.widget.table.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.repo.model.table.TableEntity;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.client.DispalyUtilsContext;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.widget.WidgetRendererPresenter;
import org.sagebionetworks.web.shared.WikiPageKey;
import org.sagebionetworks.web.shared.table.v2.Sort;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A basic TableEntity widget.  With all feature enabled, it will do both column and row CRUD.
 * 
 * 
 * @author John
 *
 */
public class TableEntityWidget implements TableEntityWidgetView.Presenter, WidgetRendererPresenter{
	
	private TableEntityWidgetView view;
	private TableEntity tableEntity;
	private SynapseClientAsync synapseClient;
	private AdapterFactory adapterFactory;
	private List<ColumnModel> schema;
	private DispalyUtilsContext displayUtilsContext;
	

	@Inject
	public TableEntityWidget(TableEntityWidgetView view,
			SynapseClientAsync synapseClient, AdapterFactory adapterFactory,
			DispalyUtilsContext displayUtilsContext) {
		super();
		this.view = view;
		this.synapseClient = synapseClient;
		this.adapterFactory = adapterFactory;
		this.displayUtilsContext = displayUtilsContext;
	}


	public void configure(TableEntity entity, boolean canEdit){
		this.tableEntity = entity;
		reloadColumnModels();
	}


	private void reloadColumnModels() {
		// Lookup the column models for this table
		if(tableEntity.getColumnIds() !=null && tableEntity.getColumnIds().size() > 0){
			// This table has columns so fetch them
			synapseClient.getColumnModelsForTableEntity(tableEntity.getId(), new AsyncCallback<List<String>>() {
				@Override
				public void onSuccess(List<String> result) {
					try {
						schema = new ArrayList<ColumnModel>(result.size());
						for(String colStr : result) {
							schema.add(new ColumnModel(adapterFactory.createNew(colStr)));
						}
						view.setSchema(schema);
					} catch (JSONObjectAdapterException e) {
						onFailure(e);
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					// There is no schema to show.
					view.setSchema(null);
					if(!displayUtilsContext.handleServiceException(caught, view)){
						view.showErrorMessage(DisplayConstants.ERROR_GENERIC_RELOAD + ": " + caught.getMessage());
					}
				}
			});
		}
	}
	
	@Override
	public void executeQuery() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addColumn(ColumnModel cm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeColumn(String columnId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyTableChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelTableChanges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRows(List<Long> rowIdsToRemove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void previousPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lastPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void firstPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrimarySort(Sort sort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();		
	}

	@Override
	public void configure(WikiPageKey wikiKey,
			Map<String, String> widgetDescriptor,
			Callback widgetRefreshRequired, Long wikiVersionInView) {
		// TODO Auto-generated method stub
		
	}

}
