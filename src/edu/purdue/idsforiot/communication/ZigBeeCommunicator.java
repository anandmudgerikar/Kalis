
package edu.purdue.idsforiot.communication;

import edu.purdue.idsforiot.DataStore;
import edu.purdue.idsforiot.packets.ZigBeePacket;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.tools.PrintfMsg;
import net.tinyos.util.*;

public class ZigBeeCommunicator extends Communicator implements MessageListener {

	private MoteIF moteIF;
	private String source;

	private String nextline = "";

	public ZigBeeCommunicator(DataStore dataStore, String source) {
		super(dataStore);
		this.source = source;
	}

	@Override
	public void listen() {
		PhoenixSource phoenix;
		if (source == null) {
			phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
		} else {
			phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		}
		System.out.print(phoenix);
		this.moteIF = new MoteIF(phoenix);
		this.moteIF.registerListener(new PrintfMsg(), this);
	}

	public void messageReceived(int to, Message message) {
		PrintfMsg msg = (PrintfMsg) message;

		for (int i = 0; i < PrintfMsg.totalSize_buffer(); i++) {
			char nextChar = (char) (msg.getElement_buffer(i));
			if (nextChar != 0) {
				nextline += nextChar;
				System.out.print(nextChar);
			}
			if (nextChar == '\n') {
				System.out.println("The full message is " + nextline);
				
				// TODO: let's unify ZigBee plain and CTP (in the TelosB app) into just CTP (calling it generically "ZigBee", and dest might be "" for broadcast)
				ZigBeePacket p = new ZigBeePacket();
				try {
					p = ZigBeePacket.parseFromLive(nextline);
				} catch (Exception ex) {
					System.out.println("Exception in Packet Creation: This packet is not recorded");
					ex.printStackTrace();
					nextline = "";
					continue;
				}

				// notify the DataStore
				System.out.println("New Packet: Notifying the Data Store");
				this.getDataStore().onNewPacket(p);
				nextline = "";
			}
		}
	}

}
