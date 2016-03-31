package com.unbank.duplicate.qiangkong;

/**
 * Created by Administrator on 2015/12/7.
 */
public class DocBuffer {
    private String docId;
    private int flag;
    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DocBuffer{" +
                "docId='" + docId + '\'' +
                ", flag=" + flag +
                '}';
    }
}
