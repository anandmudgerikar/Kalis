package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.packets.Packet;

public abstract class Module {

	private ModuleManager manager;
	
	public Module(ModuleManager mgr) {
		this.manager = mgr;
	}
	
	protected ModuleManager getManager() {
		return this.manager;
	}
	
	public abstract void onNewPacket(Packet p);
	
	public void start() {
		System.out.println("Module " + this.getClass().getSimpleName() + " loaded.");
	}
	
}
