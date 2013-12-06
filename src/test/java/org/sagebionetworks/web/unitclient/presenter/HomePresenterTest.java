package org.sagebionetworks.web.unitclient.presenter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.sagebionetworks.repo.model.AutoGenFactory;
import org.sagebionetworks.repo.model.BatchResults;
import org.sagebionetworks.repo.model.EntityHeader;
import org.sagebionetworks.repo.model.RSSEntry;
import org.sagebionetworks.repo.model.RSSFeed;
import org.sagebionetworks.repo.model.Team;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.JSONObjectAdapterImpl;
import org.sagebionetworks.web.client.GWTWrapper;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.PlaceChanger;
import org.sagebionetworks.web.client.RequestBuilderWrapper;
import org.sagebionetworks.web.client.RssServiceAsync;
import org.sagebionetworks.web.client.SearchServiceAsync;
import org.sagebionetworks.web.client.StackConfigServiceAsync;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.SynapseJSNIUtils;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.place.Home;
import org.sagebionetworks.web.client.presenter.HomePresenter;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.utils.CallbackP;
import org.sagebionetworks.web.client.view.HomeView;
import org.sagebionetworks.web.shared.MembershipInvitationBundle;
import org.sagebionetworks.web.shared.exceptions.RestServiceException;
import org.sagebionetworks.web.test.helper.AsyncMockStubber;
import org.sagebionetworks.web.unitclient.widget.entity.team.TeamListWidgetTest;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HomePresenterTest {

	HomePresenter homePresenter;
	CookieProvider cookieProvider;
	HomeView mockView;
	AuthenticationController mockAuthenticationController;
	GlobalApplicationState mockGlobalApplicationState;
	PlaceChanger mockPlaceChanger;
	StackConfigServiceAsync mockStackConfigService;
	RssServiceAsync mockRssService;
	RequestBuilderWrapper mockRequestBuilder;
	SearchServiceAsync mockSearchService; 
	SynapseClientAsync mockSynapseClient;
	CookieProvider mockCookies;
	AutoGenFactory autoGenFactory;
	SynapseJSNIUtils mockSynapseJSNIUtils;
	GWTWrapper mockGwtWrapper;
	JSONObjectAdapter adapter = new JSONObjectAdapterImpl();
	
	List<EntityHeader> testEvaluationResults;
	List<MembershipInvitationBundle> openInvitations;
	
	RSSFeed testFeed = null;
	String testTeamId = "42";
	@Before
	public void setup() throws RestServiceException, JSONObjectAdapterException{
		mockView = mock(HomeView.class);
		cookieProvider = mock(CookieProvider.class);
		mockAuthenticationController = mock(AuthenticationController.class);
		mockGlobalApplicationState = mock(GlobalApplicationState.class);
		mockPlaceChanger = mock(PlaceChanger.class);
		mockRssService = mock(RssServiceAsync.class);
		mockSearchService = mock(SearchServiceAsync.class);
		mockSynapseClient = mock(SynapseClientAsync.class);
		mockSynapseJSNIUtils = mock(SynapseJSNIUtils.class);
		mockGwtWrapper = mock(GWTWrapper.class);
		mockRequestBuilder = mock(RequestBuilderWrapper.class);
		mockCookies = mock(CookieProvider.class);
		when(mockSynapseJSNIUtils.getBaseFileHandleUrl()).thenReturn("http://synapse.org/filehandle/");
		
		autoGenFactory = new AutoGenFactory();
		BatchResults<EntityHeader> testBatchResults = new BatchResults<EntityHeader>();
		testEvaluationResults = new ArrayList<EntityHeader>();
		EntityHeader testEvaluation = new EntityHeader();
		testEvaluation.setId("eval project id 1");
		testEvaluation.setName("My Test Evaluation Project");
		testEvaluationResults.add(testEvaluation);
		testBatchResults.setTotalNumberOfResults(1);
		testBatchResults.setResults(testEvaluationResults);
		
		ArrayList<String> testBatchResultsList = new ArrayList<String>();
		for(EntityHeader eh : testBatchResults.getResults()) {
			testBatchResultsList.add(eh.writeToJSONObject(adapter.createNew()).toJSONString());
		}
		
		AsyncMockStubber.callSuccessWith(testTeamId).when(mockSynapseClient).createTeam(anyString(),any(AsyncCallback.class));
		
		AsyncMockStubber.callSuccessWith(testBatchResultsList).when(mockSynapseClient).getEntityHeaderBatch(anyList(),any(AsyncCallback.class));
		
		openInvitations = new ArrayList<MembershipInvitationBundle>();
		AsyncMockStubber.callSuccessWith(openInvitations).when(mockSynapseClient).getOpenInvitations(anyString(), any(AsyncCallback.class));
		
		testFeed = new RSSFeed();
		RSSEntry entry = new RSSEntry();
		entry.setTitle("A Title");
		entry.setAuthor("An Author");
		entry.setLink("http://somewhere");
		List<RSSEntry> entries = new ArrayList<RSSEntry>();
		entries.add(entry);
		testFeed.setEntries(entries);
		
		mockAuthenticationController = Mockito.mock(AuthenticationController.class);
		when(mockAuthenticationController.isLoggedIn()).thenReturn(true);
		when(mockGlobalApplicationState.getPlaceChanger()).thenReturn(mockPlaceChanger);
		
		homePresenter = new HomePresenter(mockView, 
				mockAuthenticationController, 
				mockGlobalApplicationState,
				mockRssService,
				mockSearchService,
				mockSynapseClient,
				adapter, 
				mockSynapseJSNIUtils,
				mockGwtWrapper,
				mockRequestBuilder, 
				mockCookies);
		verify(mockView).setPresenter(homePresenter);
		TeamListWidgetTest.setupUserTeams(adapter, mockSynapseClient);
	}	
	
	@Test
	public void testSetPlace() {
		Home place = Mockito.mock(Home.class);
		homePresenter.setPlace(place);
		verify(mockView).refresh();
		verify(mockView).refreshMyTeams(any(List.class));
	}
	
	@Test
	public void testNewsFeed() throws JSONObjectAdapterException {
		//when news is loaded, the view should be updated with the service result
		String exampleNewsFeedResult = "news feed";
		AsyncMockStubber.callSuccessWith(exampleNewsFeedResult).when(mockRssService).getCachedContent(anyString(), any(AsyncCallback.class));		
		homePresenter.loadNewsFeed();
		verify(mockView).showNews(anyString());
	}	
	
	@Test
	public void testCreateTeam() {
		//happy case
		homePresenter.createTeam("New Team");
		verify(mockSynapseClient).createTeam(anyString(), any(AsyncCallback.class));
	}
	
	@Test
	public void testCreateTeamFailure() throws RestServiceException {
		Exception simulatedException = new Exception("Simulated Error");
		AsyncMockStubber.callFailureWith(simulatedException).when(mockSynapseClient).createTeam(anyString(), any(AsyncCallback.class));
		homePresenter.createTeam("New Team");
		verify(mockSynapseClient).createTeam(anyString(), any(AsyncCallback.class));
		verify(mockView).showErrorMessage(anyString());
	}
	
	@Test
	public void testIsNoOpenInvites() {
		CallbackP<Boolean> mockCallback = mock(CallbackP.class);
		homePresenter.isOpenTeamInvites(mockCallback);
		verify(mockCallback).invoke(eq(false));
	}
	
	@Test
	public void testIsOpenInvites() {
		openInvitations.add(new MembershipInvitationBundle());
		CallbackP<Boolean> mockCallback = mock(CallbackP.class);
		homePresenter.isOpenTeamInvites(mockCallback);
		verify(mockCallback).invoke(eq(true));
	}
	
	@Test
	public void testGetChallengeProjectHeaders() {
		homePresenter.getChallengeProjectHeaders(new HashSet<String>());
		verify(mockView).setMyChallenges(anyList());
	}
	
	@Test
	public void testGetChallengeProjectHeadersFailure() {
		AsyncMockStubber.callFailureWith(new Exception("unhandled")).when(mockSynapseClient).getEntityHeaderBatch(anyList(),any(AsyncCallback.class));
		homePresenter.getChallengeProjectHeaders(new HashSet<String>());
		verify(mockView).setMyChallengesError(anyString());
	}
	
	@Test
	public void testTeam2ChallengeEndToEnd() throws RequestException {
		Team t1 = new Team();
		t1.setId("2");
		homePresenter.getChallengeProjectIds(new ArrayList<Team>());
		//grab the request callback and invoke
		ArgumentCaptor<RequestCallback> arg = ArgumentCaptor.forClass(RequestCallback.class);
		verify(mockRequestBuilder).sendRequest(anyString(), arg.capture());
		RequestCallback callback = arg.getValue();
		Response testResponse = new Response() {
			@Override
			public String getText() {
				return "{\"1\":\"syn1\", \"2\" : \"syn2\"}";
			}
			
			@Override
			public String getStatusText() {
				return null;
			}
			
			@Override
			public int getStatusCode() {
				return 0;
			}
			
			@Override
			public String getHeadersAsString() {
				return null;
			}
			
			@Override
			public Header[] getHeaders() {
				return null;
			}
			
			@Override
			public String getHeader(String header) {
				return null;
			}
		};
		callback.onResponseReceived(null, testResponse);
		verify(mockView).setMyChallenges(anyList());
	}
	
	@Test
	public void testTeam2ChallengeProjectFileCache() {
		CallbackP callback = new CallbackP() {
			@Override
			public void invoke(Object param) {
			}
		};
		when(mockCookies.getCookie(eq(HomePresenter.TEAMS_2_CHALLENGE_ENTITIES_COOKIE))).thenReturn("{\"1\":\"syn1\", \"2\" : \"syn2\"}");
		homePresenter.getTeamId2ChallengeIdWhitelist(callback);
		verify(mockRequestBuilder, times(0)).configure(any(RequestBuilder.Method.class), anyString());
		
		//but without the cookie, it should be called
		when(mockCookies.getCookie(eq(HomePresenter.TEAMS_2_CHALLENGE_ENTITIES_COOKIE))).thenReturn(null);
		homePresenter.getTeamId2ChallengeIdWhitelist(callback);
		verify(mockRequestBuilder, times(1)).configure(any(RequestBuilder.Method.class), anyString());
	}
	

}
