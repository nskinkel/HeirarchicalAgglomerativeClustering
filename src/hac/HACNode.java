package hac;

import java.util.Set;

import util.CUtil;

public class HACNode {

	private int mNodeIndex;
	private Set<Integer> mData;

	public HACNode(int nodeIndex) {
		this.mNodeIndex = nodeIndex;
		this.mData = CUtil.makeSet();
	}

	public void add(int dataIndex) {
		mData.add(dataIndex);
	}

	public Set<Integer> getData() {
		return mData;
	}

	public void merge(HACNode other) {
		mData.addAll(other.mData);
	}
	
	public int getIndex() {
		return mNodeIndex;
	}

}
