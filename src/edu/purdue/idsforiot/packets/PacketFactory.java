package edu.purdue.idsforiot.packets;

public class PacketFactory {

	/// Decodes any type of packet (from log)
	public static Packet getPacket(String raw) {
		String[] parts = raw.split(",");
		PacketTypes type = PacketTypes.valueOf(parts[0]);
		switch (type) {
			case WiFi:
				return new WifiPacket(parts);
			case ZigBeeCTP:
				return new ZigBeePacket(parts);
			case ZigBeePlain:
				return new CTPPacket(parts);
			default:
				System.out.println("Unknown PacketType.");
				return null;
		}
	}

}
