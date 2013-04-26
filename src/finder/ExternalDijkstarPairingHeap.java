package finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class ExternalDijkstarPairingHeap implements PathFinder {

	public void doit() throws IOException, URISyntaxException {

	}

	public static void main(String args[]) throws IOException,
			URISyntaxException {
		ExternalDijkstarPairingHeap e = new ExternalDijkstarPairingHeap();
		int foo[] = e.find(123, 456);
		for (int i = 0; i < foo.length; i++)
			System.out.println(foo[i]);
	}

	@Override
	public int[] find(int a, int b) throws NumberFormatException, IOException {
		Process proc;
		int result[] = null;
		try {
			proc = Runtime.getRuntime().exec("cmd /c dijkstar.exe", null,
					new File(this.getClass().getResource("").toURI()));
			BufferedReader r = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			PrintWriter p = new PrintWriter(proc.getOutputStream());
			p.println((a+1) + " " + (b+1));
			p.close();
			String s = r.readLine();
			String ss[] = s.split(" ");
			result = new int[ss.length];
			for (int i = 0; i < ss.length; i++)
				result[i] = Integer.parseInt(ss[i]);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}
}
