package edu.purdue.idsforiot;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import java.util.Date;

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
			//FileInputStream fstream = new FileInputStream(tracefilepath);
			BufferedReader br = new BufferedReader(new FileReader(tracefilepath));
			String raw;
			while ((raw = br.readLine()) != null) {
				// notify but don't log (as we are already reading from a log)
				// TODO timing must be preserved!! We need to notify only at
				// the right time according to the packet's timestamp
				String[] packet_components = raw.split(",");
				this.onNewPacket(packet_components, false);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onNewPacket(byte[] raw) {
		// enable logging by default
		this.onNewPacket(raw, true);
	}

	public void onNewPacket(byte[] raw, boolean log) {
		// decode packet, returning in case of decoding errors
		Packet p = PacketFactory.getPacket(raw.toString());
		if (p == null)
			return;
		
		//set the timestamp
		java.util.Date date= new java.util.Date();
		long curr_timestamp = (new Timestamp(date.getTime())).getTime();
		p.setTimeStamp(curr_timestamp);	
		
		if (log) {
			// log the packet on file (both raw and CSV format)
			try {
				File rawfile = new File("data/Rawpacketcapture.txt");
				OutputStream rawfileWriter = new FileOutputStream(rawfile, true);
				rawfileWriter.write(raw, 0, 10);
				rawfileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				File csvfile = new File("data/CSVpacketcapture.txt");
				FileOutputStream csvfileWriter = new FileOutputStream(csvfile, true);
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

	// if packet is recevied from a cvs file
	public void onNewPacket(String[] raw, boolean log) {
		// decode packet, returning in case of decoding errors
		Packet p = PacketFactory.getPacket(raw);
		if (p == null)
			return;
			
		// add to appropriate queue
		if (!queues.containsKey(p.getNodeID()))
			queues.put(p.getNodeID(), new LinkedList<Packet>());
		queues.get(p.getNodeID()).add(p);

		// notify the Modules
		ModuleManager.getInstance().onNewPacket(p);
	}
	
	public Map<Integer, Queue<Packet>> getQueues() {
		return queues;
	}

}
