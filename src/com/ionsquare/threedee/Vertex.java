package com.ionsquare.threedee;

public class Vertex
{
  public Vect3 position;
  public Vect3 normal;
  public Color color;
  public Vect2 texcoords;
  
  public Vertex()
  {
  }
    
  public Vertex(Vect3 position, Vect3 normal)
  {
    this.position = position;
    this.normal = normal;
  }
  
  public Vertex(Vect3 position, Vect2 texcoords)
  {
    this.position = position;
    this.texcoords = texcoords;
  }
  
  public Vertex(Vect3 position)
  {
    this.position = position;
  }
    
}
