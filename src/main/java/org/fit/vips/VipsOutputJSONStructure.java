package org.fit.vips;

import java.util.ArrayList;

public class VipsOutputJSONStructure {

    private String id;
    private int height;
    private int width;
    private ArrayList<int[][]> segmentations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ArrayList<int[][]> getSegmentations() {
        return segmentations;
    }

    public void setSegmentations(ArrayList<int[][]> segmentations) {
        this.segmentations = segmentations;
    }
}
