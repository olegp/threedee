package com.ionsquare.threedee;

public class Color
{
	public float r, g, b, a;
  
  public Color()
  {
  }
	
  public Color(float x, float y, float z) 
  {
		r = x; g = y; b = z; a = 1;	
	}
	
	public Color(float x, float y, float z, float w) 
  {
		r = x; g = y; b = z; a = 1;	a = w;
	}
	
	public Color(Color c) 
  {
		r = c.r; g = c.g; b = c.b; a = c.a;	
	}
	
	public int getRGB() 
  { 
		return ((int)255 << 24) | ((int)(r * 255) << 16) | 
					 ((int)(g * 255) << 8) | (int)(b * 255);
	}
}
