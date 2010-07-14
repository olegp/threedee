package com.ionsquare.threedee;

import java.io.*;

public class Visual
{  
  public String name;
  public Node topmost;
  
  public boolean render(Renderer renderer, Mat4 parent2world)
  {
    if(topmost != null) return topmost.render(renderer, parent2world);
    return false;
  }
  
  public void generateNormals()
  {
    if(topmost != null) topmost.generateNormals();
  }
  
  public String toString() { return name; }
  
}
