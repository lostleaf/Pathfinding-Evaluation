package finder;

import java.io.IOException;

public class Finder {

	PathFinder pathfinder;
	public static final String algoList[] = { "×î¶ÌÂ·", "K¶ÌÂ·" };

	public Finder(int algorithm) throws NumberFormatException, IOException {
		setAlgorithm(algorithm);
	}

	public Finder() throws NumberFormatException, IOException {
		setAlgorithm(0);
	}

	public void setAlgorithm(int algorithm) throws NumberFormatException,
			IOException {
		switch (algorithm) {
		case 0:
			pathfinder = new ExternalDijkstarPairingHeap();
			break;
		case 1:
			pathfinder = new ExternalKShort();
		}
	}

	public int[] find(int a, int b) throws NumberFormatException, IOException {
		return pathfinder.find(a, b);
	}

}
