package com.ionsquare.threedee;

public class Mat4
{
	public float	_11, _12, _13, _14,
								_21, _22, _23, _24,
								_31, _32, _33, _34,
								_41, _42, _43, _44;
	
	public Mat4() 
  {
  }
	
	public Mat4(	float i11, float i12, float i13, float i14,
								float i21, float i22, float i23, float i24,
								float i31, float i32, float i33, float i34,
								float i41, float i42, float i43, float i44) 
  {
		
		_11 = i11; _12 = i12; _13 = i13; _14 = i14;
		_21 = i21; _22 = i22; _23 = i23; _24 = i24;
		_31 = i31; _32 = i32; _33 = i33; _34 = i34;
		_41 = i41; _42 = i42; _43 = i43; _44 = i44;
	}
  
 	public Mat4(float data[]) 
  {
		_11 = data[0]; _12 = data[1]; _13 = data[2]; _14 = data[3];
		_21 = data[4]; _22 = data[5]; _23 = data[6]; _24 = data[7];
		_31 = data[8]; _32 = data[9]; _33 = data[10]; _34 = data[11];
		_41 = data[12]; _42 = data[13]; _43 = data[14]; _44 = data[15];
	}

  
  public Mat4(Vect3 a, Vect3 b, Vect3 c)
  {
    _11 = a.x; _12 = a.y; _13 = a.z; _14 = 0;
    _21 = b.x; _22 = b.y; _23 = b.z; _24 = 0;
    _31 = c.x; _32 = c.y; _33 = c.z; _34 = 0;
    _41 = 0; _42 = 0; _43 = 0; _44 = 1;    
  }
	
	public Mat4(Mat4 m) 
  {
		_11 = m._11; _12 = m._12; _13 = m._13; _14 = m._14;
		_21 = m._21; _22 = m._22; _23 = m._23; _24 = m._24;
		_31 = m._31; _32 = m._32; _33 = m._33; _34 = m._34;
		_41 = m._41; _42 = m._42; _43 = m._43; _44 = m._44;
	}
	
	public static Mat4 identity() 
  {
		return new Mat4(1, 0, 0, 0,
										0, 1, 0, 0,
										0, 0, 1, 0,
										0, 0, 0, 1);
	}
	
	public Mat4 transpose() 
  {
		return new Mat4(_11, _21, _31, _41,
											_12, _22, _32, _42,
											_13, _23, _33, _43,
											_14, _24, _34, _44 );
	}
	
	public Mat4 mult(Mat4 m) {
		return new Mat4(
			_11 * m._11 + _12 * m._21 + _13 * m._31 + _14 * m._41,
			_11 * m._12 + _12 * m._22 + _13 * m._32 + _14 * m._42,
			_11 * m._13 + _12 * m._23 + _13 * m._33 + _14 * m._43,
			_11 * m._14 + _12 * m._24 + _13 * m._34 + _14 * m._44,
			_21 * m._11 + _22 * m._21 + _23 * m._31 + _24 * m._41,
			_21 * m._12 + _22 * m._22 + _23 * m._32 + _24 * m._42,
			_21 * m._13 + _22 * m._23 + _23 * m._33 + _24 * m._43,
			_21 * m._14 + _22 * m._24 + _23 * m._34 + _24 * m._44,
			_31 * m._11 + _32 * m._21 + _33 * m._31 + _34 * m._41,
			_31 * m._12 + _32 * m._22 + _33 * m._32 + _34 * m._42,
			_31 * m._13 + _32 * m._23 + _33 * m._33 + _34 * m._43,
			_31 * m._14 + _32 * m._24 + _33 * m._34 + _34 * m._44,
			_41 * m._11 + _42 * m._21 + _43 * m._31 + _44 * m._41,
			_41 * m._12 + _42 * m._22 + _43 * m._32 + _44 * m._42,
			_41 * m._13 + _42 * m._23 + _43 * m._33 + _44 * m._43,
			_41 * m._14 + _42 * m._24 + _43 * m._34 + _44 * m._44 );
	}
	
	static public Mat4 rot(float theta, float phi, float gamma) 
  {
		float sint = (float)Math.sin(theta),	cost = (float)Math.cos(theta),
					sinp = (float)Math.sin(phi),		cosp = (float)Math.cos(phi),
					sing = (float)Math.sin(gamma),	cosg = (float)Math.cos(gamma);

		return new Mat4(
			cosp*cosg - sint*sinp*sing,
			-cost*sing,	
			sinp*cosg + sint*cosp*sing, 0,
			cosp*sing + sint*sinp*cosg,	
			cost*cosg, 
			sinp*sing - sint*cosp*cosg,	0,
			-cost*sinp,	
			sint, 
			cost*cosp, 0,
			0, 0, 0, 1
			);
	}
	
 	public void setXAxis(Vect3 v) 
  {
		_11 = v.x; _12 = v.y; _13 = v.z;
	}

 	public void setYAxis(Vect3 v) 
  {
		_21 = v.x; _22 = v.y; _23 = v.z;
	}

 	public void setZAxis(Vect3 v) 
  {
		_31 = v.x; _32 = v.y; _33 = v.z;
	}
  
	public void setPosition(Vect3 v) 
  {
		_41 = v.x; _42 = v.y; _43 = v.z;
	}
	
	public Vect3 getPosition() 
  {
		return new Vect3(_41, _42, _43);
	}
	
	public Mat4 getKernel() 
  {
		return new Mat4(_11, _12, _13, 0,
											_21, _22, _23, 0,
											_31, _32, _33, 0,
											0, 0, 0, 1);
		
	}
	
	public Mat4 inverse() 
  {
		Mat4 r = getKernel().transpose();
		r.setPosition(getPosition().mult(r));
		return r;
	}
	
	public Mat4 scale(float x, float y, float z) 
  {
		return new Mat4(x*_11, x*_12, x*_13, _14,
											y*_21, y*_22, y*_23, _24,
											z*_31, z*_32, z*_33, _34,
											_41, _42, _43, _44);
	}
	
	static public Mat4 frustum(float left, float right, 
															 float top, float bottom, 
															 float near, float far) 
  {
		float a = (right + left) / (right - left),
					b = (top + bottom) / (top - bottom),
					c = 1 / (far - near),
					d = - near / (far - near);
		
		return new Mat4(2 * near / (right - left), 0, 0, 0,
											0, 2 * near / (top - bottom), 0, 0,
											a, b, c, 1,
											0, 0, d, 0);
	
	}
	
	static public Mat4 perspective(float fov, float near, float far, float aspect) 
  {
		float top = (float)Math.tan(fov * 0.5) * near;
		return frustum(-aspect * top, aspect * top, top, -top, near, far);
	}
	
	static public Mat4 viewport(float width, float height) 
  {
		width /= 2; height /= 2;
		return new Mat4(	width, 0, 0, 0,
												0, -height, 0, 0,
												0, 0, 1, 0,
												width, height, 0, 1);
	}
	
		/*
		
		float planed = farplane - nearplane,
					tana = (float)Math.tan(.5f * fov), 
					w = aspect/tana, 
					h = 1.0f/tana,
					Q = farplane/planed;

		return new Mat4(	w, 0.f, 0.f, 0.f,
												0.f, h, 0.f, 0.f,
												0.f, 0.f, Q, 1.f,
												0.f, 0.f, -Q*nearplane, 0.f );
	}*/
}
