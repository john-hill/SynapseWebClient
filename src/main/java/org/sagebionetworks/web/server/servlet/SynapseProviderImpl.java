package org.sagebionetworks.web.server.servlet;

import org.sagebionetworks.client.Synapse;
import org.sagebionetworks.client.SynapseInt;

/**
 * Very simple implementation.
 * @author John
 *
 */
public class SynapseProviderImpl implements SynapseProvider {
	

	@Override
	public SynapseInt createNewClient() {
		return new Synapse();
		// ONE LINE CHANGE TO USE STUB SYNAPSE CLIENT:		
		//return SynapseClientStubUtil.createSynapseClient();		
	}


}
