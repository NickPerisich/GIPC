package consensus.asynchronous;

import inputport.InputPort;
import port.trace.consensus.ProposalLearnNotificationReceived;
import sessionport.rpc.group.GIPCSessionRegistry;
import sessionport.rpc.group.GroupRPCSessionPort;
import consensus.AnAbstractConsensusMechanism;
import consensus.ProposalState;
import consensus.ProposalFeedbackKind;

public class ALearnerConsensusMechanism<StateType> extends
		AnAbstractConsensusMechanism<StateType> implements Learner<StateType> {
	protected float maxProposalNumberReceivedInLearnNotification = -1;

	public ALearnerConsensusMechanism(GIPCSessionRegistry aRegistry, 
			String aName, short aMyId
			) {
		super(aRegistry, aName, aMyId);
	}
//	protected Learned<StateType> proposer() {
//		return proposer;
//	}
	protected void recordReceivedLearnNotification(float aProposalNumber, StateType aProposal, ProposalFeedbackKind aRejectionKind) {
		recordProposalState(aProposalNumber, aProposal);
		if (!isPending(aProposalNumber))
			return;
		maxProposalNumberReceivedInLearnNotification = Math.max(maxProposalNumberReceivedInLearnNotification, aProposalNumber);
		if (isSuccess(aRejectionKind))
			newProposalState(aProposalNumber, aProposal, ProposalState.PROPOSAL_CONSENSUS);
		else
			newProposalState(aProposalNumber, aProposal,toProposalState(aRejectionKind));
	}
	@Override
	public synchronized void learn(float aProposalNumber, StateType aProposal, ProposalFeedbackKind aRejectionKind) {
		ProposalLearnNotificationReceived.newCase(this, getObjectName(), aProposalNumber, aProposal, aRejectionKind);
		if (isValueSynchrony()) {
			waitForReceipt(aProposalNumber, aProposal);
		}
		recordReceivedLearnNotification(aProposalNumber, aProposal, aRejectionKind);
	}
//	protected void setLearnedState(float aProposalNumber, StateType aProposal, ProposalVetoKind anAgreement) {
//		if (!eventualConsistency() && learnedByTimeout()) {
//			waitForReceipt(aProposalNumber, aProposal);			
//		}
//		super.setLearnedState(aProposalNumber, aProposal, anAgreement);
//	}
//	@Override
//	public void learn(float aProposalNumber, StateType aProposal, ProposalVetoKind anAgreement) {
//		super.learn(aProposalNumber, aProposal, anAgreement);
//		if (eventualConsistency() && learnedByTimeout()) {
//			return;
//		}
//		proposer().learned(aProposalNumber, aProposal);		
//	}
	


	



	


}