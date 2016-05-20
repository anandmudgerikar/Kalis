package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.Map;

import edu.purdue.idsforiot.knowledge.Edge;
import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;

public class TopologyDiscoveryModule extends SensingModule {

	private KnowledgeBase kb;
	private Map<Edge, Integer> rssiHistory;
	private Map<Edge, Integer> edges;

	public TopologyDiscoveryModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		this.rssiHistory = new HashMap<Edge, Integer>();
		this.edges = new HashMap<Edge, Integer>();
	}

	@Override
	public void onNewPacket(Packet p) {

		Edge edge = new Edge(p.getSrc(), p.getDst());

		if (p instanceof ZigBeePacket) {
			this.getKnowledgeBase().setKnowledge("multihop", true);

			// recording RSSI values for all edges
			if (rssiHistory.containsKey(edge)) {
				// old edge
				if (rssiHistory.get(edge) != ((ZigBeePacket) p).getRSSI()) {
					// mobility
					this.rssiHistory.put(edge, ((ZigBeePacket) p).getRSSI());
					kb.setKnowledge("mobility", true);
				}
			} else {
				// new edge
				this.rssiHistory.put(edge, ((ZigBeePacket) p).getRSSI());
			}

		} else if (p instanceof WifiPacket) {
			this.getKnowledgeBase().setKnowledge("singlehop", true);

			this.edges.put(edge, 1); // edge weight set to 1 as default, but we
									 // can maintain edge weight here

			for (Edge e : edges.keySet()) {
				if (!e.getDst().equals(p.getDst())) {
					this.getKnowledgeBase().setKnowledge("multihop", true);
					System.out.println("Sensing a Multi Hop network");
				}
			}

		}
	}

}
