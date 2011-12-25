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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * The JPanel replacement containing the graphic board.
 * 
 * @author Matúš Sulír
 */
public class GraphicPanel extends JPanel implements GraphicComponent {
    private GraphicBoard board;
    
    /**
     * The default constructor.
     */
    public GraphicPanel() {
        board = new GraphicBoard(this);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                board.mouseClicked(e);
            }
        });
        
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                board.keyEvent(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                board.keyEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                board.keyEvent(e);
            }
        });
    }
    
    /**
     * Called by Swing when the component needs to be repainted.
     * @param g the graphical object which can be painted on
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        board.paint((Graphics2D)g);
    }
    
    /**
     * Returns the associated graphic board.
     * @return the board
     */
    public GraphicBoard getBoard() {
        return board;
    }
}
