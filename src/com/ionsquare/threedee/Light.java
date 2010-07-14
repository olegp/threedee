package com.ionsquare.threedee;

public class Light
{
	Renderer renderer = null;
	
	Color ambient, diffuse, specular;
	Vect3 position;
	boolean isPoint = false;
	
	public Light(Vect3 p, Color a, Color d, Color s) {
		position = p.normalize();
		ambient = new Color(a);
		diffuse = new Color(d);
		specular = new Color(s);
	}
	
	public void setPosition(Vect3 p) {
		position = p.normalize();
	}
	
	public void setColor(Color a, Color d, Color s) {
		if(a != null) ambient = new Color(a);
		if(d != null) diffuse = new Color(d);
		if(s != null) specular = new Color(s);
	}
	
	
	public void remove() {
		if(renderer != null) 
			renderer.removeLight(this);
	}
}
