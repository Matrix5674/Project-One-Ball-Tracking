package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ColorMask implements PixelFilter, Interactive {
    private int threshold;
    private List<Short> newcolorstoadd = new ArrayList<>();
    private int othercount = 0;
    private boolean Keepthis;

    public ColorMask() {
        threshold = 30;
    }

    public DImage processImage(DImage img){
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        short[][][] maskedColors = colorfullon(red, blue, green);
       // short[][] filteredRed = filterNoise(maskedColors[0], 0);
        //short[][] filteredGreen = filterNoise(maskedColors[1], 0);
        //short[][] filteredBlue = filterNoise(maskedColors[2], 0);
        img.setColorChannels(maskedColors[0], maskedColors[1], maskedColors[2]);

        //return BallTrackingAlgorithm.trackImage(img, newcolorstoadd);

       return img;
    }

    //TODO: Future update
//Make the threshold variable more accessible by letting the user input values 1-10
//and scaling them to fit the perimeter of the region.
    public short[][] filterNoise(short[][] img, int threshold){
        int widthOfSquare = 20;
        for (int r = 0; r < img.length-20; r+=5) {
            for (int c = 0; c < img[r].length-20; c+=5) {

                int sumOfPerimeter = 0;
                //Calculates perimeter
                for (int row = 0; row < widthOfSquare; row++) {
                    if (img[r + row][0] != 0) {
                        sumOfPerimeter += 1;
                    }
                    if (img[r + row][img[r].length-1] != 0) {
                        sumOfPerimeter += 1;
                    }
                }

                for (int col = 0; col < widthOfSquare; col++) {
                    if (img[img.length-1][c + col] != 0) {
                        sumOfPerimeter += 1;
                    }
                    if (img[0][c+col] != 0) {
                        sumOfPerimeter += 1;
                    }
                }


                //Removes noise if our perimeter sum is less than the threshold.
                if (sumOfPerimeter <= threshold) {
                    for (int row = 0; row < widthOfSquare; row++) {
                        for (int col = 0; col < widthOfSquare; col++) {
                            img[r+row][c+col] = 0;
                        }
                    }
                }

            }
        }

        return img;
    }
        private short[][][] noisereduc(short[][][] arr3d, int checksize) {
        short[][][] fina = {arr3d[0],arr3d[1],arr3d[2]};
        short[][] red = arr3d[0];
        short[][] green = arr3d[1];
        short[][] blue = arr3d[2];
        for (int r = checksize; r < arr3d[0].length-checksize; r++) {
            for (int c = checksize; c < arr3d[0][0].length-checksize; c++) {

                int count = 0;
                for (int i = -checksize; i < checksize; i++) {
                    for (int j = -checksize; j < checksize; j++) {
                         if(i==-checksize || j==-checksize ||i==checksize ||j==checksize){
                             count++;
                         }

                    }
                }
                int thing = (4*((2*checksize)+1))-4;
                if(count==thing);{
                    red[r][c]=0;
                    green[r][c]=0;
                    blue[r][c]=0;
                }


            }

        }
        short[][][] finallist = {red,green,blue};
        return finallist;
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
        short[][][] thing = {red,green,blue};
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