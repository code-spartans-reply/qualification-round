package com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Cache {

	private final int id;
	
	private final int size;
	
	private int residualSize;
	
	private final List<Video> cachedVideos;
	
	private int latency;
	
	public Cache(int id, int size) {
		this.id = id;
		this.size = size;
		this.residualSize = size;
		this.cachedVideos  = new ArrayList<Video>();
	}
	
	public Cache(int id, int size, int latency) {
		this(id,size);
		this.latency = latency;
	}
	
	public boolean addCachedVideo(Video video) {
		if(cachedVideos.contains(video)) {
			return false;
		}
		if (video.getSize() > residualSize) {
			return false;
		}
		cachedVideos.add(video);
		residualSize = residualSize - video.getSize();
		return true;
	}
	
	public double freeSpaceFactor() {
		return residualSize/size;
	}
}
