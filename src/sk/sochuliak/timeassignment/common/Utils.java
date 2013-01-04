package sk.sochuliak.timeassignment.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import sk.sochuliak.timeassignment.algorithms.BruteForceScheduler;


public class Utils {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static int getTotalCostOfJobParts(List<JobPart> jobPartList) {
		int result = 0;
		for (JobPart jp : jobPartList) {
			result += jp.getCost();
		}
		return result;
	}
	
	public static List<List<JobPart>> getAllNonConfilctedCombinationsOfJobPartByStartTime(int maxStartTime, List<JobPart> jobParts) {
		String combinationsFileName = "nonconfilcted-combinations.txt";
		
		int rangeFrom = 0;
		int rangeTo = maxStartTime;
		int length = jobParts.size();
		
		try {
			FileWriter fileWriter;
			fileWriter = new FileWriter(combinationsFileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			getAllNonConflictedCombinationsOfNumbers(rangeFrom, rangeTo, length, bufferedWriter, new ArrayList<Integer>(), jobParts);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<List<JobPart>> jobPartsCombinations = new ArrayList<List<JobPart>>();
		
		try {
			Scanner scanner = new Scanner(new File(combinationsFileName));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				StringTokenizer st = new StringTokenizer(line, " ");
				List<Integer> startTimes = new ArrayList<Integer>();
				while (st.hasMoreTokens()) {
					startTimes.add(Integer.valueOf(st.nextToken()));
				}
				
				List<JobPart> combination = new ArrayList<JobPart>();
				if (startTimes.size() != jobParts.size()) {
					System.err.println("Wrong sizes of lists");
				}
				
				for (int i = 0; i < jobParts.size(); i++) {
					JobPart jp = jobParts.get(i);
					JobPart newJp = new JobPart(jp);
					newJp.setStartTime(startTimes.get(i));
					combination.add(newJp);
				}
				jobPartsCombinations.add(combination);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return jobPartsCombinations;
	}
	
	public static void getAllNonConflictedCombinationsOfNumbers(int rangeFrom, int rangeTo, int length, BufferedWriter bufferedWriter, List<Integer> build, List<JobPart> jobPartList) throws IOException {
		if (build.size() == length) {
			List<JobPart> candidatesList = new ArrayList<JobPart>();
			for (int i = 0; i < build.size(); i++) {
				JobPart jp = jobPartList.get(i);
				JobPart newJp = new JobPart(jp);
				newJp.setStartTime(build.get(i));
				candidatesList.add(newJp);
			}
			if (BruteForceScheduler.isCombinationNotConflicted(Utils.getJobPartListAsMapByMachine(candidatesList), Utils.getJobPartListAsMapByJob(candidatesList))) {
				StringBuffer sb = new StringBuffer();
				for (JobPart jp : candidatesList) {
					sb.append(jp.getStartTime()).append(" ");
				}
				sb.append(Utils.LINE_SEPARATOR);
				bufferedWriter.write(sb.toString());
			}
			return;
		}
		
		for (int i = rangeFrom; i <= rangeTo; i++) {
			List<Integer> buildClone = new ArrayList<Integer>(build);
			buildClone.add(i);
			getAllNonConflictedCombinationsOfNumbers(rangeFrom, rangeTo, length, bufferedWriter, new ArrayList<Integer>(buildClone), jobPartList);
		}
	}
	
	public static Map<Integer, List<JobPart>> getJobPartListAsMapByJob(List<JobPart> jobPartList) {
		Map<Integer, List<JobPart>> result = new HashMap<Integer, List<JobPart>>();
		for (JobPart jp : jobPartList) {
			int job = jp.getJob();
			if (result.get(job) == null) {
				List<JobPart> jobsPartList = new ArrayList<JobPart>();
				result.put(job, jobsPartList);
			}
			result.get(job).add(jp);
		}
		return result;
	}
	
	public static Map<Integer, List<JobPart>> getJobPartListAsMapByMachine(List<JobPart> jobPartList) {
		Map<Integer, List<JobPart>> result = new HashMap<Integer, List<JobPart>>();
		for (JobPart jp : jobPartList) {
			int machine = jp.getMachine();
			if (result.get(machine) == null) {
				List<JobPart> jobsPartList = new ArrayList<JobPart>();
				result.put(machine, jobsPartList);
			}
			result.get(machine).add(jp);
		}
		return result;
	}
	
	public static boolean areJobPartsInTimeConflict(JobPart jp1, JobPart jp2) {
		if (jp1.getStartTime() == jp2.getStartTime()) {
			return true;
		}
		if (jp1.getStartTime() < jp2.getStartTime()
				&& jp1.getStartTime()+jp1.getCost() > jp2.getStartTime()) {
			return true;
		}
		if (jp2.getStartTime() < jp1.getStartTime()
				&& jp2.getStartTime()+jp2.getCost() > jp1.getStartTime()) {
			return true;
		}
		return false;
	}

	public static boolean areJobPartsInJobsOrderConflict(JobPart jp1,
			JobPart jp2) {
		if (Utils.areJobPartsInTimeConflict(jp1, jp2)) {
			return true;
		}
		if (jp1.getIndex() < jp2.getIndex()) {
			return jp1.getStartTime() > jp2.getStartTime();
		} else {
			return jp2.getStartTime() > jp1.getStartTime();
		}
	}

	public static Map<Integer, List<JobPart>> getSortedJobPartsInMapByIndex(Map<Integer, List<JobPart>> jobPartMap) {
		Set<Integer> indexes = jobPartMap.keySet();
		for (Integer index : indexes) {
			List<JobPart> jobPartList = jobPartMap.get(index);
			jobPartMap.put(index, Utils.getJobListSortedByIndex(jobPartList));
		}
		return jobPartMap;
	}
	
	private static List<JobPart> getJobListSortedByIndex(List<JobPart> jobPartList) {
		List<JobPart> result = new ArrayList<JobPart>();
		while (!jobPartList.isEmpty()) {
			JobPart minJobPart = Utils.getFirstJobFromListByIndex(jobPartList);
			result.add(minJobPart);
			jobPartList.remove(minJobPart);
		}
		return result;
	}

	private static JobPart getFirstJobFromListByIndex(List<JobPart> jobPartList) {
		int minIndex = Integer.MAX_VALUE;
		JobPart result = null;
		for (JobPart jobPart : jobPartList) {
			if (jobPart.getIndex() < minIndex) {
				result = jobPart;
				minIndex = jobPart.getIndex();
			}
		}
		return result;
	}
	
}
