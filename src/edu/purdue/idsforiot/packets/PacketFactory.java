package edu.purdue.idsforiot.packets;

public class PacketFactory {

	public static Packet getPacket(String raw) {
		Packet p = null;
		if ((raw.substring(0, 5)).equals("00 FF")) {
			// standard blinkApp packet format
			System.out.println("Packet is Zigbee");
			p = new Packet(raw);

		} else if ((raw.substring(0, 5)).equals("00 FE")) { // condition for CTP check (currently manually setting value to FE in packet) errors in decoding.. needs fixing
			System.out.println("Packet is CTP");
			// CTP framework packet format
			//p = new CTPPacket(raw);

		} else {
			System.out.println("Unknown Packet Format");
			// TODO: should we throw and exception?
		}

		return p;
	}

	public static CTPPacket getPacket(String[] raw) {
		// TODO: here we should also distinguish plain packets from CTP packets
		// TODO: maybe in the CSV format we should add a field at the beginning that says which type of packet it is (0 for plain, 1 for CTP, 2 for WiFi, ...)
		CTPPacket p = new CTPPacket(raw[0],raw[2],raw[5],raw[12], raw[11].split(" "));
		return p;
	}
	
	public static WifiPacket getpacket(String raw)
	{
		WifiPacket p = new WifiPacket();
		
		String packet_components[] = raw.split(" ");
		//java.sql.Timestamp ts = java.sql.Timestamp.valueOf(packet_components[0]);
		
		//System.out.println(packet_components[0] + packet_components[2] + packet_components[4] );
		p.setTimeStamp(packet_components[0]);
		p.setSourceIp(packet_components[2]);
		p.setDestIP(packet_components[4]);
		
		return p;
	}

}
