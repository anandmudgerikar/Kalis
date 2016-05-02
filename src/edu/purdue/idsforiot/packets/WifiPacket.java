package edu.purdue.idsforiot.packets;

public class WifiPacket extends Packet {
	
	public WifiPacket(String src, String dst, String data) {
		super(PacketTypes.WiFi, src, dst, data);
	}

	/// CSV FORMAT: type, timestamp, src, dst, data
	public static WifiPacket parseFromLog(String raw) {
		String[] parts = raw.split(",");
		WifiPacket p = new WifiPacket(parts[2], parts[3], parts[4]);
		p.setTimestamp(Long.parseLong(parts[1]));
		return p;
	}
	
	public static WifiPacket parseFromLive(String raw) {
		String[] parts = raw.split(" ");
		WifiPacket p = new WifiPacket(parts[2], parts[4], "");	
		return p;
	}
	

	@Override
	public String toString() {
		return "Wifi " + super.toString();
	}
}
