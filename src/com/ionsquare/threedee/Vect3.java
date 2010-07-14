package com.ionsquare.threedee;

public class Vect3
{
	public float x, y, z;
	
	public Vect3() 
  {
    x = y = z = 0;
  }
  
	public Vect3(float a, float b, float c) { 
		x = a;	y = b;	z = c;
	}
	
	public Vect3(float d[]) { 
		x = d[0];	y = d[1];	z = d[2];
	}
	
	public Vect3(Vect3 v) { 
		x = v.x; y = v.y;	z = v.z;
	}
	
	public Vect3 sub(Vect3 v) {
		return new Vect3(x - v.x, y - v.y, z - v.z);
	}
	
	public Vect3 plus(Vect3 v) {
		return new Vect3(x + v.x, y + v.y, z + v.z);
	}
  
  public void add(Vect3 v) {
   x += v.x;
   y += v.y;
   z += v.z;
  }
	
	public Vect3 minus() {
		return new Vect3(-x, -y, z);
	}
	
	public float dot(Vect3 v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public Vect3 cross(Vect3 v) {
		return new Vect3(x * v.z - z * v.y,
											z * v.x - x * v.z,
											x * v.y - y * v.x );
	}
	
	public float mag2() {
		return x*x + y*y + z*z;
	}
	
	public float mag() {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vect3 mult(float s) {
		return new Vect3(x*s, y*s, z*s);
	}
	
	public Vect3 div(float s) {
		return new Vect3(x/s, y/s, z/s);
	}
	
	public Vect3 normalize() {
		return div(mag());	
	}
	
	public Vect3 mult(Mat4 m) {
		return new Vect3(	m._11*x + m._21*y + m._31*z + m._41,
												m._12*x + m._22*y + m._32*z + m._42,
												m._13*x + m._23*y + m._33*z + m._43);
	}
	
	public Vect3 mult3(Mat4 m) {
		return new Vect3(	m._11*x + m._21*y + m._31*z,
												m._12*x + m._22*y + m._32*z,
												m._13*x + m._23*y + m._33*z);
	}
  
}
