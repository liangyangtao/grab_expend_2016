package com.unbank.duplicate.qiangkong;

import java.util.Set;

/**
 * Created by Administrator on 2016/1/15.
 */
public class Sampler {
	private Set<String> titleWords;
	private String docId;
	private Integer titleKeyId;
	private Integer contentKeyId;
	private Set<String> contentWords;

	public Sampler(String docId, Integer titleKeyId, Integer contentKeyId,
			Set<String> titleWords, Set<String> contentWords) {
		this.titleWords = titleWords;
		this.docId = docId;
		this.titleKeyId = titleKeyId;
		this.contentKeyId = contentKeyId;
		this.contentWords = contentWords;
	}

	public Set<String> getContentWords() {
		return contentWords;
	}

	public void setContentWords(Set<String> contentWords) {
		this.contentWords = contentWords;
	}

	public Sampler(Set<String> titleWords, String docId) {
		this.titleWords = titleWords;
		this.docId = docId;
	}

	public Sampler(Set<String> titleWords, String docId, Integer titleTopicId,
			Integer contentTopicId) {
		this.titleWords = titleWords;
		this.docId = docId;
		this.titleKeyId = titleTopicId;
		this.contentKeyId = contentTopicId;
	}

	public Set<String> getTitleWords() {
		return titleWords;
	}

	public void setTitleWords(Set<String> titleWords) {
		this.titleWords = titleWords;
	}

	public Integer getTitleKeyId() {
		return titleKeyId;
	}

	public void setTitleKeyId(Integer titleKeyId) {
		this.titleKeyId = titleKeyId;
	}

	public Integer getContentKeyId() {
		return contentKeyId;
	}

	public void setContentKeyId(Integer contentKeyId) {
		this.contentKeyId = contentKeyId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Override
	public String toString() {
		return "Sampler{" + "titleWords=" + titleWords + ", docId='" + docId
				+ '\'' + ", titleKeyId=" + titleKeyId + ", contentKeyId="
				+ contentKeyId + ", contentWords=" + contentWords + '}';
	}
}
