package examples.gipc.counter.sessionport.twoparty.asymmetric;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;

import consensus.Acceptor;
import consensus.ConsensusMechanism;
import consensus.Learner;
import consensus.multiparty.asymmetric.listener.AnAsymmetricMultiPartyProposerConsensusMechanism;
import consensus.multiparty.asymmetric.listener.MultiPartyAsymmetricListenerConsensusMechanism;
import consensus.twoparty.asymmetric.AnAsymmetricTwoPartyProposer;
import consensus.twoparty.symmetric.ASymmetricTwoPartyPeer;
import consensus.twoparty.symmetric.SymmetricTwoPartyPeer;
import consensus.twoparty.symmetric.TwoPartySymmetricConsensusMechanism;
import bus.uigen.visitors.AddListenersAdapterVisitor;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import port.SessionChoice;
import port.trace.consensus.ConsensusTraceUtility;
import sessionport.rpc.group.GIPCLocateSessionRegistry;
import sessionport.rpc.group.GIPCSessionRegistry;
import sessionport.rpc.group.GroupRPCSessionPort;
import examples.gipc.counter.layers.AMultiLayeServerReceiveListener;
import examples.gipc.counter.layers.AMultiLayerCounterServer;
import examples.gipc.counter.sessionport.CounterSessionMember;
import examples.mvc.rmi.duplex.ADistributedInheritingRMICounter;
import examples.mvc.rmi.duplex.DistributedRMICounter;
import examples.rmi.counter.simple.SimpleRegistryAndCounterServer;

public class ATwoPartyAsymmeticProposerLauncher implements TwoPartyMember {
	protected  ConsensusMechanism<String> greetingMechanism;
	protected  ConsensusMechanism<Integer> meaningOfLifeMechanism;
	protected  Acceptor<String> remoteGreetingMechanism;
	protected  Acceptor<Integer> remoteMeaningOfLifeMechanism;
	protected  GIPCSessionRegistry gipcRegistry;
	protected  GroupRPCSessionPort groupRPCSessionPort;
	protected  Integer numMembersToWaitFor = 2;
	protected  SessionChoice sessionChoice = SessionChoice.P2P;
	
	protected  final int MY_PORT_NUMBER = 7001;
	protected  final String MY_NAME = "1";
	public static final String GREETING_1 = "Hello";
	public static final String GREETING_2 = "Howdy";
	public static int MEANING = 42;
	
	protected  void initGreetingConsensusMechanism(short anId) {
		remoteGreetingMechanism = (Acceptor) gipcRegistry.lookupAllRemote(Acceptor.class, GREETING_CONSENSUS_MECHANISM_NAME);
		greetingMechanism = new AnAsymmetricTwoPartyProposer<>(groupRPCSessionPort, GREETING_CONSENSUS_MECHANISM_NAME, anId, remoteGreetingMechanism);
		greetingMechanism.addConsensusListener(new AGreetingConsensusListener());
		gipcRegistry.rebind(GREETING_CONSENSUS_MECHANISM_NAME, greetingMechanism);

	}	
	protected  void initMeaningOfLifeConsensusMechanism(short anId) {
		remoteMeaningOfLifeMechanism = (Acceptor) gipcRegistry.lookupAllRemote(Acceptor.class, MEANING_OF_LIFE_CONSENSUS_MECHANISM_NAME);
		meaningOfLifeMechanism = new AnAsymmetricTwoPartyProposer<>(groupRPCSessionPort, MEANING_OF_LIFE_CONSENSUS_MECHANISM_NAME, anId, remoteMeaningOfLifeMechanism);
		meaningOfLifeMechanism.addConsensusListener(new AMeaningOfLifeConsensusListener());
		gipcRegistry.rebind(MEANING_OF_LIFE_CONSENSUS_MECHANISM_NAME, meaningOfLifeMechanism);		

	}

	protected  void init(String aLocalName, int aPortNumber) {
		gipcRegistry = GIPCLocateSessionRegistry.createSessionRegistry(
				"mysession", "localhost", aPortNumber, aLocalName,
				sessionChoice, 
				numMembersToWaitFor);
		groupRPCSessionPort = gipcRegistry.getSessionPort();
		short anId = Short.parseShort(aLocalName);
		initGreetingConsensusMechanism(anId);
		initMeaningOfLifeConsensusMechanism(anId);		
	}



	public  void doOperations() {
		double aGreetingProposal1 = greetingMechanism.propose(GREETING_1);
		double aGreetingProposal2 = greetingMechanism.propose(GREETING_2);
		double aMeaningOfLifeProposal = meaningOfLifeMechanism.propose(MEANING);
		
	}
	public  void beProposer() {
		ConsensusTraceUtility.setTracing();
		init(MY_NAME, MY_PORT_NUMBER);
		doOperations();
	}

	public static void main (String[] args) {
		
		(new ATwoPartyAsymmeticProposerLauncher()).beProposer();;
	}

}
