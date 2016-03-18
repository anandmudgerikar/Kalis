package edu.purdue.idsforiot.modules;

import java.io.*;

public final class ModuleManager {

	// SINGLETON pattern
	private static ModuleManager instance = new ModuleManager();
	private ModuleManager() {}
	public static ModuleManager getInstance() {
		if (instance == null) instance = new ModuleManager();
		return instance;
	}

	
	public void start() {
		// TODO: finish the readModulesConfig() method and uncomment next line
		// readModulesConfig();

		// for now, we run some hardcoded modules
		PrintModule p1 = new PrintModule("Thread 1");
		p1.start();
	}

	@SuppressWarnings("unused")
	private void readModulesConfig() {
		// read from config file which modules to start for now
		try {
			FileInputStream configstream = new FileInputStream("data/config.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(configstream));
			String line;
			while ((line = br.readLine()) != null) {
				// TODO: use reflection to match the string "ModuleName" with
				// the class ModuleName and start that module
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
