package edu.purdue.idsforiot.packets;

public class RplPacket extends Packet
{
	private String auth; // ICMP, TCP, UDP, ...
	private String message_type; // request, reply, ...
	
	public RplPacket(String src, String dst, String data, String auth, String message_type) 
	{
		super(PacketTypes.WiFi, src, dst, data);
		this.auth = auth;
		this.message_type = message_type;
	}
	
	@Override
	public String toCSV() {
		return super.toCSV() + "," + this.getauth() + "," + this.getmessage_type();
	}
	
	// CSV FORMAT: type, timestamp, src, dst, flag A (security mode), protocol(RPL control message ICMPv6), protocol type(DIO,DAO,DIS) -- control RPL messages
		public static RplPacket parseFromLog(String raw) {
			String[] parts = raw.split(",", -1);
			RplPacket p = new RplPacket(parts[2], parts[3], parts[5], parts[4], parts[6]);
			p.setTimestamp((long) Double.parseDouble(parts[1]));
			return p;
		}
		
//		public static RplPacket parseFromLive(String raw) {
//			// TODO from wire for rpl packets
////			String[] parts = raw.split(" ");
////			WifiPacket p = new WifiPacket(parts[2], parts[4], parts[6].substring(1,3), parts[5], parts[7]); //sending flag in data for now	
////			return p;
//		}
	
	public String getauth()
	{
		return this.auth;
	}
	
	public String getmessage_type()
	{
		return this.message_type;
	}
	
	@Override
	public String toString() {
		return "Wifi " + super.toString() + " [" + this.auth + " " + this.message_type + "]";
	}
}
