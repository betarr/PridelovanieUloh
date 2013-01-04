package taskassignment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import algorithms.active.ActiveScheduler;
import algorithms.activebrute.ActiveBruteScheduler;
import algorithms.bruteforce.BruteForceScheduler;

import common.Configuration;

public class TaskAssignment {

	public static void main(String[] args) {
		int num = 20;
		Configuration config = Configuration.generate(num, num, num, num);
//		Configuration.saveTofile(config, "test.txt");
		
//		String configFileName = "testInput.txt";
//		Configuration config;
//		try {
//			config = Configuration.loadFromFile(configFileName);
//		} catch (NumberFormatException e) {
//			System.err.println("File " + configFileName + " is in wrong format.");
//			return;
//		} catch (FileNotFoundException e) {
//			System.err.println("File " + configFileName + " not found.");
//			return;
//		}
		
//		TaskAssignment.runBruteScheduler(config);
		TaskAssignment.runActiveBruteScheduler(config);
		TaskAssignment.runActiveScheduler(config);
	}
	
	public static void main2(String[] args) {
		List<String> argsList = Arrays.asList(args);
	}

	private static void runBruteScheduler(Configuration config) {
		long startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Brute Force started at " + new Date(startTime));
		
		System.out.println(config);
		BruteForceScheduler bt = new BruteForceScheduler(config);
		System.out.println(bt.schedule());
		//System.out.println(bt.scheduleFromFile("nonconfilcted-combinations.txt"));
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Brute Force ended at " + new Date(endTime));
		System.out.println("Brute Force took " + (endTime - startTime) + " ms");
	}
	
	private static void runActiveBruteScheduler(Configuration config) {
		long startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Active Brute Force started at " + new Date(startTime));
		
		System.out.println(config);
		ActiveBruteScheduler abs = new ActiveBruteScheduler(config);
		System.out.println(abs.schedule());
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Active Brute Force ended at " + new Date(endTime));
		System.out.println("Active Brute Force took " + (endTime - startTime) + " ms");
	}

	private static void runActiveScheduler(Configuration config) {
		long startTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Active started at " + new Date(startTime));
		
		System.out.println(config);
		ActiveScheduler as = new ActiveScheduler(config);
		System.out.println(as.schedule());
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Active ended at " + new Date(endTime));
		System.out.println("Active took " + (endTime - startTime) + " ms");
	}

}
