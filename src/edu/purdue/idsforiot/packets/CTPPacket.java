package edu.purdue.idsforiot.packets;

public class CTPPacket extends Packet {

	// packet structure for CPT
	private String thl;
	private String origin;
	private String seqno;
	private String collectid;

	public CTPPacket(String raw) {
		this(Integer.parseInt(raw.substring(30, 32)), raw.substring(33, 35),
			 raw.substring(8, 16), raw.substring(32, 48), raw.substring(48, 56), raw.substring(56, 64));
	}
	public CTPPacket(int nodeid, String data, String thl, String origin, String seqno, String collectid) {
		super(nodeid, data);
		this.thl = thl;
		this.origin = origin;
		this.seqno = seqno;
		this.collectid = collectid;
	}

	public String getTHL() {
		return thl;
	}

	public void setTHL(String thl) {
		this.thl = thl;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSeqNo() {
		return seqno;
	}

	public void setSeqNo(String seqno) {
		this.seqno = seqno;
	}

	public String getCollectID() {
		return collectid;
	}

	public void setCollectID(String collectid) {
		this.collectid = collectid;
	}

}
