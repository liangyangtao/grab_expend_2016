package com.unbank.duplicate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.unbank.Constants;
import com.unbank.classify.dao.ClassWebsiteReader;
import com.unbank.classify.entity.ClassSectionEntity;
import com.unbank.duplicate.dao.ArticleCrawlIdSimilarIdReader;
import com.unbank.duplicate.dao.ArticleCrawlIdSimilarIdStore;
import com.unbank.duplicate.dao.ArticleCrawlSimilarStore;
import com.unbank.duplicate.entity.SimHashBean;
import com.unbank.duplicate.util.SimHash;
import com.unbank.mybatis.dao.InformationReader;
import com.unbank.mybatis.entity.ArticleCrawlIDSimilarId;
import com.unbank.mybatis.entity.ArticleCrawlSimilar;
import com.unbank.pipeline.entity.Information;

public class SimilarityBySimHash {
	public static List<SimHashBean> simHashlist = new ArrayList<SimHashBean>();
	public static Logger logger = Logger.getLogger(SimilarityBySimHash.class);

	public static void loaddate(Integer fileindex, Integer task, Integer num) {
		simHashlist.clear();
		List<Information> informations = new InformationReader()
				.readInformationByFileIndexAndTask(fileindex, task, num);
		for (Information information : informations) {
			SimHashBean sb = getInformationSimHashBean(information);
			simHashlist.add(sb);
		}
		logger.info("去重信息加载完成！！！");
		informations.clear();
	}

	/**
	 * 两个字符串的相同率
	 */
	public static float RSAC(String str1, String str2) {
		List<String> list = new ArrayList<String>();
		for (int k = 0; k < str1.length(); k++) {
			for (int j = 0; j < str2.length(); j++) {
				if (str1.charAt(k) == str2.charAt(j)
						&& !isExist(list, str1.substring(k, k + 1))) {
					list.add(str1.substring(k, k + 1));
					break;
				}
			}
		}
		float size = (float) (list.size() * 2)
				/ (str1.length() + str2.length());
		DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，不足的补0
		String ratiostr = df.format(size);// 返回的是String类型的
		float ratio = Float.parseFloat(ratiostr);
		return ratio;
	}

	/**
	 * 是否存在相同
	 */
	private static boolean isExist(List<String> list, String dest) {
		for (String s : list) {
			if (dest.equals(s))
				return true;
		}
		return false;
	}

	public static void panchong(Information information) {
		Integer websiteId = information.getWebsite_id();
		if (Constants.WHITELIST.contains(websiteId + "")) {
			information.setFile_index((byte) 0);
			return;
		}
		try {
			SimHashBean sb = getInformationSimHashBean(information);
			boolean stopflag = isStopFlag(sb, simHashlist, information);
			changeFileIndexByStopFlag(information, stopflag);
			if (stopflag) {

				// 表明已经发现重复的 不做任何处理
			} else {
				if (simHashlist.size() >= 180000) {
					simHashlist.remove(0);
				}
				simHashlist.add(sb);
			}
		} catch (Exception e) {
			logger.info("比较去重获取SimBean出错", e);
		}

	}

	private static void changeFileIndexByStopFlag(Information information,
			boolean stopflag) {
		try {
			if (stopflag) {
				information.setFile_index((byte) 8);
			} else {
				information.setFile_index((byte) 0);
			}
		} catch (Exception e) {
			logger.info("修改FIleIndex出错", e);
		}
	}

	// public static void main(String[] args) {
	// Information information = new Information();
	// information.setCrawl_id(1);
	// information.setCrawl_title("“六一”甄选童趣信用,卡用卡或可享受专属开卡好礼");
	// information
	// .setText("	<div>  <div>  <p>在信用卡产品日趋丰富的当下，各银行都不失时机地推出了各自独具特色且兼具使用与收藏功能的动漫信用卡。这些以卡通人物、漫画形象为卡面的特色信用卡或可享受专属开卡好礼，或可对应动漫礼品兑换，部分动漫卡还支持指定卡通品牌特惠及个性化DIY，喜爱卡通的时尚白领以及心态年轻的三口之家不妨予以关注。</p>  <p>文/本刊记者 张瑾</p>  <p>“六一”临近，大街小巷都开始出现越来越多的童趣元素。在感受烂漫节日气氛的同时，你有没有为自己想过挑选一张个性化的卡通信用卡，将喜爱的经典动漫人物形象搬上卡面，满足下内心的怀旧情结呢？</p>  <p>事实上，在信用卡产品日趋丰富的当下，各银行都已不失时机地推出了各自独具特色且兼具使用与收藏功能的动漫信用卡。这些以卡通人物、漫画形象为卡面的特色信用卡或拥有独特的观赏性，或可享受专属开卡好礼、对应动漫礼品兑换，部分动漫卡还支持指定卡通品牌特惠及个性化DIY，这对广大卡通、漫画爱迷着实具有不小的吸引力，喜爱卡通的时尚白领以及心态年轻的三口之家不妨予以关注。</p>  <p>招商银行Hello Kitty信用卡</p>  <p>币种：人民币单币卡/人民币+美元双币卡</p>  <p>年费：普卡100元/卡，附属卡50元/卡；金卡300元/卡，附属卡150元/卡</p>  <p>免年费条款：首年免年费，持卡有效期一整年内(从卡片核发日起计，非自然年)单卡消费满6次(金额、形式不限)免次年年费</p>  <p>独有特色：Hello Kitty开户礼、积分优惠兑换正版Hello Kitty系列礼品</p>  <p>Kitty猫诞生于1974年，几十年间，Kitty完成了从无名小猫到全球可爱偶像的华丽蜕变。不仅吸引小朋友，也掳获了全球女性的芳心。当一代女孩长大成为母亲后，依旧会和她的女儿一样，喜欢着这只猫。招商银行推出的Hello Kitty信用卡就是以Kitty卡通题材为主题的特色信用卡，该系列信用卡共分为花样年华卡、豆蔻年华卡、洋装浪漫卡、唐装贺喜卡以及面向高端女性粉丝的金卡。除了具备招行标准信用卡的各项服务及商户优惠外，即日起至2015年6月30日，凡成功申请Hello Kitty卡普卡的新户主卡持卡人，在指定时间内刷卡或分期达标，还可登录招商银行信用卡APP掌上生活进行首刷礼品领取，领取后可获赠“多样屋限量版Hello Kitty鳄鱼纹首饰盒”或1000招行永久积分。</p>  <p>建设银行变形金刚信用卡</p>  <p>币种：人民币+美元双币卡/美元单币卡</p>  <p>年费：普卡主卡60元/卡，附属卡40元/卡；金卡主卡160元/卡，附属卡80元/卡；标准白金卡主卡580元/卡，附属卡300元/卡</p>  <p>免年费条款：普卡、金卡每个持卡年度刷卡消费、取现累计满3笔(金额不限)免当年年费；白金卡每个持卡年度刷卡消费、取现累计满10笔(金额不限)免当年年费</p>  <p>独有特色：消费积分兑换“变形金刚信用卡珍藏纪念套卡”、白金卡外币消费交易双倍积分</p>  <p>“变形金刚”诞生30年间，共推出了20多部动画作品，700余集动画剧集以及多部电影，“汽车人”们不仅是70后80后的难忘回忆，更记录了一代“地球人”的青春往事。建行携手美国孩之宝推出的“龙卡变形金刚信用卡”系列就是以“汽车人”及《变形金刚4》电影形象为卡面元素的主题信用卡。除具备建行龙卡标准信用卡的基本功能外，还特设外币交易人民币自动购汇入账、免收外汇兑换手续费、白金卡外币交易双倍积分、全面出行保障及贵宾礼遇等专属权益。动漫专属权益方面，持卡人可使用龙卡变形金刚主题信用卡金卡、白金卡消费积分兑换《变形金刚信用卡珍藏纪念套卡》。</p>  <p>农业银行：金穗喜羊羊与灰太狼联名IC信用卡</p>  <p>币种：人民币单币</p>  <p>年费：普卡主卡80元/卡，附属卡40元/卡；金卡主卡160元/卡，附属卡80元/卡；</p>  <p>免年费条款：首年免年费，持卡有效期一整年内刷卡消费满5次免次年年费</p>  <p>独有特色：IC安全芯片卡、积分礼品喜羊羊专区</p>  <p>作为时下最受欢迎的国产动画形象之一，随着动画的热播，其周边的各类童装、玩具、家居用品都深受小动画迷的喜爱。农行金穗喜羊羊与灰太狼联名卡就精选《喜羊羊与灰太狼》中的热门人物，精心打造了四款全新卡面，分别为“美羊羊”、“红太狼”女生版以及“喜羊羊”和“小灰灰”男生版。其消费积分可兑换喜羊羊专区内的限量版公仔玩偶及周边衍生产品。另一方面，卡片设计采用IC芯片设计，也拥有较高的安全保障。</p>  <p>招商银行ONEPIECE航海王信用卡</p>  <p>币种：人民币+美元双币卡</p>  <p>年费：普卡100元/卡，附属卡50元/卡</p>  <p>免年费条款：首年免年费，持卡有效期一整年内(从卡片核发日起计，非自然年)单卡消费满6次(金额、形式不限)免次年年费</p>  <p>独有特色：手办杯套装开卡礼、生日月双倍积分</p>  <p>《ONE PIECE》(又称海贼王)是日本著名漫画家尾田荣一郎在《周刊少年JUMP》上连载的超人气励志漫画，在国内青年群体间拥有很高的热度，正版漫画、动画、周边等等都相继登陆。为迎合这部分动漫迷的需求，招商银行也为此推出了以ONE PIECE人物形象为卡面的主题卡，除可享受招行标准信用卡以及JCB的各项权益，还可享受主持卡人生日月双倍积分的专属优惠。另一方面，从即日起至2015年6月30日，凡成功申请招商银行ONE PIECE航海王信用卡的新户主卡持卡人，在指定时间内刷卡或分期达标，就可通过“掌上生活”APP领取一份官方正版授权的全球独家限量版ONE PIECE航海王手办杯套装(内含一个不锈钢内胆杯和一个人物手办)，路飞版和乔巴版可任选其一领取。</p>  <p>平安银行BE@RBRICK信用卡</p>  <p>币种：人民币+美元双币卡</p>  <p>年费：主卡100元/卡；附属卡终生免年费</p>  <p>免年费条款：首年免年费，持卡有效期一整年内刷卡消费满6次免次年年费</p>  <p>独有特色：刷卡赢取BE@RBRICK套装＋限量版玩偶、独家BE@RBRICK积分、5%最低还款</p>  <p>除了“萌系”的动漫信用卡，平安银行也为喜爱“酷系”潮人推出了专属动漫卡。平安BE@RBRICK时尚信用卡以“潮人身份证”为产品定位，以手脚酷似乐高积木机器人模样的BE@RBRICK为卡面设计，造型多变可爱，色彩绚丽，超级明星玩偶形象呼之欲出。作为JCB品牌及平安共同推出的双币国际标准信用卡，BE@RBRICK主题卡不但具有JCB国际信用卡品牌以及平安银行信用卡基本功能和服务，还可享受最高100%取现额度专属取现礼遇、5%最低还款特权，并可以消费积分兑换限量BE@RBRICK积木熊及品牌周边。</p> </div></div>");
	//
	// Information information2 = new Information();
	// information2.setCrawl_title("“六一甄选信用卡 ,出了各自独具特色且兼具");
	// information2.setCrawl_id(2);
	// information2
	// .setText("	<div>  <div>  <p>将喜爱的经典动漫人物形象搬上卡面，满足下内心的怀旧情结呢？</p>  ");
	//
	// float num = RSAC(information.getCrawl_title(),
	// information2.getCrawl_title());
	// System.out.println(num);
	// SimHashBean sb = getInformationSimHashBean(information);
	// SimHashBean sb1 = getInformationSimHashBean(information2);
	// double bijiaozhi = 0;
	// if (RSAC(sb1.getTitle(), sb.getTitle()) >= 0.2) {
	// bijiaozhi = getDistance(sb1.getHashcode(), sb.getHashcode());
	// System.out.println(bijiaozhi);
	// // 相似比例打到阀值0.3视为相同
	// if (bijiaozhi < 3) {
	// information.setSimilarid(sb1.getId());
	// System.out.println("======== 文章重复了");
	// }
	// }
	// if (sb1.getTitle().equals(sb.getTitle())) {
	// bijiaozhi = 10;
	// System.out.println("======== 文章重复了");
	// }

	// }

	private static boolean isStopFlag(SimHashBean sb,
			List<SimHashBean> simHashlist, Information information) {
		boolean stopflag = false;
		for (int i = 0; i < simHashlist.size(); i++) {
			SimHashBean sb1 = simHashlist.get(i);
			// 标题相同字数打到20%可以比较
			float num = RSAC(sb1.getTitle(), sb.getTitle());

			if (num >= 0.2) {

				if (num >= 0.8) {
					stopflag = true;
					information.setSimilarid(sb1.getId());
					saveArticleCrawlSimilar(information, sb1);
					saveSimilarityId(sb1.getId(), sb.getId());
					break;
				} else {
					int bijiaozhi = getDistance(sb1.getHashcode(),
							sb.getHashcode());
					// 相似比例打到阀值小于3视为相同
					if (bijiaozhi < 3) {
						stopflag = true;
						information.setSimilarid(sb1.getId());
						saveArticleCrawlSimilar(information, sb1);
						saveSimilarityId(sb1.getId(), sb.getId());
						break;
					}
				}
			}
			if (sb1.getTitle().equals(sb.getTitle())) {
				stopflag = true;
				information.setSimilarid(sb1.getId());
				saveArticleCrawlSimilar(information, sb1);
				saveSimilarityId(sb1.getId(), sb.getId());
				break;
			}
		}
		return stopflag;
	}

	private static void saveArticleCrawlSimilar(Information information,
			SimHashBean sb1) {
		ArticleCrawlSimilar articleCrawlSimilar = new ArticleCrawlSimilar();
		List<ClassSectionEntity> classWebsiteEntities = new ClassWebsiteReader()
				.readClassWebsiteEntity();
		List<ArticleCrawlIDSimilarId> articleCrawlIDSimilarIds = new ArticleCrawlIdSimilarIdReader()
				.readArticleCrawlIdSimilarId(information.getSimilarid());
		StringBuffer classname = new StringBuffer();
		if (classWebsiteEntities != null && classWebsiteEntities.size() > 0) {
			for (ClassSectionEntity classSectionEntity : classWebsiteEntities) {
				if (classSectionEntity.getWebsiteList().contains(
						information.getWebsite_id() + "")) {
					classname.append(classSectionEntity.getClassname() + "_");
				}
			}
		}
		if (classname.length() > 0) {
			articleCrawlSimilar.setClassname(classname.toString().substring(0,
					classname.length() - 1));
		}
		articleCrawlSimilar.setCrawlId(sb1.getId());
		articleCrawlSimilar.setHotnum(articleCrawlIDSimilarIds.size() + 1);
		articleCrawlSimilar.setIstask(0);
		articleCrawlSimilar.setSimilarId(information.getCrawl_id());
		articleCrawlSimilar.setWebisteId(information.getWebsite_id());
		articleCrawlSimilar.setWebName(information.getWeb_name());
		articleCrawlSimilar.setCrawlTime(information.getCrawl_time());
		articleCrawlSimilar.setNewsTime(information.getNews_time());
		new ArticleCrawlSimilarStore()
				.saveArticleCrawlSimilar(articleCrawlSimilar);
	}

	private static SimHashBean getInformationSimHashBean(Information information) {
		String title = Jsoup.parse(information.getCrawl_title()).text();
		String text = Jsoup.parse(information.getText()).text();
		SimHashBean sb = new SimHashBean();
		sb.setId(information.getCrawl_id());
		sb.setTitle(title);
		SimHash hasha = new SimHash(text, 64);
		sb.setHashcode(hasha.getStrSimHash());
		return sb;
	}

	private static void saveSimilarityId(int crawlid, int similarid) {
		try {
			ArticleCrawlIDSimilarId articleCrawlIDSimilarId = new ArticleCrawlIDSimilarId();
			articleCrawlIDSimilarId.setCrawlid(crawlid);
			articleCrawlIDSimilarId.setSimilarid(similarid);
			new ArticleCrawlIdSimilarIdStore()
					.saveArticleCrawlIdSimilarId(articleCrawlIDSimilarId);
		} catch (Exception e) {
			logger.info("保存重复ID失败", e);
		}
	}

	public static int getDistance(String str1, String str2) {
		int distance;
		if (str1.length() != str2.length()) {
			distance = -1;
		} else {
			distance = 0;
			for (int i = 0; i < str1.length(); i++) {
				if (str1.charAt(i) != str2.charAt(i)) {
					distance++;
				}
			}
		}
		return distance;
	}

}
