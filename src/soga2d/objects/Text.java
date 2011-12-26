/*
 * Soga2D
 *
 * Copyright 2011 Matúš Sulír.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package soga2d.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import soga2d.GraphicObject;

/**
 * The graphical object containing a textual string.
 * @author Matúš Sulír
 */
public class Text extends GraphicObject {
    String text;
    Font font;
    Color color;
    
    /**
     * Constructs and empty text.
     */
    public Text() {
        this("");
    }
    
    /**
     * Constructs an object containing the given text.
     * @param text the string
     */
    public Text(String text) {
        this(text, 0, 0);
    }
    
    /**
     * Constructs an object containing the given text, with a custom initial
     * position.
     * @param text the string
     * @param x the x coordinate of the top-left point
     * @param y the y coordinate of the top-left point
     */
    public Text(String text, int x, int y) {
        this(text, x, y, new Font("Arial", Font.PLAIN, 15), Color.BLACK);
    }
    
    /**
     * Constructs an object containing the given text, with a custom initial
     * position, font an color.
     * @param text the string
     * @param x the x coordinate of the top-left point
     * @param y the y coordinate of the top-left point
     * @param font the font to use
     * @param color the text foreground color
     */
    public Text(String text, int x, int y, Font font, Color color) {
        super(x, y);
        
        this.text = text;
        this.font = font;
        this.color = color;
        
        createImageToFit();
        updateImage();
    }
    
    /**
     * Returns the current text.
     * @return the text
     */
    public String getText() {
        return text;
    }
    
    /**
     * Sets a new text to display.
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
        
        updateImage();
    }
    
    /**
     * Sets the font used when drawing the text.
     * @param font the new font
     */
    public void setFont(Font font) {
        this.font = font;
        
        updateImage();
    }
    
    /**
     * Sets the text foreground color.
     * @param color the new color
     */
    public void setColor(Color color) {
        this.color = color;
        
        updateImage();
    }
    
    /**
     * Creates an appropriate image, draws the text on it and updates the
     * graphic object.
     */
    private void updateImage() {
        beforeChange();
        createImageToFit();
        
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(color);
        
        int lineHeigth = g.getFontMetrics().getHeight();
        int top = lineHeigth;
        
        for (String line : text.split("\n")) {
            g.drawString(line, 0, top);
            top += lineHeigth;
        }
        
        afterChange();
    }
    
    /**
     * Computes the width and height of the image necessary to display the text
     * and creates it.
     */
    private void createImageToFit() {
        Graphics2D test = new BufferedImage(1, 1, image.getType()).createGraphics();
        FontMetrics metrics = test.getFontMetrics(font);
        
        int lineHeight = metrics.getHeight();
        int totalWidth = 0;
        int totalHeight = 0;
        
        for (String line : text.split("\n")) {
            int lineWidth = metrics.stringWidth(line);
            
            totalWidth = Math.max(totalWidth, lineWidth);
            totalHeight += lineHeight;
        }
        
        createImage(totalWidth, totalHeight + metrics.getDescent());
    }
}
