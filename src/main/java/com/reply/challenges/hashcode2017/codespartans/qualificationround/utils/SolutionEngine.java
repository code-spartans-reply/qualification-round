package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Cache;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Endpoint;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Request;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

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
	
	public static long calculateScore(ProblemParameters parameters, Solution solution) {
		//Still not working as it should, probably because parameters is cleaned from not used endpoints and requests
		long totalSavedTime = 0;
		long totalRequests = 0;
		
		Map<Integer,Set<Integer>> videosInCacheMap = new HashMap<>(); 
		Cache[] finalCaches = solution.getCaches();
		for (int i = 0; i < finalCaches.length; i++) {
			Set<Integer> videosInCache = videosInCacheMap.get(finalCaches[i].getId());
			if (videosInCache == null) {
				videosInCache = new HashSet<>();
				videosInCacheMap.put(finalCaches[i].getId(),videosInCache);
			}
			List<Video> cachedVideos = finalCaches[i].getCachedVideos();
			for(Video video : cachedVideos) {
				videosInCache.add(video.getId());
			}
		}
		
		List<Request> requests = parameters.getAllRequests();
		Map<Integer,Set<Request>> requestsByEpMap = new HashMap<>();
		//Requests by endPoint
		for (Request request : requests) {
			Set<Request> epReqSet = requestsByEpMap.get(request.getEndpointId());
			if (epReqSet == null) {
				epReqSet = new HashSet<>();
				requestsByEpMap.put(request.getEndpointId(),epReqSet);
			}
			epReqSet.add(request);
		}
		
		List<Endpoint> endpoints = parameters.getAllEndpoints(); 
		for (Endpoint ep : endpoints) {
			Set<Request> epRequestSet = requestsByEpMap.get(ep.getId());
			int[] cacheLatencies = ep.getCacheLatencies();
			
			TreeSet<Cache> epCachesByLatency = new TreeSet<>(new Comparator<Cache>() {
				@Override
				public int compare(Cache c1, Cache c2) {
					return c2.getLatency() - c1.getLatency();
				}
			});
			
			for (int cacheId = 0; cacheId < cacheLatencies.length; cacheId++) {
				int latency = cacheLatencies[cacheId];
				if (latency < ep.getDatacenterLatency()) {
					epCachesByLatency.add(new Cache(cacheId,0,latency));
				}
			}

			for (Iterator<Request> iterator = epRequestSet.iterator(); iterator.hasNext();) {
				Request request = iterator.next();
				if (request.getEndpointId() == ep.getId()) {
					int requestedVideo = request.getRequestedVideo();
					//Look for video in one connected cache
					totalRequests = totalRequests + request.getTimes();
					for (Cache connectedCache : epCachesByLatency) {
						Set<Integer> cachedVideos = videosInCacheMap.get(connectedCache.getId());
						if (cachedVideos != null && cachedVideos.contains(requestedVideo)) {
							totalSavedTime = totalSavedTime + (request.getTimes()*(ep.getDatacenterLatency()-connectedCache.getLatency()));
							break;
						}
					}
				}
			}
			
		}
		
		return (long) ((double)totalSavedTime/(double)totalRequests * 1000d);
	};

}
