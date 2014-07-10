package org.sagebionetworks.web.client.widget.table.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class MultipleSaveCancelPanel extends FlowPanel implements MultipleSaveCancelView {

	
	private Presenter presenter;
	private Map<String, SubView> subViewMap = new HashMap<String, SubView>();
	
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
		// Create the presenter
		presenter = new MultipleSaveCancelViewPresenter(this);
		// setup the ID
		this.getElement().setId(panelId);
		this.addStyleName("panel-group");
		// Add each child
		int index = 0;
		for (SaveCancelWidget child : children) {
			boolean last = index + 1 == children.size();
			this.add(createChildContainer(panelId, child, index, last));
			index++;
		}
		// Activate the last view.
		presenter.resetToggle();
	}

	/**
	 * Build up a child
	 * 
	 * @param parentId
	 * @param child
	 * @return
	 */
	private FlowPanel createChildContainer(String parentId,
			SaveCancelWidget child, int index, boolean last) {
		FlowPanel childPanel = new FlowPanel();
		childPanel.addStyleName("panel panel-default");
		FlowPanel panelHeading = new FlowPanel();
		panelHeading.addStyleName("panel-heading row");
		panelHeading.setHeight("50px");
		childPanel.add(panelHeading);
		final String target = parentId + "-" + index;
		StringBuilder builder = new StringBuilder();
		builder.append("<h4 class=\"panel-title\">");
		builder.append("<span class=\"link\">");
		builder.append(SafeHtmlUtils.fromString(child.getDispalyName())
				.asString());
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

		// Save
		Button saveButton = DisplayUtils.createButton(
				DisplayConstants.SAVE_BUTTON_LABEL, ButtonType.PRIMARY);
		saveButton.addStyleName("margin-left-5");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// pass the save to the presenter
				presenter.onSave(target);
			}
		});
		saveButton.setVisible(false);

		Button cancelButton = DisplayUtils.createButton(
				DisplayConstants.BUTTON_CANCEL, ButtonType.DEFAULT);
		cancelButton.addStyleName("margin-left-5");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// pass the save to the presenter
				presenter.onCancel(target);
			}
		});
		cancelButton.setVisible(false);

		
		right.add(cancelButton);
		right.add(saveButton);

		final FlowPanel targetPanel = new FlowPanel();
		targetPanel.getElement().setId(target);
		builder = new StringBuilder();
		builder.append("panel-collapse collapse");
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
		panelHeading.sinkEvents(Event.ONCLICK);
		panelHeading.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.attemptToggleSubView(target);
			}
		}, ClickEvent.getType());
		// Save the child view
		SubView subView = new SubView(target, cancelButton, saveButton, childPanel);
		this.subViewMap.put(target, subView);
		// Register this sub-view with the presenter
		presenter.registerSaveCancelWidget(target, child);
		return childPanel;
	}

	public static native void hideView(String target) /*-{
		$wnd.$('#'+target).collapse('hide')
	}-*/;

	public static native void showView(String target) /*-{
		$wnd.$('#'+target).collapse('show')
	}-*/;

	@Override
	public void enableSaveCancel(String subViewId, boolean enabled) {
		SubView sub = this.subViewMap.get(subViewId);
		sub.cancelButton.setEnabled(enabled);
		sub.saveButton.setEnabled(enabled);
		sub.cancelButton.setVisible(enabled);
		sub.saveButton.setVisible(enabled);
//		if(enabled){
//			sub.childPanel.addStyleName("alert alert-danger");
//		}else{
//			sub.childPanel.removeStyleName("alert alert-danger");
//		}
	}

	@Override
	public void showInfo(String title, String message) {
//		DisplayUtils.showInfoDialog(title, message, null);
		DisplayUtils.showInfo(title, message);
	}

	@Override
	public void collapseView(String viewId) {
		// Hide the buttons
		SubView sub = this.subViewMap.get(viewId);
		sub.cancelButton.setVisible(false);
		sub.saveButton.setVisible(false);
		hideView(viewId);
	}

	@Override
	public void expandView(String viewId) {
		showView(viewId);
		// show the buttons
		SubView sub = this.subViewMap.get(viewId);
//		sub.cancelButton.setVisible(true);
//		sub.saveButton.setVisible(true);
	}
	
	/**
	 * The elements of a sub-view.
	 *
	 */
	private static class SubView {
		String targetId;
		Button cancelButton;
		Button saveButton;
		FlowPanel childPanel;
		public SubView(String targetId, Button cancelButton, Button saveButton,
				FlowPanel childPanel) {
			super();
			this.targetId = targetId;
			this.cancelButton = cancelButton;
			this.saveButton = saveButton;
			this.childPanel = childPanel;
		}
	}

	@Override
	public void showError(String string) {
		DisplayUtils.showErrorMessage(string);	
	}

}
