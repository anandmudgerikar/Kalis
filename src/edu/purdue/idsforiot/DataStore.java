package edu.purdue.idsforiot;

import java.io.*;
import java.util.*;

import edu.purdue.idsforiot.modules.ModuleManager;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.PacketFactory;

public class DataStore {

	// SINGLETON pattern
	private static DataStore instance = new DataStore();

	public static DataStore getInstance() {
		if (instance == null)
			instance = new DataStore();
		return instance;
	}

	private Map<Integer, Queue<Packet>> queues;

	private DataStore() {
		// initializing the queues
		this.queues = new HashMap<Integer, Queue<Packet>>();
	}

	
	public void replayTrace(String tracefilepath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(tracefilepath));
			String raw;
			while ((raw = br.readLine()) != null) {
				// decode packet, skipping in case of decoding errors
				Packet p = PacketFactory.getPacket(raw.split(","));
				if (p == null) continue;

				// notify but don't log (as we are already reading from a log)
				// TODO timing must be preserved!! We need to notify only at
				// the right time according to the packet's timestamp
				this.onNewPacket(p, false);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void onNewPacket(Packet p) {
		// enable logging by default
		this.onNewPacket(p, true);
	}
	public void onNewPacket(Packet p, boolean log) {
		if (log) {
			try {
				// log the packet on file (in CSV format)
				FileOutputStream csvfileWriter = new FileOutputStream(new File("data/CSVpacketcapture.txt"), true);
				csvfileWriter.write(p.toCSV().getBytes()); 
				csvfileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
						
		// add to appropriate queue
		if (!queues.containsKey(p.getNodeID()))
			queues.put(p.getNodeID(), new LinkedList<Packet>());
		queues.get(p.getNodeID()).add(p);
		
		// notify the Modules
		ModuleManager.getInstance().onNewPacket(p);
	}


	public Queue<Packet> getQueue(Integer nodeID) {
		return queues.get(nodeID);
	}

	

}