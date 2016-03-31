package com.hankcs.textrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.sf.json.JSONArray;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

import com.hankcs.entity.HankcsKeyword;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;

/**
 * TextRank关键词提取
 * 
 * @author hankcs
 */
public class TextRankKeyword extends WordLorder {

	public static TextRankKeyword textRankKeyword;

	private static Set<String> userDefineKeywords = new HashSet<String>();
	private static Set<String> userNoDefineKeywords = new HashSet<String>();

	public synchronized static TextRankKeyword getInstance() {
		if (textRankKeyword == null) {
			textRankKeyword = new TextRankKeyword();
			textRankKeyword.read();
			HanLP.extractKeyword("导入", 1);
		}
		return textRankKeyword;
	}

	private void read() {
		loaduserdefine();
		initstopword();
	}

	/**
	 * 停用词路径
	 */
	// private static final String PATH_DIC_STOPWORD =
	// "com/hankcs/textrank/stopword.dic";
	/**
	 * 用户自定义词性
	 */

	private static final String userDefine = "userDefine";

	/**
	 * 用户自定义词频
	 */
	private static final int userDefinFre = 1000;

	public static final int nKeyword = 10;
	/**
	 * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
	 */
	static final float d = 0.85f;
	/**
	 * 最大迭代次数
	 */
	static final int max_iter = 200;
	static final float min_diff = 0.001f;

	public TextRankKeyword() {
		// System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	}

	public String getKyewordByFd(String title, String content) {
		JSONArray hankcsKeywordJsonArray = new JSONArray();
		List<String> keywordList = HanLP.extractKeyword(title + content, 100);
		List<String> temp = new ArrayList<String>();
		for (String keyword : userDefineKeywords) {
			if (title.contains(keyword.trim())) {
				keywordList.add(keyword.trim());
			}
		}

		for (int i = 0; i < keywordList.size(); i++) {
			String key = keywordList.get(i);
			boolean isexit = false;
			for (int j = 0; j < temp.size(); j++) {
				String key2 = temp.get(j);
				if (key2.contains(key)) {
					isexit = true;
					break;
				}
				if (key.contains(key2)) {
					temp.set(j, key);
					isexit = true;
					break;
				}

			}
			if (isexit) {
				continue;
			}
			if (i > 10) {
				// 如果大于10 就判断是否自定义词，是自定义的就提出来
				if (userDefineKeywords.contains(key)) {
					temp.add(key);
				}
			} else {
				if (userNoDefineKeywords.contains(key)) {

				} else {
					temp.add(key);
				}
			}

		}
		for (String string : temp) {
			HankcsKeyword hankcsKeyword = new HankcsKeyword();
			hankcsKeyword.setKeyword(string);
			hankcsKeyword.setScore(1f);
			hankcsKeywordJsonArray.add(hankcsKeyword);
		}
		return hankcsKeywordJsonArray.toString();

	}

	public String getKeyword(String title, String content) {
		List<Term> termList = ToAnalysis.parse(title + content);
		termList = FilterModifWord.modifResult(termList);
		List<String> wordList = new ArrayList<String>();
		for (Term t : termList) {
			if (t.getName().trim().length() <= 1) {
				continue;
			}
			if (shouldInclude(t)) {
				wordList.add(t.getName());
			}
		}
		Map<String, Set<String>> words = new HashMap<String, Set<String>>();
		Queue<String> que = new LinkedList<String>();
		for (String w : wordList) {
			if (!words.containsKey(w)) {
				words.put(w, new HashSet<String>());
			}
			que.offer(w);
			if (que.size() > 5) {
				que.poll();
			}

			for (String w1 : que) {
				for (String w2 : que) {
					if (w1.equals(w2)) {
						continue;
					}

					words.get(w1).add(w2);
					words.get(w2).add(w1);
				}
			}
		}
		Map<String, Float> score = new HashMap<String, Float>();
		for (int i = 0; i < max_iter; ++i) {
			Map<String, Float> m = new HashMap<String, Float>();
			float max_diff = 0;
			for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				m.put(key, 1 - d);
				for (String other : value) {
					int size = words.get(other).size();
					if (key.equals(other) || size == 0)
						continue;
					m.put(key, m.get(key) + d / size
							* (score.get(other) == null ? 0 : score.get(other)));
				}
				max_diff = Math.max(max_diff, Math.abs(m.get(key)
						- (score.get(key) == null ? 0 : score.get(key))));
			}
			score = m;
			if (max_diff <= min_diff)
				break;
		}
		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(
				score.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
			@Override
			public int compare(Map.Entry<String, Float> o1,
					Map.Entry<String, Float> o2) {
				return (o1.getValue() - o2.getValue() > 0 ? -1 : 1);
			}
		});
		JSONArray hankcsKeywordJsonArray = new JSONArray();
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i < entryList.size(); i++) {
			String key = entryList.get(i).getKey();
			boolean isexit = false;
			for (int j = 0; j < temp.size(); j++) {
				String key2 = temp.get(j);
				if (key2.contains(key)) {
					isexit = true;
					break;
				}
				if (key.contains(key2)) {
					temp.set(j, key);
					isexit = true;
					break;
				}

			}
			if (isexit) {
				continue;
			}
			if (i > 10) {
				// 如果大于10 就判断是否自定义词，是自定义的就提出来
				if (userDefineKeywords.contains(entryList.get(i).getKey())) {
					temp.add(key);
				}
			} else {
				if (userNoDefineKeywords.contains(key)) {

				} else {
					temp.add(key);
				}
			}

		}
		for (String string : temp) {
			HankcsKeyword hankcsKeyword = new HankcsKeyword();
			hankcsKeyword.setKeyword(string);
			hankcsKeyword.setScore(1f);
			hankcsKeywordJsonArray.add(hankcsKeyword);
		}
		return hankcsKeywordJsonArray.toString();
	}

	public static void main(String[] args) {
		String content = "国有银行股份制银行城商行农信社省联社工商银行农业银行中国银行建设银行交通银行招商银行中信银行光大银行华夏银行民生银行招商银行兴业银行广发银行平安银行浦发银行恒丰银行浙商银行渤海银行贷款存款资产证券化信用卡小微企业中小金融银行信贷银行银行存款银行理财民营银行首套房贷互联网金融银行卡微信银行银监会银行业协会银行贷款银行利率行长智慧银行精准营销风险预警行业授信供应链抵押质押担保事业部汇丰银行晋城银行河北银行北京银行南京银行长沙银行江苏银行上海银行花旗银行韩亚银行银团贷款吉林银行盛京银行现金管理贸易金融银行银行年报授信审批审查审批产品创新直销银行手机银行电子银行互助资金授信额度敞口银行理财银银合作银证合作银信合作银政合作银企直连网络金融银行资金金融租赁银行保理直销银行社区银行总行分行支行支行行长产品经理银行转型";
		String title = "";
		TextRankKeyword textRankKeyword = TextRankKeyword.getInstance();
		System.out.println(textRankKeyword.getKeyword(title, content));

	}

	/**
	 * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
	 * 
	 * @param term
	 * @return 是否应当
	 */
	public boolean shouldInclude(Term term) {
		if (term.getNatureStr().startsWith("n")
				|| term.getNatureStr().startsWith("v")
				|| term.getNatureStr().startsWith("d")
				|| term.getNatureStr().startsWith("a")
				|| term.getNatureStr().startsWith("userDefine")) {
			// TODO 你需要自己实现一个停用词表
			// if (!StopWordDictionary.contains(term.getName()))
			// {
			return true;
			// }
		}

		return false;
	}

	/**
	 * 获取用户自定义字典
	 */
	public void loaduserdefine() {
		try {
			List<String> list = loadWords(this.getClass().getResourceAsStream(
					"userDefine.dic"));
			if (null != list && list.size() > 0) {
				for (String str : list) {
					userDefineKeywords.add(str.trim());
					CustomDictionary.insert(str.trim(), "nz 1024");
					// UserDefineLibrary.insertWord(str.trim(), userDefine,
					// userDefinFre);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param 初始化停用词
	 */
	public void initstopword() {
		try {
			// List<String> stopword = loadWords(this.getClass()
			// .getResourceAsStream("stopword.dic"));
			// if (null != stopword && stopword.size() > 0) {
			// FilterModifWord.insertStopWords(stopword);
			// }
			List<String> userNotDefineWords = loadWords(this.getClass()
					.getResourceAsStream("userNotDefine.dic"));
			for (String string : userNotDefineWords) {
				userNoDefineKeywords.add(string);
			}
			// stopword.clear();
			userNotDefineWords.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
