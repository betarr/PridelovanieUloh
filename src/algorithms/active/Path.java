package algorithms.active;

public class Path {

	private int totalCost;
	private Edge edge;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result + totalCost;
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
		Path other = (Path) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (totalCost != other.totalCost)
			return false;
		return true;
	}

	public Path(Edge edge, int totalCost) {
		this.edge = edge;
		this.totalCost = totalCost;
	}
	
	public Path(Path path) {
		this.edge = path.getEdge();
		this.totalCost = path.getTotalCost();
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	
}
