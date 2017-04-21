package consensus.paxos;

import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import port.trace.consensus.ProposalPreparedNotificationReceived;
import port.trace.consensus.ProposalPreparedNotificationSent;
import port.trace.consensus.RemoteProposeRequestReceived;
import port.trace.consensus.RemoteProposeRequestSent;
import bus.uigen.widgets.universal.CentralUniversalWidget;
import sessionport.rpc.group.GIPCSessionRegistry;
import sun.security.util.PendingException;
import consensus.ConsensusMechanism;
import consensus.ProposalFeedbackKind;
import consensus.ProposalState;
import consensus.ReplicationSynchrony;
import consensus.central.ACentralizableConsensusMechanism;
import consensus.synchronous.ASynchronousConsensusMechanism;

public class APaxosConsensusMechanism<StateType> 
	extends APreparerConsensusMechanism<StateType>
	implements Prepared<StateType>{

	protected Map<Float, Boolean> aggregatePrepared = new HashMap();


	protected float maxProposalNumberSentInPrepareRequest = -1;

	protected float maxProposalNumberReceivedInPreparedNotification;
	protected float maxProposalNumberReceivedInSuccessfulPreparedNotification = -1;
	
	protected float maxAcceptedProposalNumberReceivedInPreparedNotification;


	public APaxosConsensusMechanism(GIPCSessionRegistry aRegistry,
			String aName, short aMyId) {
		super(aRegistry, aName, aMyId);
	}
	
	protected void setAggregatePrepared(float aProposalNumber) {
		aggregatePrepared.put(aProposalNumber, true);
	}
	
	protected boolean isAggregatePrepared(float aProposalNumber) {
		return (aggregatePrepared.get(aProposalNumber) != null) &&
				aggregatePrepared.get(aProposalNumber);
	}


	protected void recordReceivedPreparedNotification(
			float anAcceptedProposalNumber, StateType anAcceptedProposal, 
			float aPreparedProposalNumber, ProposalFeedbackKind aFeedbackKind) {
		maxProposalNumberReceivedInPreparedNotification = Math.max(
				maxProposalNumberReceivedInPreparedNotification, aPreparedProposalNumber);
		if (anAcceptedProposal != null) {
			maxAcceptedProposalNumberReceivedInPreparedNotification = Math.max(
					maxAcceptedProposalNumberReceivedInPreparedNotification,anAcceptedProposalNumber );
		}
		incrementCount(aPreparedProposalNumber, PREPARE_NOTIFICATION, 1);
	}
	private Boolean sufficientPeparers(ReplicationSynchrony aReplicationSynchrony, 
			float aPreparedProposalNumber) {
		int aNumPrepareNotifications = getCount(aPreparedProposalNumber, PREPARE_NOTIFICATION);
		return sufficientAgreements(aReplicationSynchrony, aPreparedProposalNumber, proposal(aPreparedProposalNumber), 
				numConsensusMembers(), numCurrentMembers(), 
				aNumPrepareNotifications, 
				aNumPrepareNotifications);
	}
	protected StateType preparedState(float aPreparedProposalNumber) {
		float aChosenProposalNumber = 
				maxAcceptedProposalNumberReceivedInPreparedNotification <= 0?
						aPreparedProposalNumber:maxAcceptedProposalNumberReceivedInPreparedNotification	;
		return proposal(aChosenProposalNumber);
	}
	protected void aggregatePreparedNotification (
			float anAcceptedProposalNumber, StateType anAcceptedProposal, 
			float aPreparedProposalNumber, ProposalFeedbackKind aFeedbackKind) {
		Boolean isSufficientPreparers = sufficientPeparers(getPrepareSynchrony(), aPreparedProposalNumber);
		if (isSufficientPreparers == null)
			return;
		setAggregatePrepared(aPreparedProposalNumber);
		recordAndSendAcceptRequest(aPreparedProposalNumber, preparedState(aPreparedProposalNumber));		
	}

	protected StateType successProposedState(float anAcceptedProposalNumber, StateType anAcceptedProposal, float aPreparedProposalNumber, ProposalFeedbackKind aFeedbackKind) {
		return anAcceptedProposal == null?proposal(aPreparedProposalNumber):anAcceptedProposal;
	}

	
	protected ProposalState toProposalState(float aProposalNumber, StateType aProposal, ProposalFeedbackKind aFeedbackKind) {
		ProposalState result = toProposalState(aFeedbackKind);	
//		if (result == ProposalState.PROPOSAL_CONSENSUS && 
//				( !aProposal.equals(proposal(aProposalNumber)))) { // we were overridden
//			return ProposalState.PROPOSAL_CONCURRENT_OPERATION;
//		}
		return result;
	}
	
	protected Preparer<StateType> all() {
		return (Preparer<StateType>) super.all();
	}
	
	protected void recordAndSendPrepareRequest (float aProposalNumber, StateType aProposal) {
		recordPrepareRequest(aProposalNumber, aProposal);
		sendPrepareRequest(aProposalNumber, aProposal);		
	}
    protected void recordPrepareRequest (float aProposalNumber, StateType aProposal) {
    	maxProposalNumberSentInPrepareRequest =
    			Math.max(maxProposalNumberSentInPrepareRequest, aProposalNumber);    	
	}
    protected void sendPrepareRequest (float aProposalNumber, StateType aProposal) {
		all().prepare(aProposalNumber, aProposal);
	}	
	protected void localPropose(float aProposalNumber, StateType aProposal) {	
		if (isNonAtomic()) {
			super.localPropose(aProposalNumber, aProposal);
		}  else {			
			recordAndSendPrepareRequest(aProposalNumber, aProposal);
		}
   }
	
	
	protected void sendAcceptedNotification(float aProposalNumber, StateType aProposal, ProposalFeedbackKind aFeedbackKind){
		sendAcceptedNotificationToLearners(aProposalNumber, aProposal, aFeedbackKind);
	}
	protected void sendAcceptedNotificationToLearners(float aProposalNumber, StateType aProposal, ProposalFeedbackKind aFeedbackKind){
		all().accepted(aProposalNumber, aProposal, aFeedbackKind);
	}
	
	@Override
	public void prepared(float anAcceptedProposalNumber, StateType anAcceptedProposal, float aPreparedProposalNumber, ProposalFeedbackKind aFeedbackKind) {
		ProposalPreparedNotificationReceived.newCase(this, getObjectName(), anAcceptedProposalNumber, anAcceptedProposal, aPreparedProposalNumber, aFeedbackKind);

		recordReceivedPreparedNotification(anAcceptedProposalNumber, anAcceptedProposal, aPreparedProposalNumber, aFeedbackKind);
		if (!isPending(aPreparedProposalNumber) || isAggregatePrepared(aPreparedProposalNumber)) {
			return;
		}
		if (!isSuccess(aFeedbackKind)) {
			newProposalState(aPreparedProposalNumber, proposal(aPreparedProposalNumber), toProposalState(aPreparedProposalNumber, anAcceptedProposal, aFeedbackKind));
			return;
		} 
		aggregatePreparedNotification(anAcceptedProposalNumber, anAcceptedProposal, aPreparedProposalNumber, aFeedbackKind);
		
			
	}
//	protected void recordReceivedPrepareNotification(float anAcceptedProposalNumber, StateType anAcceptedProposal, float aPreparedProposalNumber, ProposalFeedbackKind aFeedbackKind){
//		recordProposalState(anAcceptedProposalNumber, anAcceptedProposal);
//		ma
//		

//		maxReceivedAcceptedNotificationProposalNumber = Math.max(maxReceivedAcceptedNotificationProposalNumber, aProposalNumber);
//		incrementCount(aProposalNumber, ACCEPT_NOTIFICATION, 1);			
//
//		if (isSuccess(aFeedbackKind)) {
//			lastAcceptedProposal = aProposal;
//			lastAcceptedProposalNumber = Math.max(aProposalNumber, lastAcceptedProposalNumber);
//			incrementCount(aProposalNumber, ACCEPT_SUCCESS, 1);			
//		}
//	}
	
//	protected float maxPreparedProposalNumber() {
//		return maxPreparedProposalNumber;
//	}
//	protected StateType maxPreparedProposal() {
//		return maxPreparedProposal;
//	}
//	protected float lastAcceptedProposalNumber() {
//		return lastAcceptedProposalNumber;
//	}

	
}
