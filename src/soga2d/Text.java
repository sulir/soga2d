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
package soga2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The graphical object containing a textual string.
 * @author Matúš Sulír
 */
public class Text extends GraphicObject {
    String text;
    Font font = new Font("Arial", Font.PLAIN, 15);
    Color color = Color.BLACK;
    
    /**
     * Constructs and empty text.
     */
    public Text() {
        text = "";
        updateSize();
    }
    
    /**
     * Constructs an object containing the given text.
     * @param text the string
     */
    public Text(String text) {
        this.text = text;
        updateSize();
    }
    
    /**
     * Paints the text.
     * @param g the graphics to draw on
     */
    @Override
    public void paint(Graphics2D g) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, 0, height);
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
    }
    
    /**
     * Sets the font used when drawing the text.
     * @param font the new font
     */
    public void setFont(Font font) {
        this.font = font;
        updateSize();
    }
    
    /**
     * Sets the text foreground color.
     * @param color the new color
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Computes and updates the width and height of the graphic object
     * according to the actual text size.
     */
    private void updateSize() {
        Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        LineMetrics metrics = font.getLineMetrics(text, context);
        
        width = (int) bounds.getWidth();
        height = (int) metrics.getHeight();
    }
}
