package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;

public class RPLSensingModule extends SensingModule{
	
	private String auth;
	private String time_synchonized;
	private String higherlevel_auth;

	public RPLSensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		this.auth = "0";
		this.higherlevel_auth = "0";
		this.time_synchonized = "0";
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
			if(((RplPacket) p).getauth().equals("1"))
			{
				this.getKnowledgeBase().setKnowledge("rplauth", true);
				//System.out.println("RPL packets are authenticated");
			}
			else
			{
				this.getKnowledgeBase().setKnowledge("rplauth", false);
				//System.out.println("RPL packets are not authenticated");
			}	

			if(((RplPacket) p).getData().equals("RPL2"))
			{
				this.getKnowledgeBase().setKnowledge("lowerrplauth", true);
				//System.out.println("RPL packets are authenticated");
			}
			else
			{
				this.getKnowledgeBase().setKnowledge("lowerrplauth", false);
				//System.out.println("RPL packets are not authenticated");
			}	
			
			if(((RplPacket) p).getData().equals("RPL3"))
			{
				this.getKnowledgeBase().setKnowledge("timesynch", true);
				//System.out.println("RPL packets are authenticated");
			}
			else
			{
				this.getKnowledgeBase().setKnowledge("timesynch", false);
				//System.out.println("RPL packets are not authenticated");
			}	
		}
		
	}

}
