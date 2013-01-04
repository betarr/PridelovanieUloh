package algorithms.active;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.JobPart;

public class Node {

	private JobPart jobPart;
	private List<Edge> xEdges;
	private List<Edge> yEdges;
	
	private boolean u = false;
	private boolean v = false;
	
	public Node() {
		this(null);
	}
	
	public Node(JobPart jobPart) {
		this.jobPart = jobPart;
		this.xEdges = new ArrayList<Edge>();
		this.yEdges = new ArrayList<Edge>();
	}

	
	public void addXEdge(Edge edge) {
		if (!this.isU() && this.xEdges.size() > 0) {
			System.err.println("Node cannot have more then one x edge");
		}
		this.xEdges.add(edge);
	}
	
	public void addYEdge(Edge edge) {
		this.yEdges.add(edge);
	}
	
	public void removeYEdge(Edge edge) {
		this.yEdges.remove(edge);
	}
	
	public Node getJPNodeByJobPart(JobPart searchedJobPart) {
		for (Edge edge : this.xEdges) {
			Node node = edge.getToNode();
			if (node.isV()) {
				continue;
			}
			if (node.getJobPart().getJob() != searchedJobPart.getJob()) {
				continue;
			}
			if (searchedJobPart.equals(node.getJobPart())) {
				return node;
			}
			return node.getJPNodeByJobPart(searchedJobPart);
		}
		return null;
	}
	
	public int getLongestCostToNodeWithJobPart(JobPart searchedJobPart) {
		List<Path> paths = new ArrayList<Path>();
		List<Integer> maxValues = new ArrayList<Integer>();
		for (Edge edge : this.xEdges) {
			paths.add(new Path(edge, 0));
		}
		for (Edge edge : this.yEdges) {
			paths.add(new Path(edge, 0));
		}
		while (!paths.isEmpty()) {
			List<Path> pathCopy = this.getCopyOfPathList(paths);
			for (int i = 0; i < pathCopy.size(); i++) {
				Path path = pathCopy.get(i);
				Node nextNode = path.getEdge().getToNode();
				if (nextNode.isV()) {
					paths.remove(path);
					continue;
				}
				if (searchedJobPart.equals(nextNode.getJobPart())) {
					maxValues.add(path.getTotalCost());
					paths.remove(path);
					continue;
				}
				int newTotalCost = path.getTotalCost() + nextNode.getJobPart().getCost();
				for (Edge edge : nextNode.getxEdges()) {
					paths.add(new Path(edge, newTotalCost));
				}
				for (Edge edge : nextNode.getyEdges()) {
					paths.add(new Path(edge, newTotalCost));
				}
				paths.remove(path);
			}
		}
		return Collections.max(maxValues);
	}
	
	public int getLongestPathToV() {
		List<Path> paths = new ArrayList<Path>();
		int totalCost = (this.isU()) ? 0 : this.getJobPart().getCost();
		
		List<Integer> maxValues = new ArrayList<Integer>();
		for (Edge edge : this.xEdges) {
			paths.add(new Path(edge, totalCost));
		}
		for (Edge edge : this.yEdges) {
			paths.add(new Path(edge, totalCost));
		}
		while (!paths.isEmpty()) {
			List<Path> pathsToRemove = new ArrayList<Path>();
			List<Path> pathsToAdd = new ArrayList<Path>();
			for (Path path : paths) {
				Node nextNode = path.getEdge().getToNode();
				if (nextNode.isV()) {
					maxValues.add(path.getTotalCost());
					pathsToRemove.add(path);
				} else {
					int newTotalCost = path.getTotalCost() + nextNode.getJobPart().getCost();
					for (Edge edge : nextNode.getxEdges()) {
						pathsToAdd.add(new Path(edge, newTotalCost));
					}
					for (Edge edge : nextNode.getyEdges()) {
						pathsToAdd.add(new Path(edge, newTotalCost));
					}
					pathsToRemove.add(path);
				}
			}
			paths = this.addAndRemovePaths(paths, pathsToAdd, pathsToRemove);
		}
		return Collections.max(maxValues);
	}
	
	private List<Path> addAndRemovePaths(List<Path> paths,
			List<Path> pathsToAdd, List<Path> pathsToRemove) {
		paths.removeAll(pathsToRemove);
		paths.addAll(pathsToAdd);
		return paths;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobPart == null) ? 0 : jobPart.hashCode());
		result = prime * result + (u ? 1231 : 1237);
		result = prime * result + (v ? 1231 : 1237);
		result = prime * result + ((xEdges == null) ? 0 : xEdges.hashCode());
		result = prime * result + ((yEdges == null) ? 0 : yEdges.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (jobPart == null) {
			if (other.jobPart != null)
				return false;
		} else if (!jobPart.equals(other.jobPart))
			return false;
		if (u != other.u)
			return false;
		if (v != other.v)
			return false;
		if (xEdges == null) {
			if (other.xEdges != null)
				return false;
		} else if (!xEdges.equals(other.xEdges))
			return false;
		if (yEdges == null) {
			if (other.yEdges != null)
				return false;
		} else if (!yEdges.equals(other.yEdges))
			return false;
		return true;
	}

	private List<Path> getCopyOfPathList(List<Path> paths) {
		List<Path> pathCopy = new ArrayList<Path>();
		for (Path path : paths) {
			pathCopy.add(new Path(path));
		}
		return pathCopy;
	}

	public JobPart getJobPart() {
		return jobPart;
	}

	public void setJobPart(JobPart jobPart) {
		this.jobPart = jobPart;
	}

	public List<Edge> getxEdges() {
		return xEdges;
	}

	public void setxEdges(List<Edge> xEdges) {
		this.xEdges = xEdges;
	}

	public List<Edge> getyEdges() {
		return yEdges;
	}

	public void setyEdges(List<Edge> yEdges) {
		this.yEdges = yEdges;
	}

	public boolean isU() {
		return u;
	}

	public void setU(boolean u) {
		this.u = u;
	}

	public boolean isV() {
		return v;
	}

	public void setV(boolean v) {
		this.v = v;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[u: ").append(this.isU()).append(", v:").append(this.isV()).append(", job part: ").append(this.getJobPart()).append("]");
		return sb.toString();
	}

}
