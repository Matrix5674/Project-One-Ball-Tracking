package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;


public class ColorMask implements PixelFilter, Interactive {
    private int threshold;
    private int count;
    short[] newcolorstoadd = new short[Integer.MAX_VALUE];
    private int othercount = 0;

    public ColorMask() {
        threshold = 50;
        count=0;
    }

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        count = 0;
        short[][][]thing =colorfullon(red,blue,green, count);
        short[][] red1 = thing[0];
        short[][] green2 = thing[1];
        short[][] blue3 = thing[2];
        img.setColorChannels(red1, green2, blue3);
        return img;
    }
    public short[][][] colorfullon(short[][] red, short[][]blue, short[][] green, int count) {
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                double reddist = ((newcolorstoadd[count] - red[r][c]) * (newcolorstoadd[count] - red[r][c]));
                count++;
                double bluedist = (newcolorstoadd[count] - blue[r][c]) * (newcolorstoadd[count] - blue[r][c]);
                count++;
                double greendist = (newcolorstoadd[count] - green[r][c]) * (newcolorstoadd[count] - green[r][c]);
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
        count += 1;
        return thing;
    }

    @Override
    public void Keypressed(char key) {
        if(key=='m')
            System.out.println(threshold);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        othercount=0;
        if(count>1) {
            short t = red[mouseY][mouseX];
            short b = blue[mouseY][mouseX];
            short g = green[mouseY][mouseX];
            newcolorstoadd[othercount]=t;
            othercount++;
            newcolorstoadd[othercount]=b;
            othercount++;
            newcolorstoadd[othercount]=g;
            othercount++;
        }
        count++;
        System.out.println(count);
    }

    @Override
    public void keyPressed(char key) {
        if (key == 'l')
            threshold++;
        else if (key == 'o' && threshold > 0)
            threshold--;
    }
}
