package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
		Map<Integer,TreeSet<Entry<int[],Long>>> cacheToSortedVideoEpCacheSavingMap = new HashMap<>();
		for (int id = 0; id<cacheServersNumber; id ++) {
			Cache cache = new Cache(id, cacheCapacityInMb);
			caches.add(cache);
			cacheMap.put(id, cache);
			cacheToSortedVideoEpCacheSavingMap.put(id, getEntryTreeSet());
		}
		
		List<Endpoint> endpoints = parameters.getEndpoints();//Ranker.rankEndpoints(parameters); 
		List<Request> requests = parameters.getRequests(); 
		List<Video> videos = parameters.getVideos();//Ranker.rankVideos(parameters.videos(),parameters.requests()); 
		
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
				log.trace("Found same request for {}", key);
				requestsMap.put(key, videoRequest + request.getTimes());
			}
		}

		for (Video video: videos) {
			videoMap.put(video.getId(), video);
		}
		
		int vidNumbers = videos.size();
		log.info("Videos: {}, requests: {}, endpoints: {}, caches: {}", new Object[]{vidNumbers, requests.size(), endpoints.size(), caches.size()});
		
		
		log.info("Get all the timings and all the latencies for all the requests ep->video");
		
		TreeSet<Entry<int[],Long>> sortedVideoEpCacheSavingSet = getEntryTreeSet();
		
		
		Iterator<Video> vidIterator = videos.iterator();
		while(vidIterator.hasNext()) {
			Video video = vidIterator.next();
			log.trace("Processing video {}/{} (-1)", video.getId(), vidNumbers);
			
			Iterator<Endpoint> epIterator = endpoints.iterator();
			while(epIterator.hasNext()) {
				Endpoint ep = epIterator.next();
				String vidEpKey = video.getId() + "-" + ep.getId();

				Long totalReq = requestsMap.get(vidEpKey);
				if(totalReq != null) {
					//Video is requested by endpoint
					int noCacheLatency = ep.getDatacenterLatency();

					int[] cacheLatencies = ep.getCacheLatencies();
					for(int cacheId = 0; cacheId<cacheLatencies.length; cacheId++) {
						int cacheLatency = cacheLatencies[cacheId];
						int[] vidEpCacheKey = {video.getId(),ep.getId(),cacheId};
						
						if (cacheLatency < noCacheLatency) { //Disconnected caches may be modeled with infinite latency
								Long totalSaving = calculateTotalTime(noCacheLatency-cacheLatencies[cacheId],totalReq);
								SimpleEntry<int[], Long> entry = new AbstractMap.SimpleEntry<int[], Long>(vidEpCacheKey, totalSaving);
								sortedVideoEpCacheSavingSet.add(entry);
								cacheToSortedVideoEpCacheSavingMap.get(cacheId).add(entry);
						} else {
							log.trace("Endpoint+Cache not connected: " + ep.getId() + "-" + cacheId);
						}
					}
				}
			}
		}
		
		log.info("Insert videos in caches by biggest saving (ignoring size)");
		
		Set<Long> cachedRequests = new HashSet<>();
		
		Iterator<Entry<int[], Long>> iterator; 

		int lastCachedSize = -1;
		while(true) {
			//compare with last size, if not changed stop
			if (lastCachedSize == cachedRequests.size()) {
				break;
			}
			lastCachedSize = cachedRequests.size();
			iterator = sortedVideoEpCacheSavingSet.iterator();
			log.info("Cached {} requests",lastCachedSize);
			while (iterator.hasNext()) {
				Entry<int[], Long> entry =  iterator.next();
				final int[] vidEpCacheKeys = entry.getKey();
				
				final int videoId = vidEpCacheKeys[0];
				final int epId = vidEpCacheKeys[1];
				final int cacheId = vidEpCacheKeys[2];
				
				long vidEpKey = createKey(videoId,epId);
				if(!cachedRequests.contains(vidEpKey)) { 
					if(cacheMap.get(cacheId).addCachedVideo(videoMap.get(videoId))){
						cachedRequests.add(vidEpKey);
						log.debug("Video cached {}",vidEpKey);
//						iterator.remove();
						//Reset and restart
						//iterator = sortedVideoEpCacheSavingSet.iterator();
					}
				} else {
					//ep+video cached -> skip
//					iterator.remove();
				}
			}
		}
		
		
		return new Solution(caches.toArray(new Cache[0]));
	}

	private static TreeSet<Entry<int[], Long>> getEntryTreeSet() {
		return new TreeSet<>(new Comparator<Entry<int[],Long>>() {

			@Override
			public int compare(Entry<int[], Long> e1, Entry<int[], Long> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});
	}
	
	static long calculateTotalTime(long latency, long numberOfRequests) {
		return latency*numberOfRequests;
	}
	
	static void throwIfNotNull(Object o, String exceptionMessage) {
		if (o != null) {
			throw new RuntimeException(exceptionMessage);
		}
		
	}
	
	long createKey(int id1, int id2) {
		return id1*100000l + id2;
	}
	
}
