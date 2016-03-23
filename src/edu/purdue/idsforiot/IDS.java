package edu.purdue.idsforiot;

import edu.purdue.idsforiot.modules.ModuleManager;

public class IDS {

	public static void main(String[] args) {
		
		// parse the command line args
		String source = null;
		if (args.length == 2 && args[0].equals("-comm")) {
			source = args[1];
		} else if (args.length > 0) {
			System.err.println("usage: java edu.purdue.idsforiot.IDS [-comm PACKETSOURCE]");
			System.err.println("       (default packet source from MOTECOM environment variable)");
			System.exit(2);
		}

		// TODO: read possible tracefile from command line argument. For now, it's hardcoded.
		String tracefile = "/home/odroid/tinyos-main/project/ids/data/Rawpacket_capture.txt";
		
		// create a Communicator to intercept packets
		Communicator communicator = new Communicator(source);
				
		// start the ModuleManager to load the necessary modules
		try {
			ModuleManager.getInstance().start();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!tracefile.equals(null)) {
			// replay a trace
			DataStore.getInstance().replayTrace(tracefile);
		} else {
			// start listening for live packets
			communicator.listen();
		}
		
	}

}
