package com.ionsquare.threedee;

public class Vect4
{
	public float x, y, z, w;
	
	public Vect4() 
  { 
    x = y = z = w = 0;
  }
  
	public Vect4(float a, float b, float c, float d) 
  {
		x = a; y = b; z = c; w = d;	
	}
	
	public Vect4(float d[]) 
  {
		x = d[0]; y = d[1]; z = d[2]; w = d[3];
	}
	
	public Vect4(Vect4 v) 
  {
		x = v.x; y = v.y; z = v.z; w = v.w;
	}
	
	public Vect4(Vect3 v) 
  {
		x = v.x; y = v.y; z = v.z; w = 1;
	}

  public void set(Vect3 v)
  {
		x = v.x; y = v.y; z = v.z; w = 1;
  }
  
	public Vect4 mult(Mat4 m) {
		return new Vect4(	m._11*x + m._21*y + m._31*z + m._41*w,
												m._12*x + m._22*y + m._32*z + m._42*w,
												m._13*x + m._23*y + m._33*z + m._43*w,
												m._14*x + m._24*y + m._34*z + m._44*w);
	}
}
