package com;

import java.awt.*;

public class BackGroup {
    Image bg=Toolkit.getDefaultToolkit().getImage("imgs/bg.jpg");
    Image bg1=Toolkit.getDefaultToolkit().getImage("imgs/bg1.jpg");
    Image peo=Toolkit.getDefaultToolkit().getImage("imgs/peo2.png");
    public void backPaint(Graphics g)
    {
        g.drawImage(bg1,0,0,null);
        g.drawImage(bg,0,200,null);
        g.drawImage(peo,(int) (500-125)/2,110,null);

    }
}
