package edu.purdue.iot;

public class CTPPacket extends Packet {

	// packet structure for CPT
	private String thl;
	private String origin;
	private String seqno;
	private String collectid;

	public CTPPacket(int nodeid, String data, String thl, String origin, String seqno, String collectid) {
		super(nodeid, data);
		this.thl = thl;
		this.origin = origin;
		this.seqno = seqno;
		this.collectid = collectid;
	}

	public String getThl() {
		return thl;
	}

	public void setThl(String thl) {
		this.thl = thl;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getCollectid() {
		return collectid;
	}

	public void setCollectid(String collectid) {
		this.collectid = collectid;
	}

}
