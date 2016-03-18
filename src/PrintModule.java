import java.io.*;
import java.util.Map.Entry;
import java.util.Queue;

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
		DataStore ds = new DataStore();
		try {
			ds.update_queues();
		} catch (IOException e) {
			System.out.println("Error in input from data store");
		}

		int n;
		try {
			// printing out all the packet captures for each queue
			for(Entry<Integer, Queue<String>> q : ds.queues.entrySet()) {
				if (q.getValue().isEmpty()) {
					System.out.println("Queue for node " + q.getKey() + " is empty");
				} else {
					while (!q.getValue().isEmpty())
						System.out.println(q.getValue().remove());
				}
			}
		} catch (NumberFormatException e) {
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
