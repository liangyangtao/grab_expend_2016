package com.unbank.keyword;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.unbank.keyword.dao.ArticleCheckKeywordStore;
import com.unbank.keyword.dao.ArticleUnbankKeywordReader;
import com.unbank.mybatis.entity.ArticleCheckKeyword;
import com.unbank.mybatis.entity.ArticleUnbankKeyword;

public class UnbankKeywordChecker {
	public static Set<String> unbankKeywords = new HashSet<String>();

	public static void init() {
		unbankKeywords.clear();
		List<ArticleUnbankKeyword> articleUnbankKeywords = new ArticleUnbankKeywordReader()
				.readUnbankKeyword();
		for (ArticleUnbankKeyword articleUnbankKeyword : articleUnbankKeywords) {
			unbankKeywords.add(articleUnbankKeyword.getKeyword());
		}
		articleUnbankKeywords.clear();
	}

	public void checkKeyword(String keyword) {
		if (unbankKeywords.contains(keyword)) {
			return;
		}
		unbankKeywords.add(keyword);
		saveCheckKeyword(keyword);

	}

	private void saveCheckKeyword(String keyword) {
		ArticleCheckKeyword articleCheckKeyword = new ArticleCheckKeyword();
		articleCheckKeyword.setKeyword(keyword);
		new ArticleCheckKeywordStore()
				.saveArticleCheckKeyword(articleCheckKeyword);
	}

}
