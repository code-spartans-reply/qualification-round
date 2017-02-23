package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Endpoint;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Request;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class InputReader {

	public static ProblemParameters readInputParametersFrom(Path inputFile) throws IOException {
		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(inputFile, "UTF-8")) {
			log.info("Reading from input file {}", inputFile);

			log.info("Reading common input data");
			final int videosNum = inputData.nextInt();
			final int endpointsNum = inputData.nextInt();
			final int requestsNum = inputData.nextInt();
			final int cachesNum = inputData.nextInt();
			final int cachesCapacity = inputData.nextInt();

			log.info("Reading videos data");
			final List<Video> videos = new LinkedList<>();
			for (int i = 0; i < videosNum; ++i) {
				videos.add(new Video(i, inputData.nextInt()));
				log.trace("Video {} collected", i);
			}

			log.info("Reading endpoint data");
			final List<Endpoint> endpoints = new LinkedList<>();
			for (int i = 0; i < endpointsNum; ++i) {
				final int datacenterLatency = inputData.nextInt();
				final int connectedCachesNumber = inputData.nextInt();

				if (cachesNum > 0) {
					final int[] cachesLatencies = new int[cachesNum];
					int cacheCounter = 0;
					for (int j = 0; j < connectedCachesNumber; ++j) {
						final int cacheId = inputData.nextInt();
						final int cacheLatency = inputData.nextInt();

						for (; cacheCounter < cacheId; ++cacheCounter) {
							cachesLatencies[cacheCounter] = Integer.MAX_VALUE;
							log.trace("Endpoint {}, cache[{}] latency = {}", i, cacheCounter, cachesLatencies[cacheCounter]);
						}
						cacheCounter = cacheId;
						cachesLatencies[cacheCounter] = cacheLatency;
						log.trace("Endpoint {}, cache[{}] latency = {}", i, cacheCounter, cachesLatencies[cacheCounter]);
					}
					for (; cacheCounter < cachesNum; ++cacheCounter) {
						cachesLatencies[cacheCounter] = Integer.MAX_VALUE;
						log.trace("Endpoint {}, cache[{}] latency = {}", i, cacheCounter, cachesLatencies[cacheCounter]);
					}

					endpoints.add(new Endpoint(i, datacenterLatency, cachesLatencies));
					log.trace("Endpoint {} collected", i);
				} else {
					log.warn("Endpoint {} skipped since it's not connected to any cache", i);
				}
			}

			final Set<Integer> connectedEndpointsIds = endpoints.stream().map(Endpoint::getId)
					.collect(Collectors.toSet());
			final List<Request> requests = new LinkedList<>();
			log.info("Reading requests");
			for (int i = 0; i < requestsNum; ++i) {
				final int requestedVideoId = inputData.nextInt();
				final int endpointId = inputData.nextInt();
				final int requestsCardinality = inputData.nextInt();
				
				if (connectedEndpointsIds.contains(endpointId)) {
					requests.add(new Request(requestedVideoId, requestsCardinality));
					log.trace("Request {} collected", i);
 				} else {
 					log.warn("Request {} discarded since it comes from a disconnected endpoint", i);
 				}
			}
			
			problemParameters = new ProblemParameters(videosNum,endpointsNum,requestsNum,cachesNum,cachesCapacity,videos,endpoints,requests);
			log.info("Parameters read: {}", problemParameters);
		}

		return problemParameters;

	}

}
