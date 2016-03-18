import java.io.*;
import java.util.*;

public class DataStore {

	public Map<Integer, Queue<String>> queues;

	public void update_queues() throws IOException {

		FileInputStream fstream = new FileInputStream(
				"/home/odroid/tinyos-main/project/ids/data/Rawpacket_capture.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		FileOutputStream fstream2 = new FileOutputStream(
				"/home/odroid/tinyos-main/project/ids/data/CVSpacket_capture.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fstream2));

		// we assume number of nodes is n=10
		FileInputStream configstream = new FileInputStream(
				"/home/odroid/tinyos-main/project/ids/data/Rawpacket_capture.txt");
		BufferedReader cr = new BufferedReader(new InputStreamReader(configstream));
		int n;

		n = Integer.parseInt((br.readLine())); // get other parameters from config file
		cr.close();

		queues = new HashMap<Integer, Queue<String>>(); // initializing the queues

		// packet structure for BlinkToRadio
		String strLine;
		String node_id;
		String message;

		// packet structure for CPT
		String thl;
		String origin;
		String seqno;
		String collectid;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			System.out.println(strLine.substring(0, 5));

			if ((strLine.substring(0, 5)).equals("00 FF")) {
				// standard blinkApp packet format
				System.out.println("Packet is Zigbee");
				node_id = strLine.substring(30, 32);
				message = strLine.substring(33, 35);

				// System.out.println("Node is "+strLine.substring(30,32));
				// System.out.println("Message is "+strLine.substring(33,35));
				//
				// add to CVS file
				bw.write(strLine.substring(30, 32));
				bw.write(",");
				bw.write(strLine.substring(33, 35));
				// bw.write(",");

				// add to appropriate queue
				Integer nid = Integer.parseInt(node_id);
				if (!queues.containsKey(nid))
					queues.put(nid, new LinkedList<String>());
				queues.get(nid).add(message); // added to appropriate queue in data store

			} else if ((strLine.substring(0, 5)).equals("00 FE")) // condition for CPT check) (currently manually setting value to FE in packet) errorsin decoding.. needs fixing
			{
				// CPT framework packet format
				thl = strLine.substring(8, 16);
				origin = strLine.substring(32, 48);
				seqno = strLine.substring(48, 56);
				collectid = strLine.substring(56, 64);

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
		}

		// Close the input stream
		br.close();
		bw.close();

	}

}