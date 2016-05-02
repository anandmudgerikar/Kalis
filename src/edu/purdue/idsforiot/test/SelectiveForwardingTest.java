package edu.purdue.idsforiot.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.purdue.idsforiot.IDS;
import edu.purdue.idsforiot.IDSforIoTException;

public class SelectiveForwardingTest {
	
	protected IDS ids;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		this.ids = new IDS();
	}
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}

	
	
	@Test
	public void testMultiHopSelectiveForwardingFromTrace() {
		String expected = "DETECTED: Selective Forwarding attack by Entity 0x01 (Module MultiHopSelectiveForwardingModule) [e]\n" +
					      "DETECTED: Selective Forwarding attack by Entity 0x03 (Module MultiHopSelectiveForwardingModule) [g]\n" +
					      "DETECTED: Selective Forwarding attack by Entity 0x03 (Module MultiHopSelectiveForwardingModule) [h]\n" +
					      "DETECTED: Selective Forwarding attack by Entity 0x01 (Module MultiHopSelectiveForwardingModule) [l]\n";
		
		try {
			this.ids.start("", "data/ZigBeeSelFwd.txt");
		    assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

}
