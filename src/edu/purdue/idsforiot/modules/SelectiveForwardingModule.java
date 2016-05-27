package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.ZigBeePacket;

public class SelectiveForwardingModule extends DetectionModule {

	private Map<String, Queue<Packet>> queues;


    public SelectiveForwardingModule(ModuleManager mgr) {
		super(mgr);
	}
    
	public void start() {
        // initializing the queues
        this.queues = new HashMap<String, Queue<Packet>>();
        
        super.start();
	}
	
	private Queue<Packet> getQueueFor(String nodeId) {
        if (!this.queues.containsKey(nodeId))
        	this.queues.put(nodeId, new LinkedList<Packet>());
        return this.queues.get(nodeId);
	}
	
	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof ZigBeePacket)) return;
		
		Queue<Packet> srcQ = this.getQueueFor(p.getSrc());
		if (srcQ.isEmpty()){
			// packet originated from src
		} else if (srcQ.peek().equals(p)) {
			// normal propagation
			srcQ.poll();
		} else {
			// selective forwarding detected!
			while (true) {
				Packet head = srcQ.poll();
				if (head == null || head.equals(p)) break;
				this.getManager().onDetection(this, "Selective Forwarding", p.getSrc(), head);
			}
		}
		
		// push packet to queue of dest
		this.getQueueFor(p.getDst()).offer(p);
	}

	

	public static boolean shouldBeActive(KnowledgeBase kb) {
		return kb.getKnowledgeBooleanOrFalse("multihop");
	}


}
