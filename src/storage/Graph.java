package storage;

import io.MyReader;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

public class Graph {
	private static Point pos[];
	private static String name[];
	private static Vector<Integer> link[];
	private static Edge edge[];

	static boolean initialised = false;

	public static Point trans(int x, int y) {

		x -= 26;
		y -= 52;
		int X = (int) (6928.0 / (3133 - 52) * y );
		int Y = (int) (6719.0 / (3002 - 26) * (3002 - 26 - x) );

		return new Point(X, Y);
	}

	@SuppressWarnings("unchecked")
	public static void init() throws NumberFormatException, IOException {
		initialised = true;
		MyReader reader = new MyReader("/gdata/recv.txt");
		MyReader reader1 = new MyReader("/gdata/allname.txt");
		int n = reader.nextInt();
		pos = new Point[n];
		link = new Vector[n];
		name = new String[n];

		for (int i = 0; i < n; i++) {

			int x = reader.nextInt(), y = reader.nextInt();
			reader.nextInt();/* ignore z */
			pos[i] = trans(x, y);
			link[i] = new Vector<Integer>();
			name[i] = reader1.readLine();
		}

		reader = new MyReader("/gdata/rece.txt");
		reader1 = new MyReader("/gdata/recepath.txt");

		int m = reader.nextInt();
		edge = new Edge[m << 1];

		for (int i = 0; i < m; i++) {
			int fr = reader.nextInt(), to = reader.nextInt();
			fr--;
			to--;

			int distance = reader.nextInt();
			int cnt = reader1.nextInt();
			Point tmp[] = new Point[cnt + 2];

			tmp[0] = new Point(pos[fr]);
			tmp[cnt + 1] = new Point(pos[to]);
			for (int j = 1; j <= cnt; j++) {

				int x = reader1.nextInt(), y = reader1.nextInt();
				tmp[j] = trans(x, y);
				// new Point(y, (3026 - x))
			}

			Point tmp1[] = new Point[cnt + 2];
			for (int j = cnt + 1, k = 0; k < cnt + 2; j--, k++)
				tmp1[j] = new Point(tmp[k]);

			edge[i << 1] = new Edge(tmp, distance, fr, to,
					String.valueOf((i << 1)));
			link[fr].add(i << 1);
			edge[(i << 1) + 1] = new Edge(tmp1, distance, to, fr,
					String.valueOf((i << 1) + 1));
			link[to].add((i << 1) + 1);
		}
	}

	public static Point getPosition(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return pos[index];
	}

	public static String getPointName(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return name[index];
	}

	public static Vector<Integer> getAdjList(int index)
			throws NumberFormatException, IOException {
		if (!initialised)
			init();
		return link[index];
	}

	public static String getEdgeName(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return edge[index].name;
	}

	public static Point[] getEdgePoints(int index)
			throws NumberFormatException, IOException {
		if (!initialised)
			init();
		return edge[index].point;
	}

	public static int getEdgeFrom(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return edge[index].from;
	}

	public static int getEdgeTo(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return edge[index].to;
	}

	public static int getEdgeDis(int index) throws NumberFormatException,
			IOException {
		if (!initialised)
			init();
		return edge[index].dis;
	}

	public static int getNum() throws NumberFormatException, IOException {
		if (!initialised)
			init();
		return name.length;
	}
}
