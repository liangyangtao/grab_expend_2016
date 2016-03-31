package com.unbank.duplicate.qiangkong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbank.mybatis.dao.InformationReader;
import com.unbank.pipeline.entity.Information;

public class DuplicateService {

	public static String userDefineWords = "userDefine.dic";
	public static String wordFilePath = "wordsDic.txt";
	public static String topicModelFilePath = "topicModel.txt";

	private static int K;
	private static Map<String, Integer> word2Index;
	private static List<Tmodel> topicModelParam;

	private static int counter = 0; // 队列计数器
	private static int currentIndex = 0; // 队列下标
	private static final int queueSize = 360000; // 队列最大可容纳的新闻个数
	public static final ReadWriteLock lock = new ReentrantReadWriteLock(false);
	public static volatile Sampler[] circularQueue = null; // 队列
	public static Map<Integer, Set<Integer>> titleSimSamplers = null; // key-队列编号
	public static Map<Integer, Set<Integer>> contentSimSamplers = null; // key-队列编号
	static LinkedBlockingQueue<Object> informationQueue = new LinkedBlockingQueue<Object>();



	/**
	 * 系统启动初始化
	 */
	public static void init(Integer fileindex, Integer task, Integer num) {
		Gson gson = new Gson();
		/**
		 * 1. 加载基础词典
		 */
		String words = FileReader(wordFilePath);
		word2Index = gson.fromJson(words,
				new TypeToken<Map<String, Integer>>() {
				}.getType());
		/**
		 * 2.加载topic model 参数
		 */
		String topicModel = FileReader(topicModelFilePath);
		topicModelParam = gson.fromJson(topicModel,
				new TypeToken<List<Tmodel>>() {
				}.getType());
		K = topicModelParam.size();
		/**
		 * 3.加载用户自定义词典 （指定不可分割的词）
		 */
		addUserDefineWords();

		/**
		 * 4. 实例化样本库 （自动清理）
		 */
		circularQueue = new Sampler[queueSize];
		titleSimSamplers = new HashMap<Integer, Set<Integer>>();
		contentSimSamplers = new HashMap<Integer, Set<Integer>>();
		/**
		 * 5.启动分词器
		 */
		ToAnalysis.parse("");
		/***
		 * 6, 事先读取一部分进行去重
		 */

		List<Information> informations = new InformationReader()
				.readInformationByFileIndexAndTask(fileindex, task, num);
		for (Information information : informations) {
			int did = information.getCrawl_id();
			String title = information.getCrawl_title();
			String html = information.getText();
			DuplicateService duplicate = new DuplicateService();
			duplicate.docDuplicate(did + "", title, html);
		}
	}

//	public static void printFile(String name,String data,boolean flag) {
//        PrintWriter pw = null;
//        try {
//        	File fileTarget = new File(name);
//            if (!fileTarget.getParentFile().exists()) {
//            	fileTarget.getParentFile().mkdirs();
//            }
//            //TODO
//            pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileTarget,flag),"utf-8"));
//            pw.println(data);    
//            pw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
	/**
	 * 添加用户词典
	 */
	private static void addUserDefineWords() {
		BufferedReader br = null;
		try {

			String classPath = DuplicateService.class.getClassLoader()
					.getResource("").toURI().getPath();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					classPath + userDefineWords), "utf-8"));
			/**
			 * 一行为一个自定义词
			 */
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				UserDefineLibrary.insertWord(line.trim(), "userDefine", 1000);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件读取器
	 * 
	 * @param path
	 * @return
	 */
	private static String FileReader(String path) {
		String result = null;
		BufferedReader br = null;
		try {
			String classPath = DuplicateService.class.getClassLoader()
					.getResource("").toURI().getPath();
			File fileTarget = new File(classPath + path);
			if (fileTarget.exists()) {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(fileTarget), "utf-8"));
				result = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 假设：相似新闻所使用的词大多相同且对应的主题分布相近 选择最相关的topic 来划分 文档去重
	 */
	private DocBuffer docDuplicate(String docId,
			Map<Integer, Integer> titleWordFeature,
			Map<Integer, Integer> contentWordFeature, Set<String> titleWords,
			Set<String> contentWords) {
		/**
		 * 获取文档最重要的topic编号
		 */
		int tiltekeyId = 150;
		int contentkeyId = getMaxTopicId(contentWordFeature);
		if (titleWordFeature.size() > 1) {
			tiltekeyId = getMaxTopicId(titleWordFeature);
		} else if (titleWordFeature.size() == 1) {
			tiltekeyId = 150 + titleWordFeature.entrySet().iterator().next()
					.getKey();
		}
		/**
		 * 存储队列部分
		 */
		Sampler sampler = new Sampler(docId, tiltekeyId, contentkeyId,
				titleWords, contentWords);
		return isExist(sampler);
	}

	private double jaccardSimilarity(Set<String> s1, Set<String> s2,
			double value) {
		Set<String> ss = s1;
		Set<String> os = s2;
		if (s1.size() > s2.size()) {
			ss = s2;
			os = s1;
		}
		int unioncount = 0;
		int allcount = s1.size() + s2.size();
		for (String s : ss) {
			if (os.contains(s)) {
				unioncount++;
			}
		}
		// 二个文本的相似度与对应词集合交集大小成正比，差集大小成反比
		/*
		 * double simValue=(double)(ss.size()-unioncount)/(unioncount+1);
		 * if(simValue<value){ simValue=1.0; }
		 */
		/*
		 * double simValue=(double)unioncount/ss.size(); if(simValue>0.86){
		 * simValue=1.0; }
		 */
		// double simValue=0.0;
		double simValue = (double) (ss.size() - unioncount) / (unioncount + 1);
		if (ss.size() > 7 && simValue < 0.24) {
			simValue = 1.0;
		} else {
			simValue = (double) unioncount / (allcount - unioncount);
		}
		return simValue;
	}

	/**
	 * 队列中是否存在 find
	 */
	private DocBuffer isExist(Sampler sample) {
		lock.writeLock().lock();
		String idFlag = "";
		idFlag = exist(sample, true);
		if ("".equals(idFlag)) {
			idFlag = exist(sample, false);
		}
		// 加入队列
		if ("".equals(idFlag)) {
			addElementToQueue(sample);
		}
		DocBuffer docBuffer = new DocBuffer();
		if ("".equals(idFlag)) { // 插入到数据库
			docBuffer.setDocId(sample.getDocId());
			docBuffer.setFlag(3);
		} else { // 重复数据更新热度
			docBuffer.setDocId(idFlag);
			docBuffer.setFlag(4);
		}
		lock.writeLock().unlock();
		return docBuffer;
	}

	private String exist(Sampler sampler, boolean isTitle) {
		String idFlag = "";
		Map<Integer, Set<Integer>> simIndexs = null;
		Integer maxTopicId = 0;
		Set<String> s1 = null;
		double diffValue = 0.12;
		if (isTitle) {
			maxTopicId = sampler.getTitleKeyId();
			simIndexs = titleSimSamplers;
			s1 = sampler.getTitleWords();
			diffValue = 0.14;
		} else {
			maxTopicId = sampler.getContentKeyId();
			simIndexs = contentSimSamplers;
			s1 = sampler.getContentWords();
			diffValue = 0.21; // 0.21 0.07(>)
		}
		if (simIndexs.containsKey(maxTopicId)) {
			Set<Integer> ids = simIndexs.get(maxTopicId);
			for (Integer id : ids) {
				Sampler otherSampler = circularQueue[id];
				Set<String> s2 = null;
				if (!isTitle) {
					s2 = otherSampler.getContentWords();
				} else {
					s2 = otherSampler.getTitleWords();
				}
				double dis = jaccardSimilarity(s1, s2, diffValue);
				if (dis >= 0.7) {
					idFlag = otherSampler.getDocId();
					break;
				}
			}
		}
		return idFlag;
	}

	private void addElementToQueue(Sampler sampler) {
		currentIndex = currentIndex % queueSize;
		if (queueSize == counter) { // remove
			Integer lastTitleKey = circularQueue[currentIndex].getTitleKeyId();
			Integer lastContentKey = circularQueue[currentIndex]
					.getContentKeyId();
			titleSimSamplers.get(lastTitleKey)
					.remove(new Integer(currentIndex));
			contentSimSamplers.get(lastContentKey).remove(
					new Integer(currentIndex));
		} else {
			counter++;
		}
		// add element
		if (!titleSimSamplers.containsKey(sampler.getTitleKeyId())) {
			Set<Integer> partIds = new HashSet<Integer>();
			titleSimSamplers.put(sampler.getTitleKeyId(), partIds);
		}
		if (!contentSimSamplers.containsKey(sampler.getContentKeyId())) {
			Set<Integer> partIds = new HashSet<Integer>();
			contentSimSamplers.put(sampler.getContentKeyId(), partIds);
		}
		titleSimSamplers.get(sampler.getTitleKeyId()).add(currentIndex);
		contentSimSamplers.get(sampler.getContentKeyId()).add(currentIndex);
		circularQueue[currentIndex] = sampler;
		currentIndex++;
	}

	/**
	 * 文本切词
	 */
	private Map<Integer, Integer> cutWordsForContent(String content,
			Set<String> words) {
		// 分词处理
		List<Term> termList = ToAnalysis.parse(content);
		Map<Integer, Integer> wordFeature = new HashMap<Integer, Integer>();
		// c ,m,d,f,p,r ,ud,y,x,uv,uj,ug,ud （ ）
		boolean flag = true;
		int index = 0;
		for (Term t : termList) {
			String key = t.getName().trim();
			if ("（".equals(key) || "(".equals(key) || "[".equals(key)
					|| "【".equals(key)) {
				index = 0;
				flag = false;
			}
			if ("）".equals(key) || ")".equals(key) || "]".equals(key)
					|| "】".equals(key)) {
				index = 0;
				flag = true;
			}
			if ("".equals(key) || !flag) {
				index++;
				if (index > content.length() / 3) {
					flag = true;
				}
				continue;
			}
			if (t.getNatureStr().startsWith("c")
					|| t.getNatureStr().startsWith("m")
					|| t.getNatureStr().startsWith("d")
					|| "".equals(key)
					|| t.getNatureStr().startsWith("f")
					|| t.getNatureStr().startsWith("p")
					|| t.getNatureStr().startsWith("r")
					|| t.getNatureStr().startsWith("ud")
					|| t.getNatureStr().startsWith("y")
					|| t.getNatureStr().startsWith("x")
					|| t.getNatureStr().startsWith("uv")
					|| t.getNatureStr().startsWith("uj")
					|| t.getNatureStr().startsWith("ug")
					|| t.getNatureStr().startsWith("ud")
					|| t.getNatureStr().startsWith("w")) {
				continue;
			}
			// 词典过滤
			if (word2Index.containsKey(key)) {
				Integer wordId = word2Index.get(key);
				if (wordFeature.containsKey(wordId)) {
					wordFeature.put(wordId, wordFeature.get(wordId) + 1);
				} else {
					wordFeature.put(wordId, 1);
				}
			}
			if (t.getNatureStr().startsWith("userDefine")
					|| t.getNatureStr().startsWith("v")
					|| t.getNatureStr().startsWith("d")
					|| t.getNatureStr().startsWith("a")
					|| t.getNatureStr().startsWith("n")
					|| t.getNatureStr().startsWith("m")) {
				if (!words.contains(key)) {
					words.add(key);
				}
			}
		}
		// TODO
		return wordFeature;
	}

	/**
	 * title 切词
	 * 
	 * @return
	 */
	private Map<Integer, Integer> cutWordsForTitle(String content,
			Set<String> words) {
		// 分词处理
		List<Term> termList = ToAnalysis.parse(content);
		Map<Integer, Integer> wordFeature = new HashMap<Integer, Integer>();
		boolean flag = true;
		for (Term t : termList) {
			String key = t.getName().trim();
			if ("（".equals(key) || "(".equals(key) || "[".equals(key)
					|| "【".equals(key)) {
				flag = false;
			}
			if ("）".equals(key) || ")".equals(key) || "]".equals(key)
					|| "】".equals(key)) {
				flag = true;
			}
			if ("".equals(key) || !flag) {
				continue;
			}
			if (t.getNatureStr().startsWith("c")
					|| t.getNatureStr().startsWith("m")
					|| t.getNatureStr().startsWith("d")
					|| t.getNatureStr().startsWith("f")
					|| t.getNatureStr().startsWith("p")
					|| t.getNatureStr().startsWith("r")
					|| t.getNatureStr().startsWith("ud")
					|| t.getNatureStr().startsWith("y")
					|| t.getNatureStr().startsWith("x")
					|| t.getNatureStr().startsWith("uv")
					|| t.getNatureStr().startsWith("uj")
					|| t.getNatureStr().startsWith("ug")
					|| t.getNatureStr().startsWith("ud")
					|| t.getNatureStr().startsWith("w")
					|| " ".equals(key) || " :".equals(key) || "  ".equals(key)
					|| " ".equals(key) || ")".equals(key.trim())
					|| "]".equals(key.trim()) || "?".equals(key.trim())
					|| " ".equals(key)) {
				continue;
			}
			if (!words.contains(key)) {
				words.add(key);
			}
			// 词典过滤
			if (word2Index.containsKey(key)) {
				Integer wordId = word2Index.get(key);
				if (wordFeature.containsKey(wordId)) {
					wordFeature.put(wordId, wordFeature.get(wordId) + 1);
				} else {
					wordFeature.put(wordId, 1);
				}
			}
		}
		return wordFeature;
	}

	private int getMaxTopicId(Map<Integer, Integer> wordFeature) {
		double[] X = new double[K];
		int maxTopicId = 0;
		for (int i = 0; i < topicModelParam.size(); i++) {
			Tmodel model = topicModelParam.get(i);
			X[i] = 0;
			for (Iterator<Map.Entry<Integer, Integer>> wordandfres = wordFeature
					.entrySet().iterator(); wordandfres.hasNext();) {
				Map.Entry<Integer, Integer> wordandfre = wordandfres.next();
				Integer wordId = wordandfre.getKey();
				Integer fre = wordandfre.getValue();
				if (model.getPwz().containsKey(wordId)) {
					double pwz = model.getPwz().get(wordId);
					X[i] -= Math.log(pwz) * fre;
				}
			}
			if (X[i] > X[maxTopicId]) {
				maxTopicId = i;
			}
		}
		return maxTopicId;
	}

	/**
	 * 新闻去重 0，1 2 3 4
	 * 
	 * @return 0-》空文档 1-》英语新闻 2-》其他问题文档 3-》没有重复的中文新闻 4-》重复的中文新闻
	 */
	public synchronized DocBuffer docDuplicate(String did, String title,
			String html) {
		DocBuffer docBuffer = new DocBuffer();
		if (html != null && !"".equals(html.trim())) {
			Document doc = Jsoup.parseBodyFragment(html);
			Element pRoot = doc.body();
			String text = pRoot.text();
			/**
			 * 只处理中文新闻 英语新闻不处理
			 */
			if (text != null && !"".equals(text.trim())) {
				int start = (int) text.length() / 4;
				int end = (int) 3 * text.length() / 4;
				String partContent = "";
				if (start != -1 && end != -1 && end > start) {
					partContent = text.substring(start, end);
				} else {
					partContent = text;
				}
				String partCon = partContent.toLowerCase().replaceAll(" ", "");
				partCon = partCon.replaceAll("\\pP|\\pS", "");
				if (partCon.matches("^\\w+$")) { // 外语新闻
					docBuffer.setDocId(did);
					docBuffer.setFlag(1);
				} else { // 中文新闻
					docBuffer = isPlicate(did, title, text);
				}
			} else {
				docBuffer.setDocId(did); // 空文档
				docBuffer.setFlag(0);
			}
		} else {
			docBuffer.setDocId(did); // 空文档
			docBuffer.setFlag(0);
		}
		return docBuffer;
	}

	private DocBuffer isPlicate(String did, String title, String content) {
		Set<String> contentWords = new HashSet<String>();
		content = title + "\t" + title + "\t" + content; // title+"\t"+
		Map<Integer, Integer> contentWordFeature = cutWordsForContent(content,
				contentWords);
		Map<Integer, Integer> conords = new HashMap<Integer, Integer>();

		/**
		 * tf 选择特称词
		 */
		List<Map.Entry<Integer, Integer>> mappingList = new ArrayList<Map.Entry<Integer, Integer>>(
				contentWordFeature.entrySet());
		// 通过比较器实现比较排序
		Collections.sort(mappingList,
				new Comparator<Map.Entry<Integer, Integer>>() {
					public int compare(Map.Entry<Integer, Integer> mapping1,
							Map.Entry<Integer, Integer> mapping2) {
						return mapping2.getValue().compareTo(
								mapping1.getValue());
					}
				});
		int size = mappingList.size() * 3 / 4;
		size = size > 2 ? size : 3;
		for (Map.Entry<Integer, Integer> mapping : mappingList) {
			if (conords.size() < size) {
				conords.put(mapping.getKey(), mapping.getValue());
			}
		}
		if (contentWordFeature.size() < 2) { // 问题文档 eg..乱码
			DocBuffer docBuffer = new DocBuffer();
			docBuffer.setDocId(did);
			docBuffer.setFlag(2);
			return docBuffer;
		} else {
			title = title.replaceAll("原标题", "");
			title = title.replaceAll("本报记者", "");
			title = title.replaceAll("本报实习记者", "");
			title = title.replaceAll(".*您当前所处位置", "");
			Set<String> titleWords = new HashSet<String>();
			Map<Integer, Integer> titleWordFeature = cutWordsForTitle(title,
					titleWords);
			return docDuplicate(did, titleWordFeature, conords, titleWords,
					contentWords);
		}
	}
}
