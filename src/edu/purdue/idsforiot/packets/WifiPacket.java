package edu.purdue.idsforiot.packets;

public class WifiPacket extends Packet {

	public WifiPacket(String[] rawparts) {
		this(rawparts[3], rawparts[5], "");
		
		// TODO: once the WiFi reading is done real-time, we will just let the timestamp be automatically generated in the Packet constructor
		this.setTimestamp(Long.parseLong(rawparts[1]));
	}
	
	public WifiPacket(String src, String dst, String data) {
		super(PacketTypes.WiFi, src, dst, data);
	}

	
	public static WifiPacket parseFromLog(String raw) {
		
	}
	public static WifiPacket parseFromLive(String raw) {
		
	}
	

	@Override
	public String toString() {
		return "Wifi " + super.toString();
	}
}
