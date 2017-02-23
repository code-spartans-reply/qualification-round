package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Cache;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Endpoint;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Request;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class SolutionEngine {
	
	public static Solution processSolution(ProblemParameters parameters) {
		log.info("Start looking for a solution to problem");
		
		int cacheServersNumber = parameters.getCacheServersNumber();
		int cacheCapacityInMb = parameters.getCacheCapacityInMb();
	
		Map<Integer,Cache> cacheMap = new HashMap<>();
		
		List<Cache> caches = new ArrayList<>(cacheServersNumber);
		for (int id = 0; id<cacheServersNumber; id ++) {
			Cache cache = new Cache(id, cacheCapacityInMb);
			caches.add(cache);
			cacheMap.put(id, cache);
		}
		
		List<Endpoint> endpoints = Ranker.rankEndpoints(parameters); 
		List<Request> requests = parameters.getRequests(); 
		List<Video> videos = Ranker.rankVideos(parameters.videos(), parameters.requests()); 
		
		Iterator<Cache> cacheIterator = caches.iterator();
		
		Map<Integer,Endpoint> endpointMap = new HashMap<>();
		Map<String,Long> requestsMap = new HashMap<>();
		Map<Integer,Video> videoMap = new HashMap<>();
		
		for (Endpoint endpoint : endpoints) {
			endpointMap.put(endpoint.getId(), endpoint);
		}

		for (Request request: requests) {
			String key = request.getRequestedVideo()+"-"+request.getEndpointId();
			Long videoRequest = requestsMap.get(key);
			if (videoRequest == null) {
				requestsMap.put(key, request.getTimes());
			} else {
				log.info("Found same request for {}", key);
				requestsMap.put(key, videoRequest + request.getTimes());
			}
		}

		for (Video video: videos) {
			videoMap.put(video.getId(), video);
		}
		
		log.info("Videos: {}, requests: {}, endpoints: {}, caches: {}", new Object[]{videos.size(), requests.size(), endpoints.size(), caches.size()});
		
		Iterator<Video> vidIterator = videos.iterator();
		
		while(vidIterator.hasNext()) {
			Video video = vidIterator.next();
			Iterator<Endpoint> epIterator = endpoints.iterator();
			while(epIterator.hasNext()) {
				Endpoint ep = epIterator.next();
				int[] cacheLatencies = ep.getCacheLatencies();
				for(int i = 0; i < cacheLatencies.length; i++) {
					//calculateTotalTime(cacheLatencies[i],);
				}
			}

			
		}
		
		return new Solution(caches.toArray(new Cache[0]));
	}
	
	static long calculateTotalTime(long latency, long numberOfRequests) {
		return latency*numberOfRequests;
	}

}
