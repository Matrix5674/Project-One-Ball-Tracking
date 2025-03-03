package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTrackingAlgorithm {

    public DImage trackImage(DImage img) {
        short[][] imgBW = img.getBWPixelGrid();

        // Do stuff with color channels here


        img.setPixels(imgBW);
        return img;
    }

    public short[][] findAreasOfInterest(short[][] img){
        short[][] areasOfInterest = new short[img.length][img[0].length];
        int topPixel;
        int bottomPixel;
        for (int r = 0; r < img.length; r++) {
            for (int c = 0; c < img[r].length; c++) {
                if (areasOfInterest[r][c] == 255) {
                    //Find the topmost pixel of the ball by looping over rows.
                    //Find loop over till you find another white pixel that doesn't have any white pixels adjacent to it.
                    //Find the pixel in between the points to determine the center
                }
            }
        }
    }


}
