package edu.purdue.idsforiot.packets;

public class PacketFactory {

	/// Decodes any type of packet (from log)
	public static Packet getPacket(String raw) {
		String[] parts = raw.split(",");
		PacketTypes type = PacketTypes.valueOf(parts[0]);
		switch (type) {
			case WiFi:
				if(parts[5].equals("RPL")) //if RPL control packet
					return RplPacket.parseFromLog(raw);
				else
					return WifiPacket.parseFromLog(raw);
			case ZigBee:
				return ZigBeePacket.parseFromLog(raw);
			default:
				System.out.println("Unknown PacketType.");
				return null;
		}
	}

}
