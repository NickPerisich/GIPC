package util.trace.port.objects;

import util.trace.port.GIPCQueueCreated;

public class ReceivedMessageQueueCreated extends GIPCQueueCreated{
	public ReceivedMessageQueueCreated(String aMessage, Object aFinder, Object aQueue) {
		super(aMessage, aFinder, aQueue);
	}

	public static ReceivedMessageQueueCreated newCase(Object aFinder, Object aQueue) {
    	String aMessage = "Queue " + aQueue + " created for " + "received messages";
    	ReceivedMessageQueueCreated retVal =
   	    new ReceivedMessageQueueCreated(aMessage, aFinder, aQueue);
    	retVal.announce();
    	return retVal;
	}

}
