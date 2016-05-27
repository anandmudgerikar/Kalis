package edu.purdue.idsforiot;

import java.io.*;
import java.util.*;

import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.PacketFactory;

public class DataStore {

	private IDS ids;
	
    private Map<String, List<Packet>> trafficHistory;

    public DataStore(IDS ids) {
    	this.ids = ids;
        // initializing the queues
        this.trafficHistory = new HashMap<String, List<Packet>>();
    }

    
    public void replayTrace(String tracefilepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tracefilepath));
            String raw;
            Packet prevPkt = null;
            
            while ((raw = br.readLine()) != null) {
            	// ignore comment lines
            	if (raw.equals("") || raw.startsWith("//")) continue;
            	
                // decode packet, skipping in case of decoding errors
                Packet p = PacketFactory.getPacket(raw);
                if (p == null) continue;

                // notify but don't log (as we are already reading from a log)
                long time_diff = prevPkt == null ? 0 : p.getTimestamp() - prevPkt.getTimestamp();
                try {
                   Thread.sleep(time_diff * 1000); //sleeping for appropriate time
                } catch(InterruptedException ex) {
                   Thread.currentThread().interrupt();
                }
                this.onNewPacket(p, false);
                prevPkt = p;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    public void onNewPacket(Packet p) {
        // enable logging by default
        this.onNewPacket(p, true);
    }
    public void onNewPacket(Packet p, boolean log) {
        if (log) {
            try {
                // log the packet on file (in CSV format)
                FileOutputStream csvfileWriter = new FileOutputStream(new File("data/log.txt"), true);
                csvfileWriter.write(p.toCSV().getBytes()); 
                OutputStreamWriter osw = new OutputStreamWriter(csvfileWriter);
                osw.append(System.getProperty("line.separator"));
                osw.close();
                csvfileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // add to appropriate queue
        if (!trafficHistory.containsKey(p.getSrc()))
            trafficHistory.put(p.getSrc(), new LinkedList<Packet>());
        trafficHistory.get(p.getSrc()).add(p);
        
        // notify the Modules
        System.out.println("New Packet: Notifying the Module Manager");
        this.ids.getModuleManager().onNewPacket(p);
    }
    
    
    

    public List<Packet> getTrafficHistoryFor(String nodeID) {
        return trafficHistory.get(nodeID);
    }
}