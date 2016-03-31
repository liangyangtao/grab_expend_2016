package com.hankcs.textrank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class WordLorder {
	private final static Logger logger = Logger.getLogger(WordLorder.class);

	public static List<String> loadWords(InputStream input) {
		String line;
		List<String> words = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input,
					"UTF-8"), 1024);
			while ((line = br.readLine()) != null) {
				line = line.trim().toLowerCase();
				if (line.isEmpty()) {
					continue;
				}
				words.add(line);
			}
			br.close();
		} catch (IOException e) {
			logger.info("WARNING: cannot open user words list!", e);
		}
		return words;
	}
}
