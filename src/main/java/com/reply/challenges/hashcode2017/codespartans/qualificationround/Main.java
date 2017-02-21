package com.reply.challenges.hashcode2017.codespartans.qualificationround;

import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.InputReader;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.OutputRenderer;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.utils.SolutionEngine;

public final class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

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

			final Solution result = SolutionEngine.processSolution(parameters);

			OutputRenderer.renderOutput(result, parameters, outputFile);
		}
	}


}
