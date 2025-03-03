package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {
    private short t;
    private short g;
    private short b;
    private int threshold;

    public ColorMask() {
        threshold = 100;
    }

    public DImage processImage(DImage img) {
        short[][] grayScale = img.getBWPixelGrid();

        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                double thing = (double) ((t - red[r][c]) * (t - red[r][c]) + (b - blue[r][c]) * (b - blue[r][c]) + (g - green[r][c]) * (g - green[r][c]));
                double difference = Math.sqrt(thing);
                if (difference > threshold) {
                    red[r][c] = 0;//grayScale[r][c];
                    blue[r][c] = 0;//grayScale[r][c];
                    green[r][c] = 0;//grayScale[r][c];
                } else {
                    red[r][c] = 110;//grayScale[r][c];
                    blue[r][c] = 110;//grayScale[r][c];
                    green[r][c] = 100;
                }
            }
        }

        img.setColorChannels(red, green, blue);
        return img;
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
        t=red[mouseY][mouseX];
        b=blue[mouseY][mouseX];
        g=green[mouseY][mouseX];
    }

    @Override
    public void keyPressed(char key) {
        if(key== 'l')
            threshold++;
        else if(key=='o')
            threshold--;

    }
}
