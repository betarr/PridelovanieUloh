package algorithms.active;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import algorithms.Scheduler;

import common.Configuration;
import common.JobPart;
import common.Result;
import common.Utils;

public class ActiveScheduler extends Scheduler {
	
	private Configuration config;
	
	private Map<Integer, List<JobPart>> jobPartsByJob;
	private Graph graph;
	
	public ActiveScheduler(Configuration config) {
		this.config = config;
		
		this.jobPartsByJob = Utils.getSortedJobPartsInMapByIndex(this.config.getJobsPartsAsMapByJobs());
		this.graph = new Graph(this.jobPartsByJob);
	}

	@Override
	public Result schedule() {
		Result result = new Result();
		
		List<JobPart> omega = this.getListOfFistJobPartByJob(this.jobPartsByJob);
		List<Integer> rList = this.getRForJobPartsInOmega(omega);
		
		while (!omega.isEmpty()) {
			if (omega.size() != rList.size()) {
				System.err.println("Omega list and r list can not have different sizes");
				return null;
			}
			List<Integer> tList = this.getTForJobPartsInOmega(omega, rList);
			int minIIndex = -1;
			int minT = Integer.MAX_VALUE;
			for (int i = 0; i < tList.size(); i++) {
				if (tList.get(i) < minT) {
					minT = tList.get(i);
					minIIndex = i;
				}
			}
			int minI = omega.get(minIIndex).getMachine();
			
			List<JobPart> omega2 = this.getOmega2JobParts(omega, rList, minT, minI);
			
			JobPart toResult = (omega2.size()==1) ? omega2.get(0) : this.getBestJobPartToResult(omega2, this.graph);
			result.addToResult(toResult, omega, rList);
			this.graph.addYEdgesToGraph(toResult);
			List<JobPart> newOmega = new ArrayList<JobPart>();
			for (JobPart jp : omega) {
				if (jp.equals(toResult)) {
					Node jpNode = graph.getJPNodeByJobPart(jp);
					Node nextNode = graph.getNodeSuccessorOnXEdge(jpNode);
					if (!nextNode.isV()) {
						newOmega.add(nextNode.getJobPart());
					}
				} else {
					newOmega.add(jp);
				}
			}
			List<Integer> newRList = this.getRForJobPartsInOmega(newOmega);
			omega = newOmega;
			rList = newRList;
		}
		return result;
	}

	private List<JobPart> getListOfFistJobPartByJob(
			Map<Integer, List<JobPart>> jobPartsByJob2) {
		
		List<JobPart> result = new ArrayList<JobPart>();
		for (List<JobPart> jobPartList : jobPartsByJob2.values()) {
			JobPart jp = jobPartList.get(0);
			if (jp != null) {
				result.add(jp);
			}
		}
		return result;
	}
	
	private List<Integer> getRForJobPartsInOmega(List<JobPart> omega) {
		List<Integer> rList = new ArrayList<Integer>();
		for (JobPart jp : omega) {
			Integer longestPath = this.graph.getLongestCostFromStartToNodeWithJobPart(jp);
			rList.add(longestPath);
		}
		return rList;
	}
	
	private List<Integer> getTForJobPartsInOmega(List<JobPart> omega, List<Integer> rList) {
		List<Integer> tList = new ArrayList<Integer>();
		for (int i = 0; i < omega.size(); i++) {
			int r = rList.get(i);
			int cost = omega.get(i).getCost();
			tList.add(r + cost);
		}
		return tList;
	}
	
	private List<JobPart> getOmega2JobParts(List<JobPart> omega, List<Integer> rList, int minT, int minI) {
		List<JobPart> omega2 = new ArrayList<JobPart>();
		for (int i = 0; i < omega.size(); i++) {
			JobPart jp = omega.get(i);
			if (jp.getMachine() == minI) {
				if (rList.get(i) < minT) {
					omega2.add(jp);
				}
			}
		}
		return omega2;
	}
	
	private JobPart getBestJobPartToResult(List<JobPart> omega2, Graph currentGraph) {
		int minLB = -1;
		int minLBIndex = -1;
		
		for (int i = 0; i < omega2.size(); i++) {
			JobPart jobPart = omega2.get(i);
			Node fromNode = currentGraph.getJPNodeByJobPart(jobPart);
			List<Node> allNodesByMachine = currentGraph.getAllNodesByMachine(jobPart.getMachine());
			allNodesByMachine.remove(fromNode);
			List<Node> reallyAddedEdgeToNodes = new ArrayList<Node>();
			for (Node toNode : allNodesByMachine) {
				if (toNode.getyEdges().size() == 0) {
					currentGraph.setYEdgeBetweenNodes(fromNode, toNode);
					reallyAddedEdgeToNodes.add(toNode);
				}
			}

			int currentLB = this.calculateLBForGraph(currentGraph);
			if (minLB == -1 || currentLB < minLB) {
				minLB = currentLB;
				minLBIndex = i;
			}
			
			for (Node toNode : reallyAddedEdgeToNodes) {
				currentGraph.removeYEdgeBetweenNodes(fromNode, toNode);
			}
		}
		
		return omega2.get(minLBIndex);
	}

	private int calculateLBForGraph(Graph currentGraph) {
		int criticalPathCost = currentGraph.getCostOfCriticalPath();
		int maxLB = 0;
		
		int numberOfMachines = this.config.getNumberOfMachines();
		for (int i = 1; i <= numberOfMachines; i++) {
			List<Node> nodesByMachine = currentGraph.getAllNodesByMachine(i);
			if (nodesByMachine.size() == 0) {
				continue;
			}
			
			int currentLB = this.calculateDalayForMachine(nodesByMachine, currentGraph, criticalPathCost);
			maxLB = (currentLB > maxLB) ? currentLB : maxLB;
		}
		
		return maxLB;
	}

	private int calculateDalayForMachine(List<Node> nodesByMachine, Graph currentGraph, int criticalPathCost) {
		int jobPartsCount = nodesByMachine.size();
		
		DelaySolver ds = new DelaySolver();
		ds.setCriticalPathCost(criticalPathCost);
		for (int i = 0; i < jobPartsCount; i++) {
			Node node = nodesByMachine.get(i);
			JobPart jp = node.getJobPart();
			
			int r = currentGraph.getLongestCostFromStartToNodeWithJobPart(jp);
			int delta = currentGraph.getLongestCostFromNodeWithJobPartToEnd(jp);
			int d = criticalPathCost - delta + jp.getCost();
			
			ds.add(r, delta, d, jp.getCost());
		}
		return ds.calculateDelay();
	}

}
