package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		
		List<Cache> caches = new ArrayList<>(cacheServersNumber);
		for (int id = 0; id<cacheServersNumber; id ++) {
			caches.add(new Cache(id, cacheCapacityInMb));
		}
		
		List<Endpoint> endpoints = parameters.getEndpoints(); 
		List<Request> requests = parameters.getRequests(); 
		List<Video> videos = Ranker.rankVideos(parameters); 
		
		Iterator<Cache> cacheIterator = caches.iterator();
		
		log.info("Videos: {}, requests: {}, endpoints: {}, caches: {}", new Object[]{videos.size(), requests.size(), endpoints.size(), caches.size()});
		
		while(cacheIterator.hasNext()) {
			Cache cache = cacheIterator.next();
			log.debug("Processing cache {}",cache.getId());
			Iterator<Video> vidIterator = videos.iterator(); 
			while (vidIterator.hasNext()) {
				Video video = vidIterator.next();
				if (cache.addCachedVideo(video))  {
					log.debug("Added video {}",video.getId());
					vidIterator.remove();
				}
			};
		}
		
		return new Solution(caches.toArray(new Cache[0]));
	}

}
