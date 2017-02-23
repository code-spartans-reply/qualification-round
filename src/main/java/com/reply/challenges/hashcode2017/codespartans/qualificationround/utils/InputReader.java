package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import com.reply.challenges.hashcode2017.codespartans.qualificationround.model.problem.ProblemParameters;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class InputReader {

	public static ProblemParameters readInputParametersFrom(Path inputFile) throws IOException {
		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(inputFile, "UTF-8")) {
			log.info("Reading from input file {}", inputFile);

			// TODO Read fixed data
			// final int firstDatum = inputData.nextInt();
			// final int secondDatum = inputData.nextInt();

			log.info("Start reading repeated input data");
			final Collection<Object> something = new LinkedList<>();
			while (inputData.hasNextLine()) {
				if (inputData.hasNextInt()) {
					log.trace("Repeated input line detected, reading values");
					// TODO read repeated fields data
					// int repData1 = inputData.nextInt();
					// int repData2 = inputData.nextInt();

					// TODO create repeated value object
					final Object object = new Object(/* repData1, repData2 */ );

					// TODO collect it
					something.add(object);
					log.trace("Object collected");
				} else {
					log.trace("Consuming last line");
					inputData.nextLine();
				}
			}

//			problemParameters = new ProblemParameters(/* ... */);
			log.info("Parameters read: {}", problemParameters);
		}

		return problemParameters;

	}

}
