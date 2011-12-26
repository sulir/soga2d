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
import java.awt.Graphics2D;
import soga2d.GraphicObject;

/**
 *
 * @author Matúš Sulír
 */
public class Rectangle extends GraphicObject {
    private int width;
    private int height;
    private Color outline;
    private Color fill;
    
    public Rectangle(int x, int y, int width, int height, Color outline, Color fill) {
        super(x, y);
        
        this.width = width;
        this.height = height;
        this.outline = outline;
        this.fill = fill;
        
        draw();
    }
    
    public void setWidth(int width) {
        this.width = width;
        draw();
    }
    
    public void setHeight(int height) {
        this.height = height;
        draw();
    }
    
    public void setOutlineColor(Color outline) {
        this.outline = outline;
        draw();
    }
    
    public void setFill(Color fill) {
        this.fill = fill;
        draw();
    }
    
    private void draw() {
        beforeChange();
        
        createImage(width, height);
        Graphics2D g = image.createGraphics();
        
        g.setColor(fill);
        g.fillRect(x, y, getWidth(), getHeight());
        g.setColor(outline);
        g.drawRect(x, y, getWidth(), getHeight());
        
        afterChange();
    }
}
