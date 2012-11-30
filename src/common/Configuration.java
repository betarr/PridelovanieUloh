package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Configuration {

	private int numberOfMachines;
	private List<JobPart> jobsParts;
	
	public static Configuration generate(int numOfMachines, int numOfJobs, int numOfJobsParts, int maxCost) {
		Configuration config = new Configuration();
		
		config.setNumberOfMachines(numOfMachines);
		
		Random r = new Random();
		for (int i = 0; i < numOfJobsParts; i++) {
			int jobIndex = r.nextInt(numOfJobs)+1;
			int machineIndex = r.nextInt(numOfMachines)+1;
			int cost = r.nextInt(maxCost)+1;
			JobPart jp = new JobPart();
			jp.setJob(jobIndex);
			jp.setMachine(machineIndex);
			jp.setCost(cost);
			config.addJobPart(jp);
		}
		
		return config;
	}
	
	public static void saveTofile(final Configuration config, final String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append(config.numberOfMachines).append(System.lineSeparator());
		for (JobPart jp : config.jobsParts) {
			sb.append(jp.getJob()).append(" ")
					.append(jp.getMachine()).append(" ")
					.append(jp.getCost()).append(System.lineSeparator());
		}
		
		try {
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration loadFromFile(String fileName) {
		try {
			Configuration config = new Configuration();
			
			Scanner scanner = new Scanner(new File(fileName));
			boolean firstRowReaded = false;
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();
				if (!firstRowReaded) {
					config.setNumberOfMachines(Integer.parseInt(row));
					firstRowReaded = true;
				} else {
					StringTokenizer st = new StringTokenizer(row, " ");
					JobPart jb = new JobPart();
					jb.setJob(Integer.parseInt(st.nextToken()));
					jb.setMachine(Integer.parseInt(st.nextToken()));
					jb.setCost(Integer.parseInt(st.nextToken()));
					config.addJobPart(jb);
				}
			}
			scanner.close();
			return config;
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " not found!");
			return null;
		}
	}
	
	public void addJobPart(JobPart jobPart) {
		if (this.jobsParts == null) {
			this.jobsParts = new ArrayList<JobPart>();
		}
		this.jobsParts.add(jobPart);
	}

	public int getNumberOfMachines() {
		return numberOfMachines;
	}

	public void setNumberOfMachines(int numberOfMachines) {
		this.numberOfMachines = numberOfMachines;
	}

	public List<JobPart> getJobsParts() {
		return jobsParts;
	}

	public void setJobsParts(List<JobPart> jobsParts) {
		this.jobsParts = jobsParts;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append(System.lineSeparator());
		sb.append("numberOfMachines: ").append(this.numberOfMachines).append(System.lineSeparator());
		sb.append("Job parts:").append(System.lineSeparator());
		for (JobPart jp : this.jobsParts) {
			sb.append(jp).append(System.lineSeparator());
		}
		return sb.toString();
	}
}
