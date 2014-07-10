package org.sagebionetworks.web.client.widget.table.entity;

import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A sample implementation of a save of a 
 * @author jmhill
 *
 */
public class SampleSaveCanceWidget extends FlowPanel implements SaveCancelWidget {

	private String dispalyName;
	private String text;
	private TextBox editor;
	private ChangeListener listener;
	
	public SampleSaveCanceWidget(String displayName, String text){
		this.dispalyName = displayName;
		this.text = text;
		Label label = new Label();
		label.setText("Change Me:");
		label.addStyleName("margin-left-5");
		this.add(label);
		editor = new TextBox();
		editor.setValue(text);
		editor.addStyleName("margin-left-5");
		this.add(editor);
	}
	
	@Override
	public void onSave() {
		// Save the text
		text = editor.getText();
	}

	@Override
	public void onCancel() {
		editor.setText(text);
	}

	@Override
	public void addEditListener(final ChangeListener listener) {
		this.listener = listener;
		this.editor.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				listener.setHasChanges(true);	
			}
		});
	}

	@Override
	public String getDispalyName() {
		return dispalyName;
	}

	
}
