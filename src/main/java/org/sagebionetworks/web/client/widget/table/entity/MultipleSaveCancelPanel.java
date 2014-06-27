package org.sagebionetworks.web.client.widget.table.entity;

import java.util.List;

import org.sagebionetworks.web.client.DisplayConstants;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * This is an accordion panel that can contain multiple Widgets each with a save
 * cancel. Only one widget will be active at a time, so the user will never see
 * multiple save-cancel buttons.
 * 
 * @author jmhill
 * 
 */
public class MultipleSaveCancelPanel extends FlowPanel {

	/**
	 * Create a new MultipleSaveCancelPanel providing the children.
	 * 
	 * @param panelId
	 *            Provide a unique ID for this panel. This controls the
	 *            accordion grouping of all children.
	 * @param children
	 */
	public MultipleSaveCancelPanel(String panelId,
			List<SaveCancelWidget> children) {
		// setup the ID
		this.getElement().setId(panelId);
		this.addStyleName("panel-group");
		// Add each child
		int index = 0;
		for(SaveCancelWidget child: children){
			this.add(createChildContainer(panelId, child, index++));
		}
	}

	/**
	 * Build up a child
	 * @param parentId
	 * @param child
	 * @return
	 */
	private FlowPanel createChildContainer(String parentId, SaveCancelWidget child, int index) {
		
//		if(true){
//			FlowPanel childPanel = new FlowPanel();
//			childPanel.add(new HTML("<h4>"+child.getDispalyName()+"</h4>"));
//			return childPanel;
//		}
		
		FlowPanel childPanel = new FlowPanel();
		childPanel.addStyleName("panel panel-default");
		FlowPanel panelHeading = new FlowPanel();
		panelHeading.addStyleName("panel-heading row");	
		childPanel.add(panelHeading);
		String target = parentId+"-"+index;
		StringBuilder builder = new StringBuilder();
		builder.append("<h4 class=\"panel-title\">");
		builder.append("<a data-toggle=\"collapse\" data-parent=\"#").append(parentId).append("\" href=\"#").append(target).append(">");
		builder.append(SafeHtmlUtils.fromString(child.getDispalyName()).asString());
		builder.append("</a>");
		builder.append("</h4>");
		HTML panelTitle = new HTML("<h4>"+SafeHtmlUtils.fromString(child.getDispalyName()).asString()+"</h4>");


		FlowPanel left = new FlowPanel();			
		left.addStyleName("col-xs-7 col-sm-9 col-md-10");
		left.add(panelTitle);
		FlowPanel right = new FlowPanel();
		right.addStyleName("col-xs-5 col-sm-3 col-md-2 text-align-right largeIconButton");
		panelHeading.add(left);
		panelHeading.add(right);
		
		//Save
		Button saveButton = new Button();
		saveButton.addStyleName("btn-primary margin-right-5");
		saveButton.setText(DisplayConstants.SAVE_BUTTON_LABEL);
		
		Button cancelButton = new Button();
		cancelButton.addStyleName("btn-default margin-right-5");
		cancelButton.setText("Cancel");
		
		right.add(cancelButton);
		right.add(saveButton);
		
		FlowPanel targetPanel = new FlowPanel();
		targetPanel.getElement().setId(target);
		targetPanel.addStyleName("panel-collapse collapse in");
		childPanel.add(targetPanel);
		
		FlowPanel targetPanelBody = new FlowPanel();
		targetPanelBody.addStyleName("panel-body");
		targetPanelBody.add(child);
		targetPanel.add(targetPanelBody);
		
		return childPanel;
	}

}
