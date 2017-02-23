package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import lombok.Value;

@Value
public class Request {

	private final int requestedVideo;
	
	private final int endpointId;
	
	private final long times;
}
