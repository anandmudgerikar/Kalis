package edu.purdue.idsforiot;

import java.io.*;
import java.util.*;

import edu.purdue.idsforiot.modules.ModuleManager;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.PacketFactory;

public class DataStore {

    // SINGLETON pattern
    private static DataStore instance = new DataStore();

    public static DataStore getInstance() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }

    private Map<String, Queue<Packet>> queues;

    private DataStore() {
        // initializing the queues
        this.queues = new HashMap<String, Queue<Packet>>();
    }

    
    public void replayTrace(String tracefilepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tracefilepath));
            String raw;
            Packet prevPkt = null;
            
            while ((raw = br.readLine()) != null) {
                // decode packet, skipping in case of decoding errors
                Packet p = PacketFactory.getPacket(raw);
                if (p == null) continue;

                // notify but don't log (as we are already reading from a log)
                long time_diff = prevPkt == null ? 0 : p.getTimestamp() - prevPkt.getTimestamp();
                try {
                   Thread.sleep(time_diff); //sleeping for appropriate time
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
                FileOutputStream csvfileWriter = new FileOutputStream(new File("data/CSVpacketcapture.txt"), true);
                csvfileWriter.write(p.toCSV().getBytes()); 
                csvfileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
                        
        // add to appropriate queue
        if (!queues.containsKey(p.getSrc()))
            queues.put(p.getSrc(), new LinkedList<Packet>());
        queues.get(p.getSrc()).add(p);
        
        // notify the Modules
        ModuleManager.getInstance().onNewPacket(p);
    }
    
    
    

    public Queue<Packet> getQueue(String nodeID) {
        return queues.get(nodeID);
    }

    

}