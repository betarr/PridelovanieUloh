package taskassignment;

import bruteforce.BruteForce;

import common.Configuration;

public class TaskAssignment {

	public static void main(String[] args) {
//		Configuration config = Configuration.generate(3, 5, 10, 4);
//		System.out.println(config);
//		Configuration.saveTofile(config, "test.txt");
		
		Configuration config = Configuration.loadFromFile("test.txt");
		System.out.println(config);
		BruteForce bt = new BruteForce(config);
		System.out.println(bt.compute());
		System.out.println(bt.getJobsByMachines());
	}

}
