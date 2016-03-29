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
			FileInputStream fstream = new FileInputStream(tracefilepath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String raw;
			while ((raw = br.readLine()) != null) {
				// notify but don't log (as we are already reading from a log)
				// TODO timing must be preserved!! We need to notify only at
				// the right time according to the packet's timestamp
				this.onNewPacket(raw.getBytes(), false, true);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onNewPacket(byte[] raw) {
		// enable logging by default
		this.onNewPacket(raw, true,false);
	}

	public void onNewPacket(byte[] raw, boolean log, boolean trace) {
		// decode packet, returning in case of decoding errors
		Packet p = PacketFactory.getPacket(raw.toString());
		if (p == null)
			return;
		
		//if it is a tracefile, set the appropriate timestamps
		if(trace)
		{
			java.util.Date date= new java.util.Date();
			long curr_timestamp = (new Timestamp(date.getTime())).getTime();
			p.setTimeStamp(curr_timestamp);
					
			 
			
		}

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

	public Map<Integer, Queue<Packet>> getQueues() {
		return queues;
	}

}