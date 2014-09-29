package hac.distance;

import hac.HACNode;

import java.util.Set;

import util.CUtil;

public class Jaccard implements DistanceFunction {

	public double distance(HACNode nodeI, HACNode nodeJ) {
		Set<Integer> nodeIData = nodeI.getData();
		Set<Integer> nodeJData = nodeJ.getData();
		Set<Integer> intersect = CUtil.makeSet();

		intersect.addAll(nodeIData);
		intersect.retainAll(nodeJData);
		
		double unionSize = nodeIData.size() + nodeJData.size() - intersect.size();
		
		return intersect.size() / unionSize;
	}
}