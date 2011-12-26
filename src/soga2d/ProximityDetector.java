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

import java.awt.Point;
import soga2d.events.ProximityListener;

/**
 * The proximity detector can find out whether the distance between two object
 * is less then specified and notify a listener when such a condition occurs.
 * @author Matúš Sulír
 */
public class ProximityDetector implements Detector {

    /**
     * The type of the distance to measure.
     * 
     * Currently only one type is supported.
     */
    public enum DistanceType {
        /**
         * The distance will be measured from the center of the first
         * rectangle to the center of the second rectangle.
         */
        CENTER_TO_CENTER
    }
    
    private GraphicObject first;
    private GraphicObject second;
    private int distance;
    private DistanceType distanceType;
    private ProximityListener listener;
    private boolean wereNear = false;
    
    /**
     * Constructs the proximity detector.
     * @param first the first object
     * @param second the second object
     * @param distance the maximum distance (inclusive)
     * @param distanceType how to measure the distance
     */
    public ProximityDetector(GraphicObject first, GraphicObject second, int distance, DistanceType distanceType) {
        this.first = first;
        this.second = second;
        this.distance = distance;
        this.distanceType = distanceType;
    }
    
    /**
     * Finds out whether two objects are in proximity.
     * @return true if they are in proximity, false otherwise
     */
    public boolean objectsNear() {
        if (distanceType == DistanceType.CENTER_TO_CENTER) {
            Point firstCenter = getCenter(first);
            Point secondCenter = getCenter(second);
            int centerDistance = (int) firstCenter.distance(secondCenter);
            
            if (centerDistance <= distance)
                return true;
        }
        
        return false;
    }
    
    /**
     * Registers a proximity listener (can be only one).
     * 
     * The listener is notified once each time the objects come into proximity.
     * @param listener 
     */
    public void setListener(ProximityListener listener) {
        this.listener = listener;
        
        first.addDetector(this);
        second.addDetector(this);
    }
    
    /**
     * This method is used by a graphic object to notify this detector about
     * any relevant change.
     */
    @Override
    public void objectChanged() {
        if (listener != null) {
            boolean areNear = objectsNear();
            
            if (!wereNear && areNear)
                listener.onProximity();

            wereNear = areNear;
        }
    }
    
    /**
     * Returns the central point of the rectangle.
     * @param rectangle the rectangle
     * @return the central point
     */
    private Point getCenter(GraphicObject rectangle) {
        int x = rectangle.getX() + rectangle.getWidth() / 2;
        int y = rectangle.getY() + rectangle.getHeight() / 2;
        
        return new Point(x, y);
    }
}
