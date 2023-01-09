package com;

public class MyProperty {
    protected final String path;
    protected final int width;
    protected final int height;
    protected final int score;
    protected final int weight;
    protected final boolean isBoom;
    public MyProperty(String path,int width,int height,int score,int weight,boolean isBoom)
    {
        this.weight=weight;
        this.score=score;
        this.path=path;
        this.height=height;
        this.width=width;
        this.isBoom=isBoom;
    }
}
