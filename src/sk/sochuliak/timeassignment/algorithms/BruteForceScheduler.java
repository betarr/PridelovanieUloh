package sk.sochuliak.timeassignment.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import sk.sochuliak.timeassignment.common.Configuration;
import sk.sochuliak.timeassignment.common.JobPart;
import sk.sochuliak.timeassignment.common.Result;
import sk.sochuliak.timeassignment.common.Scheduler;
import sk.sochuliak.timeassignment.common.Utils;



public class BruteForceScheduler extends Scheduler {

	private Configuration config;
	private int sumOfCosts;
	
	public BruteForceScheduler(Configuration config) {
		this.config = config;
		this.sumOfCosts = this.config.getSumOfCosts();
	}
	
	@Override
	public Result schedule() {
		List<List<JobPart>> combinationsOfJobParts = Utils.getAllNonConfilctedCombinationsOfJobPartByStartTime(this.sumOfCosts-1, this.config.getJobsParts());
		
		Map<Integer, List<JobPart>> bestCombinationByMachine = null;
		int bestCombinationScore = -1;
		
		for (List<JobPart> listOfJobParts : combinationsOfJobParts) {
			Map<Integer, List<JobPart>> combinationsOfJobPartByMachine = Utils.getJobPartListAsMapByMachine(listOfJobParts);
			int combinationScore = getScoreOfCombination(combinationsOfJobPartByMachine);
			if (bestCombinationScore == -1
					|| combinationScore < bestCombinationScore) {
				bestCombinationScore = combinationScore;
				bestCombinationByMachine = combinationsOfJobPartByMachine;
			}
		}
		
		Result result = transformCombinationToResult(bestCombinationByMachine);
		return result;
	}
	
	public Result scheduleFromFile(String fileName) {
		List<JobPart> jobParts = this.config.getJobsParts();
		Map<Integer, List<JobPart>> bestCombinationByMachine = null;
		int bestCombinationScore = -1;
		try {
			Scanner scanner = new Scanner(new File(fileName));
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
				
				Map<Integer, List<JobPart>> combinationsOfJobPartByMachine = Utils.getJobPartListAsMapByMachine(combination);
				int combinationScore = getScoreOfCombination(combinationsOfJobPartByMachine);
				if (bestCombinationScore == -1 || combinationScore < bestCombinationScore) {
					bestCombinationScore = combinationScore;
					bestCombinationByMachine = combinationsOfJobPartByMachine;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Result result = transformCombinationToResult(bestCombinationByMachine);
		return result;
	}
	
	@SuppressWarnings("unused")
	private void printOutCombination(
			Map<Integer, List<JobPart>> combinationsOfJobPartByMachine, String type) {
		StringBuilder sb = new StringBuilder();
		Set<Integer> keys = combinationsOfJobPartByMachine.keySet();
		for (Integer key : keys) {
			sb.append(type).append(" ").append(key).append(Utils.LINE_SEPARATOR);
			List<JobPart> list = combinationsOfJobPartByMachine.get(key);
			for (JobPart jp : list) {
				sb.append(jp).append(Utils.LINE_SEPARATOR);
			}
			sb.append(Utils.LINE_SEPARATOR);
		}
		System.out.println(sb.toString());
	}

	public static boolean isCombinationNotConflicted(
			Map<Integer, List<JobPart>> jobPartCombinationByMachine,
			Map<Integer, List<JobPart>> jobPartCombinationByJob) {
		boolean isConfictOnSomeMachine = BruteForceScheduler.isConflictOnSomeMachine(jobPartCombinationByMachine);
		boolean isConfictOnJobs = BruteForceScheduler.isConflictOnJobs(jobPartCombinationByJob);
		return !isConfictOnSomeMachine && !isConfictOnJobs;
	}
	
	private static boolean isConflictOnSomeMachine(
			Map<Integer, List<JobPart>> jobPartCombinationByMachine) {
		for (List<JobPart> jobPartList : jobPartCombinationByMachine.values()) {
			if (BruteForceScheduler.isConflictOnMachine(jobPartList)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isConflictOnMachine(List<JobPart> jobPartList) {
		for (JobPart jp : jobPartList) {
			if (BruteForceScheduler.isAnyOtherRunningExceptActualAtSameTime(jobPartList, jp)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAnyOtherRunningExceptActualAtSameTime(
			List<JobPart> jobPartList, JobPart actual) {
		for (JobPart jp : jobPartList) {
			if (!jp.equals(actual) && Utils.areJobPartsInTimeConflict(jp, actual)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isConflictOnJobs(Map<Integer, List<JobPart>> jobPartCombinationByJob) {
		Set<Integer> jobsNumber = jobPartCombinationByJob.keySet();
		for (Integer jobNumber : jobsNumber) {
			if (BruteForceScheduler.areJobsInConflict(jobPartCombinationByJob.get(jobNumber))) {
				return true;
			}
		}
		return false;
	}

	private static boolean areJobsInConflict(List<JobPart> jobPartsList) {
		for (JobPart jp : jobPartsList) {
			if (BruteForceScheduler.isJobPartInRightOrderToOthers(jobPartsList, jp)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isJobPartInRightOrderToOthers(List<JobPart> jobPartsList,
			JobPart jp) {
		for (JobPart actual : jobPartsList) {
			if (!jp.equals(actual) && Utils.areJobPartsInJobsOrderConflict(jp, actual)) {
				return true;
			}
		}
		return false;
	}

	private int getScoreOfCombination(Map<Integer, List<JobPart>> jobPartCombinationByMachine) {
		List<Integer> costOfMachines = new ArrayList<Integer>();
		for (List<JobPart> jobPartList : jobPartCombinationByMachine.values()) {
			costOfMachines.add(getValueOfCostForJobPartListPerMachine(jobPartList));
		}
		return Collections.max(costOfMachines);
	}
	
	private int getValueOfCostForJobPartListPerMachine(List<JobPart> jobPartList) {
		int max = 0;
		for (JobPart jp : jobPartList) {
			int value = jp.getStartTime() + jp.getCost();
			max = (value < max) ? value : max;
		}
		return max;
	}

	private Result transformCombinationToResult(Map<Integer, List<JobPart>> jobPartCombination) {
		Result result = new Result();
		result.setResult(jobPartCombination);
		return result;
	}
}
