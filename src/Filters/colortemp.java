package Filters;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class colortemp implements PixelFilter, Interactive {
    private int threshold = 50;
    private List<Short> newcolorstoadd = new ArrayList();

    public colortemp() {
    }

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        short[][][] maskedColors = this.colorfullon(red, blue, green);
        img.setColorChannels(maskedColors[0], maskedColors[1], maskedColors[2]);
        return img;
    }

    public short[][][] colorfullon(short[][] red, short[][] blue, short[][] green) {
        for(int r = 0; r < red.length; ++r) {
            for(int c = 0; c < red[r].length; ++c) {
                boolean keepPixel = false;

                for(int i = 0; i < this.newcolorstoadd.size(); i += 3) {
                    short selectedR = (Short)this.newcolorstoadd.get(i);
                    short selectedB = (Short)this.newcolorstoadd.get(i + 1);
                    short selectedG = (Short)this.newcolorstoadd.get(i + 2);
                    double reddist = (double)((selectedR - red[r][c]) * (selectedR - red[r][c]));
                    double bluedist = (double)((selectedB - blue[r][c]) * (selectedB - blue[r][c]));
                    double greendist = (double)((selectedG - green[r][c]) * (selectedG - green[r][c]));
                    double difference = Math.sqrt(reddist + bluedist + greendist);
                    if (difference <= (double)this.threshold) {
                        keepPixel = true;
                        break;
                    }
                }

                if (!keepPixel) {
                    red[r][c] = 0;
                    blue[r][c] = 0;
                    green[r][c] = 0;
                }
            }
        }

        return new short[][][]{red, blue, green};
    }

    public void Keypressed(char key) {
    }

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        this.newcolorstoadd.add(red[mouseY][mouseX]);
        this.newcolorstoadd.add(blue[mouseY][mouseX]);
        this.newcolorstoadd.add(green[mouseY][mouseX]);
        PrintStream var10000 = System.out;
        int var10001 = this.newcolorstoadd.size();
        var10000.println("Colors stored: " + var10001 / 3);
    }

    public void keyPressed(char key) {
        if (key == 'l') {
            ++this.threshold;
        } else if (key == 'o' && this.threshold > 0) {
            --this.threshold;
        } else if (key == 'r') {
            this.newcolorstoadd.clear();
        }

    }
}
