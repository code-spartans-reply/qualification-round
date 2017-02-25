package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;

public abstract class SolutionEngine {

	public abstract Solution processSolution(ProblemParameters parameters);

	protected static TreeSet<Entry<int[], Long>> getEntryTreeSet() {
		return new TreeSet<>(new Comparator<Entry<int[], Long>>() {

			@Override
			public int compare(Entry<int[], Long> e1, Entry<int[], Long> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});
	}

	protected static long calculateTotalTime(long latency, long numberOfRequests) {
		return latency * numberOfRequests;
	}

	static void throwIfNotNull(Object o, String exceptionMessage) {
		if (o != null) {
			throw new RuntimeException(exceptionMessage);
		}

	}

	protected static long createKey(int id1, int id2) {
		// Assumes ids not over 99999
		return id1 * 100000l + id2;
	}
	
	public static long evaluatePoints(ProblemParameters parameters, Solution solution) {
		return -1;
	};

}
