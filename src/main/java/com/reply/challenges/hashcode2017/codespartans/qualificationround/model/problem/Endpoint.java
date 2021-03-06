package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import java.util.Arrays;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class Endpoint {

	private final int id;
	
	private final int datacenterLatency;
	
	private final int[] cacheLatencies;
	
	final IntStream cacheLatencies() {
		return Arrays.stream(this.cacheLatencies);
	}
}
