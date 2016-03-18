package edu.purdue.iot;
import java.io.*;
import java.util.*;

public class DataStore {

	// SINGLETON pattern
	private static DataStore instance = new DataStore();
	public static DataStore getInstance() {
		if (instance == null) instance = new DataStore();
		return instance;
	}
	
	
	private Map<Integer, Queue<Packet>> queues;
	
	
	
	private DataStore() {
		this.queues = new HashMap<Integer, Queue<Packet>>(); // initializing the queues
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
				
				String nodeid = strLine.substring(30, 32);
				String data = strLine.substring(33, 35);
				p = new Packet(Integer.parseInt(nodeid), data);
				
				// add to CVS file
				bw.write(p.getNodeID());
				bw.write(",");
				bw.write(p.getData());
				// bw.write(",");

			} else if ((strLine.substring(0, 5)).equals("00 FE")) { // condition for CPT check (currently manually setting value to FE in packet) errorsin decoding.. needs fixing
				// CPT framework packet format
				String nodeid = strLine.substring(30, 32);
				String data = strLine.substring(33, 35);
				String thl = strLine.substring(8, 16);
				String origin = strLine.substring(32, 48);
				String seqno = strLine.substring(48, 56);
				String collectid = strLine.substring(56, 64);
				p = new CTPPacket(Integer.parseInt(nodeid), data, thl, origin, seqno, collectid);

				// add to CVS file
				bw.write(thl);
				bw.write(",");
				bw.write(origin);
				bw.write(",");
				bw.write(seqno);
				bw.write(",");
				bw.write(collectid);
				bw.write(",");
				
			} else {
				System.out.println("Unknown Packet Format");
				continue;
			}

			// add to appropriate queue
			if (!queues.containsKey(p.getNodeID()))
				queues.put(p.getNodeID(), new LinkedList<Packet>());
			queues.get(p.getNodeID()).add(p);
		}

		// Close the input stream
		br.close();
		bw.close();

	}

	
	
	public Map<Integer, Queue<Packet>> getQueues() {
		return queues;
	}

}