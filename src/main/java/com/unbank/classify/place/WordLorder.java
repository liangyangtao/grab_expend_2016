package com.unbank.classify.place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.apache.log4j.Logger;

public class WordLorder {
	private final static Logger logger = Logger.getLogger(WordLorder.class);

	public static Hashtable<String, Set<String>> cityTable = new Hashtable<String, Set<String>>();

	public void init() {
		loadWords(this.getClass().getResourceAsStream("placeDic3.txt"));
	}

	public static void loadWords(InputStream input) {
		String line;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input,
					"UTF-8"), 1024);
			while ((line = br.readLine()) != null) {
				line = line.trim().toLowerCase();
				if (line.isEmpty()) {
					continue;
				}
				String[] temp = line.split("	");
				for (int i = 0; i < temp.length; i++) {
					String string = temp[i];
					if (string.isEmpty() || string.length() <= 1) {
						continue;
					}
					Set<String> citys = null;
					if (cityTable.get(string) == null) {
						citys = new HashSet<String>();
					} else {
						citys = cityTable.get(string);
					}
					citys.add(temp[0]);
					cityTable.put(string, citys);
//					System.out.println(string + "***********" + citys);

				}
			}
			br.close();
		} catch (IOException e) {
			logger.info("WARNING: cannot open user words list!", e);
		}
	}
}
