package com.ionsquare.threedee;

import java.util.*;
import java.io.*;

public class Node
{
  public final static int   NC_NONE       = 0,
                            NC_MESH       = 1,
                            NC_LIGHT      = 2,
                            NC_CAMERA     = 3,
                            NC_MODEL      = 4;
    
 	public String name;
	public Mat4 toparent = Mat4.identity();

  public int contentstype;
  public Object contents = null;

  public Vector childnodes = new Vector();
  
  public int getVertexCount()
  {
    int count = 0;
    if(contentstype == NC_MESH && contents != null)
      count += ((Mesh)contents).getVertexCount();
    
    for(int i = 0; i < childnodes.size(); i ++)
      count += ((Node)childnodes.elementAt(i)).getVertexCount();
    
    return count;   
  }
  
  public int getTriangleCount()
  {
    int count = 0;
    if(contentstype == NC_MESH && contents != null)
      count += ((Mesh)contents).getTriangleCount();
    
    for(int i = 0; i < childnodes.size(); i ++)
      count += ((Node)childnodes.elementAt(i)).getTriangleCount();
    
    return count;    
  }
  
  public boolean render(Renderer renderer, Mat4 parent2world)
  {
    Mat4 toworld = toparent.mult(parent2world);

    if(contentstype == NC_MESH && contents != null) {
      renderer.setModel(toworld);
      ((Mesh)contents).render(renderer);
    }
    
    for(int i = 0; i < childnodes.size(); i ++)
      ((Node)childnodes.elementAt(i)).render(renderer, toworld);
 
    return true;
  }
  
  public void generateNormals()
  {
    if(contentstype == NC_MESH && contents != null)
      ((Mesh)contents).generateNormals();
    
    for(int i = 0; i < childnodes.size(); i ++)
      ((Node)childnodes.elementAt(i)).generateNormals();
  }
}
