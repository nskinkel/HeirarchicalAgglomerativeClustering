package hac.distance;

import hac.HACNode;

public interface DistanceFunction {
	double distance(HACNode nodeI, HACNode nodeJ);
}
