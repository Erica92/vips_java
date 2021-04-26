package org.fit.vips;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fit.cssbox.layout.Viewport;
import org.w3c.dom.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class VipsOutputJSON {

    private Document doc = null;
    private boolean _escapeOutput = true;
    private int pDoC = 0;
    private int order = 1;
    private String filename = "VIPSResult";

    public VipsOutputJSON() {
    }

    public VipsOutputJSON(int pDoC) {
        this.pDoC = pDoC;
    }

    private void writeVisualJSONBlocks(ArrayList<int[][]> segmentations, VisualStructure visualStructure) {

        //TODO in this way the segmentation is flat --> next step: take back hierarchy of blocks
//        ArrayList<Point> polygon = new ArrayList<Point>();
        int[] p1 = new int[]{visualStructure.getX(), visualStructure.getY()};
        int[] p2 = new int[]{visualStructure.getX(), visualStructure.getY() + visualStructure.getHeight()};
        int[] p3 = new int[]{visualStructure.getX() + visualStructure.getWidth(), visualStructure.getY() + visualStructure.getHeight()};
        int[] p4 = new int[]{visualStructure.getX() + visualStructure.getWidth(), visualStructure.getY()};
        int[][] polygon = new int[][]{p1,p2,p3,p4};
//        polygon.add(new Point(visualStructure.getX(), visualStructure.getY()));
//        polygon.add(new Point(visualStructure.getX(), visualStructure.getY() + visualStructure.getHeight()));
//        polygon.add(new Point(visualStructure.getX() + visualStructure.getWidth(), visualStructure.getY() + visualStructure.getHeight()));
//        polygon.add(new Point(visualStructure.getX() + visualStructure.getWidth(), visualStructure.getY()));
//        ArrayList<ArrayList> multiPolygon = new ArrayList<>();
//        multiPolygon.add(polygon);
//        segmentations.add(multiPolygon);
        segmentations.add(polygon);

        if(this.pDoC >= visualStructure.getDoC()){
            for(VisualStructure child : visualStructure.getChildrenVisualStructures()){
                this.writeVisualJSONBlocks(segmentations, child);
            }
        }
    }

    public void writeJSON(VisualStructure visualStructure, Viewport pageViewport){
        ArrayList boxes = new ArrayList();
        VipsOutputJSONStructure output = new VipsOutputJSONStructure();

        output.setHeight(pageViewport.getHeight());
        output.setWidth(pageViewport.getWidth());


        if(this.pDoC >= visualStructure.getDoC()){
            for(VisualStructure child : visualStructure.getChildrenVisualStructures()){
                this.writeVisualJSONBlocks(boxes, child);
            }
        }

        output.setSegmentations(boxes);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
        String currentTms = sdf.format(new Date());

//        filename = filename + System.nanoTime() + ".json";
        filename = filename + "_" + currentTms + ".json";

        try {
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(output);

            if (_escapeOutput)
            {
                FileWriter fstream = new FileWriter(filename);
                fstream.write(result);
                fstream.close();
            }
            else
            {
                StringWriter writer = new StringWriter();
                //transformer.transform(source, new StreamResult(writer));
                //String result = writer.toString();

                result = result.replaceAll("&gt;", ">");
                result = result.replaceAll("&lt;", "<");
                result = result.replaceAll("&quot;", "\"");

                FileWriter fstream = new FileWriter(filename);
                fstream.write(result);
                fstream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables or disables output escaping
     * @param value
     */
    public void setEscapeOutput(boolean value)
    {
        _escapeOutput = value;
    }

    /**
     * Sets permitted degree of coherence pDoC
     * @param pDoC pDoC value
     */
    public void setPDoC(int pDoC)
    {
        if (pDoC <= 0 || pDoC> 11)
        {
            System.err.println("pDoC value must be between 1 and 11! Not " + pDoC + "!");
            return;
        }
        else
        {
            this.pDoC = pDoC;
        }
    }

    /**
     * Sets output filename
     * @param filename Filename
     */
    public void setOutputFileName(String filename)
    {
        if (!filename.equals(""))
        {
            this.filename = filename;
        }

    }

}
