package edu.purdue.idsforiot;

import edu.purdue.idsforiot.modules.ModuleManager;

public class IDS {

	public static void main(String[] args) throws Exception {
		
		// parse the command line args
		String source = null;
		String tracefile = null;
		for (String a : args) {
			if (a.startsWith("-comm=")) source = a.substring("-comm=".length());
			else if (a.startsWith("-t=")) tracefile = a.substring("-t=".length());
			else {
				System.err.println("usage: java edu.purdue.idsforiot.IDS [-comm=packetsource] [-t=tracefile]");
				System.err.println("       (default packet source from MOTECOM environment variable)");
				System.exit(2);
			}
		}		
		
		// start the ModuleManager to load the necessary modules
		try {
			ModuleManager.getInstance().start();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(2);
		}
		
		if (tracefile.equals(null)) {
			// create a Communicator to intercept packets
			//Communicator communicator = new Communicator(source);
			Communicator2 communicator = new Communicator2(source);
			
			// start listening for live packets
			communicator.listen();
		} else {
			// replay a trace
			DataStore.getInstance().replayTrace(tracefile);
		}
		
	}

}
