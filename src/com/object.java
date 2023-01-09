package com;
import java.awt.*;
public class object {
    public object(String path,int width,int height)
    {
        this.image= Toolkit.getDefaultToolkit().getImage(path);
        this.width=width;
        this.height=height;
    }
    //坐标
    protected int x;
    protected int y;
    //宽高
    protected int width;
    protected int height;
    //重量
    protected int weight;
    //积分
    private int score=0;
    //图像
    protected Image image;
    //控制爆破
    protected boolean isBoom;
    public object setXY(int x,int y)
    {
        this.x=x;
        this.y=y;
        return this;
    }
    public void paintSelf(Graphics g)
    {
        g.drawImage(image,x,y,null);
    }
    public boolean logic(Point point) //判断抓取金块
    {
        return this.getRec().contains(point);
    }

    public object setScore(int score) {
        this.score = score;
        return this;
    }

    public int getScore() {
        return score;
    }

    public object setWeight(int weight) {
        this.weight =10-weight;
        return this;
    }
    public Rectangle getRec()
    {
        return new Rectangle(x,y,width,height);
    }
//设置为可爆破状态
    public object setBoom(boolean isBoom) {
        this.isBoom=isBoom;
        return this;
    }
//    public void BoomPaint(Graphics g,GoldFrame frame) throws InterruptedException {
////        Image boomImage=Toolkit.getDefaultToolkit().getImage("imgs/boomR.png");
////        g.drawImage(boomImage,this.width/2-22,this.height/2-15,null);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            new VoicePlayer().boomPlay();
//        } catch (FileNotFoundException | JavaLayerException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
