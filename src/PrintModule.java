import java.io.*;

public class PrintModule implements Runnable {
	private Thread t;
	private String threadName;

	PrintModule(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);

		System.out.println("\n Print Module working \n");
		Data_Store ds = new Data_Store();
		try {
			ds.update_queues();
		} catch (IOException e) {
			System.out.println("Error in input from data store");
		}

		FileInputStream configstream;
		int n;
		try {
			configstream = new FileInputStream("/home/odroid/tinyos-main/project/ids/data/config.txt");
			BufferedReader cr = new BufferedReader(new InputStreamReader(configstream));

			n = Integer.parseInt((cr.readLine())); // get other parameters from
													// config file
			cr.close();

			// printing out all the packet captures for each queue
			for (int i = 0; i < n; i++) {
				if (ds.queues[i].isEmpty()) {
					System.out.println("Queue for node " + i + "is empty");
				} else {
					while (!ds.queues[i].isEmpty())
						System.out.println(ds.queues[i].remove());
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
}
