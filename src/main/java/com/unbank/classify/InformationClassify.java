package com.unbank.classify;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.unbank.classify.dao.ClassKeywordReader;
import com.unbank.classify.dao.ClassSectionReader;
import com.unbank.classify.dao.ClassTitleKeywordsReader;
import com.unbank.classify.dao.ClassWebsiteReader;
import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.classify.entity.Label;
import com.unbank.classify.entity.PtfDoc;
import com.unbank.classify.entity.TempRelation;
import com.unbank.mybatis.dao.LabelReader;
import com.unbank.mybatis.dao.TempRelationReader;
import com.unbank.pipeline.entity.Information;

public class InformationClassify {

	public static Map websitMap = new HashMap();
	public static Map classMap = new HashMap();
	public static List<Integer> websitList = new ArrayList<Integer>();
	public static Map labelMap = new HashMap();
	private static Logger logger = Logger.getLogger(InformationClassify.class);
	private static final QName SERVICE_NAME = new QName(
			"http://cxfinterface.unbank.com/", "AutoclassinterfaceImplService");
	public static List<ClassSectionEntity> classSectionEntities = new ArrayList<ClassSectionEntity>();

	public static List<ClassSectionEntity> classWebsiteEntities = new ArrayList<ClassSectionEntity>();

	public static List<ClassSectionEntity> classKeywordsEntities = new ArrayList<ClassSectionEntity>();

	public static List<ClassSectionEntity> classTitleKeywordsEntities = new ArrayList<ClassSectionEntity>();

	public static void init() {
		classSectionEntities = new ClassSectionReader()
				.readClassSectionEntity();
		classWebsiteEntities = new ClassWebsiteReader()
				.readClassWebsiteEntity();
		classKeywordsEntities = new ClassKeywordReader()
				.readClassKeywordEntity();
		classTitleKeywordsEntities = new ClassTitleKeywordsReader()
				.readClassTitleKeywordsEntity();
		List<TempRelation> classlist = new TempRelationReader()
				.selectTempRelation();
		List<Label> labels = new LabelReader().selectLabel();
		if (labels != null && labels.size() > 0) {
			for (Label label : labels) {
				labelMap.put(label.getId(), label.getClassname());
			}
		}
		if (classlist != null && classlist.size() > 0) {
			for (TempRelation temp : classlist) {
				if (temp.getState() == 1) {
					// 同步分类
					classMap.put(String.valueOf(temp.getClassid()), temp);
				}
			}
		}

	}

	public void transInformation(Information information) {
		informationClassfy(information);
	}

	private void informationClassfy(Information information) {
		List<PtfDoc> ptfDocs = new ArrayList<PtfDoc>();
		boolean isexit = false;
		// 如果通过指定的websiteid
		classWebsiteClassfy(information, ptfDocs);
		// 根据关键词分类
		isexit = classByKeyWordClassfy(information, ptfDocs);
		if (!isexit) {
			// 通过板块名称
			isexit = classSecitionClassfy(information, ptfDocs);
			if (!isexit) {
				// 走赵健分类
				xunlianClassfy(information, ptfDocs);
			}

		}
		// 根据标题中含有的关键词分类
		classByTitleKeywordClassfy(information, ptfDocs);
		if (ptfDocs.size() > 0) {
			information.setObject(ptfDocs);
		} else {
			logger.info(information.getCrawl_id() + "没有分到类");
		}
	}

	private void classByTitleKeywordClassfy(Information information,
			List<PtfDoc> ptfDocs) {
		if (classTitleKeywordsEntities != null
				&& classTitleKeywordsEntities.size() > 0) {
			for (ClassSectionEntity classSectionEntity : classTitleKeywordsEntities) {
				Set<String> websiteList = classSectionEntity.getWebsiteList();
				String title = information.getCrawl_title();
				title = Jsoup.parse(title).text();
				title = title.toLowerCase();
				for (String string : websiteList) {
					string = string.toLowerCase();
					// 此处修改？
					String keys[] = string.split("\\&");
					boolean isTrue = true;
					if (keys.length >= 2) {
						for (String string2 : keys) {
							if (!title.contains(string2)) {
								isTrue = false;
								break;
							}
						}
						if (isTrue) {
							fillInfoPtfDoc(information, ptfDocs,
									classSectionEntity);
							break;
						}
					} else {
						if (title.contains(string)) {
							fillInfoPtfDoc(information, ptfDocs,
									classSectionEntity);
							break;
						}
					}

				}
			}
		}
	}

	private void fillInfoPtfDoc(Information information, List<PtfDoc> ptfDocs,
			ClassSectionEntity classSectionEntity) {
		PtfDoc ptfDoc = new PtfDoc();
		ptfDoc.setClassid(classSectionEntity.getId());
		ptfDoc.setLabelName(classSectionEntity.getClassname());
		ptfDoc.setStrucutureId(classSectionEntity.getStrucutureid());
		ptfDoc.setTemplateId(classSectionEntity.getTemplateid());
		ptfDoc.setForumId(classSectionEntity.getForumid());
		ptfDocs.add(ptfDoc);
		information
				.setClassname(information.getClassname() == null ? classSectionEntity
						.getClassname() : information.getClassname() + "_"
						+ classSectionEntity.getClassname());
	}

	private boolean classByKeyWordClassfy(Information information,
			List<PtfDoc> ptfDocs) {
		boolean isExis = false;
		try {
			if (classKeywordsEntities != null
					&& classKeywordsEntities.size() > 0) {
				for (ClassSectionEntity classSectionEntity : classKeywordsEntities) {
					Set<String> websiteList = classSectionEntity
							.getWebsiteList();
					String title = information.getCrawl_title();
					String content = information.getText();
					String temp = title + content;
					for (String string : websiteList) {
						Set<String> keywods = new HashSet<String>();
						String keys[] = string.split("\\&");
						for (String string2 : keys) {
							String keys2[] = string2.split("\\;");
							for (String string3 : keys2) {
								String keys3[] = string3.split("\\+");
								for (String string4 : keys3) {
									if (string4.trim().isEmpty()) {
										continue;
									}
									keywods.add(string4.trim());
								}
							}
						}
						boolean isKeyword = true;
						for (String keyword : keywods) {
							if (!temp.contains(keyword)) {
								isKeyword = false;
								break;
							}
						}
						// 2.表达式a;b+c&d+e;f
						// &前面是在标题中查找，&后面是在内容中查找，+两边的词时必须包含，;两边的词是多个词包含一个。
						if (isKeyword) {
							isExis = true;
							if (classSectionEntity.getId() == 5) {
								information
										.setWebsite_id(10000000 + information
												.getWebsite_id());
							}

							fillInfoPtfDoc(information, ptfDocs,
									classSectionEntity);
							break;
						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("板块标签分类出错", e);
		}
		return isExis;

	}

	private boolean classWebsiteClassfy(Information information,
			List<PtfDoc> ptfDocs) {
		boolean isexit = false;
		try {
			// List<Integer> classids = new ArrayList<Integer>();
			if (classWebsiteEntities != null && classWebsiteEntities.size() > 0) {
				for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
					if (classSectionEntity.getWebsiteList().contains(
							information.getWebsite_id() + "")) {
						fillInfoPtfDoc(information, ptfDocs, classSectionEntity);
						// classids.add(classSectionEntity.getId());
						isexit = true;
					}
				}
			}
			// information.setClassIds(classids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("板块标签分类出错", e);
		}
		return isexit;
	}

	private void xunlianClassfy(Information information, List<PtfDoc> ptfDocs) {
		try {
			URL wsdlURL = AutoclassinterfaceImplService.WSDL_LOCATION;
			AutoclassinterfaceImplService ss = new AutoclassinterfaceImplService(
					wsdlURL, SERVICE_NAME);
			Autoclassinterface port = ss.getAutoclassinterfaceImplPort();
			JSONObject inJson = new JSONObject();
			inJson.put("id", information.getCrawl_id());
			inJson.put("content", information.getText());
			inJson.put("title", information.getCrawl_title());
			JSONObject outJson = null;
			String resulltStr = null;
			resulltStr = port.comparesimilarity(inJson.toString());
			if (resulltStr != null && new JSONObject(resulltStr) != null) {
				outJson = new JSONObject(resulltStr);
				if (outJson != null && "0".equals(outJson.get("errcode"))) {
					String classids = (String) outJson.get("class");
					String allClassid[] = classids.split(",");
					// 是否已经有指定分类
					boolean isClass = false;
					// 修改如果是银行高管的话把ID设为1711
					for (String classid : allClassid) {
						if (classid.equals("5")) {
							information.setWebsite_id(10000000 + information
									.getWebsite_id());
							String string = "5";
							if (classMap != null
									&& classMap.containsKey(string)
									&& classMap.get(string) != null) {
								PtfDoc ptfDoc = new PtfDoc();
								ptfDoc.setClassid(Integer.parseInt(string));
								ptfDoc.setLabelName((String) labelMap
										.get(Integer.parseInt(string)));
								TempRelation temp = (TempRelation) classMap
										.get(string);
								ptfDoc.setStrucutureId(temp.getStrucutureid());
								ptfDoc.setTemplateId(temp.getTemplateid());
								ptfDoc.setForumId(temp.getForumid());
								ptfDocs.add(ptfDoc);

							}
							isClass = true;
							break;
						}
						if (classid.equals("6")) {
							String string = "6";
							if (classMap != null
									&& classMap.containsKey(string)
									&& classMap.get(string) != null) {
								PtfDoc ptfDoc = new PtfDoc();
								ptfDoc.setClassid(Integer.parseInt(string));
								ptfDoc.setLabelName((String) labelMap
										.get(Integer.parseInt(string)));
								TempRelation temp = (TempRelation) classMap
										.get(string);
								ptfDoc.setStrucutureId(temp.getStrucutureid());
								ptfDoc.setTemplateId(temp.getTemplateid());
								ptfDoc.setForumId(temp.getForumid());
								ptfDocs.add(ptfDoc);
							}
							isClass = true;
							break;
						}
					}
					if (isClass) {
						return;
					}

					if (allClassid.length == 2) {
						String string = allClassid[1];
						if (classMap != null && classMap.containsKey(string)
								&& classMap.get(string) != null) {
							PtfDoc ptfDoc = new PtfDoc();
							ptfDoc.setClassid(Integer.parseInt(string));
							ptfDoc.setLabelName((String) labelMap.get(Integer
									.parseInt(string)));
							TempRelation temp = (TempRelation) classMap
									.get(string);
							ptfDoc.setStrucutureId(temp.getStrucutureid());
							ptfDoc.setTemplateId(temp.getTemplateid());
							ptfDoc.setForumId(temp.getForumid());
							ptfDocs.add(ptfDoc);
						}
					} else if (allClassid.length > 2) {
						String maxClassid = allClassid[allClassid.length - 1];
						if (Integer.parseInt(maxClassid) > 13) {
							String string = allClassid[1];
							if (classMap != null
									&& classMap.containsKey(string)
									&& classMap.get(string) != null) {
								PtfDoc ptfDoc = new PtfDoc();
								ptfDoc.setClassid(Integer.parseInt(string));
								ptfDoc.setLabelName((String) labelMap
										.get(Integer.parseInt(string)));
								TempRelation temp = (TempRelation) classMap
										.get(string);
								ptfDoc.setStrucutureId(temp.getStrucutureid());
								ptfDoc.setTemplateId(temp.getTemplateid());
								ptfDoc.setForumId(temp.getForumid());
								ptfDocs.add(ptfDoc);
								information.setClassname(information
										.getClassname() == null ? ptfDoc
										.getLabelName() : information
										.getClassname()
										+ "_"
										+ ptfDoc.getLabelName());
							}
							string = maxClassid;
							if (classMap != null
									&& classMap.containsKey(string)
									&& classMap.get(string) != null) {
								PtfDoc ptfDoc = new PtfDoc();
								ptfDoc.setClassid(Integer.parseInt(string));
								ptfDoc.setLabelName((String) labelMap
										.get(Integer.parseInt(string)));
								TempRelation temp = (TempRelation) classMap
										.get(string);
								ptfDoc.setStrucutureId(temp.getStrucutureid());
								ptfDoc.setTemplateId(temp.getTemplateid());
								ptfDoc.setForumId(temp.getForumid());
								ptfDocs.add(ptfDoc);
								information.setClassname(information
										.getClassname() == null ? ptfDoc
										.getLabelName() : information
										.getClassname()
										+ "_"
										+ ptfDoc.getLabelName());
							}
						} else {
							String string = allClassid[1];
							if (classMap != null
									&& classMap.containsKey(string)
									&& classMap.get(string) != null) {
								PtfDoc ptfDoc = new PtfDoc();
								ptfDoc.setClassid(Integer.parseInt(string));
								ptfDoc.setLabelName((String) labelMap
										.get(Integer.parseInt(string)));
								TempRelation temp = (TempRelation) classMap
										.get(string);
								ptfDoc.setStrucutureId(temp.getStrucutureid());
								ptfDoc.setTemplateId(temp.getTemplateid());
								ptfDoc.setForumId(temp.getForumid());
								ptfDocs.add(ptfDoc);
								information.setClassname(information
										.getClassname() == null ? ptfDoc
										.getLabelName() : information
										.getClassname()
										+ "_"
										+ ptfDoc.getLabelName());
							}

						}

					}

				}
			}
		} catch (Exception e) {
			logger.info("赵健分类异常", e);
		}
	}

	// private boolean informationIsClassfy(Information information) {
	// if (websitMap != null && websitList != null && websitList.size() > 0) {
	// TempRelation tempTR = null;
	// for (Integer strid : websitList) {
	// if (websitMap.containsKey(strid)
	// && websitMap.get(strid) != null) {
	// tempTR = (TempRelation) websitMap.get(strid);
	// List<String> tempWebSit = Arrays.asList(tempTR
	// .getWebtitleList().split(","));
	// if (tempWebSit.contains(information.getWebsite_id() + "")) {
	// List<PtfDoc> ptfDocs = new ArrayList<PtfDoc>();
	// PtfDoc ptfDoc = new PtfDoc();
	// ptfDoc.setClassid(0);
	// ptfDoc.setLabelName("行业");
	// ptfDoc.setStrucutureId(tempTR.getStrucutureid());
	// ptfDoc.setTemplateId(tempTR.getTemplateid());
	// ptfDoc.setForumId(tempTR.getForumid());
	// ptfDocs.add(ptfDoc);
	// information.setObject(ptfDocs);
	// return true;
	// }
	// }
	// }
	// }
	//
	// return false;
	// }

	private boolean classSecitionClassfy(Information information,
			List<PtfDoc> ptfDocs) {
		boolean isexit = false;
		try {
			if (classSectionEntities != null && classSectionEntities.size() > 0) {
				for (ClassSectionEntity classSectionEntity : classSectionEntities) {
					if (classSectionEntity.getWebsiteList().contains(
							information.getWebsite_id() + "")) {
						fillInfoPtfDoc(information, ptfDocs, classSectionEntity);
						isexit = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("板块标签分类出错", e);
		}
		return isexit;
	}

}
