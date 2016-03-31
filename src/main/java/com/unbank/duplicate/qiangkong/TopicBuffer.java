package com.unbank.duplicate.qiangkong;

/**
 * Created by Administrator on 2015/12/7.
 */
public class TopicBuffer implements Comparable<TopicBuffer>{
    private Integer index;
    private Double  value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public TopicBuffer(Integer index, Double value) {
        this.index = index;
        this.value = value;
    }
    public TopicBuffer() {}

    @Override
    public int compareTo(TopicBuffer o) {
        if(this.getValue()>o.getValue()){
            return 1;
        }else if(this.getValue()<o.getValue()){
            return -1;
        }else {
            return 0;
        }
    }
}
