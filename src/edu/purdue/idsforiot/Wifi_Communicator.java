package edu.purdue.idsforiot;

import java.io.*;

import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.PacketFactory;
import edu.purdue.idsforiot.packets.WifiPacket;


public class Wifi_Communicator{

	private static Process tcpProcess;
	private static BufferedReader tcpStream;
	private InputStream tcpInput;
	private boolean keepRunning = true;
	
	public void listen()
	{
		System.out.println("Starting TCPdump");
		try {
			String params[] = {"sudo","tcpdump","-ln", "-i","eth0","-c","2"}; //reading just two packets for now
			tcpProcess = Runtime.getRuntime().exec(params);
			tcpStream = new BufferedReader(new InputStreamReader(tcpProcess.getInputStream()), 1);
			
			String raw_bytes;
			while((raw_bytes = tcpStream.readLine()) != null)
			{
				System.out.println(raw_bytes);
				
				// we got a packet: decode it, skipping in case of decoding errors
				WifiPacket p = PacketFactory.getpacket(raw_bytes);
				if (p == null) continue;
								
				// notify the DataStore
				DataStore.getInstance().onNewPacket(p);
			}
			}
		catch(Exception e)
			{
			System.err.println("TCPDump initialize failure.");
			e.printStackTrace();
			}
		System.out.println("TCPdump stopping");
	}
	
	
}
