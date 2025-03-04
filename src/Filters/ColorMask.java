package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;


public class ColorMask implements PixelFilter, Interactive {
    private short t;
    private short g;
    private short b;
    private short t2;
    private short g2;
    private short b2;
    private int threshold;
    private int count;

    public ColorMask() {
        threshold = 50;
        count=0;
    }

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        short[][][]thing =colorfullon(red,blue,green);
        short[][] red1 = thing[0];
        short[][] green2 = thing[1];
        short[][] blue3 = thing[2];
        img.setColorChannels(red1, blue3, green2);
        return img;
    }
    public short[][][] colorfullon(short[][] red, short[][]blue, short[][] green) {
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                double reddist = ((t - red[r][c]) * (t - red[r][c]));
                double bluedist = (b - blue[r][c]) * (b - blue[r][c]);
                double greendist = (g - green[r][c]) * (g - green[r][c]);
                double thing = reddist + bluedist + greendist;
                double difference = Math.sqrt(thing);
                if (difference > threshold) {
                    red[r][c] = 0;
                    blue[r][c]=0;
                    green[r][c]=0;
                }
            }
        }
        short[][][] thing = {red,blue,green};
        return thing;
    }

    @Override
    public void Keypressed(char key) {
        if(key=='t')
            System.out.println(threshold);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        if(count>1) {
            t = red[mouseY][mouseX];
            b = blue[mouseY][mouseX];
            g = green[mouseY][mouseX];
        } //else if (count==1){
            //t2 = red[mouseY][mouseX];
            //b2 = blue[mouseY][mouseX];
            //g2 = green[mouseY][mouseX];
        //}
        count++;
        System.out.println(count);
    }

    @Override
    public void keyPressed(char key) {
        if(key== 'l')
            threshold++;
        else if(key=='o')
            threshold--;

    }
}
