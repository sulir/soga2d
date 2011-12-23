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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import soga2d.events.KeyPressListener;
import soga2d.events.MouseClickListener;

/**
 * The graphic object is a rectangular item located on a graphic board, e.g. an
 * image or a text.
 * 
 * When subclassing this class:
 * <ul>
 * <li>Set the <code>x</code> and <code>y</code> properties if needed.</li>
 * <li>Draw the appropriate content into the <code>image</code> property.</li>
 * <li>Call the <code>repaint()</code> method after the each position, size or
 * content change.</li>
 * </ul>
 * 
 * <em>Note:</em> Calling <code>repaint()</code> in a constructor is unnecessary
 * because there is no board associated with the object yet.
 * @author Matúš Sulír
 */
public abstract class GraphicObject {
    /**
     * The internal bitmap image.
     * 
     * Contains also the width and height of this graphical object.
     */
    protected BufferedImage image;
    
    /**
     * The x position on the board.
     */
    protected int x;
    
    /**
     * The y position on the board.
     */
    protected int y;
    
    private GraphicBoard board;
    private MouseClickListener mouseClickListener;
    private KeyPressListener keyPressListener;
    private List<CollisionDetector> collisionDetectors = new ArrayList<CollisionDetector>();
    
    /**
     * Constructs an object with x = 0, y = 0, width = 1 and height = 1.
     */
    public GraphicObject() {
        this(0, 0, 1, 1);
    }
    
    /**
     * Contructs an object with a custom position and size.
     * @param x the x coordinate (the leftmost is 0)
     * @param y the y coordinate (the topmost is 0)
     * @param width the width
     * @param height the height
     */
    public GraphicObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        
        createImage(width, height);
    }
    
    /**
     * Initializes the internal image to an empty one (containing only
     * transparent pixels).
     * @param width the image width
     * @param height the image height
     */
    protected final void createImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    /**
     * Returns the Graphics2D object from the internal image.
     * 
     * This is a utility method used when subclasses want to draw on the image.
     * @return the Graphics2D object
     */
    protected Graphics2D getGraphics() {
        return (Graphics2D) image.getGraphics();
    }
    
    /**
     * Returns the current content of the graphic object as an image.
     * 
     * This is used to accomplish double buffering.
     */
    BufferedImage getImage() {
        return image;
    }

    /**
     * Assigns the object to the concrete board.
     * 
     * This causes the object to be drawn on the board whenever needed.
     * @param board 
     */
    void assignBoard(GraphicBoard board) {
        this.board = board;
        repaint();
    }
    
    /**
     * Returns the current object's width.
     * @return the width
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Returns the current object's height.
     * @return the height
     */
    public int getHeight() {
        return image.getHeight();
    }
    
    /**
     * Returns an x coordinate of the object located on a graphic board.
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns an y coordinate of the object located on a graphic board.
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Returns a <code>Rectangle</code> representaion of the object's position
     * and size.
     * @return the rectangle
     */
    public Rectangle getRectangle() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
    
    /**
     * Moves the object to a new location on its associated board.
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        
        repaint();
        notifyCollisionDetectors();
    }
    
    /**
     * Moves an object on its associated board.
     * @param deltaX the x coordinate change (can be either negative or positive)
     * @param deltaY the y coordinate change (can be either negative or positive)
     */
    public void moveBy(int deltaX, int deltaY) {
        moveTo(x += deltaX, y += deltaY);
    }
    
    /**
     * Moves this object in front of an another object along the Z-axis.
     * @param what the object which will be behind the first one (this objet will not be moved)
     */
    public void moveInFrontOf(GraphicObject what) {
        if (board != null)
            board.moveInFrontOf(this, what);
        
        repaint();
    }
    
    /**
     * Moves this object behind all other objects along the Z-axis.
     */
    public void sendToBackground() {
        if (board != null)
            board.sendToBackground(this);
        
        repaint();
    }
    
    /**
     * Moves this object in front of all other objecs along the Z-axis.
     */
    public void bringToForeground() {
        if (board != null)
            board.bringToForeground(this);
       
        repaint();
    }
    
    /**
     * Registers a mouse click listener (can be only one).
     * @param listener the object which will react on mouse-clicks
     */
    public void setMouseClickListener(MouseClickListener listener) {
        mouseClickListener = listener;
    }
    
    /**
     * Registers a key press listener (can be only one).
     * @param listener the object which will react on key-presses
     */
    public void setKeyPressListener(KeyPressListener listener) {
        keyPressListener = listener;
    }
    
    /**
     * Finds out whether this objects graphically colides with the specified object.
     * @param object the second object
     * @return true if the objects collide, false otherwise
     * @see CollisionDetector
     */
    public boolean collidesWith(GraphicObject object) {
        return new CollisionDetector(this, object).objectsCollide();
    }
    
    /**
     * Adds a collision detector to notify when the object changes (e.g. moves).
     * @param detector the collision detector
     */
    void addCollisionDetector(CollisionDetector detector) {
        collisionDetectors.add(detector);
    }
    
    /**
     * Nofifies all associated collision detectors after the object changes.
     */
    private void notifyCollisionDetectors() {
        for (CollisionDetector detector : collisionDetectors)
            detector.objectChanged();
    }
    
    /**
     * Called when the user clicked on an area where this component is located.
     */
    void mouseClicked() {
        if (mouseClickListener != null)
            mouseClickListener.onClick();
    }
    
    /**
     * Called when the user pressed a key while the component bound to the
     * board had focus.
     * @param event the key event object
     */
    void keyPressed(KeyEvent event) {
        if (keyPressListener != null)
            keyPressListener.onKeyPress(event);
    }
    
    /**
     * Repaints this object on a board and other (or all) objects if necessary
     * (depends on an implementation).
     */
    protected void repaint() {
        if (board != null)
            board.repaintAll();
    }
}
