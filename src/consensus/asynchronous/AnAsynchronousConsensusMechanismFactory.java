package consensus.asynchronous;

import sessionport.rpc.group.GIPCSessionRegistry;
import sessionport.rpc.group.GroupRPCSessionPort;
import consensus.AnAbstractConsensusMechanismFactory;
import consensus.ConsensusMechanism;
import consensus.synchronous.ASynchronousConsensusMechanism;

public class AnAsynchronousConsensusMechanismFactory<StateType> extends AnAbstractConsensusMechanismFactory<StateType> {

	@Override
	protected ConsensusMechanism instantiateConsensusMehanism(
			GIPCSessionRegistry aRegistry, String anObjectName, short aMyId) {
		return new AnAsynchronousConsensusMechanism(aRegistry, anObjectName, aMyId);
	}

}