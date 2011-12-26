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
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import soga2d.GraphicObject;

/**
 * The animation is displayed as a static image until the start() method is
 * called. Then the frames are shown ciclically until the stop() method call.
 * Finally, the static image is displayed again.
 * @author Matúš Sulír
 */
public class Animation extends GraphicObject {

    private int interval;
    private BufferedImage staticImage;
    private BufferedImage[] frames;
    private int index = 0;
    private Timer timer;
    private boolean active = false;

    /**
     * Constructs an animation.
     *
     * @param interval the time between two frames, in milliseconds
     * @param staticImage the static image to show when the animation is stopped
     * @param images the file names of the animation images
     */
    public Animation(int interval, String staticImage, String... images) throws IOException {
        this.interval = interval;
        this.frames = new BufferedImage[images.length];
        this.staticImage = Picture.loadImage(staticImage);
        
        int i = 0;
        for (String fileName : images) {
            this.frames[i++] = Picture.loadImage(fileName);
        }

        showImage(this.staticImage);
    }

    /**
     * Starts the animation.
     */
    public void start() {
        if (!active) {
            active = true;
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    showNext();
                }
            }, 0, interval);
        }
    }

    /**
     * Stops the animation and displays the static image.
     */
    public void stop() {
        if (active) {
            active = false;
            timer.cancel();

            index = 0;
            showImage(staticImage);
        }
    }

    /**
     * Shows the next animation frame.
     */
    private void showNext() {
        index = (index + 1) % frames.length;
        showImage(frames[index]);
    }

    /**
     * Shows the specified image.
     * @param image the image to show
     */
    private void showImage(BufferedImage image) {
        beforeChange();
        this.image = image;
        afterChange();
    }
}
