package com;
import java.awt.*;
public class Line {
    private Image hook=Toolkit.getDefaultToolkit().getImage("imgs/hookR.png");
    //起点坐标
    private final int beginX=250;
    private final int beginY=190;
    //终点坐标
    protected int endX;
    protected int endY;
    private double n=0.1; //控制角度，范围0到1.
    //长度
    private double length=40;
    private int dir=1;   //控制方向
    //控制状态
    static int state=0;
    private object object;  //记录当前物品
    private GoldFrame goldFrame;
    //控制返回速度
    public Line(GoldFrame goldFrame)
    {
        this.goldFrame=goldFrame;
    }
    //线初始坐标
           //0左右摇摆，1抓取，2收回.
    public void paintSelf(Graphics g)
    {
        switch (state)
        {
            case 0:
                if(n<0.1)
                {
                    dir=1;
                }
                else if (n>0.9)
                {
                    dir=-1;
                }
                n+=0.005*dir;
                break;
            case 1:length+=7;    //抓取
                if(length*Math.sin(n*Math.PI)>=590||Math.abs(length*Math.cos(n*Math.PI))>=240)
                    state=2;
                var o1= goldFrame.obj.stream().filter(o->o.logic(new Point(endX,endY))).findAny();
//                for (object o: goldFrame.obj)
//                {
//                    if(o.logic(new Point(endX,endY)))   //抓取物品判断
//                    {
//                        object=o;
//                        state=3;
//                        break;
//                    }
//                }
                if(o1.isPresent())
                {
                    object=o1.get();
                    state=3;
                }
                break;
            case 2:         //返回
                length-=7;
                if(length<40)state=0;
                break;
            case 3:   length-=object.weight;    //抓取物品返回
                if(length<40)
                {
                    goldFrame.setAllScore(object.getScore());
                    state=0;
                    goldFrame.obj.remove(object);       //移除物品
                }
                if(GoldFrame.boomFlag&&object.isBoom)
                {
                    goldFrame.obj.remove(object);
                    GoldFrame.boomFlag=false;
                    new VoicePlayer().boomPlay();
                    state=2;
                }
                object.x=endX-object.width/2;
                object.y=endY;
                break;
            default:break;
        }
        endX=(int) (beginX+length*Math.cos(n*Math.PI));
        endY=(int)(beginY+length*Math.sin(n*Math.PI));
        g.setColor(Color.RED);
        g.drawLine(beginX-1,beginY,endX-1,endY);
        g.drawLine(beginX,beginY,endX,endY);
        g.drawLine(beginX+1,beginY,endX+1,endY);
        g.drawImage(hook,endX-24,endY-2,null);
    }

}
