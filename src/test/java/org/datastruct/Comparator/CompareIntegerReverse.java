package org.datastruct.Comparator;

import java.util.Comparator;

public class CompareIntegerReverse implements Comparator<Integer> {
	@Override
	public int compare(Integer o1, Integer o2) {
		return -(o1.compareTo(o2));
	}	
}
