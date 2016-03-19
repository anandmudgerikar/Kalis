package edu.purdue.idsforiot;

import java.io.*;

import net.tinyos.packet.*;
import net.tinyos.util.*;

public class Communicator {

	private PacketSource reader;

	public Communicator(String source) {
		if (source == null)
			this.reader = BuildSource.makePacketSource();
		else
			this.reader = BuildSource.makePacketSource(source);

		if (reader == null) {
			System.err.println("Invalid packet source (check your MOTECOM environment variable)");
			System.exit(2);
		}
	}

	public void listen() {
		try {
			this.reader.open(PrintStreamMessenger.err);
			while (true) {
				// wait for a new intercepted packet
				byte[] raw = this.reader.readPacket();

				// we got a packet, notify the DataStore
				DataStore.getInstance().onNewPacket(raw);
			}
		} catch (IOException e) {
			System.err.println("Error on " + reader.getName() + ": " + e);
		}
	}
}
