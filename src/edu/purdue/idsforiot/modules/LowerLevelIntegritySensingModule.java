package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;

public class LowerLevelIntegritySensingModule extends SensingModule {
	
	private String lowerlevelauth;

	public LowerLevelIntegritySensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		this.lowerlevelauth = "1"; //Manually sensing for current implementation
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
				if(this.lowerlevelauth.equals("1"))
					this.getKnowledgeBase().setKnowledge("lowerrplauth", true);
				else
					this.getKnowledgeBase().setKnowledge("lowerrplauth", false);
		}
	}

}
