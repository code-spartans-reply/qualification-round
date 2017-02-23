package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Endpoint;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Request;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Video;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class Ranker {

	public static List<Video> rankVideos(Stream<Video> videos, Stream<Request> requests) {
		final Set<VideoRequests> videoRequests = videos.map(video -> new VideoRequests(video))
				.collect(Collectors.toSet());
		Map<Integer, VideoRequests> requestsForVideo = Maps.uniqueIndex(videoRequests,
				vrequest -> vrequest.getVideo().getId());

		requests.forEach(request -> requestsForVideo.get(request.getRequestedVideo()).accountRequest(request));

		if (log.isDebugEnabled()) {
			requestsForVideo.values().forEach(vReq -> log.debug("Accounted {} requests for video {}",
					vReq.getTotalRequests(), vReq.getVideo().getId()));
		}

		final List<Video> rankedVideos = new TreeSet<>(requestsForVideo.values()).stream()
				.filter(vReq -> vReq.getTotalRequests() > 0).map(VideoRequests::getVideo).collect(Collectors.toList());

		if (log.isDebugEnabled()) {
			final List<String> videoStringList = new LinkedList<>();
			rankedVideos.forEach(video -> videoStringList.add(String.format("Video %d", video.getId())));
			log.debug("Final ordered video list: {}", String.join(",", videoStringList));
		}

		return rankedVideos;
	}

	@Data
	@EqualsAndHashCode(exclude = "totalRequests")
	private static class VideoRequests implements Comparable<VideoRequests> {

		private final Video video;

		@Setter(value = AccessLevel.PRIVATE)
		private long totalRequests = 0L;

		public void accountRequest(final Request request) {
			checkArgument(checkNotNull(request).getRequestedVideo() == this.video.getId());
			this.totalRequests += request.getTimes();
		}

		@Override
		public int compareTo(VideoRequests o) {
			final int requestDiff = (int) (o.totalRequests - this.totalRequests);
			return requestDiff != 0 ? requestDiff : this.equals(o) ? 0 : -1;
		}

	}
	
	public static List<Endpoint> rankEndpoints(ProblemParameters parameters) {
		return parameters.getEndpoints();
	}

}
