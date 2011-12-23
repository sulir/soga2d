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

import java.awt.Rectangle;
import soga2d.events.CollisionListener;

/**
 * The collision detector can find out whether two object are in a graphical
 * collision and notify the listener when a collision between two particular
 * objects occurs.
 * @author Matúš Sulír
 */
public class CollisionDetector {
    private GraphicObject first;
    private GraphicObject second;
    private CollisionListener listener;
    
    /**
     * Constructs a collision detector for two graphical objects.
     * @param first the first object
     * @param second the second object
     */
    public CollisionDetector(GraphicObject first, GraphicObject second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Finds out whether this object collides with the specified object.
     * 
     * Two objects are in a collision if at least one non-transparent pixel of
     * one object has the same coordinates on the board as at least one
     * non-transparent pixel of the second object.
     * @param object the second object
     * @return true if the objecs collide, false otherwise
     */
    public boolean objectsCollide() {
        Rectangle intersection = first.getRectangle().intersection(second.getRectangle());
        
        if (!intersection.isEmpty()) {
            for (int x = (int) intersection.getX(); x < intersection.getMaxX(); x++) {
                for (int y = (int) intersection.getY(); y < intersection.getMaxY(); y++) {
                    int firstPixel = getARGBAtBoardCoordinate(first, x, y);
                    int secondPixel = getARGBAtBoardCoordinate(second, x, y);
                    
                    if (!isPixelTransparent(firstPixel) && !isPixelTransparent(secondPixel))
                        return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Registers a collision listener (can be only one).
     * 
     * The listener is notified after each change of one of the objects if
     * the objects are currently in a collision. Only moving the objects
     * can fire this event, other changes are ignored so far.
     * @param listener 
     */
    public void setListener(CollisionListener listener) {
        this.listener = listener;
        
        first.addCollisionDetector(this);
        second.addCollisionDetector(this);
    }
    
    /**
     * This method is used by a graphic object to notify this detector about
     * any change which could cause the collision.
     */
    void objectChanged() {
        if (objectsCollide())
            listener.onCollision();
    }
    
    /**
     * Returns an integer representation of a pixel at given location in ARGB
     * color model.
     * 
     * Also translates the given board coordinates into the object coordinates as needed.
     * @param object the object
     * @param x the x coordinate on the graphic board
     * @param y the y coordinate on the graphic board
     * @return the ARGB representation of the pixel
     */
    private int getARGBAtBoardCoordinate(GraphicObject object, int x, int y) {
        return object.getImage().getRGB(x - object.getX(), y - object.getY());
    }
    
    /**
     * Finds out whether the pixel is 100% transparent.
     * @param pixel the ARGB pixel
     * @return true if the pixel is transparent, false otherwise
     */
    private boolean isPixelTransparent(int pixel) {
        return ((pixel >> 24) & 0xFF) == 0;
    }
}
