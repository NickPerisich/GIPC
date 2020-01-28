package examples.mvc.rmi.collaborative;

import examples.mvc.local.duplex.DuplexFrostyModel;
import examples.mvc.local.simplex.ProgramLauncher;
import examples.mvc.rmi.muser.AMultiUserRMIClientMVC_Launcher;
import examples.rmi.counter.DistributedCounter;

public class ACollaborativeRMIClientMVCLauncher extends
		AMultiUserRMIClientMVC_Launcher implements ProgramLauncher {
	public ACollaborativeRMIClientMVCLauncher(String aName, String aHost) {
		super(aName, aHost);
	}

	protected DuplexFrostyModel getFrostyModel() {
		try {
			CollaborativeRMIUpperCaser upperCasePrinter = (CollaborativeRMIUpperCaser) serverRMIRegistry
					.lookup(upperCaserName());
			return new ACollaborativeRMIFrostyModel(upperCasePrinter,
					(DistributedCounter) getCounter(), name);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected String upperCaserName() {
		return ACollaborativeRMIServerMVCLauncher.UPPER_CASER_NAME;
	}

	public static void main(String[] args) {
		(new ACollaborativeRMIClientMVCLauncher("Test Client", "localhost"))
				.launch();
	}
}
