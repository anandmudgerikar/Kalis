package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;
import edu.purdue.idsforiot.packets.Edge;

public abstract class SensingModule extends Module {
	
	private KnowledgeBase kb;
	
	public SensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr);
		this.kb = kb;
		
	}

	protected KnowledgeBase getKnowledgeBase() {
		return this.kb;
	}
	
	
	
	
}

	