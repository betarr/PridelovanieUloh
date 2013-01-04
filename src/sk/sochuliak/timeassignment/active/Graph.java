package sk.sochuliak.timeassignment.active;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.sochuliak.timeassignment.common.JobPart;
import sk.sochuliak.timeassignment.common.Utils;


public class Graph {

	private Node u;
	private Node v;
	
	public Graph(Map<Integer, List<JobPart>> jobPartMap) {
		this.u = new Node();
		this.u.setU(true);
		this.v = new Node();
		this.v.setV(true);
		
		Set<Integer> jobIndexes = jobPartMap.keySet();
		for (Integer jobIndex : jobIndexes) {
			this.addXPathInGraph(jobIndex, jobPartMap.get(jobIndex));
		}
	}
	
	public Graph(Graph graph, Map<Integer, List<JobPart>> jobPartMap) {
		this.u = new Node();
		this.u.setU(true);
		this.v = new Node();
		this.v.setV(true);
		
		Map<Integer, List<JobPart>> jobPartMapCopy = new HashMap<Integer, List<JobPart>>();
		for (Integer jobIndex : jobPartMap.keySet()) {
			List<JobPart> jobPartList = new ArrayList<JobPart>();
			for (JobPart jp : jobPartMap.get(jobIndex)) {
				jobPartList.add(new JobPart(jp));
			}
			jobPartMapCopy.put(jobIndex, jobPartList);
		}
		
		for (Integer jobIndex : jobPartMapCopy.keySet()) {
			this.addXPathInGraph(jobIndex, jobPartMapCopy.get(jobIndex));
		}
		
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(graph.getU());
		while (!nodes.isEmpty()) {
			Node node = nodes.get(0);
			nodes.remove(node);
			
			for (Edge edge : node.getyEdges()) {
				Node fromNode = this.getJPNodeByJobPart(edge.getFromNode().getJobPart());
				Node toNode = this.getJPNodeByJobPart(edge.getToNode().getJobPart());
				this.setYEdgeBetweenNodes(fromNode, toNode);
			}
			
			for (Edge edge : node.getxEdges()) {
				nodes.add(edge.getToNode());
			}
		}
	}

	private void addXPathInGraph(Integer jobIndex, List<JobPart> jobPartList) {
		Edge lastAddedEdge = new Edge();
		lastAddedEdge.setFromNode(this.u);
		this.u.addXEdge(lastAddedEdge);
		
		for (JobPart jp : jobPartList) {
			Node jpNode = new Node(jp);
			lastAddedEdge.setToNode(jpNode);
			Edge newEdge = new Edge();
			newEdge.setFromNode(jpNode);
			jpNode.addXEdge(newEdge);
			lastAddedEdge = newEdge;
		}
		
		lastAddedEdge.setToNode(this.v);
	}
	
	public Node getJPNodeByJobPart(JobPart jobPart) {
		return this.u.getJPNodeByJobPart(jobPart);
	}
	
	public void setYEdgeBetweenNodes(Node fromNode, Node toNode) {
		Edge yEdge = new Edge();
		yEdge.setFromNode(fromNode);
		yEdge.setToNode(toNode);
		fromNode.addYEdge(yEdge);
	}
	
	public void removeYEdgeBetweenNodes(Node fromNode, Node toNode) {
		Edge edgeToRemove = null;
		for (Edge edge : fromNode.getyEdges()) {
			if (edge.getToNode().equals(toNode)) {
				edgeToRemove = edge;
				break;
			}
		}
		fromNode.removeYEdge(edgeToRemove);
	}
	
	public Node getNodeSuccessorOnXEdge(Node node) {
		return node.getxEdges().get(0).getToNode();
	}
	
	public int getLongestCostFromStartToNodeWithJobPart(JobPart jobPart) {
		return this.u.getLongestCostToNodeWithJobPart(jobPart);
	}
	
	public int getLongestCostFromNodeWithJobPartToEnd(JobPart jp) {
		Node node = this.getJPNodeByJobPart(jp);
		return node.getLongestPathToV();
	}
	
	public int getCostOfCriticalPath() {
		return this.u.getLongestPathToV();
	}
	
	public void addYEdgesToGraph(JobPart toResult) {
		Node fromNode = this.getJPNodeByJobPart(toResult);
		List<Node> nodes = this.getAllNodesByMachine(toResult.getMachine());
		for (Node toNode : nodes) {
			if (!toNode.getJobPart().equals(toResult)) {
				if (toNode.getyEdges().size() == 0) {
					this.setYEdgeBetweenNodes(fromNode, toNode);
				}
			}
		}
	}
	
	public List<Node> getAllNodesByMachine(int machineIndex) {
		List<Node> result = new ArrayList<Node>();
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(this.u);
		while (!nodes.isEmpty()) {
			Node node = nodes.get(0);
			nodes.remove(node);
			if (!node.isV() && !node.isU() && node.getJobPart().getMachine() == machineIndex) {
				result.add(node);
			}
			for (Edge edge : node.getxEdges()) {
				nodes.add(edge.getToNode());
			}
		}
		return result;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		List<Node> nodesToOutput = new ArrayList<Node>();
		nodesToOutput.add(this.u);
		while (!nodesToOutput.isEmpty()) {
			Node node = nodesToOutput.get(0);
			nodesToOutput.remove(node);
			for (Edge edge : node.getxEdges()) {
				sb.append(node).append(" x -> ").append(edge.getToNode()).append(Utils.LINE_SEPARATOR);
				nodesToOutput.add(edge.getToNode());
			}
			for (Edge edge : node.getyEdges()) {
				sb.append(node).append(" y -> ").append(edge.getToNode()).append(Utils.LINE_SEPARATOR);
				nodesToOutput.add(edge.getToNode());
			}
			sb.append(Utils.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	public Node getU() {
		return u;
	}

	public void setU(Node u) {
		this.u = u;
	}

	public Node getV() {
		return v;
	}

	public void setV(Node v) {
		this.v = v;
	}

}
