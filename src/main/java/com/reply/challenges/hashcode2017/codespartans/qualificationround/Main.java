package com.reply.challenges.hashcode2017.codespartans.qualificationround;

import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.algos.JustByRankingVideos;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.InputReader;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.OutputRenderer;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.SolutionEngine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			log.error("arguments: input files folder and output file folder");
			System.exit(1);;
		}

		final Path path = FileSystems.getDefault().getPath(args[0]);
		final DirectoryStream<Path> inputFilesStream = Files.newDirectoryStream(path,
				file -> file.toString().endsWith(".in"));
		for (final Path inputFile : inputFilesStream) {
			final Path outputFile = FileSystems.getDefault()
					.getPath(args[1], inputFile.getFileName().toString().replaceAll("\\.in$", ".out")).toAbsolutePath();


			final ProblemParameters parameters = InputReader.readInputParametersFrom(inputFile);

			final Solution solution = new JustByRankingVideos().processSolution(parameters);

			long score = SolutionEngine.calculateScore(parameters, solution);
			log.info("Score for {} is {}",inputFile.getFileName(),score);
			OutputRenderer.renderOutput(solution, parameters, outputFile);
		}
	}


}
