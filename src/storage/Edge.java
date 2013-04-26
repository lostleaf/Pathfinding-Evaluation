package storage;

import java.awt.Point;

public class Edge {
	public Point point[];
	public int dis,from,to;
	public String name;
	public Edge(Point p[], int dis,int from,int to,String name) {
		this.point = p;
		this.dis = dis;
		this.from=from;
		this.to=to;
		this.name=name;
	}

	public int getDis() {
		return dis;
	}

	public Point[] getPoint() {
		return point;
	}
}
