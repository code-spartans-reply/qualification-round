package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import com.google.common.io.Files;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;
import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.Solution;

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
			// TODO writer.print(result.data)
			writer.flush();
		}
	}

}
