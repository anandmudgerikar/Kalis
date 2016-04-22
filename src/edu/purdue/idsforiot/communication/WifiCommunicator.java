package edu.purdue.idsforiot.communication;

import java.io.*;

import edu.purdue.idsforiot.DataStore;
import edu.purdue.idsforiot.packets.WifiPacket;

public class WifiCommunicator implements Communicator {

	private static Process tcpProcess;
	private static BufferedReader tcpStream;
	
	// TODO: what are the following 2 lines, and do we need them?
	// private InputStream tcpInput;
	// private boolean keepRunning = true;

	/* (non-Javadoc)
	 * @see edu.purdue.idsforiot.Communicator#listen()
	 */
	@Override
	public void listen() {
		System.out.println("Starting TCPdump");
		try {
			String params[] = { "sudo", "tcpdump", "-ln", "-i", "eth0", "-c", "2" }; // reading just two packets for now
			tcpProcess = Runtime.getRuntime().exec(params);
			tcpStream = new BufferedReader(new InputStreamReader(tcpProcess.getInputStream()), 1);

			String raw_bytes;
			while ((raw_bytes = tcpStream.readLine()) != null) {
				System.out.println(raw_bytes);

				// we got a packet: decode it, skipping in case of decoding errors
				WifiPacket p;
				try {
					p = new WifiPacket(raw_bytes.split(" "));
				} catch (Exception ex) {
					continue;
				}

				// notify the DataStore
				DataStore.getInstance().onNewPacket(p);
			}
		} catch (Exception e) {
			System.err.println("TCPDump initialize failure.");
			e.printStackTrace();
		}
		System.out.println("TCPdump stopping");
	}

}
