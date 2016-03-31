package com.hankcs.textrank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hankcs.hanlp.dictionary.CustomDictionary;

public class Tsd extends WordLorder {

	public static void main(String[] args) {
		new Tsd().loaduserdefine();
	}

	public void loaduserdefine() {
		try {
			List<String> list = loadWords(this.getClass().getResourceAsStream(
					"ciku.txt"));
			List<String> newlist = new ArrayList<String>();
			if (null != list && list.size() > 0) {
				for (String str : list) {
					if (str.trim().isEmpty()) {
						continue;
					} else {
						str = str.replace("\"", "");
						if (newlist.contains(str)) {
							continue;
						} else {
							newlist.add(str.trim());
						}
					}
				}
			}
			Collections.sort(newlist);
			for (String string : newlist) {
				System.out.println(string);
				printFile("E://b.txt", string, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void printFile(String name, String data, boolean flag) {
		PrintWriter pw = null;
		try {
			File fileTarget = new File(name);
			if (!fileTarget.getParentFile().exists()) {
				fileTarget.getParentFile().mkdirs();
			}
			// TODO
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					fileTarget, flag), "utf-8"));
			pw.println(data);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
