package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Cache {

	private final int id;
	
	private final long size;
	
	private final List<Video> cachedVideos;
	
	public Cache(int id, long size) {
		this.id = id;
		this.size = size;
		this.cachedVideos  = new ArrayList<Video>();
	}
	
	public void addCachedVideo(Video video) {
		cachedVideos.add(video);
	}
	
	public List<Video> getCachedVideos () {
		return cachedVideos;
	}
}
