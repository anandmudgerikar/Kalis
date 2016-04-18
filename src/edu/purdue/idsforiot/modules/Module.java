package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public interface Module {

	public void onNewPacket(Packet p);
	
	public void onNewPacket(WifiPacket p);
	
	public void start();
	
}
