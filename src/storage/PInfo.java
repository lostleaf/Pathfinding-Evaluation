package storage;

import java.awt.Point;
import java.util.Vector;

public class PInfo {
	public String name;
	public Vector<Edge> link;
	public Point pos;
	public int id;

	public PInfo(String name, Vector<Edge> link, Point pos, int id) {
		this.name = new String(name);
		this.link = new Vector<Edge>(link);
		this.pos = new Point(pos);
		this.id = id;
	}

}
