package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import java.util.List;
import java.util.stream.Stream;

import lombok.Value;

@Value
public class ProblemParameters {
	
	private final int videosNumber;
	
	private final int endpointsNumber;
	
	private final int requestsNumber;
	
	private final int cacheServersNumber;
	
	private final int cacheCapacityInMb;
	
	private final List<Video> videos;
	
	private final List<Endpoint> endpoints;
	
	private final List<Request> requests;
	
	private final List<Endpoint> allEndpoints;
	
	private final List<Request> allRequests;
	
	
	public Stream<Video> videos() {
		return videos.stream();
	}
	
	public Stream<Endpoint> endpoints() {
		return endpoints.stream();
	}

	public Stream<Request> requests() {
		return requests.stream();
	}
}
