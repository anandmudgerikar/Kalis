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
			p = new CTPPacket(raw);
			
		} else {
			System.out.println("Unknown Packet Format");
			// TODO: should we throw and exception?
		}
		
		return p;
	}

}
