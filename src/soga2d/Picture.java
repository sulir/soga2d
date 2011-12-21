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

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The image (bitmap) graphical object.
 * @author Matúš Sulír
 */
public class Picture extends GraphicObject {
    private Image image;
    
    /**
     * Constructs a picture from a file.
     * 
     * The width and height is computed automatically. The initial x and y
     * coorditates are 0.
     * @param fileName the file to open
     * @throws IOException when the file could not be open
     */
    public Picture(String fileName) throws IOException {
        this(fileName, 0, 0);
    }
    
    /**
     * Constructs a picture from a file.
     * 
     * The width and height is computed automatically.
     * @param fileName the file to open
     * @param x the initial x coordinate on a board
     * @param y the initial y coordinate on a board
     * @throws IOException when the file could not be open
     */
    public Picture(String fileName, int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        
        image = ImageIO.read(GraphicObject.class.getClassLoader().getResource(fileName));
        
        width = image.getWidth(null);
        height = image.getHeight(null);
    }
    
    /**
     * Paints the image on a Graphics2D object.
     * @param g the graphical object
     */
    @Override
    public void paint(Graphics2D g) {
        if (image != null)
            g.drawImage(image, 0, 0, null);
    }
}
