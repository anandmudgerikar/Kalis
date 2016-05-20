package edu.purdue.idsforiot.packets;

public class ZigBeePacket extends Packet {
	
	// link layer headers
	private int rssi; // check if modlink quality is same as RSSI value
	private String frameType;

	// packet structure for CPT
	private int thl;
	private String origin;
	private long seqno;
	private String collectid;

	public ZigBeePacket() {
		super();
	}
			
	public ZigBeePacket(String src, String dest, String data, int rssi, String frameType, int thl, String origin, long seqNo, String collectId) {
		super(PacketTypes.ZigBee, src, dest, data);
		
		this.frameType = frameType;
		this.rssi = rssi;
		this.thl = thl;
		this.origin = origin;
		this.seqno = seqNo;
		this.collectid = collectId;
	}
	

	@Override
	public String toCSV() {
		return super.toCSV() + "," + this.getRSSI() + "," + this.getFrameType() + "," + this.getTHL() + "," + this.getOrigin() + "," + this.getSeqNo() + "," + this.getCollectID();
	}
	
	/// CSV FORMAT: type, timestamp, src, dst, data, RSSI, frametype, THL, Origin, SeqNo, CollectID
	public static ZigBeePacket parseFromLog(String raw) {
		String[] parts = raw.split(",");
		ZigBeePacket p = new ZigBeePacket(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]), parts[6], Integer.parseInt(parts[7]), parts[8], Long.parseLong(parts[9]), parts[10]);
		p.setTimestamp(Long.parseLong(parts[1]));
		return p;
	}

	/// Raw FORMAT: frametype, srcAddrMode, srcShortAddr or srcExtendedAddr, srcPANId, dstAddrMode, dstShortAddr or dstExtendedAddr, dstPANId, DSN, headerLen, header, payloadLen, payload, linkQuality
	public static ZigBeePacket parseFromLive(String raw) {
		String[] parts = raw.split(",");
		String[] payload = parts[11].split(" ");
		ZigBeePacket p = new ZigBeePacket(parts[2], parts[5], "" /* empty data for now */,
										  Integer.parseInt(parts[12]), parts[0],
										  payload[1].getBytes()[0], payload[4] + payload[5].substring(2), payload[6].getBytes()[0], payload[7]);
		
		return p;
	}
	
		

	
	
	public int getTHL() {
		return thl;
	}
	public void setTHL(int thl) {
		this.thl = thl;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public long getSeqNo() {
		return seqno;
	}
	public void setSeqNo(long seqno) {
		this.seqno = seqno;
	}
	public String getCollectID() {
		return collectid;
	}
	public void setCollectID(String collectid) {
		this.collectid = collectid;
	}
	public String getFrameType() {
		return frameType;
	}
	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	public int getRSSI() {
		return rssi;
	}
	public void setRSSI(int rssi) {
		this.rssi = rssi;
	}


	@Override
	public String toString() {
		// TODO: print all fields
		return "ZigBee " + super.toString() + " CTP [FrameType=" + this.getFrameType() + ", RSSI=" + this.getRSSI() + ", Collect id=" + this.getCollectID() 
				+ ", Seq No=" + this.getSeqNo() + ", Origin=" + this.getOrigin() + ", THL=" + this.getTHL() + "]";
	}
	

}
