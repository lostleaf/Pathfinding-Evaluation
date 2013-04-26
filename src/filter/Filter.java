package filter;

import java.io.IOException;

import storage.Graph;

public class Filter {

	public static int lcs(String a, String b) {
		if(a==null||b==null)return -1;
		int f[][] = new int[a.length() + 1][b.length() + 1];
		for (int i = 1; i <= a.length(); i++)
			for (int j = 1; j <= b.length(); j++)
				if (a.charAt(i - 1) == b.charAt(j - 1))
					f[i][j] = f[i - 1][j - 1] + 1;
				else
					f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
		return f[a.length()][b.length()];
	}

	public static int[] filter(String name) throws NumberFormatException,
			IOException {
		int n = Graph.getNum();
		int level[] = new int[n];
		int result[] = new int[10];

		for (int i = 0; i < n; i++)
			level[i] = lcs(name, Graph.getPointName(i));

		for (int i = 0; i < 10; i++) {
			int ch = i;
			for (int j = i + 1; j < n; j++)
				if (level[j] > level[ch])
					ch = j;
			result[i] = ch;
			level[ch] = -1;
		}
		return result;
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		int r[] = filter("»À√Ò");
		for (int i = 0; i < 10; i++)
			System.out.println(Graph.getPointName(r[i]));
	}

}
