package com.ionsquare.threedee;

import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.awt.event.*;

import java.net.*;
import java.io.*;

public class Threedee extends Applet implements Runnable, KeyListener
{
  float width, height;

  FrameBuffer framebuffer = new FrameBuffer();
  Rasterizer rasterizer = new Rasterizer();
  Renderer renderer = new Renderer();

  // ImageObserver
  public boolean imageUpdate(Image image, int flags, int x, int y, int w, int h)
  {
  /*
    if(flags & SOMEBITS) != 0) {
      // partial image

    } else if ((flags & ABORT) != 0) {
      // file not found
    }

    return (flags & (ALLBITS | ABORT)) == 0;
    */
    return false;
  }

  public void init()
  {
    addKeyListener(this);
    enableEvents(AWTEvent.KEY_EVENT_MASK);

    setBackground(new java.awt.Color(0, 0, 0));

    width = getSize().width;
    height = getSize().height;
    framebuffer.create(this, getSize().width, getSize().height);
    // try { buffer = getImage(getCodeBase(), "test.jpg"); } catch (Exception e) { }
  }

  Visual visual = null;
  float a = 0, b = 0, c = 0;

  boolean moveUp = false, moveDown = false;
  float z = 75;


  public void keyPressed(KeyEvent e)
  {
    if(e.getKeyCode() == e.VK_UP)
      moveUp = true;
    if(e.getKeyCode() == e.VK_DOWN)
      moveDown = true;
  }

  public void keyReleased(KeyEvent e)
  {
    if(e.getKeyCode() == e.VK_UP)
      moveUp = false;
    if(e.getKeyCode() == e.VK_DOWN)
      moveDown = false;
  }

  public void keyTyped(KeyEvent e)
  {
  }

  void render()
  {
    if(moveUp) z += 1;
    if(moveDown) z -= 1;

    a += 0.02f;
    b += 0.03f;
    c += 0.04f;

    Mat4 r = Mat4.rot(a, b, c);
    r.setPosition(new Vect3(0, 0, z));

    if(visual != null)
      visual.render(renderer, r);
  }

  Thread motor = null;
  boolean isRunning = true;

  public void start()
  {
    if(motor == null){
      motor = new Thread(this);
      motor.start();
    }
  }

  public void stop()
  {
    isRunning = false;
  }

  public void paint(Graphics g)
  {
  }

  InputStream getInputStream(String name)
  {
    InputStream in = null;
    try {
      in = new URL(getCodeBase(), name).openStream();
    } catch(Exception e) { }

    if(in == null){
       try {
         in = new FileInputStream(name);
       } catch(Exception f) { }
    }

    return in;
  }

  public void run()
  {
    Graphics graphics = getGraphics();
    renderer.setViewport(width, height);
    renderer.setProjection(Mat4.perspective(3.1415f/2.f, 1, 20, (float)width/(float)height));

    renderer.setFrameBuffer(framebuffer);
    renderer.setRasterizer(rasterizer);

    renderer.setMaterial(new Material(new Color(0.5f, 0.5f, 0.5f),
                                      new Color(0.5f, 0.5f, 0.5f),
                                      new Color(1, 1, 1), 50));

    renderer.addLight(new Light(new Vect3(0.5f, 0.5f, -0.2f),
                      new Color(0.2f, 0.2f, 0.2f),
                      new Color(0.5f, 1, 1),
                      new Color(1, 1, 1)));


    renderer.addLight(new Light(new Vect3(1, -1, -1),
                      new Color(0, 0, 0),
                      new Color(0, 0, 1),
                      new Color(1, 1, 1)));

    final String visualname = "test.3dd";

    visual = VisualLoader.load(getInputStream(visualname));
    if(visual != null) {
     visual.generateNormals();

    } else
      System.out.println("Failed to load visual " + visualname);

    long l = System.currentTimeMillis();
    int frames = 0;
    String info = "";

    graphics.setColor(new java.awt.Color(255, 255, 255));

    while(isRunning == true) {

      while(System.currentTimeMillis() - l > 1000) {
        l += 1000;
        info = Integer.toString(frames);
        frames = 0;
      }

      frames ++;

      framebuffer.clearPBuffer(0);
      framebuffer.clearZBuffer(Float.POSITIVE_INFINITY);

      render();

      framebuffer.update(graphics);
      graphics.drawString(info, 10, 10);
    }
  }

  static class MyAdapter extends WindowAdapter
  {
    public void windowClosing(WindowEvent e) {
      System.exit(0);
    }
  }

  public static void main(String args[]) {
    Frame w = new Frame("Threedee");
    Threedee threedee = new Threedee();

    w.add("Center", threedee);
    w.pack();
    w.setSize(640, 400);
    w.setResizable(false);
    w.show();

    threedee.init();
    threedee.start();

    w.addWindowListener(new MyAdapter());
  }
}
