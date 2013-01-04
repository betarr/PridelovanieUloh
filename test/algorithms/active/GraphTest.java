package algorithms.active;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import common.Configuration;
import common.JobPart;
import common.Utils;

public class GraphTest {

	@Test
	public void testGetJPNodeByJobPart() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		JobPart searchedJobPart = new JobPart();
		searchedJobPart.setJob(3);
		searchedJobPart.setMachine(1);
		searchedJobPart.setCost(3);
		searchedJobPart.setIndex(2);
		Node findedNode = graph.getJPNodeByJobPart(searchedJobPart);
		JobPart findedJobPart = findedNode.getJobPart();
		assertNotNull(findedJobPart);
		assertEquals(searchedJobPart.getJob(), findedJobPart.getJob());
		assertEquals(searchedJobPart.getMachine(), findedJobPart.getMachine());
		assertEquals(searchedJobPart.getCost(), findedJobPart.getCost());
		assertEquals(searchedJobPart.getIndex(), findedJobPart.getIndex());
		
		JobPart searchedJobPart2 = new JobPart();
		searchedJobPart2.setJob(2);
		searchedJobPart2.setMachine(2);
		searchedJobPart2.setCost(2);
		searchedJobPart2.setIndex(1);
		Node findedNode2 = graph.getJPNodeByJobPart(searchedJobPart2);
		JobPart findedJobPart2 = findedNode2.getJobPart();
		assertNotNull(findedJobPart2);
		assertEquals(searchedJobPart2.getJob(), findedJobPart2.getJob());
		assertEquals(searchedJobPart2.getMachine(), findedJobPart2.getMachine());
		assertEquals(searchedJobPart2.getCost(), findedJobPart2.getCost());
		assertEquals(searchedJobPart2.getIndex(), findedJobPart2.getIndex());
	}
	
	@Test
	public void testGetNodeSuccessorOnXEdge() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		
		JobPart jp = new JobPart();
		jp.setJob(1);
		jp.setMachine(3);
		jp.setCost(2);
		jp.setIndex(2);
		
		Node node = graph.getJPNodeByJobPart(jp);
		JobPart findedJobPart = node.getJobPart();
		assertNotNull(findedJobPart);
		
		JobPart expectedSuccessorJobPart = new JobPart();
		expectedSuccessorJobPart.setJob(1);
		expectedSuccessorJobPart.setMachine(2);
		expectedSuccessorJobPart.setCost(3);
		expectedSuccessorJobPart.setIndex(3);
		
		Node actualSuccessor = graph.getNodeSuccessorOnXEdge(node);
		assertNotNull(actualSuccessor);
		JobPart actualSuccessorJobPart = actualSuccessor.getJobPart();
		
		assertNotNull(actualSuccessorJobPart);
		assertEquals(expectedSuccessorJobPart.getJob(), actualSuccessorJobPart.getJob());
		assertEquals(expectedSuccessorJobPart.getMachine(), actualSuccessorJobPart.getMachine());
		assertEquals(expectedSuccessorJobPart.getCost(), actualSuccessorJobPart.getCost());
		assertEquals(expectedSuccessorJobPart.getIndex(), actualSuccessorJobPart.getIndex());
	}
	
	@Test
	public void testGetLongestCostFromStartToNodeWithJobPart() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		
		JobPart jp = new JobPart();
		jp.setJob(1);
		jp.setMachine(2);
		jp.setCost(3);
		jp.setIndex(3);
		
		int expectedTotalCost = 5;
		int actualTotalCost = graph.getLongestCostFromStartToNodeWithJobPart(jp);
		assertEquals(expectedTotalCost, actualTotalCost);
		
		JobPart jp2 = new JobPart();
		jp2.setJob(3);
		jp2.setMachine(3);
		jp2.setCost(4);
		jp2.setIndex(3);
		
		Node nodeFrom = graph.getJPNodeByJobPart(jp2);
		Node nodeTo = graph.getJPNodeByJobPart(jp);
		graph.setYEdgeBetweenNodes(nodeFrom, nodeTo);
		
		int expectedTotalCost2 = 9;
		int actualTotalCost2 = graph.getLongestCostFromStartToNodeWithJobPart(jp);
		assertEquals(expectedTotalCost2, actualTotalCost2);
	}
	
	@Test
	public void testGetAllNodesByJob() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		
		List<Node> nodes = graph.getAllNodesByMachine(3);
		assertEquals(3, nodes.size());
		
		for (Node node : nodes) {
			assertEquals(3, node.getJobPart().getMachine());
		}
	}
	
	@Test
	public void testGetCostOfCriticalPath() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		
		int expectedCost = 9;
		int actualCost = graph.getCostOfCriticalPath();
		assertEquals(expectedCost, actualCost);
		
		JobPart jp = new JobPart();
		jp.setJob(2);
		jp.setMachine(2);
		jp.setCost(2);
		jp.setIndex(1);
		Node node1 = graph.getJPNodeByJobPart(jp);
		
		JobPart jp2 = new JobPart();
		jp2.setJob(3);
		jp2.setMachine(2);
		jp2.setCost(2);
		jp2.setIndex(1);
		Node node2 = graph.getJPNodeByJobPart(jp2);
		
		JobPart jp3 = new JobPart();
		jp3.setJob(1);
		jp3.setMachine(2);
		jp3.setCost(3);
		jp3.setIndex(3);
		Node node3 = graph.getJPNodeByJobPart(jp3);
		
		graph.setYEdgeBetweenNodes(node1, node2);
		graph.setYEdgeBetweenNodes(node1, node3);
		
		int expectedCost2 = 11;
		int actualCost2 = graph.getCostOfCriticalPath();
		assertEquals(expectedCost2, actualCost2);
	}
	
	@Test
	public void testGetLongestCostFromNodeWithJobPartToEnd() {
		Graph graph = this.buildGraphFromConfigFile("testInput.txt");
		
		JobPart jp = new JobPart();
		jp.setJob(2);
		jp.setMachine(3);
		jp.setCost(3);
		jp.setIndex(2);
		Node fromNode = graph.getJPNodeByJobPart(jp);
		
		JobPart jp2 = new JobPart();
		jp2.setJob(1);
		jp2.setMachine(3);
		jp2.setCost(2);
		jp2.setIndex(2);
		Node toNode = graph.getJPNodeByJobPart(jp2);
		
		graph.setYEdgeBetweenNodes(fromNode, toNode);
		
		int expectedCostToV = 10;
		
		JobPart fromJP = new JobPart();
		fromJP.setJob(2);
		fromJP.setMachine(2);
		fromJP.setCost(2);
		fromJP.setIndex(1);
		int actualCostToV = graph.getLongestCostFromNodeWithJobPartToEnd(fromJP);
		assertEquals(expectedCostToV, actualCostToV);
	}
	
	@Test
	public void testCopyConstructor() {
		String fileName = "testInput.txt";
		Graph graph = this.buildGraphFromConfigFile(fileName);
		Configuration config = this.getConfigurationFromFile(fileName);
		
		JobPart jp = new JobPart();
		jp.setJob(2);
		jp.setMachine(3);
		jp.setCost(3);
		jp.setIndex(2);
		Node fromNode = graph.getJPNodeByJobPart(jp);
		
		JobPart jp2 = new JobPart();
		jp2.setJob(1);
		jp2.setMachine(3);
		jp2.setCost(2);
		jp2.setIndex(2);
		Node toNode = graph.getJPNodeByJobPart(jp2);
		graph.setYEdgeBetweenNodes(fromNode, toNode);
		
		Graph newGraph = new Graph(graph, config.getJobsPartsAsMapByJobs());
		assertEquals(graph.toString().length(), newGraph.toString().length());
	}
	
	private Graph buildGraphFromConfigFile(String configFileName) {
		Configuration config = this.getConfigurationFromFile(configFileName);
		Map<Integer, List<JobPart>> sortedJobPartsByJob = Utils.getSortedJobPartsInMapByIndex(config.getJobsPartsAsMapByJobs());
		return new Graph(sortedJobPartsByJob);
	}
	
	private Configuration getConfigurationFromFile(String configFileName) {
		Configuration config;
		try {
			config = Configuration.loadFromFile(configFileName);
			return config;
		} catch (NumberFormatException e) {
			System.err.println("File " + configFileName + " is in wrong format.");
			Assert.fail("File " + configFileName + " is in wrong format.");
			return null;
		} catch (FileNotFoundException e) {
			System.err.println("File " + configFileName + " not found.");
			Assert.fail("File " + configFileName + " not found.");
			return null;
		}
	}
}
