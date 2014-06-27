package org.sagebionetworks.web.client.widget.table.entity;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.DisplayUtils.ButtonType;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.SynapseJSNIUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TableColumnWidgetViewImpl extends Composite implements TableColumnWidgetView {
	
	public interface Binder extends UiBinder<Widget, TableColumnWidgetViewImpl> {	}
	
	@UiField
	SimplePanel mainPanel;
	
	SageImageBundle sageImageBundle;
	Presenter presenter;
	SynapseJSNIUtils jsniUtils;	
	List<ColumnDetailsPanel> columnPanelOrder;
	FlowPanel addColumnPanel;
	private static int sequence = 0;
	
	@Inject
	public TableColumnWidgetViewImpl(final Binder uiBinder, SageImageBundle sageImageBundle, SynapseJSNIUtils jsniUtils, PortalGinInjector ginInjector) {
		this.sageImageBundle = sageImageBundle;
		this.jsniUtils = jsniUtils;
		initWidget(uiBinder.createAndBindUi(this));
		this.sageImageBundle = sageImageBundle;
	}

	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showInfo(String title, String message) {
		DisplayUtils.showInfo(title, message);
	}

	@Override
	public void showErrorMessage(String message) {
		DisplayUtils.showErrorMessage(message);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColumns(List<ColumnModel> columns) {
//		Widget editor = buildColumnsEditor(columns);
//		mainPanel.add(editor);
	
		mainPanel.add(new Label("Testing..."));
		
		mainPanel.setVisible(true);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void applyFailed() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Builds a widget for the column editor/view panel
	 * @param columns
	 * @return
	 */
	private Widget buildColumnsEditor(List<ColumnModel> columns) {
		FlowPanel parent = new FlowPanel();
		parent.addStyleName("panel-group");
		String accordionId = "accordion-" + ++sequence;
		parent.getElement().setId(accordionId);
		
		// add header
		parent.add(new HTML("<h4>" + DisplayConstants.COLUMN_DETAILS + "</h4>"));
		
		final FlowPanel allColumnsPanel = new FlowPanel();
		columnPanelOrder = new ArrayList<ColumnDetailsPanel>();
		for(int i=0; i<columns.size(); i++) {
			final ColumnModel col = columns.get(i);			
			final ColumnDetailsPanel columnPanel = new ColumnDetailsPanel(accordionId, col, "contentId" + ++sequence);
			
			columnPanel.getMoveUp().addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					// swap columns
					int formerIdx = columnPanelOrder.indexOf(columnPanel);
					TableViewUtils.swapColumns(columnPanelOrder, allColumnsPanel, columnPanel, formerIdx, formerIdx-1, null);
				}

			});
			columnPanel.getMoveDown().addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					// swap columns
					int formerIdx = columnPanelOrder.indexOf(columnPanel);
					TableViewUtils.swapColumns(columnPanelOrder, allColumnsPanel, columnPanel, formerIdx, formerIdx+1, null);
				}

			});
			columnPanel.getDelete().addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					columnPanel.addStyleName("fade");
					// allow for fade before removal
					Timer t = new Timer() {								
						@Override
						public void run() {
							allColumnsPanel.remove(columnPanel);
							columnPanelOrder.remove(columnPanel);
							
							// update ends, if needed
							int size = columnPanelOrder.size();
							if(size > 0) {
								TableViewUtils.setArrowVisibility(0, size, columnPanelOrder.get(0).getMoveUp(), columnPanelOrder.get(0).getMoveDown());
								TableViewUtils.setArrowVisibility(size-1, size, columnPanelOrder.get(size-1).getMoveUp(), columnPanelOrder.get(size-1).getMoveDown());
							}
						}
					};
					t.schedule(250);
				}
			});
			if(i==0) columnPanel.getMoveUp().setVisible(false);
			if(i==columns.size()-1) columnPanel.getMoveDown().setVisible(false); 
			
			columnPanelOrder.add(columnPanel);
			allColumnsPanel.add(columnPanel);
		}
		parent.add(allColumnsPanel);

		// Add Column
		addColumnPanel = new FlowPanel();
		addColumnPanel.addStyleName("well margin-top-15");		

		Button addColumnBtn = DisplayUtils.createIconButton(DisplayConstants.ADD_COLUMN, ButtonType.DEFAULT, "glyphicon-plus");
		addColumnBtn.addStyleName("margin-top-15");	
		addColumnBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if(addColumnPanel.isVisible()) addColumnPanel.setVisible(false);
				else addColumnPanel.setVisible(true);
			}
		});
		parent.add(addColumnBtn);
		parent.add(addColumnPanel);
		
		return parent;
	}

}
