package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.util.List;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Endpoint;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

public class Ranker {

	public static List<Video> rankVideos(ProblemParameters parameters) {
		return parameters.getVideos();
	}

	public static List<Endpoint> rankEndpoints(ProblemParameters parameters) {
		return parameters.getEndpoints();
	}

}
