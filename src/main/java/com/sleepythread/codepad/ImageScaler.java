package com.sleepythread.codepad;

import java.util.*;
import java.util.function.Function;


/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */


/*
You are arranging images on the home page of a major website! You have a list of images in this format:

[
  new Image(100, 80, "kitty1.jpg"),
  new Image(10, 20, "kitty2.jpg")
]

Your job is to organize these images into spaced rows. Given a page width and row height, scale each image so that it is exactly as tall as the row, and maintains its aspect ratio.

Then add each image into an array of rows, such that the total row width does not exceed the given page width. If an image can't fit within a given row, it should be added to the next row instead. You won't need to re-order the images.

In ascii art (not to scale) this would look like:

Before:
------------------------
|                      |         -----------------------------
|        kitty1        |---------|                           |
|       (100x80)       ||kitty2 ||        kitty3             |
|                      ||(10x20)||        (150x60)           | <more kitty photos>
--------------------------------------------------------------

After:
--------------------------------------------
|   kitty1    ||kitty2 ||      kitty3      |      <--- row 1
|  (50x40)    ||(20x40)||     (100x40)     |
----------------------------------------------
|    kitty4      ||   kitty5       |  kitty6  |   <--- row 2
|   (80x40)      ||   (80x40)      |  (60x40) |
-----------------------------------------------


Your function should have the following signature:

  makeIntoRows(images, pageWidth, rowHeight)

See the following an example call and correct results for a page width of 220, and a row height of 40.

Input:

Image images[] = [
  new Image(100, 80, "kitty1.jpg"),
  new Image(10, 20, "kitty2.jpg"),
  new Image(150, 60, "kitty3.jpg"),
  new Image(10, 5, "kitty4.jpg"),
  new Image(40, 20, "kitty5.jpg"),
  new Image(60, 40, "kitty6.jpg")
]

Method Call:

makeIntoRows(images, 220, 40)

Output:

-------------------
width: 50, height: 40, src: kitty1.jpg
width: 20, height: 40, src: kitty2.jpg
width: 100, height: 40, src: kitty3.jpg
-------------------
width: 80, height: 40, src: kitty4.jpg
width: 80, height: 40, src: kitty5.jpg
width: 60, height: 40, src: kitty6.jpg


You can assume:
- Each image has positive integer height and widths.
- No image is wider than the given page width.

For a refresher on ratios: x1/y1 = x2/y2

y2 = x2/y1 * x2

- The ratio between two images is:
  resizedImageWidth/resizedImageHeight = origImageWidth/origImageHeight
- solving for resizedImageWidth:
  resizedImageWidth = origImageWidth/origImageHeight * resizedImageHeight
 */
class Solution {
    public static void main(String[] args) {
        List<Image> images = new ArrayList<Image>();

        images.add(new Image(100, 80, "kitty1.jpg"));
        images.add(new Image(10, 20, "kitty2.jpg"));
        images.add(new Image(150, 60, "kitty3.jpg"));
        images.add(new Image(10, 5, "kitty4.jpg"));
        images.add(new Image(40, 20, "kitty5.jpg"));
        images.add(new Image(60, 40, "kitty6.jpg"));

        List<List<Image>> rows = Solution.makeIntoRows(images, 220, 40);

        for (List<Image> row : rows) {
            System.out.println("-------------------");
            for (Image image : row) {
                System.out.println(image);
            }
        }
    }

    public static List<List<Image>> makeIntoRows(List<Image> images, int pageWidth, int rowHeight) {
        // Your solution goes here.
        // No images - nothing to do
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        var scaledList = images.stream()
                .map(new ImageScalerFunction(rowHeight))
                .toList();

        var rowMapper = new ImageRowMapperFunction(pageWidth);

        // Feel free to modify any existing code, it’s here to help not constrain you.
        return rowMapper.apply(scaledList);
    }
}

class ImageScalerFunction implements Function<Image, Image> {

    private final int height;

    public ImageScalerFunction(int height) {
        this.height = height;
    }

    @Override
    public Image apply(Image input) {
        var ratio = ((double)input.getWidth())/input.getHeight();
        var scaledWidth = (ratio * height);
        var scaledImage = new Image((int)scaledWidth, this.height, input.getSrc());
        return scaledImage;
    }

}

class ImageRowMapperFunction implements Function<List<Image>, List<List<Image>>> {

    private final int width;

    public ImageRowMapperFunction(int width) {
        this.width = width;
    }

    public List<List<Image>> apply(List<Image> scaledImages) {
        var rows = new ArrayList<List<Image>>();
        var row = new ImageRow(width);
        for(Image image : scaledImages) {
           if (!row.addToRow(image)) {
               rows.add(row.getImages());
               row.reset();
           }
        }
        if (!row.getImages().isEmpty()) {
            rows.add(row.getImages());
        }
        return rows;
    }
}

class ImageRow {
    private final int maxWidth;
    private int availableWidth;
    private List<Image> images = new ArrayList<>();

    public ImageRow(int maxWidth) {
        this.maxWidth = maxWidth;
        this.availableWidth = this.maxWidth;
    }

    public boolean addToRow(Image image) {
        if (image.getWidth() > maxWidth) {
            System.out.println("Image is too wide. Skipped: " + image);
        }
        if (availableWidth >= image.getWidth()) {
            images.add(image);
            availableWidth  -= image.getWidth();
            return true;
        }
        return false;
    }

    public List<Image> getImages() {
        return images;
    }

    public void reset() {
        images = new ArrayList<>();
        availableWidth = maxWidth;
    }
}

class Image {
    private int width, height;
    private String src;


    public Image (int width, int height, String src) {
        this.width = width;
        this.height = height;
        this.src = src;
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    public String getSrc () {
        return this.src;
    }

    public String toString () {
        return String.format("width: %d, height: %d, src: %s", getWidth(), getHeight(), getSrc());
    }

    public float getRatio() {
        return (float)this.width/(float)this.height;
    }
}



