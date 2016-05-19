package edu.purdue.idsforiot.packets;

public class Edge {

	private String source;
	private String dest;
	
	public Edge(String src, String dest)
	{
		this.source = src;
		this.dest = dest;
	}
	
	public String getDest()
	{
		return this.dest;
	}
}
