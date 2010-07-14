package com.ionsquare.threedee;

import java.util.*;

public class Mesh
{
  String name;
  Vector groups = new Vector();
  
  public int getVertexCount()
  {
    int count = 0;
    for(int i = 0; i < groups.size(); i ++)
      count += ((MaterialGroup)groups.elementAt(i)).getVertexCount();
    return count; 
  }
  
  public int getTriangleCount()
  {
    int count = 0;
    for(int i = 0; i < groups.size(); i ++)
      count += ((MaterialGroup)groups.elementAt(i)).getTriangleCount();
    return count; 
   }
  
  
  public boolean render(Renderer renderer)
  {
    for(int i = 0; i < groups.size(); i ++)
      ((MaterialGroup)groups.elementAt(i)).render(renderer);
    
    return true;
  }
  
  public void generateNormals()
  {
    for(int i = 0; i < groups.size(); i ++)
      ((MaterialGroup)groups.elementAt(i)).generateNormals();
  }
}
