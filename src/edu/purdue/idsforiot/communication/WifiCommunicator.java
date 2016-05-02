package edu.purdue.idsforiot.communication;

import java.io.*;

import edu.purdue.idsforiot.DataStore;
import edu.purdue.idsforiot.packets.WifiPacket;

public class WifiCommunicator implements Communicator {

	private static Process tcpdumpProcess;
	private static BufferedReader tcpdumpStream;
	
	// TODO: what are the following 2 lines, and do we need them?
	// private InputStream tcpInput;
	// private boolean keepRunning = true;
	
	@Override
	public void listen() {
		System.out.println("Starting TCPdump");
		try {
			// TODO: we need to read ALL packets and get each packet in real time, not waiting for the stream to close
			String params[] = { "sudo", "tcpdump", "-ln", "-i", "eth0"}; // reading just two packets for now
			tcpdumpProcess = Runtime.getRuntime().exec(params);
			tcpdumpStream = new BufferedReader(new InputStreamReader(tcpdumpProcess.getInputStream()), 1);

			String rawLine;
			while ((rawLine = tcpdumpStream.readLine()) != null) {
				System.out.println(rawLine);

				try {
					// we got a packet: decode it, skipping in case of decoding errors
					WifiPacket p = WifiPacket.parseFromLive(rawLine);

					// notify the DataStore
					DataStore.getInstance().onNewPacket(p);
				} catch (Exception ex) {
					System.out.println("Exception in parsing wifi packet");
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.println("TCPDump initialize failure.");
			e.printStackTrace();
		}
		System.out.println("TCPdump stopping");
	}

}
