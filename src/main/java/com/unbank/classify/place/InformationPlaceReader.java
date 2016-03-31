package com.unbank.classify.place;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class InformationPlaceReader {
	public static Log logger = LogFactory.getLog(InformationPlaceReader.class);
	static WordLorder wordLorder = new WordLorder();

	public String getPlace(String text) {
		StringBuffer placeBuffer = new StringBuffer();
		Set<String> places = new HashSet<String>();
		try {
			List<Term> termList = HanLP.segment(text);
			for (Term term : termList) {
				Set<String> word = wordLorder.cityTable.get(term.word);
				if (word != null) {
					for (String string : word) {
						places.add(string.trim());
					}
				}
			}
			for (String string : places) {
				placeBuffer.append(string + " ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		if (placeBuffer.length() > 1) {
			logger.info("识别出地名:" + placeBuffer);
			return placeBuffer.substring(0, placeBuffer.length() - 1);
		} else {
			return "";
		}
	}

}
