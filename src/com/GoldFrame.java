package com;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
public class GoldFrame extends JFrame {
    //初始爆破次数(3)
    private int boomAmount=3;
    //爆破标志
    static boolean boomFlag=false;
    //物块总数
    private int AllObjNum;
    //总分数
    private int AllScore=0;
    //目的分数
    private int AimScore=10;
    //控制程序运行
    private boolean run=true;
    //存储物块
    protected ConcurrentLinkedQueue<object> obj=new ConcurrentLinkedQueue<>();
    //背景
    private BackGroup backGroup=new BackGroup();
    //预设画板
    private Image offScreenImage;
    //预设画笔
    Graphics gImage;
    private final Line line=new Line(this);
    //物块属性
    MyProperty[] properties = new MyProperty[5];
    //时间控制
    private final long AllTime=60;
    private long  startTime;
    private long endTime;
    public GoldFrame()
    {
        properties[0] = new MyProperty("imgs/gold0.gif", 36, 36, 10, 6,false);
        properties[1] = new MyProperty("imgs/gold1.png", 36, 36, 25, 8,false);
        properties[2] = new MyProperty("imgs/gold22.png", 80, 80, 50, 9,false);
        properties[3] = new MyProperty("imgs/samllrockR.png", 39, 40, 0, 7,true);
        properties[4] = new MyProperty("imgs/BigrockR.png", 69, 70, 0, 9,true);
        AllObjNum=20;
    }
    @Override
    public void paint(Graphics g) {
        offScreenImage=this.createImage(500,800);
        gImage=offScreenImage.getGraphics();             //提前绘制，解决人物闪动问题；
        backGroup.backPaint(gImage);  //绘制背景
        for (object o:obj)       //绘制物品
        {
            o.paintSelf(gImage);
        }
        line.paintSelf(gImage); //绘制红线
        Font font1=new Font("楷体",Font.BOLD,20);
        var g2=(Graphics2D) gImage;
        g2.setFont(font1);
        g2.drawString("得分: "+AllScore,360,90);
        g2.drawString("爆破次数:"+boomAmount,20,90);
        endTime=System.currentTimeMillis();    //获得时间
        long nowTime=AllTime-(endTime-startTime)/1000;//毫秒化秒
        if(nowTime<=0)
        {
            Font font=new Font("楷体",Font.BOLD,50);
            g2.setFont(font);
            g2.drawString("Game Over!",this.getWidth()/2-font.getSize()*2,this.getHeight()/2);
            run=false;

        }
        else g2.drawString("TIme:"+nowTime,200,90);
        if(AllScore==AimScore)
        {
            Font font=new Font("楷体",Font.BOLD,50);
            g2.setFont(font);
            g2.drawString("You Win!",this.getWidth()/2-font.getSize()*2,this.getHeight()/2);
            run=false;
        }
        g.drawImage(offScreenImage,0,0,null);
    }
    //购买爆破次数并判断(50一次)
    public boolean isBuy()
    {
        if(AllScore>=50)
        {
            AllScore-=50;
            AimScore-=50;
            boomAmount++;
            return true;
        }
        else return false;
    }
    //判断是否可以爆破并扣除次数
    public boolean isBoomWay()
    {
        if(boomAmount>0)
        {
            boomAmount--;
            boomFlag=true;
            return true;
        }
       else return false;
    }
    public void launch()  //初始化
    {
        startTime=System.currentTimeMillis();
        addObject();
        AimScore = 0;
        for (object o : obj)       //得到总积分
        {
            AimScore += o.getScore();
        }
        setSize(500, 800);  //设置窗口属性
        setVisible(true);
        setTitle("黄金矿工");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                         //1:左键 2：滚轮，3：右键;
                if (e.getButton() == 1&&Line.state!=3)       //抓取
                {
                    Line.state = 1;
                }
                if(e.getButton()==3&&Line.state==3)   //爆破
                {
                    isBoomWay();
                }
                if(e.getButton()==2)   //购买爆破次数
                {
                    isBuy();
                }
            }
        });
        Runnable r = () ->
        {
            var player = new VoicePlayer();
            new Thread(player).start();
            while (run) {
                repaint();
                if(player.beginFlag)
                {
                    new Thread(player).start();
                    player.beginFlag=false;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            player.interrupt();     //标记该线程，停止音乐.
        };
        new Thread(r).start();
    }

    public void setAllScore(int score) {     //添加分数
        AllScore+=score;
    }
    public void addObject()  //添加元素
    {
        object element;   //元素
        boolean isAdd;    //判断能否添加元素
        int num;   //控制生成类型
        //坐标
        int x;
        int y;
        for (int i=0;i<AllObjNum;i++) {
            num=(int) (Math.random()*5);
            do {
                isAdd=false;       //更新判断值
                x = (int) (Math.random() * 450);
                y = (int) (Math.random() * 430 + 220);
                element = new object(properties[num].path, properties[num].width, properties[num].height
                ).setScore(properties[num].score).setWeight(properties[num].weight).setXY(x, y).setBoom(properties[num].isBoom);
                for (object o : obj) {
                    if (o.getRec().intersects(element.getRec())) {
                        isAdd = true;
                    }
                }
            }while (isAdd);
            obj.add(element);
        }
        }
    public static void main(String[] args) {
        var frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        var panel = new LaunchPane();
        var button = new JButton("Start");
        panel.add(button,BorderLayout.CENTER);
        frame.add(panel);
        panel.revalidate();
        button.addActionListener(e->
        {
            new GoldFrame().launch();
            frame.setVisible(false);
        });
    }
}
class LaunchPane extends JPanel
{
    @Override
    public void paintComponent(Graphics g) {
        var g2=(Graphics2D)g;
        var font = new Font("楷体", Font.BOLD + Font.ITALIC, 20);
        g2.setFont(font);
        var context = g2.getFontRenderContext();
        var content = new String("鼠标左抓取，右爆破石块");
        g2.drawString(content, (int) (this.getX()+this.getWidth()/2-font.getStringBounds(content,context).getWidth()/2),
                this.getY()+this.getHeight()/2);
        g2.drawString("滚轮购买爆破次数(50一次)", (int) (this.getX()+this.getWidth()/2-font.getStringBounds("滚轮" +
                        "购买爆破次数(50一次)",context).getWidth()/2),
                (int) (this.getY()+this.getHeight()/2+font.getStringBounds(content,context).getHeight()));
    }
}
//new Thread(()->  //爆破线程
//        {
//        boolean flag = true;
//        while (flag&&isBoomWay()) {
//        Point point = e.getPoint();
//        for (object o : obj) {
//        if (o.getRec().contains(point))
//        {
//        if (o.isBoom) {
//        new VoicePlayer().boomPlay();
//        obj.remove(o);
//        flag = false;
//        }
//        break;
//        }
//        }
//        }
//        }).start();