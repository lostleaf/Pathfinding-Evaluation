package finder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ExternalKShort implements PathFinder {

	public void doit() throws IOException, URISyntaxException {

	}

	public static void main(String args[]) throws IOException,
			URISyntaxException {
		ExternalKShort e = new ExternalKShort();
		int foo[] = e.find(2729, 2802);
		for (int i = 0; i < foo.length; i++)
			System.out.println(foo[i]);
	}

	@Override
	public int[] find(int a, int b) throws NumberFormatException, IOException {
		Process proc;
		int result[] = null, k = 1;
		boolean flag = true;
		while (flag) {
			flag = false;
			String strk = JOptionPane.showInputDialog("Please input k");
			try {
				k = Integer.parseInt(strk);
			} catch (NumberFormatException ne) {
				JOptionPane.showMessageDialog(null, "It is not a number!");
				flag = true;
			}
			if (k > 10) {
				JOptionPane.showMessageDialog(null,
						"Pleasn input a number not bigger than 10");
				flag = true;
			}
		}
		// System.out.println(k);
		try {
			proc = Runtime.getRuntime().exec("cmd /c kshort.exe", null,
					new File(this.getClass().getResource("").toURI()));
			Scanner cin = new Scanner(proc.getInputStream());
			PrintWriter p = new PrintWriter(proc.getOutputStream());
			p.println((a + 1) + " " + (b + 1) + " " + k);
			p.close();
			for (int i = 1; i < k; i++)
				cin.nextLine();
			int n = cin.nextInt();
			result = new int[n];
			for (int i = 0; i < n; i++)
				result[i] = cin.nextInt() - 2;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}
}
