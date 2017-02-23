package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import java.util.Arrays;
import java.util.stream.LongStream;

import lombok.Data;

@Data
public class Endpoint {

	private final int id;
	
	private final long datacenterLatency;
	
	private final long[] cacheLatencies;
	
	final LongStream cacheLatencies() {
		return Arrays.stream(this.cacheLatencies);
	}
}
