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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import soga2d.GraphicObject;

/**
 * The image (bitmap) graphical object.
 * @author Matúš Sulír
 */
public class Picture extends GraphicObject {
    /**
     * Constructs an empty (transparent) 1x1 px picture at [0, 0].
     */
    public Picture() { }
    
    /**
     * Constructs a picture from a file located in the classpath.
     * 
     * The width and height is computed automatically. The initial x and y
     * coorditates are 0.
     * @param fileName the file to open
     * @throws IOException when the file could not be loaded
     */
    public Picture(String fileName) throws IOException {
        this(fileName, 0, 0);
    }
    
    /**
     * Constructs a picture from a file located in the classpath.
     * 
     * The width and height is computed automatically.
     * @param fileName the file to open
     * @param x the initial x coordinate on a board
     * @param y the initial y coordinate on a board
     * @throws IOException when the file could not be loaded
     */
    public Picture(String fileName, int x, int y) throws IOException {
        super(x, y);
        
        image = Picture.loadImageFromClasspath(fileName);
    }
    
    /**
     * Constructs a picture from a file.
     * 
     * The width and height is computed automatically. The initial x and y
     * coorditates are 0.
     * @param fileName the file to open
     * @throws IOException when the file could not be loaded
     */
    public Picture(File file) throws IOException {
        this(file, 0, 0);
    }
    
    /**
     * Constructs a picture from a file.
     * 
     * The width and height is computed automatically.
     * @param fileName the file to open
     * @param x the initial x coordinate on a board
     * @param y the initial y coordinate on a board
     * @throws IOException when the file could not be loaded
     */
    public Picture(File file, int x, int y) throws IOException {
        super(x, y);
        
        image = ImageIO.read(file);
    }
    
    /**
     * Loads the picture from a file and adjusts the size automatically.
     * @param file the input file
     * @throws IOException when the file could not be loaded
     */
    public void loadFromFile(File file) throws IOException {
        beforeChange();
        image = ImageIO.read(file);
        afterChange();
    }
    
    /**
     * Helper method - loads the image from the classpath, e.g. from this JAR file.
     * @param name the path including the file name
     * @return the loaded image
     * @throws IOException when the image could not be loaded
     */
    public static BufferedImage loadImageFromClasspath(String name) throws IOException {
        return ImageIO.read(GraphicObject.class.getClassLoader().getResource(name));
    }
}
