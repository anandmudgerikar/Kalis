package edu.purdue.idsforiot;
import java.io.*;
import java.util.*;

import edu.purdue.idsforiot.packets.CTPPacket;
import edu.purdue.idsforiot.packets.Packet;

public class DataStore {

	// SINGLETON pattern
	private static DataStore instance = new DataStore();
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
		return instance;
	}
	
	
	private Map<Integer, Queue<Packet>> queues;
	
	
	
	private DataStore() {
		// initializing the queues
		this.queues = new HashMap<Integer, Queue<Packet>>();
	}
	

	public void update_queues() throws IOException {

		FileInputStream fstream = new FileInputStream("/home/odroid/tinyos-main/project/ids/data/Rawpacket_capture.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		FileOutputStream fstream2 = new FileOutputStream("/home/odroid/tinyos-main/project/ids/data/CVSpacket_capture.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fstream2));


		String strLine;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			Packet p;
			if ((strLine.substring(0, 5)).equals("00 FF")) {
				// standard blinkApp packet format
				System.out.println("Packet is Zigbee");
				
				p = new Packet(strLine);
				
				// add to CVS file
				bw.write(p.getNodeID());
				bw.write(",");
				bw.write(p.getData());
				bw.write(",");

			} else if ((strLine.substring(0, 5)).equals("00 FE")) { // condition for CTP check (currently manually setting value to FE in packet) errors in decoding.. needs fixing
				System.out.println("Packet is CTP");
				// CTP framework packet format
				p = new CTPPacket(strLine);

				// add to CVS file
				bw.write(((CTPPacket)p).getTHL());
				bw.write(",");
				bw.write(((CTPPacket)p).getOrigin());
				bw.write(",");
				bw.write(((CTPPacket)p).getSeqNo());
				bw.write(",");
				bw.write(((CTPPacket)p).getCollectID());
				bw.write(",");
				
			} else {
				System.out.println("Unknown Packet Format");
				continue;
			}

			this.onNewPacket(p);
		}

		// Close the input stream
		br.close();
		bw.close();

	}

	
	public void onNewPacket(Packet p) {
		// add to appropriate queue
		if (!queues.containsKey(p.getNodeID()))
			queues.put(p.getNodeID(), new LinkedList<Packet>());
		queues.get(p.getNodeID()).add(p);
	}
	
	
	public Map<Integer, Queue<Packet>> getQueues() {
		return queues;
	}

}