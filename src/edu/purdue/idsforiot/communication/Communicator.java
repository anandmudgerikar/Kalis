package edu.purdue.idsforiot.communication;

import edu.purdue.idsforiot.DataStore;

public abstract class Communicator {

	private DataStore dataStore;
	
	public Communicator(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	protected DataStore getDataStore() {
		return this.dataStore;
	}
	
	public abstract void listen();

}