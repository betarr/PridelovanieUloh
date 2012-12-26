package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static int getTotalCostOfJobParts(List<JobPart> jobPartList) {
		int result = 0;
		for (JobPart jp : jobPartList) {
			result += jp.getCost();
		}
		return result;
	}
	
	public static List<List<JobPart>> getAllCombinationsOfJobPartByStartTime(int maxStartTime, List<JobPart> jobParts) {
		int rangeFrom = 1;
		int rangeTo = maxStartTime;
		int length = jobParts.size();
		List<List<Integer>> startTimeCombinations = new ArrayList<List<Integer>>();
		
//		long startTime = Calendar.getInstance().getTimeInMillis();
		getAllCombinationsOfNumbers(rangeFrom, rangeTo, length, startTimeCombinations, new ArrayList<Integer>());
//		long endTime = Calendar.getInstance().getTimeInMillis();
//		long totalTime = endTime - startTime;
		
//		for (List<Integer> combination : startTimeCombinations) {
//			System.out.println(combination);
//		}
//		System.out.println("Generated " + startTimeCombinations.size() + " combinations in " + totalTime + " miliseconds.");
//		System.out.println("Max start time: " + maxStartTime);
		
		List<List<JobPart>> jobPartsCombinations = new ArrayList<List<JobPart>>();
		for (List<Integer> combination : startTimeCombinations) {
			List<JobPart> jobPartList = new ArrayList<JobPart>();
			for (int i = 0; i < combination.size(); i++) {
				JobPart jp = jobParts.get(i);
				JobPart newJp = new JobPart(jp);
				int newStartTime = combination.get(i);
				newJp.setStartTime(newStartTime);
				jobPartList.add(newJp);
			}
			jobPartsCombinations.add(jobPartList);
		}
		return jobPartsCombinations;
	}
	
	public static void getAllCombinationsOfNumbers(int rangeFrom, int rangeTo, int length, List<List<Integer>> result, List<Integer> build) {
		if (build.size() == length) {
			result.add(build);
			return;
		}
		
		for (int i = rangeFrom; i <= rangeTo; i++) {
			List<Integer> buildClone = new ArrayList<Integer>(build);
			buildClone.add(i);
			getAllCombinationsOfNumbers(rangeFrom, rangeTo, length, result, new ArrayList<Integer>(buildClone));
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
