package inputport.rpc.duplex;

import java.util.HashMap;
import java.util.Map;

import examples.mvc.local.duplex.ACounter;
import inputport.rpc.GIPCRegistry;
import port.AnAbstractPortLauncher;
import port.PortAccessKind;
import port.PortKind;
import port.PortMessageKind;



public class AnAbstractDuplexRPCPortLauncher extends AnAbstractPortLauncher implements GIPCRegistry  {
	protected Map<String, Object> registry = new HashMap();
	public AnAbstractDuplexRPCPortLauncher(String aServerName,
			String aServerPort) {
		super (aServerName, aServerPort);
	}
	public AnAbstractDuplexRPCPortLauncher(String aClientName, String aServerHost, String aServerId, String aServerName) {
		super(aClientName, aServerHost, aServerId, aServerName);		
	}
	@Override
	protected PortAccessKind getPortAccessKind() {
		return PortAccessKind.DUPLEX;
	}	
	public AnAbstractDuplexRPCPortLauncher() {
	}	
	
	@Override
	protected PortMessageKind getPortMessageKind() {
		return PortMessageKind.RPC;
	}
	@Override
	protected void registerRemoteObjects() {		
		for (String aKey:registry.keySet()) {
			register(aKey, registry.get(aKey));
		}
	}
//	@Override
//	public void rebindAndListen(String aName, Object anObject) {
//		rebind(aName, anObject);
//		launch();
//	}
	
	public void rebind(String aName, Object anObject) {
//		registry.put(aName, anObject);
		register(aName, anObject);
	}

//	public void listen() {
//		launch();
//	}	
}
