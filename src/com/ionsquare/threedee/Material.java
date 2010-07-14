package com.ionsquare.threedee;

public class Material
{
	Color ambient, diffuse, specular;
	float power;
	
	public Material(Color a, Color d, Color s, float p) {
		ambient = new Color(a);
		diffuse = new Color(d);
		specular = new Color(s);
		power = p;
	}
  
  public Material()
  {
    ambient = new Color(1, 1, 1, 1);
    diffuse = new Color(1, 1, 1, 1);
    specular = new Color(1, 1, 1, 1);
    power = 20;
  }
}