package com.ionsquare.threedee;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * A class representing the frame buffer.
 * <p>
 * The frame buffer encapsulates the pixel data. This includes both the color
 * and a depth (z-buffer) values. The class allows direct access to pixel data
 * as well as functions for processing the data indirectly.<br>
 * Usually the data is modified either via the indirect post processing
 * functions (such as Antialias) or via the Rasterizer which handles the drawing
 * of polygon data to the frame buffer.
 * </p>
 *
 * @author Oleg Podsechin
 * @version 1.0
 * @see com.ionsquare.threedee.Rasterizer
 */
public class FrameBuffer
{
  public int width, height, length;

  public int pbuffer[] = null;
  public float zbuffer[] = null;

  ColorModel colormodel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);

  MemoryImageSource source;
  Image image;

  void postProcess()
  {
    // blur();
  }

  public void update(Graphics graphics)
  {
    if(image != null && source != null) {
      postProcess();
      //source.newPixels(0, 0, width, height, false);
      source.newPixels();
      graphics.drawImage(image, 0, 0, null);
    }
  }

  public void create(Applet applet, int width, int height)
  {
    this.width = width;
    this.height = height;

    length = width * height;

    pbuffer = new int[length];
    zbuffer = new float[length];
    source = new MemoryImageSource(width, height, colormodel, pbuffer, 0, width);
    source.setAnimated(true);

    if(applet == null)
      image = Toolkit.getDefaultToolkit().createImage(source);
    else
      image = applet.createImage(source);
  }

  void clearPBuffer(int value)
  {
    // Arrays.fill(pbuffer, value);
    for(int i = 0; i < length; i ++)
      pbuffer[i] = value;
  }

  void clearZBuffer(float value)
  {
    for(int i = 0; i < length; i ++)
      zbuffer[i] = value;
  }

  void blur()
  {
    // B[i][j] = ((A[i][j]*2+A[i][j-1]+A[i][j+1])*4 + A[i][j-1]+A[i][j+1]-A[i][j-3]-A[i][j+3])/16

  }
}
