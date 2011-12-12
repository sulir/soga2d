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
import java.util.ArrayList;
import java.util.List;

/**
 * The container of all graphic objects.
 * 
 * @author Matúš Sulír
 */
public class GraphicBoard {
    private GraphicComponent component;
    private List<GraphicObject> items = new ArrayList<GraphicObject>();
    private boolean locked = false;
    
    public GraphicBoard(GraphicComponent component) {
        this.component = component;
    }
    
    void paint(Graphics2D g) {
        for (GraphicObject object : items) {
            object.paint((Graphics2D)g.create(object.getX(), object.getY(), object.getWidth(), object.getHeight()));
        }
    }
    
    public void addObject(GraphicObject object) {
        if (!items.contains(object)) {
            object.assignBoard(this);
            items.add(object);
        }
    }
    
    public void removeObject(GraphicObject object) {
        if (items.remove(object))
            object.assignBoard(null);
    }
    
    public void clear() {
        items.clear();
        repaintAll();
    }
    
    public void lock() {
        locked = true;
    }
    
    public void unlock() {
        locked = false;
        repaintAll();
    }
    
    void moveInFrontOf(GraphicObject object, GraphicObject inFrontOfWhat) {
        items.remove(object);
        int index = items.indexOf(inFrontOfWhat);
        
        if (index != -1)
            items.add(index, object);
    }
    
    void sendToBackground(GraphicObject object) {
        items.remove(object);
        items.add(0, object);
    }
    
    void bringToForeground(GraphicObject object) {
        items.remove(object);
        items.add(object);
    }
    
    void repaintAll() {
        if (!locked)
            component.repaintAll();
    }
}
