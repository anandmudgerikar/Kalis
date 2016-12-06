package edu.purdue.idsforiot.communication;

import java.io.*;

import edu.purdue.idsforiot.DataStore;
import edu.purdue.idsforiot.packets.WifiPacket;

public class WifiCommunicator extends Communicator {

	private static Process tcpdumpProcess;
	private static BufferedReader tcpdumpStream;
	
	public WifiCommunicator(DataStore dataStore) {
		super(dataStore);
	}
	
	
	@Override
	public void listen() {
		System.out.println("Starting TCPdump");
		try {
			// TODO: we need to read ALL packets and get each packet in real time, not waiting for the stream to close
			String params[] = { "sudo", "tcpdump", "-ln", "-i", "eth0"}; //change eth0 to wlan0 when listening to wifi traffic
			tcpdumpProcess = Runtime.getRuntime().exec(params);
			tcpdumpStream = new BufferedReader(new InputStreamReader(tcpdumpProcess.getInputStream()), 1);

			String rawLine;
			while ((rawLine = tcpdumpStream.readLine()) != null) {
				System.out.println(rawLine);

				try {
					// we got a packet: decode it, skipping in case of decoding errors
					WifiPacket p = WifiPacket.parseFromLive(rawLine);

					// notify the DataStore
					this.getDataStore().onNewPacket(p);
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
