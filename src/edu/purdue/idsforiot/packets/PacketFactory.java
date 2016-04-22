package edu.purdue.idsforiot.packets;

public class PacketFactory {

	/// Decodes any type of packet (from log)
	public static Packet getPacket(String raw) {
		String[] parts = raw.split(",");
		PacketTypes type = PacketTypes.valueOf(parts[0]);
		switch (type) {
			case WiFi:
				return WifiPacket.parseFromLog(raw);
			case ZigBeeCTP:
				return ZigBeePacket.parseFromLog(raw);
			default:
				System.out.println("Unknown PacketType.");
				return null;
		}
	}

}
