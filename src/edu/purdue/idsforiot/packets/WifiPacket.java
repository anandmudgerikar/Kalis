package edu.purdue.idsforiot.packets;

public class WifiPacket extends Packet {

	private String protocol; // ICMP, TCP, UDP, ...
	private String protocolType; // request, reply, ...
	
	public WifiPacket(String src, String dst, String data, String protocol, String protocolType) {
		super(PacketTypes.WiFi, src, dst, data);
		this.protocol = protocol;
		this.protocolType = protocolType;
	}
	
	/// CSV FORMAT: type, timestamp, src, dst, data
	public static WifiPacket parseFromLog(String raw) {
		String[] parts = raw.split(",");
		WifiPacket p = new WifiPacket(parts[2], parts[3], (parts.length >=5 ? parts[4] : ""), parts[5], parts[6]);
		p.setTimestamp(Long.parseLong(parts[1]));
		return p;
	}
	
	public static WifiPacket parseFromLive(String raw) {
		String[] parts = raw.split(" ");
		WifiPacket p = new WifiPacket(parts[2], parts[4], parts[6].substring(1,3), parts[5], parts[7]); //sending flag in data for now	
		return p;
	}
	

	@Override
	public String toString() {
		return "Wifi " + super.toString() + " [" + this.protocol + " " + this.protocolType + "]";
	}
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
}
