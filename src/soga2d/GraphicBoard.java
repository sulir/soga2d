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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import soga2d.events.KeyPressListener;

/**
 * The container of all graphic objects.
 * 
 * @author Matúš Sulír
 */
public class GraphicBoard {
    private GraphicComponent component;
    private List<GraphicObject> items = new ArrayList<GraphicObject>();
    private boolean locked = false;
    private KeyPressListener keyPressListener;
    
    /**
     * Constructs a graphic board bound to the GUI component.
     * @param component the component to bind
     */
    public GraphicBoard(GraphicComponent component) {
        this.component = component;
    }
    
    /**
     * Called when the bound component is being redrawn.
     * @param g the graphics which can be drawn on
     */
    void paint(Graphics2D g) {
        for (GraphicObject object : items) {
            g.drawImage(object.getImage(), object.getX(), object.getY(), null);
        }
    }
    
    /**
     * Adds an object to the board and draws it.
     * @param object the graphical object to be added
     */
    public void addObject(GraphicObject object) {
        if (!items.contains(object)) {
            object.assignBoard(this);
            items.add(object);
        }
    }
    
    /**
     * Removes the object from the board.
     * @param object the graphical object to be removed
     */
    public void removeObject(GraphicObject object) {
        if (items.remove(object))
            object.assignBoard(null);
    }
    
    /**
     * Removes all objects from the board and clears it.
     */
    public void clear() {
        items.clear();
        repaintAll();
    }
    
    /**
     * Causes the board not to be redrawn until unlock() is called.
     * 
     * This method can be used to speed up drawing of a large amount of objects
     * at once. Another use is to avoid poor user experience when the objects
     * are drawn one after another with a visible pause.
     */
    public void lock() {
        locked = true;
    }
    
    /**
     * Unlocks and repaints the board.
     * 
     * @see #lock()
     */
    public void unlock() {
        locked = false;
        repaintAll();
    }
    
    /**
     * Registers a key press listener (can be only one).
     * @param listener the listener
     */
    public void setKeyPressListener(KeyPressListener listener) {
        keyPressListener = listener;
    }
    
    /**
     * Moves the object in front of an another object along the Z-axis.
     * @param object the object to move
     * @param inFrontOfWhat the object which will be behind the first one (this objet will not be moved)
     */
    void moveInFrontOf(GraphicObject object, GraphicObject inFrontOfWhat) {
        items.remove(object);
        int index = items.indexOf(inFrontOfWhat);
        
        if (index != -1)
            items.add(index, object);
    }
    
    /**
     * Moves the object behind all other objects along the Z-axis.
     * @param object the object to move
     */
    void sendToBackground(GraphicObject object) {
        items.remove(object);
        items.add(0, object);
    }
    
    /**
     * Moves the object in front of all other objecs along the Z-axis.
     * @param object the object to move
     */
    void bringToForeground(GraphicObject object) {
        items.remove(object);
        items.add(object);
    }
    
    /**
     * Called by the bound component when a mouse click event occurred.
     * @param event the mouse event object
     */
    void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        
        for (GraphicObject object : items) {
            if (x >= object.getX() && x < object.getX() + object.getWidth()
                    && y >= object.getY() && y < object.getY() + object.getHeight()) {
                object.mouseClicked();
                break;
            }
        }
    }
    
    /**
     * Called by the bound component when a key-press event occurred
     * while the component had focus.
     * @param event the key event object
     */
    void keyPressed(KeyEvent event) {
        if (keyPressListener != null)
            keyPressListener.onKeyPress(event);
        
        for (GraphicObject object : items)
            object.keyPressed(event);
    }
    
    /**
     * Repaints the whole board.
     */
    void repaintAll() {
        if (!locked)
            component.repaintAll();
    }
}
