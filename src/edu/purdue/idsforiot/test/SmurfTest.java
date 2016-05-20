package edu.purdue.idsforiot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.purdue.idsforiot.IDS;
import edu.purdue.idsforiot.IDSforIoTException;

public class SmurfTest {

	protected IDS ids;
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		this.ids = new IDS();
	}

	@Before
	public void setUpStreams() {
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setErr(null);
	}

	@Test
	public void testMultiHopSelectiveForwardingFromTrace() {
		String expected = "DETECTED: ICMP Flood attack by Entity 10.0.0.13.22: (Module ICMPFloodModule) [I]\n"
						+ "DETECTED: Smurf attack by Entity 10.0.0.13.22: (Module SmurfModule) [I]\n";

		try {
			this.ids.start("", "data/Smurf.txt");
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

}
