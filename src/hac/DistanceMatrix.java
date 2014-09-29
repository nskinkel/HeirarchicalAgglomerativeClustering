package hac;

import hac.DistanceMatrix.Pair;
import hac.distance.DistanceFunction;

import java.util.Arrays;
import java.util.Map;

public class DistanceMatrix {
	
	static class Pair {
		public int I;
		public int J;
		
		public Pair() {
			I = -1;
			J = -1;
		}
		
		@Override
		public String toString() {
			return I + "," + J;
		}
	}

	private double[][] mDist;
	private DistanceFunction mDistFunction;
	private int mNodeSize;
	private Map<Integer, HACNode> mNodeMap;

	public DistanceMatrix(int size, DistanceFunction distFunction) {
		this.mDistFunction = distFunction;
		mDist = new double[size][size];
		mNodeSize = size;
	}

	public void init(Map<Integer, HACNode> nodeMap) {
		
		System.out.println("Initializing distances...");

		this.mNodeMap = nodeMap;

		for (int i = 0; i < mDist.length-1; i++) {
			HACNode nodeI = nodeMap.get(i);
			for (int j = i+1; j < mDist.length; j++) {
				HACNode nodeJ = nodeMap.get(j);
				mDist[i][j] = mDistFunction.distance(nodeI, nodeJ);
			}
		}
//		System.out.println(Arrays.deepToString(mDist));
	}

	public int nodeSize() {
		return mNodeSize;
	}

	public Pair getMinPair() {
		
		double currentMin = Double.MAX_VALUE;
		Pair currentMinPair = new Pair();
		
		for (int i = 0; i < mDist.length-1; i++) {
			for (int j = i+1; j < mDist.length; j++) {
				double distance = mDist[i][j];
				if (distance < currentMin) {
					currentMin = distance;
					currentMinPair.I = i;
					currentMinPair.J = j;
				}
			}
		}
		
		return currentMinPair;
	}
	
	public void removeMergedNode(Pair minPair) {
		// clear distances for old node J before merge
		for (int x = 0; x < minPair.J; x++) {
			mDist[x][minPair.J] = Double.MAX_VALUE;
		}
		for (int x = minPair.J+1; x < mDist.length; x++) {
			mDist[minPair.J][x] = Double.MAX_VALUE;
		}
		
		mNodeSize--;
	}

	public void updateDistance(Pair minPair) {

		// minPair.I is the new node
		HACNode newNode = mNodeMap.get(minPair.I);

		// compute distances for new merged node I
		for (int x = 0; x < minPair.I; x++) {
			HACNode nodeX = mNodeMap.get(x);
			mDist[x][minPair.I] =  mDistFunction.distance(newNode, nodeX);
		}
		for (int x = minPair.I+1; x < mDist.length; x++) {
			HACNode nodeX = mNodeMap.get(x);
			mDist[minPair.I][x] =  mDistFunction.distance(newNode, nodeX);
		}
	}
}