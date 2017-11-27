package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;

public class RPLTimeSensingModule extends SensingModule {
	
	private String timesynch;

	public RPLTimeSensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		this.timesynch = "0"; //Manually sensing for current implementation
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
				if(this.timesynch.equals("1"))
					this.getKnowledgeBase().setKnowledge("timesynch", true);
				else
					this.getKnowledgeBase().setKnowledge("timesynch", false);
		}
	}

}
