package taskassignment;

import java.io.FileNotFoundException;

import algorithms.BruteForceScheduler;

import common.Configuration;

public class TaskAssignment {

	public static void main(String[] args) {
//		Configuration config = Configuration.generate(3, 5, 10, 4);
//		Configuration config = Configuration.generate(3, 4, 5, 6);
//		System.out.println(config);
//		Configuration.saveTofile(config, "test.txt");
		
		String configFileName = "testInput.txt";
		Configuration config;
		try {
			config = Configuration.loadFromFile(configFileName);
		} catch (NumberFormatException e) {
			System.err.println("File " + configFileName + " is in wrong format.");
			return;
		} catch (FileNotFoundException e) {
			System.err.println("File " + configFileName + " not found.");
			return;
		}
		System.out.println(config);
		BruteForceScheduler bt = new BruteForceScheduler(config);
		System.out.println(bt.schedule());
	}

}
