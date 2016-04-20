package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.CTPPacket;
import edu.purdue.idsforiot.packets.WifiPacket;


public class PrintModule implements Module, Runnable {
	private Thread t;
	private String threadName;
	
	PrintModule(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	@Override
	public void onNewPacket(Packet p) {
		System.out.println(p);
	}
	
	public void onNewPacket(WifiPacket p) {
		System.out.println(p);
	}
}
