package trace.port.rpc;

import trace.port.MessageReceiveInfo;
import inputport.rpc.RemoteCall;

public class CallInfo extends MessageReceiveInfo {
	RemoteCall call;
	

	public CallInfo(String aMessage, Object aSource, Object aDestination, String aRemoteEndPoint, RemoteCall aCall) {
		super(aMessage, aSource, aDestination, aRemoteEndPoint);
		call = aCall;
	}
	
	public RemoteCall getCall() {
		return call;
	}
//	public static void newCase(Object aSource, Object aDestination, String aRemoteEndPoint, RemoteCall aCall) {
//    	String aMessage = "Call: " + aCall + " with remote end point: " + aRemoteEndPoint + 
//    			" forwarded by " +  aSource + " to " + aDestination ;
//   	    new CallInfo(aMessage, aSource, aDestination, aRemoteEndPoint, aCall);
//
//	}

}