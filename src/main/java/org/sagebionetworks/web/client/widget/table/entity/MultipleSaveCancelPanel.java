package org.sagebionetworks.web.client.widget.table.entity;

import java.util.List;

import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.DisplayUtils.ButtonType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
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
			boolean last = index+1 == children.size();
			this.add(createChildContainer(panelId, child, index, last));
			index++;
		}
	}

	/**
	 * Build up a child
	 * @param parentId
	 * @param child
	 * @return
	 */
	private FlowPanel createChildContainer(String parentId, SaveCancelWidget child, int index, boolean last) {
		FlowPanel childPanel = new FlowPanel();
		childPanel.addStyleName("panel panel-default");
		FlowPanel panelHeading = new FlowPanel();
		panelHeading.addStyleName("panel-heading row");	
		childPanel.add(panelHeading);
		final String target = parentId+"-"+index;
		StringBuilder builder = new StringBuilder();
		builder.append("data-toggle=\"collapse\" data-parent=\"#").append(parentId).append("\" href=\"#").append(target).append("\"");
		String toggle = builder.toString();
		builder = new StringBuilder();
		builder.append("<h4 class=\"panel-title\">");
		builder.append("<span class=\"link\">");
		builder.append(SafeHtmlUtils.fromString(child.getDispalyName()).asString());
		builder.append("</span>");
		builder.append("</h4>");
		HTML panelTitle = new HTML(builder.toString());

		FlowPanel left = new FlowPanel();			
		left.addStyleName("col-xs-7 col-sm-9 col-md-10");
		left.add(panelTitle);

		FlowPanel right = new FlowPanel();
		right.addStyleName("col-xs-5 col-sm-3 col-md-2 text-align-right largeIconButton");
		panelHeading.add(left);
		panelHeading.add(right);
		
		//Save
		Button saveButton = DisplayUtils.createButton(DisplayConstants.SAVE_BUTTON_LABEL, ButtonType.PRIMARY);
		saveButton.addStyleName("margin-left-5");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DisplayUtils.showInfo("Save", "Clicked");
				
			}
		});
		
		Button cancelButton = DisplayUtils.createButton(DisplayConstants.BUTTON_CANCEL, ButtonType.DEFAULT);
		cancelButton.addStyleName("margin-left-5");
		
		right.add(cancelButton);
		right.add(saveButton);
		
		final FlowPanel targetPanel = new FlowPanel();
		targetPanel.getElement().setId(target);
		builder = new StringBuilder("panel-collapse collapse");
		if(last){
			builder.append(" in");
		}
		targetPanel.addStyleName(builder.toString());
		childPanel.add(targetPanel);
		
		FlowPanel targetPanelBody = new FlowPanel();
		targetPanelBody.addStyleName("panel-body");
		targetPanelBody.add(child);
		targetPanel.add(targetPanelBody);
		
		// clicking on panel should toggle
		left.sinkEvents(Event.ONCLICK);
		left.addHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				DisplayUtils.showInfo("Clicked: ", "Target: "+target);
				toggle("#"+target);
			}}, ClickEvent.getType());
		
		return childPanel;
	}
	
	public static native void toggle(String target) /*-{
	  	$wnd.$(target).collapse('toggle')
	}-*/;

}
