package edu.purdue.idsforiot.packets;

public class ZigBeePacket extends Packet {

	public ZigBeePacket(String[] rawparts) {
		this(rawparts[2], rawparts[3]);
		this.setTimestamp(Long.parseLong(rawparts[1]));
	}
	
	public ZigBeePacket(String src, String data) {
		super(PacketTypes.ZigBeePlain, src, "", data);
	}
	public ZigBeePacket(String src, String dst, String data) {
		super(PacketTypes.ZigBeeCTP, src, dst, data);
	}
	

	@Override
	public String toString() {
		return "ZigBee " + super.toString();
	}

}
