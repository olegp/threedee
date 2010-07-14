package com.ionsquare.threedee;

public class Vect2
{
  public float x, y;
	
	public Vect2() 
  {
    x = y = 0;
  }
  
	public Vect2(float a, float b) { 
		x = a;	y = b;
	}
	
	public Vect2(float d[]) { 
		x = d[0];	y = d[1];
	}
	
	public Vect2(Vect2 v) { 
		x = v.x; y = v.y;
	}
	
	public Vect2 sub(Vect2 v) {
		return new Vect2(x - v.x, y - v.y);
	}
	
	public Vect2 add(Vect2 v) {
		return new Vect2(x + v.x, y + v.y);
	}
	
	public Vect2 minus() {
		return new Vect2(-x, -y);
	}
	
	public float mag2() {
		return x*x + y*y;
	}
	
	public float mag() {
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public Vect2 mult(float s) {
		return new Vect2(x*s, y*s);
	}
	
	public Vect2 div(float s) {
		return new Vect2(x/s, y/s);
	}
	
	public Vect2 normalize() {
		return div(mag());	
	}
}
