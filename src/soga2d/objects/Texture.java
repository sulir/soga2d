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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import soga2d.GraphicObject;

/**
 * The image repeated horizontally and vertically to fill the given area.
 * @author Matúš Sulír
 */
public class Texture extends GraphicObject {
    /**
     * Constructs an empty (transparent) texture.
     * @param width the width of the area to fill
     * @param height the height of the area to fill
     */
    public Texture(int width, int height) {
        super(0, 0, width, height);
    }
    
    /**
     * Constructs a texture from an image file in the classpath, starting from
     * the point [0, 0].
     * @param fileName the image file name
     * @param width the width of the area to fill
     * @param height the height of the area to fill
     * @throws IOException if the image can not be loaded
     */
    public Texture(String fileName, int width, int height) throws IOException {
        super(0, 0, width, height);
        
        fill(Picture.loadImageFromClasspath(fileName));
    }
    
    /**
     * Creates a texture from a file.
     * @param file the input file
     * @param width the width of the area to fill 
     * @param height the height of the area to fill
     * @throws IOException when the image can not be loaded
     */
    public Texture(File file, int width, int height) throws IOException {
        super(0, 0, width, height);
        
        fill(Picture.loadImageFromFile(file));
    }
    
    /**
     * Creates a texture from a <code>BufferedImage</code> object.
     * @param image the source image
     * @param width the width of the area to fill
     * @param height the height of the area to fill
     */
    public Texture(BufferedImage image, int width, int height) {
        super(0, 0, width, height);
        
        fill(image);
    }
    
    /**
     * Loads the texture from a file.
     * @param file the input file.
     * @throws IOException when the image could not be loaded
     */
    public void loadFromFile(File file) throws IOException {
        fill(Picture.loadImageFromFile(file));
    }
    
    /**
     * Fills the internal image with the specified texture image and does
     * the needed updates.
     * @param textureImage the texture image
     */
    private void fill(BufferedImage textureImage) {
        beforeChange();
        
        TexturePaint texturePaint = new TexturePaint(textureImage, new Rectangle(textureImage.getWidth(), textureImage.getHeight()));
        Graphics2D g = this.image.createGraphics();
        
        g.setPaint(texturePaint);
        g.fill(new Rectangle(getWidth(), getHeight()));
        
        afterChange();
    }
}
