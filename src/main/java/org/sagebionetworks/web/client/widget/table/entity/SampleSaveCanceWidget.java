package org.sagebionetworks.web.client.widget.table.entity;

import com.extjs.gxt.ui.client.widget.Label;
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
	private SavableListener listener;
	
	public SampleSaveCanceWidget(String displayName, String text){
		this.dispalyName = displayName;
		this.text = text;
		Label label = new Label();
		label.setText("field");
		this.add(label);
		editor = new TextBox();
		editor.setValue(text);
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
	public void addEditListener(SavableListener listener) {
		this.listener = listener;
	}

	@Override
	public String getDispalyName() {
		return dispalyName;
	}

	
}
