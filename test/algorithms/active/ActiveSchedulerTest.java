package algorithms.active;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;

import algorithms.Scheduler;

import common.Configuration;
import common.Result;

public class ActiveSchedulerTest {

	@Test
	public void testSchedule() {
		String configFileName = "testInput.txt";
		Configuration config;
		try {
			config = Configuration.loadFromFile(configFileName);
		} catch (NumberFormatException e) {
			System.err.println("File " + configFileName + " is in wrong format.");
			Assert.fail("File " + configFileName + " is in wrong format.");
			return;
		} catch (FileNotFoundException e) {
			System.err.println("File " + configFileName + " not found.");
			Assert.fail("File " + configFileName + " not found.");
			return;
		}
		Scheduler asScheduler = new ActiveScheduler(config);
		Result result = asScheduler.schedule();
		System.out.println(result);
	}
}
