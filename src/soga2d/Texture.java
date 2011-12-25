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
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The image repeated horizontally and vertically to fill the given area.
 * @author Matúš Sulír
 */
public class Texture extends GraphicObject {
    /**
     * Constructs a texture from an image file, starting from the point [0, 0].
     * @param fileName the image file name
     * @param width the width of the area to fill
     * @param height the height of the area to fill
     * @throws IOException if the image can not be loaded
     */
    public Texture(String fileName, int width, int height) throws IOException {
        this(fileName, 0, 0, width, height);
    }
    
    /**
     * Constructs a texture from an image file.
     * @param fileName the image file name
     * @param x the starting point x coordinate on the board
     * @param y the starting point y coordinate on the board
     * @param width the width of the area to fill
     * @param height the height of the area to fill
     * @throws IOException if the image can not be loaded
     */
    public Texture(String fileName, int x, int y, int width, int height) throws IOException {
        super(x, y, width, height);
        
        BufferedImage loadedImage = ImageIO.read(GraphicObject.class.getClassLoader().getResource(fileName));
        TexturePaint texturePaint = new TexturePaint(loadedImage, new Rectangle(loadedImage.getWidth(null), loadedImage.getHeight(null)));
        
        Graphics2D g = image.createGraphics();
        g.setPaint(texturePaint);
        g.fill(new Rectangle(width, height));
    }
}
