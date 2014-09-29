package hac;

import hac.DistanceMatrix.Pair;
import hac.distance.Jaccard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.CUtil;
import util.IndexedList;

public class HAC {

	private static Set<String> mNodeSet;
	private static Set<String> mDataSet;
	private static IndexedList<String> mNodeList;
	private static IndexedList<String> mDataList;

	public static void main(String[] args) throws IOException {
		Map<String, Set<String>> edgeMap = read(args);
		mNodeList = new IndexedList<String>(mNodeSet);
		mDataList = new IndexedList<String>(mDataSet);
		Map<Integer, HACNode> nodeMap = makeNodeMap(edgeMap);
		DistanceMatrix matrix = new DistanceMatrix(mNodeList.size(), new Jaccard());

		matrix.init(nodeMap);
		
		System.out.println("Begin clustering...");

		while (matrix.nodeSize() > 1) {
			Pair minPair = matrix.getMinPair();
			HACNode nodeI = nodeMap.get(minPair.I);
			HACNode nodeJ = nodeMap.get(minPair.J);
			nodeI.merge(nodeJ);
			
			matrix.removeMergedNode(minPair);
			matrix.updateDistance(minPair);
			System.out.println(minPair);
		}
	}
	
	private static Map<Integer, HACNode> makeNodeMap(Map<String, Set<String>> edgeMap) {
		Map<Integer, HACNode> nodeMap = CUtil.makeMap();
		
		for (Entry<String, Set<String>> entrySet : edgeMap.entrySet()) {
			int nodeIndex = mNodeList.getIndex(entrySet.getKey());
			HACNode hNode = new HACNode(nodeIndex);
			for (String d : entrySet.getValue()) {
				int dataIndex = mDataList.getIndex(d);
				hNode.add(dataIndex);
			}
			nodeMap.put(nodeIndex, hNode);
		}

		return nodeMap;
	}

	private static Map<String, Set<String>> read(String[] args) throws IOException {
		
		System.out.println("Begin reading file...");
		
		BufferedReader b = new BufferedReader(new FileReader(args[0]));
		int nodeIndex = Integer.valueOf(args[1]);
		int dataIndex = Integer.valueOf(args[2]);
		
		mNodeSet = CUtil.makeSet();
		mDataSet = CUtil.makeSet();
		
		Map<String, Set<String>> edgeMap = CUtil.makeMap();
		
		String s = null;
		String split[] = null;

		while ((s = b.readLine()) != null) {
			split = s.trim().split(",");
			String node = split[nodeIndex];
			mNodeSet.add(node);
			String neighbor = split[dataIndex];
			mDataSet.add(neighbor);
			Set<String> data = edgeMap.get(node);
			if (data == null) {
				data = CUtil.makeSet();
				edgeMap.put(node, data);
			}
			data.add(neighbor);
		}
		b.close();
		
		return edgeMap;
	}

}
