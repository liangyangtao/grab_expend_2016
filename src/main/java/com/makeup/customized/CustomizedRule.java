package com.makeup.customized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import com.makeup.dao.CustModelConstraintReader;
import com.makeup.dao.CustModelReader;
import com.makeup.dao.CustUserReader;
import com.makeup.entity.ModuleRule;
import com.unbank.mybatis.entity.CustModule;
import com.unbank.mybatis.entity.CustModuleConstraint;
import com.unbank.mybatis.entity.CustUser;
import com.unbank.pipeline.entity.Information;

public class CustomizedRule {

	public void fillCustomizedRule(Information information) {
		List<ModuleRule> moduleRules = getUserCustomizedRule();
		List<ModuleRule> moduleRulesTemp = new ArrayList<ModuleRule>();
		int websiteid = information.getWebsite_id();
		// String webName = information.getWeb_name();
		String labels = information.getLabels();
		List<String> lebelsTemp = Arrays.asList(labels.split("_"));
		for (ModuleRule moduleRule : moduleRules) {
			String mustNotTagNames = moduleRule.getMustNotTagNames();
			String mustTagNames = moduleRule.getMustTagNames();
			String shouldTagNames = moduleRule.getShouldTagNames();
			String websites = moduleRule.getWebsiteIDs();
			boolean istrue = true;
			if (moduleRule.getModuleconstraint() == null) {
				istrue = false;
				continue;
			}

			if (websites != null && (!websites.isEmpty())) {
				List<String> websitesList = Arrays.asList(websites.split("_"));
				// 是否符合指定的数据源
				if (websitesList == null && websitesList.size() == 0) {

				} else {
					if (!websitesList.contains(websiteid + "")) {
						istrue = false;
						continue;
					}
				}
			}
			if (mustTagNames != null && (!mustTagNames.isEmpty())) {
				List<String> mustTagNamesList = Arrays.asList(mustTagNames
						.split("_"));
				if (istrue) {
					// 是否包含必须出现的标签
					for (String string : mustTagNamesList) {
						if (lebelsTemp.contains(string.trim())) {

						} else {
							istrue = false;
							break;
						}
					}
				}
			}
			if (!istrue) {
				continue;
			}
			if (mustNotTagNames != null && (!mustNotTagNames.isEmpty())) {
				List<String> mustNotTagNamesList = Arrays
						.asList(mustNotTagNames.split("_"));
				if (istrue) {
					// 是否包含不要的标签
					for (String string : mustNotTagNamesList) {
						if (lebelsTemp.contains(string.trim())) {
							istrue = false;
							break;
						}
					}

				}
			}
			if (!istrue) {
				continue;
			}
			if (shouldTagNames != null && (!shouldTagNames.isEmpty())) {
				List<String> shouldTagNamesList = Arrays.asList(shouldTagNames
						.split("_"));
				if (istrue) {
					boolean isShould = false;
					for (String string : lebelsTemp) {
						if (shouldTagNamesList.contains(string.trim())) {
							isShould = true;
							break;
						}
					}
					if (isShould) {
						istrue = true;
					} else {
						istrue = false;
						continue;
					}
				}
			}
			if (istrue) {
				moduleRulesTemp.add(moduleRule);
				System.out.println(moduleRule.getUserid() + "    "
						+ moduleRule.getPid() + "    "
						+ moduleRule.getParentid() + "    "
						+ moduleRule.getMid() + "        "
						+ moduleRule.getMname() + "    "
						+ moduleRule.getModuleconstraint()
						+ " =========         符合规则");
			} else {
				System.out.println(moduleRule.getUserid() + "    "
						+ moduleRule.getPid() + "    "
						+ moduleRule.getParentid() + "    "
						+ moduleRule.getMid() + "        "
						+ moduleRule.getMname() + "    "
						+ moduleRule.getModuleconstraint()
						+ "  !!!!!!!!!!!!!!!    不符合规则");
			}
		}
		information.setModuleRules(moduleRulesTemp);

	}

	private List<ModuleRule> getUserCustomizedRule() {
		List<ModuleRule> moduleRules = new ArrayList<ModuleRule>();
		List<CustUser> users = new CustUserReader().readerUserByEnable(1);
		for (CustUser custUser : users) {
			List<CustModule> pcustModules = new CustModelReader()
					.readerCustModelByUserid(custUser.getId());
			for (CustModule custModule : pcustModules) {
				String pid = custModule.getParentid() + "";
				getUserModule(moduleRules, custModule, pid);
			}
		}
		return moduleRules;
	}

	public void getUserModule(List<ModuleRule> moduleRules,
			CustModule custModule, String pid) {
		List<CustModule> childCustModules = new CustModelReader()
				.readerCustModelByPid(custModule.getMid());
		if (childCustModules.size() == 0) {
			fillCustModuleRule(moduleRules, custModule, pid);
		} else {
			for (CustModule custModule2 : childCustModules) {
				String temp = pid + "_" + custModule2.getParentid();
				getUserModule(moduleRules, custModule2, temp);
			}
		}

	}

	public void fillCustModuleRule(List<ModuleRule> moduleRules,
			CustModule custModule, String pid) {
		ModuleRule moduleRule = new ModuleRule();
		moduleRule.setAdminid(custModule.getAdminid());
		moduleRule.setMid(custModule.getMid());
		moduleRule.setMname(custModule.getMname());
		moduleRule.setUserid(custModule.getUserid());
		moduleRule.setParentid(custModule.getParentid());
		moduleRule.setPid(pid);
		List<CustModuleConstraint> custModuleConstraints = new CustModelConstraintReader()
				.readerCustModelConstraintByMid(custModule.getMid());
		for (CustModuleConstraint custModuleConstraint : custModuleConstraints) {
			String moduleconstraint = custModuleConstraint
					.getModuleconstraint();
			if (moduleconstraint == null) {
				continue;
			}
			moduleRule.setModuleconstraint(moduleconstraint);
			JSONObject jsonObject = JSONObject.fromObject(moduleconstraint);
			try {
				if (moduleconstraint.contains("mustNotTagNames")) {
					String mustNotTagNames = jsonObject
							.getString("mustNotTagNames");
					moduleRule.setMustNotTagNames(mustNotTagNames);
				}
			} catch (Exception e) {
				moduleRule.setMustNotTagNames("");
			}
			try {
				if (moduleconstraint.contains("mustTagNames")) {
					String mustTagNames = jsonObject.getString("mustTagNames");
					moduleRule.setMustTagNames(mustTagNames);
				}
			} catch (Exception e) {
				moduleRule.setMustTagNames("");
			}
			try {
				if (moduleconstraint.contains("shouldTagNames")) {
					String shouldTagNames = jsonObject
							.getString("shouldTagNames");
					moduleRule.setShouldTagNames(shouldTagNames);
				}
			} catch (Exception e) {
				moduleRule.setShouldTagNames("");
			}
			try {
				if (moduleconstraint.contains("websiteIDs")) {
					String websiteIDs = jsonObject.getString("websiteIDs");
					moduleRule.setWebsiteIDs(websiteIDs);
				}
			} catch (Exception e) {
				moduleRule.setWebsiteIDs("");
			}
		}
		moduleRules.add(moduleRule);
	}
}
