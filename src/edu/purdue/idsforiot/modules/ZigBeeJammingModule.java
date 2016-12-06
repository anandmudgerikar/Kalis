package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.ZigBeePacket;;

public class ZigBeeJammingModule extends DetectionModule {
	
	public ZigBeeJammingModule(ModuleManager mgr) {
		super(mgr);
	}
	
	public static boolean shouldBeActive(KnowledgeBase kb) {
		for (String value : kb.getAllPerNodeTrafficFrequencies(TrafficType.ZigBeeCTP).values()) {
			if (Float.parseFloat(value) >= .8)
				return true;
		}
		return false;
	}
	public static String[] subscribedKnowggets() {
		return new String[] { "trafficFrequency." + TrafficType.ZigBeeCTP.toString() };
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof ZigBeePacket)) return;

		if (this.getManager().getKnowledgeBase().getTrafficFrequency(TrafficType.ZigBeeCTP, p.getDst()) >= 1)
			this.getManager().onDetection(this, "ZigBee Jamming on " + p.getDst(), "?", p);
	}
}
