
package edu.purdue.idsforiot.communication;

import edu.purdue.idsforiot.DataStore;
import edu.purdue.idsforiot.packets.CTPPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.tools.PrintfMsg;
import net.tinyos.util.*;

public class ZigBeeCommunicator implements Communicator, MessageListener {

	private MoteIF moteIF;
	private String source;

	private String nextline = "";

	public ZigBeeCommunicator(String source) {
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
				
				// TODO: how do I know here if it's plain zigbee or ctp?
				// TODO: let's unify ZigBee plain and CTP into just CTP where dest might be "" for broadcast
				ZigBeePacket p;
				try {
					p = new CTPPacket(nextline.split(","));
				} catch (Exception ex) {
					continue;
				}

				// notify the DataStore
				DataStore.getInstance().onNewPacket(p);
				nextline = "";
			}
		}
	}

}
