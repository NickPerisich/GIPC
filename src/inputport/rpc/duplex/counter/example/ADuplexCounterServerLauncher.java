package inputport.rpc.duplex.counter.example;


import examples.counter.ACounter;
import inputport.rpc.duplex.AnAbstractDuplexRPCServerPortLauncher;

public class ADuplexCounterServerLauncher extends AnAbstractDuplexRPCServerPortLauncher implements DuplexCounterServerLauncher {
	public ADuplexCounterServerLauncher(String aServerName,
			String aServerPort) {
		super (aServerName, aServerPort);
	}
	@Override
	protected void registerRemoteObjects() {		
		register(COUNTER1, new ACounter());
		register(COUNTER2, new ACounter());
	}
	public static void main (String[] args) {
		(new ADuplexCounterServerLauncher(COUNTER_SERVER_NAME, COUNTER_SERVER_ID)).launch();
	}
}
