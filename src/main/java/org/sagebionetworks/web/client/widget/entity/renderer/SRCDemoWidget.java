package org.sagebionetworks.web.client.widget.entity.renderer;

import static org.sagebionetworks.web.client.ClientProperties.PROP_TYPES_JS;
import static org.sagebionetworks.web.client.ClientProperties.REACT_MEASURE_JS;
import static org.sagebionetworks.web.client.ClientProperties.REACT_TOOLTIP_JS;
import static org.sagebionetworks.web.client.ClientProperties.SYNAPSE_REACT_COMPONENTS_JS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.web.client.resources.ResourceLoader;
import org.sagebionetworks.web.client.resources.WebResource;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.widget.WidgetRendererPresenter;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.shared.WikiPageKey;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author Jay
 *
 */
public class SRCDemoWidget implements SRCDemoWidgetView.Presenter, WidgetRendererPresenter {
	private SRCDemoWidgetView view;
	private Map<String, String> descriptor;
	private SynapseAlert synAlert;
	private ResourceLoader resourceLoader;
	
	@Inject
	public SRCDemoWidget(SRCDemoWidgetView view,
			SynapseAlert synAlert,
			ResourceLoader resourceLoader) {
		this.view = view;
		this.synAlert = synAlert;
		this.resourceLoader = resourceLoader;
		view.setSynAlertWidget(synAlert);
		view.setPresenter(this);
	}
	
	public void clear() {
		synAlert.clear();
		view.setDemoVisible(false);
	}
	
	@Override
	public void configure(WikiPageKey wikiKey, Map<String, String> widgetDescriptor, Callback widgetRefreshRequired, Long wikiVersionInView) {
		//set up view based on descriptor parameters
		descriptor = widgetDescriptor;
		clear();
		
		//gather the data, and then show the chart
		view.setLoadingVisible(true);
		view.setLoadingMessage("Loading...");
		showDemo();
	}
	
	public void showDemo() {
		view.setLoadingVisible(true);
		AsyncCallback<Void> initializedCallback = new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				if (resourceLoader.isLoaded(SYNAPSE_REACT_COMPONENTS_JS) && 
						resourceLoader.isLoaded(PROP_TYPES_JS) && 
						resourceLoader.isLoaded(REACT_MEASURE_JS) &&
						resourceLoader.isLoaded(REACT_TOOLTIP_JS)) {
					showDemo();	
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				synAlert.handleException(caught);
			}
		};
		
		if (!resourceLoader.isLoaded(SYNAPSE_REACT_COMPONENTS_JS)) {
			List<WebResource> resources = new ArrayList<>();
			resources.add(PROP_TYPES_JS);
			resources.add(REACT_MEASURE_JS);
			resources.add(REACT_TOOLTIP_JS);
			resources.add(SYNAPSE_REACT_COMPONENTS_JS);
			resourceLoader.requires(resources, initializedCallback);
			return;
		}
		
		try {
			view.setLoadingVisible(false);
			view.setDemoVisible(true);
		} catch (Throwable ex) {
			synAlert.showError("Error showing table: " + ex.getMessage());
		}
	}
	

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
