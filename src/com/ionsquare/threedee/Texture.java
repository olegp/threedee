package com.ionsquare.threedee;

import java.awt.*;
import java.awt.image.*;

public class Texture
{
  public String name;
  public int data[] = null;
  public int width, height;

  public Texture(String name)
  {
    this.name = name;

    /*
    Image image = Toolkit.getDefaultToolkit().getImage(name);
    image = image.getScaledInstance(256, 256, Image.SCALE_SMOOTH);

    //width = image.getWidth();
    //height = image.getHeight();
    width = height = 256;

    data = new int[width * height];

    PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, data, 0, width);
    try {
      pixelgrabber.grabPixels();
    } catch(InterruptedException e) { }
    */
  }
}
