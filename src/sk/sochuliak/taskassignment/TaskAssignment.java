package sk.sochuliak.taskassignment;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sk.sochuliak.timeassignment.algorithms.ActiveBruteScheduler;
import sk.sochuliak.timeassignment.algorithms.ActiveScheduler;
import sk.sochuliak.timeassignment.algorithms.BruteForceScheduler;
import sk.sochuliak.timeassignment.common.Configuration;
import sk.sochuliak.timeassignment.common.Utils;



public class TaskAssignment {

	public static void main2(String[] args) {
//		int num = 20;
//		Configuration config = Configuration.generate(num, num, num, num);
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
//		TaskAssignment.runActiveBruteScheduler(config);
//		TaskAssignment.runActiveScheduler(config);
	}
	
	public static void main(String[] args) {
		List<String> argsList = Arrays.asList(args);
		
		String filePath = "";
		try {
			Configuration config = null;
			
			boolean configCommand = argsList.get(0).equals("-config");
			boolean helpCommand = argsList.get(0).equals("-help");
			
			if (configCommand) {
				int fromFileCommandIndex = TaskAssignment.getIndexOfStringInList(argsList, "-file");
				int generateCommandIndex = TaskAssignment.getIndexOfStringInList(argsList, "-generate");
				
				if (fromFileCommandIndex != -1) {
					filePath = argsList.get(fromFileCommandIndex+1);
					config = Configuration.loadFromFile(filePath);
				} else if (generateCommandIndex != -1) {
					int numOfMachines = Integer.parseInt(argsList.get(generateCommandIndex+1));
					int numOfJobs = Integer.parseInt(argsList.get(generateCommandIndex+2));
					int numOfJobParts = Integer.parseInt(argsList.get(generateCommandIndex+3));
					int maxCost = Integer.parseInt(argsList.get(generateCommandIndex+4));
					config = Configuration.generate(numOfMachines, numOfJobs, numOfJobParts, maxCost);
				} else {
					TaskAssignment.printOutWrongText();
					return;
				}
				boolean bruteCommand = argsList.contains("-brute");
				boolean activeBrute = argsList.contains("-activebrute");
				boolean active = argsList.contains("-active");
				boolean toFileCommand = argsList.contains("-tofile");
				
				if (bruteCommand) {
					TaskAssignment.runBruteScheduler(config);
				}
				if (activeBrute) {
					TaskAssignment.runActiveBruteScheduler(config);
				}
				if (active) {
					TaskAssignment.runActiveScheduler(config);
				}
				if (toFileCommand) {
					int toFileCommandIndex = TaskAssignment.getIndexOfStringInList(argsList, "-tofile");
					String fileName = argsList.get(toFileCommandIndex+1);
					Configuration.saveTofile(config, fileName);
					System.out.println("Configuration saved to " + fileName + ".");
				}
				
				if (!bruteCommand && !activeBrute && !active &&!toFileCommand) {
					TaskAssignment.printOutWrongText();
				}
			} else if (helpCommand) {
				TaskAssignment.printOutHelpText();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			TaskAssignment.printOutWrongText();
		} catch (NumberFormatException e) {
			TaskAssignment.printOutWrongText();
		} catch (FileNotFoundException e) {
			TaskAssignment.printOutFileNotFoundText(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printOutHelpText() {
		StringBuffer sb = new StringBuffer();
		sb.append("TASK ASSIGNMENT").append(Utils.LINE_SEPARATOR).append(Utils.LINE_SEPARATOR);
		sb.append("Using:").append(Utils.LINE_SEPARATOR)
			.append(Utils.TAB).append("-config").append(Utils.LINE_SEPARATOR)
			.append(Utils.TAB).append("[-generate numberOfMachines numOfJobs numOfJobParts maximalCost]").append(Utils.LINE_SEPARATOR)
			.append(Utils.TAB).append("[-file fileName]").append(Utils.LINE_SEPARATOR)
			.append(Utils.TAB).append("-brute || -activebrute || -active").append(Utils.LINE_SEPARATOR).append(Utils.LINE_SEPARATOR)
			.append(Utils.TAB).append("-help");
		
		System.out.println(sb.toString());
	}
	
	private static void printOutWrongText() {
		System.err.println("I am afraid you do not know how to use me. If you need help, use switcher -help.");
	}
	
	private static void printOutFileNotFoundText(String filePath) {
		System.err.println("File " + filePath + " not found.");
	}
	
	private static int getIndexOfStringInList(List<String> list, String string) {
		for (int i = 0; i < list.size(); i++) {
			if (string.equals(list.get(i))) {
				return i;
			}
		}
		return -1;
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
		System.out.println();
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
		System.out.println();
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
		System.out.println();
	}

}
