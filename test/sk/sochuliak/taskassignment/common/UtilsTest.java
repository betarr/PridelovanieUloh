package sk.sochuliak.taskassignment.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import sk.sochuliak.timeassignment.common.JobPart;
import sk.sochuliak.timeassignment.common.Utils;

public class UtilsTest {

	@Test
	public void testGetJobPartListAsMapByJob() {
		List<JobPart> input = new ArrayList<JobPart>();
		input.add(this.createJobPartWithJob(1));
		input.add(this.createJobPartWithJob(2));
		input.add(this.createJobPartWithJob(3));
		input.add(this.createJobPartWithJob(1));
		input.add(this.createJobPartWithJob(2));
		input.add(this.createJobPartWithJob(2));
		
		Map<Integer, List<JobPart>> actualMap = Utils.getJobPartListAsMapByJob(input);
		
		int expectedJob1Count = 2;
		int actualJob1Count = actualMap.get(1).size();
		assertEquals(expectedJob1Count, actualJob1Count);
		
		int expectedJob2Count = 3;
		int actualJob2Count = actualMap.get(2).size();
		assertEquals(expectedJob2Count, actualJob2Count);
		
		int expectedJob3Count = 1;
		int actualJob3Count = actualMap.get(3).size();
		assertEquals(expectedJob3Count, actualJob3Count);
	}
	
	@Test
	public void testGetJobPartListAsMapByMachine() {
		List<JobPart> input = new ArrayList<JobPart>();
		input.add(this.createJobPartWithMachine(1));
		input.add(this.createJobPartWithMachine(2));
		input.add(this.createJobPartWithMachine(3));
		input.add(this.createJobPartWithMachine(1));
		input.add(this.createJobPartWithMachine(2));
		input.add(this.createJobPartWithMachine(2));
		
		Map<Integer, List<JobPart>> actualMap = Utils.getJobPartListAsMapByMachine(input);
		
		int expectedMachine1Count = 2;
		int actualMachine1Count = actualMap.get(1).size();
		assertEquals(expectedMachine1Count, actualMachine1Count);
		
		int expectedMachine2Count = 3;
		int actualMachine2Count = actualMap.get(2).size();
		assertEquals(expectedMachine2Count, actualMachine2Count);
		
		int expectedMachine3Count = 1;
		int actualMachine3Count = actualMap.get(3).size();
		assertEquals(expectedMachine3Count, actualMachine3Count);
	}
	
	@Test
	public void testAreJobPartsInTimeConflict() {
		JobPart jp1 = new JobPart();
		jp1.setStartTime(2);
		jp1.setCost(3);
		
		JobPart jp2 = new JobPart();
		jp2.setStartTime(4);
		jp2.setCost(2);
		
		JobPart jp3 = new JobPart();
		jp3.setStartTime(6);
		jp3.setCost(4);
		
		assertEquals(true, Utils.areJobPartsInTimeConflict(jp1, jp2));
		assertEquals(false, Utils.areJobPartsInTimeConflict(jp2, jp3));
		assertEquals(false, Utils.areJobPartsInTimeConflict(jp1, jp3));
		
	}
	
	@Test
	public void testAreJobPartsInJobsOrderConflict() {
		JobPart jp1 = new JobPart();
		jp1.setIndex(2);
		jp1.setStartTime(1);
		
		JobPart jp2 = new JobPart();
		jp2.setIndex(2);
		jp2.setStartTime(3);
		
		JobPart jp3 = new JobPart();
		jp3.setIndex(4);
		jp3.setStartTime(5);
		
		assertEquals(true, Utils.areJobPartsInJobsOrderConflict(jp1, jp2));
		assertEquals(false, Utils.areJobPartsInJobsOrderConflict(jp2, jp3));
	}
	
	@Test
	public void testGetSortedJobPartsInMapByIndex() {
		List<JobPart> input = new ArrayList<JobPart>();

		int[] machines = {1, 1, 1, 1, 2, 2, 3, 3, 3};
		int[] indexes = {3, 1, 5, 4, 3, 4, 4, 7, 2};
		
		for (int i = 0; i < machines.length; i++) {
			JobPart jp = this.createJobPartWithMachine(machines[i]);
			jp.setIndex(indexes[i]);
			input.add(jp);
		}
		
		Map<Integer, List<JobPart>> mapByMachines = Utils.getJobPartListAsMapByMachine(input);
		Map<Integer, List<JobPart>> mapByMachinesSortedByIndex = Utils.getSortedJobPartsInMapByIndex(mapByMachines);
		
		for (List<JobPart> jobPartList : mapByMachinesSortedByIndex.values()) {
			if (jobPartList.size() > 1) {
				for (int i = 0; i < jobPartList.size()-1; i++) {
					assertEquals(true, jobPartList.get(i).getIndex() <= jobPartList.get(i+1).getIndex());
				}
			}
		}
	}
	
	private JobPart createJobPartWithJob(int job) {
		JobPart jp = new JobPart();
		jp.setJob(job);
		return jp;
	}
	
	private JobPart createJobPartWithMachine(int machine) {
		JobPart jp = new JobPart();
		jp.setMachine(machine);
		return jp;
	}
	
}
