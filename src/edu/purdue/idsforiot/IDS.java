package edu.purdue.idsforiot;

import edu.purdue.idsforiot.communication.ZigBeeCommunicator;
import edu.purdue.idsforiot.modules.ModuleManager;

public class IDS {

	public static void main(String[] args) throws Exception {
		
		// parse the command line args
		String source = null;
		String tracefile = "";
		for (String a : args) {
			if (a.startsWith("-comm=")) source = a.substring("-comm=".length());
			else if (a.startsWith("-t=")) tracefile = a.substring("-t=".length());
			else {
				System.err.println("usage: java edu.purdue.idsforiot.IDS [-comm=packetsource] [-t=tracefile]");
				System.err.println("       (default packet source from MOTECOM environment variable)");
				System.exit(2);
			}
		}
		
		IDS ids = new IDS();
		try {
			ids.start(source, tracefile);
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	public void start(String source, String tracefile) throws IDSforIoTException {
		ModuleManager.getInstance().start();
		
		if (tracefile.equals("")) {
			// create Communicators to intercept packets and start listening for live packets
			ZigBeeCommunicator zigbeecommunicator = new ZigBeeCommunicator(source);
			WifiCommunicator wificommunicator = new WifiCommunicator();
			zigbeecommunicator.listen();
			wificommunicator.listen();
		} else {
			// replay a trace
			DataStore.getInstance().replayTrace(tracefile);
		}
	}
	
}
