/*
 * Tomas Popela, xpopel11, 2012
 * VIPS - Visual Internet Page Segmentation
 * Module - Vips.java
 */

package org.fit.vips;

import java.io.*;
import java.net.*;
import org.w3c.dom.*;
                                                                                                                                      
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.demo.DOMSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.fit.cssbox.layout.Viewport;
                                                                                                                                      
public class Vips 
{
	private static URL _url = null;
	private static DOMAnalyzer _domAnalyzer = null;
    private static javax.swing.JPanel _browserCanvas = null;
    private static Viewport _viewport = null;
    
	/*
	 * With help of CssBox gets the DOM tree of page 
	 */
	private static void getDomTree(InputStream urlStream) {
	
        DOMSource parser = new DOMSource(urlStream);
        try {
        	Document domTree = parser.parse();
	        _domAnalyzer = new DOMAnalyzer(domTree, _url);
	        _domAnalyzer.attributesToStyles();
	        _domAnalyzer.addStyleSheet(null, CSSNorm.stdStyleSheet());
	        _domAnalyzer.addStyleSheet(null, CSSNorm.userStyleSheet());
	        _domAnalyzer.getStyleSheets();
        } catch (Exception e) {
        	System.out.print(e.getMessage());
        }
	}
	
	private static void getViewport() {
		
        _browserCanvas = new BrowserCanvas(_domAnalyzer.getRoot(), _domAnalyzer, new java.awt.Dimension(1000, 600), _url);
        _viewport = ((BrowserCanvas) _browserCanvas).getViewport();
        
	}

	/*
	 * Main entrance to VIPS
	 */
    public static void main(String args[]) 
    {
    	// we've just one argument - web address of page
    	if (args.length != 1)
    		System.exit(0);
    	
        try {
    		_url = new URL(args[0]);
            URLConnection urlConnection = _url.openConnection();
            InputStream urlStream = urlConnection.getInputStream();
            
            getDomTree(urlStream);
            getViewport();
            
            VipsParser vipsParser = new VipsParser(_domAnalyzer, _viewport);
            
            vipsParser.parse();
            
            
            urlStream.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
}
