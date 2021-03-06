package examples.gipc.consensus.centralizable;

import consensus.ConsensusMechanismFactory;
import consensus.ReplicationSynchrony;
import consensus.central.ACentralizableConsensusMechanismFactory;
import examples.gipc.consensus.AnExampleProposerLauncher;

public class ACentralizableMemberLauncher extends AnExampleProposerLauncher  {

	public ACentralizableMemberLauncher(String aLocalName,
			int aPortNumber) {
		super(aLocalName, aPortNumber);
	}

	@Override
	protected ConsensusMechanismFactory<Integer> meaningConsensusMechanismFactory() {
		return new ACentralizableConsensusMechanismFactory<>();
	}

	@Override
	protected void customizeConsensusMechanisms() {
		meaningOfLifeMechanism.setCentralized(true);
		meaningOfLifeMechanism.setAcceptSynchrony(ReplicationSynchrony.ALL_SYNCHRONOUS);
		
		
	}
	


}
