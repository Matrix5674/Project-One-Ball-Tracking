package Filters;

import core.DImage;


import java.util.ArrayList;

public class BallTrackingAlgorithm {

    public DImage trackImage(DImage img) throws Exception {
        short[][] imgBW = img.getBWPixelGrid();
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();


        // Do stuff with color channels here
        int[] colors = {100, 120, 180};

        int[][] centers = findAllCenters(imgBW, colors);
        ArrayList<int[]> pointsOnTheOutline = findSquaresForEachBall(centers);

        for (int i = 0; i < pointsOnTheOutline.size(); i++) {
            int[] point = pointsOnTheOutline.get(i);
            red[ point[0] ][ point[1] ] = 255;
        }

        img.setColorChannels(red, green, blue);
        return img;
    }

    public ArrayList<int[]> findSquaresForEachBall(int[][] centers) {
        ArrayList<int[]> points = new ArrayList<>();
        for (int[] center : centers) {
            ArrayList<int[]> p = findSquare(center[2], center); //center[2] is the radius.

            //Same as running for loop and adding all the elements in p to points.
            points.addAll(p);
        }

        return points;
    }
    //TODO Fix this method because it returns points in an unusable order. Return the points in the order (x, y).
    public ArrayList<int[]> findSquare(int radius, int[] center){
        ArrayList<int[]> arrayOfPointsOnSquare = new ArrayList<>();

        int xTranslation = center[0];
        int yTranslation = center[1];

        for (int theta = 0; theta < 360; theta++) {
            int PixelX = (int) (Math.cos(theta) * radius) + xTranslation;
            int PixelY = (int) (Math.sin(theta) * radius) + yTranslation;

            arrayOfPointsOnSquare.add(new int[]{PixelX, PixelY});
        }



        return arrayOfPointsOnSquare;
    }


    public int[][] findAllCenters (short[][] img, int[] allColors) throws Exception {
        int[][] centers = new int[allColors.length][2];
        for (int color = 0; color < allColors.length; color++) {
            centers[color] = findCenterForOneColor(img, color);
        }

        return centers;
    }

    public int[] findCenterForOneColor(short[][] img, int color) throws Exception {
        int[] centers = null;

        int topPixel = -1;
        int bottomPixel = -1;
        int radiusOfCircle = -1;
        int rowOfCenter = -1;
        int colOfCenter = -1;

        for (int r = 0; r < img.length; r++) {
            for (int c = 0; c < img[r].length; c++) {
                //Find the topmost pixel of the ball by looping over rows.
                //Find loop over till you find another white pixel that doesn't have any white pixels adjacent to it.
                //Find the pixel in between the points to determine the center
                if (checkIfEndPixel(img, r, color) && topPixel == -1) {
                    topPixel = img[r][c];
                }
                if (checkIfEndPixel(img, r, color) && topPixel != -1) {
                    bottomPixel = img[r][c];
                }

                if (topPixel != -1 && bottomPixel != -1) {
                    rowOfCenter = r/2;
                    colOfCenter = c;
                    radiusOfCircle = rowOfCenter - r;
                    centers = new int[]{rowOfCenter, colOfCenter, radiusOfCircle};
                }
            }
        }

        if (centers == null) throw new Exception("findCenterForOneColor(): Centers are null for some reason.");

        return centers;
    }



    public boolean checkIfEndPixel(short[][] img, int row, int color){
        int countOfColoredPixels = 0;
        int thresholdOfColoredPixelsPerRow = 10;
        for (int c = 0; c < img[0].length; c++) {
            if (img[row][c] == color) countOfColoredPixels += 1;
            if (countOfColoredPixels > thresholdOfColoredPixelsPerRow) return false;
        }
        return true;
    }


}
