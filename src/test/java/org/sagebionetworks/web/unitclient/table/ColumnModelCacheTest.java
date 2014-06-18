package org.sagebionetworks.web.unitclient.table;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.org.json.AdapterFactoryImpl;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.table.ColumnModelCache;
import org.sagebionetworks.web.client.transform.JSONEntityFactory;
import org.sagebionetworks.web.client.transform.JSONEntityFactoryImpl;
import org.sagebionetworks.web.shared.exceptions.NotFoundException;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit test for the ColumnModelCache 
 * @author John
 *
 */
public class ColumnModelCacheTest {

	private static AdapterFactory adapterFactory = new AdapterFactoryImpl();
	private static JSONEntityFactory jsonEntityFactory = new JSONEntityFactoryImpl(adapterFactory);
	
	SynapseClientAsync mockSynapseClient;
	Map<String, ColumnModel> serverData;
	
	ColumnModelCache cache;
	
	@Before
	public void before(){
		mockSynapseClient = mock(SynapseClientAsync.class);
		// Stub the lookup from the server
		doAnswer(new Answer<Void>(){
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				List<String> ids = (List<String>) invocation.getArguments()[0];
				AsyncCallback<List<String>> asyncCallback = (AsyncCallback<List<String>>) invocation.getArguments()[1];
				// Build up the results
				List<String> results = new ArrayList<String>();
				for(String id: ids){
					ColumnModel cm = serverData.get(id);
					if(cm == null){
						asyncCallback.onFailure(new NotFoundException(id));
						return null;
					}
					results.add(jsonEntityFactory.createJsonStringForEntity(cm));
				}
				asyncCallback.onSuccess(results);
				return null;
			}}).when(mockSynapseClient).getColumnModels(any(List.class), any(AsyncCallback.class));
		// Create some data
		serverData = new HashMap<String, ColumnModel>();
		for(int i=0; i<5; i++){
			ColumnModel cm = new ColumnModel();
			cm.setId(""+i);
			cm.setName("n"+i);
			serverData.put(cm.getId(), cm);
		}
		cache = new ColumnModelCache(adapterFactory, mockSynapseClient);
	}
	
	@Test
	public void testRoundTrip(){
		List<String> ids = Arrays.asList("0","1");
		List<ColumnModel> captured = getColumnsFromCache(ids);
		assertEquals(2, captured.size());
		// there should be two columns in the cache.
		assertEquals(2, cache.getCacheSize());
		// If we ask for the same columns again, it should be a cache hit.
		captured = getColumnsFromCache(ids);
		assertEquals(2, captured.size());
		// there should be two columns in the cache.
		assertEquals(2, cache.getCacheSize());
		// Even though we accessed the cache twice, the server should have only been called once.
		verify(mockSynapseClient, times(1)).getColumnModels(any(List.class), any(AsyncCallback.class));
	}
	
	@Test
	public void testMixHitMiss(){
		List<String> ids = Arrays.asList("0","1");
		List<ColumnModel> captured = getColumnsFromCache(ids);
		assertEquals(2, captured.size());
		// there should be two columns in the cache.
		assertEquals(2, cache.getCacheSize());
		// This time ask for a new value
		captured = getColumnsFromCache(Arrays.asList("2","1"));
		assertEquals(2, captured.size());
		assertEquals("There should now be three columns in the cache",3, cache.getCacheSize());
		// Now get all three again
		captured = getColumnsFromCache(Arrays.asList("0","2","1"));
		assertEquals(3, captured.size());
		assertEquals(3, cache.getCacheSize());
		
		// The server should have been hit twice.
		verify(mockSynapseClient, times(2)).getColumnModels(any(List.class), any(AsyncCallback.class));
	}
	
	/**
	 * Helper to make a call to the cache.
	 * @param ids
	 * @return
	 */
	public List<ColumnModel> getColumnsFromCache(List<String> ids ){
		final List<ColumnModel> captured = new LinkedList<ColumnModel>();
		cache.getColumnModels(ids, new Callback<List<ColumnModel>, Throwable>() {
			
			@Override
			public void onSuccess(List<ColumnModel> result) {
				captured.addAll(result);
			}
			
			@Override
			public void onFailure(Throwable reason) {
				fail("Should not have failed");
				
			}
		});
		return captured;
	}
}
