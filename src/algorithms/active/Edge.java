package algorithms.active;

public class Edge {

	private Node fromNode;
	private Node toNode;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromNode == null) ? 0 : fromNode.hashCode());
		result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
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
		Edge other = (Edge) obj;
		if (fromNode == null) {
			if (other.fromNode != null)
				return false;
		} else if (!fromNode.equals(other.fromNode))
			return false;
		if (toNode == null) {
			if (other.toNode != null)
				return false;
		} else if (!toNode.equals(other.toNode))
			return false;
		return true;
	}

	public Node getFromNode() {
		return fromNode;
	}
	
	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}
	
	public Node getToNode() {
		return toNode;
	}
	
	public void setToNode(Node toNode) {
		this.toNode = toNode;
	}
}
