package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public class SYNFloodModule extends DetectionModule {

	public SYNFloodModule(ModuleManager mgr) {
		super(mgr);
	}

	public static boolean shouldBeActive(KnowledgeBase kb) {
		return kb.getTrafficFrequency(TrafficType.TCPSYN) >= .5;
	}
	public static String[] subscribedKnowggets() {
		return new String[] { "trafficFrequency." + TrafficType.TCPSYN.toString() };
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof WifiPacket)) return;
		
		if (this.getManager().getKnowledgeBase().getTrafficFrequency(TrafficType.TCPSYN) >= 1)
			this.getManager().onDetection(this, "SYN Flood", p.getSrc(), p);
	}

}
