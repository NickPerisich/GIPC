package inputport.datacomm.simplex.buffer.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface WriteCommandFactory {			
	WriteCommand createWriteCommand (
			SelectionManager aSelectingRunnable, SocketChannel  aSocketChannel, ByteBuffer aWriteBuffer);
	WriteCommand createWriteCommand (
			SelectionManager aSelectingRunnable, SocketChannel  aSocketChannel, ByteBuffer aWriteBuffer, Integer anInterestOps);

}
