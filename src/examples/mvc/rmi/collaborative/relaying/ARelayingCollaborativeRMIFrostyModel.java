package examples.mvc.rmi.collaborative.relaying;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import examples.mvc.rmi.collaborative.ACollaborativeRMIFrostyModel;
import examples.rmi.counter.DistributedCounter;
import util.models.PropertyListenerRegisterer;

public class ARelayingCollaborativeRMIFrostyModel extends
		ACollaborativeRMIFrostyModel implements PropertyChangeListener {
	DistributedRMIEchoer echoer;
	public ARelayingCollaborativeRMIFrostyModel(
			RelayingCollaborativeRMIUpperCaser anUpperCaser,
			DistributedRMIEchoer anEchoer, DistributedCounter aCounter,
			String aUserName) {
		super(anUpperCaser, aCounter, aUserName);
		echoer = anEchoer;
		try {
			anUpperCaser.addListener(aUserName, anEchoer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		((PropertyListenerRegisterer) echoer).addPropertyChangeListener(this);
	}
	protected void processUpperCase(String aString) {
		super.processUpperCase(aString);
		try {
			((RelayingCollaborativeRMIUpperCaser) upperCaser).relayToOthers(
					userName + " said:" + aString, userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		String oldOutput = output;
		output = (String) evt.getNewValue();
		propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(this,
				"output", oldOutput, output));
	}
}
