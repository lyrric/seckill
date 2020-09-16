package com.github.lyrric.model;

import java.util.List;

/**
 * Created on 2020-09-16.
 *
 * @author wangxiaodong
 */
public class Area {

    private String name;
    private String value;

    private List<Area> children;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Area> getChildren() {
        return children;
    }

    public void setChildren(List<Area> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return name + "-" + value ;
    }
}
