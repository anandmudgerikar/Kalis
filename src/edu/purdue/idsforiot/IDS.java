package edu.purdue.idsforiot;

import edu.purdue.idsforiot.communication.WifiCommunicator;
import edu.purdue.idsforiot.communication.ZigBeeCommunicator;
import edu.purdue.idsforiot.knowledge.KnowledgeBase;
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
	
	
	private ModuleManager moduleManager;
	private KnowledgeBase kb;
	private DataStore dataStore;

	private String idsNodeId;
	
	
	public void start(String source, String tracefile) throws IDSforIoTException {
		this.setIDSNodeId(java.util.UUID.randomUUID().toString());
		this.dataStore = new DataStore(this);
		this.kb = new KnowledgeBase(this);
		this.moduleManager = new ModuleManager(this);
		this.moduleManager.start();
		
		if (tracefile.equals("")) {
			// create Communicators to intercept packets and start listening for live packets
			ZigBeeCommunicator zigbeecommunicator = new ZigBeeCommunicator(this.dataStore, source);
			WifiCommunicator wificommunicator = new WifiCommunicator(this.dataStore);
			zigbeecommunicator.listen();
			wificommunicator.listen();
		} else {
			// replay a trace
			this.dataStore.replayTrace(tracefile);
		}
	}


	public String getIDSNodeId() {
		return idsNodeId;
	}
	private void setIDSNodeId(String idsNodeId) {
		this.idsNodeId = idsNodeId;
	}


	public KnowledgeBase getKnowledgeBase() {
		return this.kb;
	}
	public ModuleManager getModuleManager() {
		return this.moduleManager;
	}
	
}
