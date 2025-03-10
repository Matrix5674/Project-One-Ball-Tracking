package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;
import java.util.List;


public class ColorMask implements PixelFilter, Interactive {
    private int threshold;
    private int count;
    private List<Short> newcolorstoadd = new ArrayList<>();
    private int othercount = 0;
    private boolean Keepthis;

    public ColorMask() {
        threshold = 50;
        count=0;
    }

    public DImage processImage(DImage img){
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        count = 0;
        short[][][] maskedColors = this.colorfullon(red, blue, green);
        img.setColorChannels(maskedColors[0], maskedColors[1], maskedColors[2]);
        return BallTrackingAlgorithm.trackImage(img, newcolorstoadd);
    }

    public short[][][] colorfullon(short[][] red, short[][]blue, short[][] green) {
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                Keepthis = false;

                for(int i = 0; i < this.newcolorstoadd.size(); i += 3) {
                    short thisR = (Short) this.newcolorstoadd.get(i);
                    short thisB = (Short) this.newcolorstoadd.get(i + 1);
                    short thisG = (Short) this.newcolorstoadd.get(i + 2);

                    double reddist = (double) ((thisR - red[r][c]) * (thisR - red[r][c]));
                    double bluedist = (double) ((thisB - blue[r][c]) * (thisB - blue[r][c]));
                    double greendist = (double) ((thisG - green[r][c]) * (thisG - green[r][c]));

                    double thing = reddist + bluedist + greendist;
                    double difference = Math.sqrt(thing);

                    if (difference <= (double) this.threshold) {
                        Keepthis = true;
                        break;
                    }
                }


                if (!Keepthis) {
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

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        this.newcolorstoadd.add(red[mouseY][mouseX]);
        this.newcolorstoadd.add(blue[mouseY][mouseX]);
        this.newcolorstoadd.add(green[mouseY][mouseX]);
    }

    @Override
    public void keyPressed(char key) {
        if (key == 'l')
            threshold++;
        else if (key == 'o' && threshold > 0)
            threshold--;
        else if (key == 'r') {
            this.newcolorstoadd.clear();
        } else if(key=='m')
            System.out.println(threshold);
    }
}
