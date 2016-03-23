package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.packets.Packet;

public interface Module {

	public void onNewPacket(Packet p);
	
	public void start();
	
}
