package inputport.datacomm.simplex.buffer.nio;

import inputport.nio.AConnectCommand;
import inputport.nio.AConnectCommandFactory;
import inputport.nio.ConnectCommand;
import inputport.nio.ConnectCommandFactory;
import inputport.nio.SelectionManager;

import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class AWritingConnectCommandFactory extends AConnectCommandFactory implements ConnectCommandFactory {

	@Override
	public ConnectCommand createConnectCommand(
			SelectionManager aSelectionManager, SocketChannel aSocketChannel,
			InetAddress aServerHost, int aPort) {
		// write will set the interestops
		return new AConnectCommand (aSelectionManager, aSocketChannel, aServerHost, aPort, null);
	}

	

}