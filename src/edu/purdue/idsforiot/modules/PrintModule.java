package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.packets.Packet;

public class PrintModule extends Module {

	public PrintModule(ModuleManager mgr) {
		super(mgr);
	}

	public void onNewPacket(Packet p) {
		System.out.println(p);
	}

}
