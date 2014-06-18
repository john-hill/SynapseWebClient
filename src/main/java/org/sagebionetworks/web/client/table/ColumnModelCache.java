package org.sagebionetworks.web.client.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.client.SynapseClientAsync;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * ColumnModle objects are immutable, making them ideal for client-side caching.
 * 
 * @author John
 *
 */
public class ColumnModelCache {
	
	private AdapterFactory adapterFactory;
	private SynapseClientAsync synapseClient;
	private Map<String, ColumnModel> cache = new HashMap<String, ColumnModel>();

	@Inject
	public ColumnModelCache(AdapterFactory adapterFactory,
			SynapseClientAsync synapseClient) {
		super();
		this.adapterFactory = adapterFactory;
		this.synapseClient = synapseClient;
	}

	/**
	 * Get the requested a list of ColumnModels by ID.
	 * If the requested columns are already in the cache, the callback will be called in-line.
	 * For a cache miss, the Columns will be fetched, then passed along to the provided callback.
	 * @param ids
	 * @param callback
	 */
	public void getColumnModels(List<String> ids, Callback<List<ColumnModel>, Throwable> callback){
		// Are all of the values in the cache?
		List<ColumnModel> results = new ArrayList<ColumnModel>(ids.size());
		List<String> misses = new LinkedList<String>();
		for(String id: ids){
			ColumnModel cm = cache.get(id);
			if(cm == null){
				// Cache miss
				misses.add(id);
			}else{
				// Cache hit
				results.add(cm);
			}
		}
		// If we have any misses then we need to fetch the data from the server
		if(misses.isEmpty()){
			// All of the results were in the cache.
			callback.onSuccess(results);
		}else{
			// Hit up the server the misses
			asynchFetch(ids, misses, callback);
		}

	}
	
	/**
	 * Fetch the misses from the server, then return all of the results to the caller.
	 * @param all
	 * @param misses
	 * @param callback
	 */
	private void asynchFetch(final List<String> all, List<String> misses, final Callback<List<ColumnModel>, Throwable> callback){
		// Fetch the misses from the server
		synapseClient.getColumnModels(misses, new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> result) {
				try {
					// Add all of the misses to the cache
					for(String colStr : result) {
						ColumnModel cm = new ColumnModel(adapterFactory.createNew(colStr));
						cache.put(cm.getId(), cm);
					}
					// All of the columns should now be in the cache.
					List<ColumnModel> results = new ArrayList<ColumnModel>(all.size());
					for(String id: all){
						ColumnModel cm = cache.get(id);
						if(cm == null){
							callback.onFailure(new IllegalStateException("ColumnModel cache was corrupted"));
							return;
						}
						results.add(cm);
					}
					// Give the caller the results
					callback.onSuccess(results);
				} catch (JSONObjectAdapterException e) {
					onFailure(e);
				}
			}
			
			@Override
			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}
		});
	}
	
	/**
	 * The current size of the cache.
	 * @return
	 */
	public int getCacheSize(){
		return this.cache.size();
	}
}
