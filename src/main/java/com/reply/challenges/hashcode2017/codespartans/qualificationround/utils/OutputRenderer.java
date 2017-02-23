package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.io.Files;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Cache;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class OutputRenderer {
	
	public static void renderOutput(Solution result, ProblemParameters parameters, Path outputFile) throws IOException {
		log.info("Start writing solution to output file {}", outputFile);
		
		// Ensure target directory exists:
		Files.createParentDirs(outputFile.toFile());
		try (final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile.toFile())))) {
			
			List<Cache> cleanedCacheList = cleanEmptyCaches(result.getCaches());
			
			writer.println(cleanedCacheList.size());
			
			for(Cache cache : cleanedCacheList) {
				writer.print(cache.getId() + " ");
				List<Video> cachedVideos = cache.getCachedVideos();
				for (Iterator<Video> iterator = cachedVideos.iterator(); iterator.hasNext();) {
					Video video = (Video) iterator.next();
					writer.print(video.getId());
					if (iterator.hasNext()) {
						writer.print(" ");
					}
				}
			}
			
			// TODO writer.print(result.data)
			writer.flush();
		}
	}

	private static List<Cache> cleanEmptyCaches(Cache[] caches) {
		List<Cache> cleanedCacheList = new ArrayList<>();
		if (caches != null) {
			for (int i = 0; i < caches.length; i++) {
				if (!caches[i].getCachedVideos().isEmpty()) {
					cleanedCacheList.add(caches[i]);
				}
			}
		}
		return cleanedCacheList;		
	}


	
}


