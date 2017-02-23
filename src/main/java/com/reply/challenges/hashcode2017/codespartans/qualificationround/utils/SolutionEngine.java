package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.ArrayList;
import java.util.List;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Cache;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class SolutionEngine {
	
	public static Solution processSolution(ProblemParameters parameters) {
		log.info("Start looking for a solution to problem {}", parameters);
		
		int cacheServersNumber = parameters.getCacheServersNumber();
		int cacheCapacityInMb = parameters.getCacheCapacityInMb();
		
		List<Cache> caches = new ArrayList<>(cacheServersNumber);
		for (int id = 0; id<cacheServersNumber; id ++) {
			caches.add(new Cache(id, cacheCapacityInMb));
		}
		
		return new Solution(caches.toArray(new Cache[0]));
	}

}
