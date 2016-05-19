package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;

public class EnvironmentSensingModule extends SensingModule {

	public EnvironmentSensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
	}

	@Override
	public void onNewPacket(Packet p) {
		if (p instanceof ZigBeePacket)
			this.getKnowledgeBase().setKnowledge("multihop", true);
		else if (p instanceof WifiPacket)
			this.getKnowledgeBase().setKnowledge("singlehop", true);
	}

}
