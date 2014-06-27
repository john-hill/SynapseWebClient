package org.sagebionetworks.web.client.widget.table.entity;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.SynapseJSNIUtils;
import org.sagebionetworks.web.shared.table.v2.RowChange;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TableEntityWidgetViewImpl extends Composite implements
		TableEntityWidgetView {

	public interface Binder extends UiBinder<Widget, TableEntityWidgetViewImpl> {
	}

	@UiField
	HTMLPanel buttonToolbar;
	@UiField
	SimplePanel columnEditorPanel;
	@UiField
	SimplePanel tableLoading;
	@UiField
	SimplePanel errorMessage;
	SageImageBundle sageImageBundle;
	Presenter presenter;
	SynapseJSNIUtils jsniUtils;
	TableColumnWidget tableColumnWidget;

	@Inject
	public TableEntityWidgetViewImpl(final Binder uiBinder,
			SageImageBundle sageImageBundle, SynapseJSNIUtils jsniUtils,
			PortalGinInjector ginInjector,
			TableColumnWidget tableColumnWidget
			) {
		this.sageImageBundle = sageImageBundle;
		this.jsniUtils = jsniUtils;
		initWidget(uiBinder.createAndBindUi(this));
		this.sageImageBundle = sageImageBundle;
		this.tableColumnWidget = tableColumnWidget;
	}

	@Override
	public void showLoading() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showInfo(String title, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQuery(String query) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showQueryErrorMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearQueryErrorMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RowChange> getRowChanges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelRowChanges() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSchema(String tableId, List<ColumnModel> schema) {
//		this.columnEditorPanel.add(this.tableColumnWidget.asWidget());

		List<SaveCancelWidget> children = new ArrayList<SaveCancelWidget>(3);
		children.add(new SampleSaveCanceWidget("one", "Start text one"));
		children.add(new SampleSaveCanceWidget("two", "Start text two"));
		children.add(new SampleSaveCanceWidget("three", "Start text three"));
		
		MultipleSaveCancelPanel mscp = new MultipleSaveCancelPanel("exampleId", children);
		this.columnEditorPanel.add(mscp);
//		this.columnEditorPanel.add(new Label("Testing...."));
		this.columnEditorPanel.setVisible(true);
		// Pass it along
		this.tableColumnWidget.configure(tableId, schema);
	}

}
