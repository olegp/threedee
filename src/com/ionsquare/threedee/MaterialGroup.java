package com.ionsquare.threedee;

public class MaterialGroup
{
  public Material material = new Material();

  public String texturename, reflmapname, bumpmapname;  
  
  public final static int PT_POINTLIST      = 1, 
                          PT_LINELIST       = 2, 
                          PT_LINESTRIP      = 3, 
                          PT_TRIANGLELIST   = 4, 
                          PT_TRIANGLESTRIP  = 5;
  
  public int primitivetype;

  public Vertex vertices[];
	public int indices[];
  
  public boolean render(Renderer renderer)
  {
    Texture texture = null;
    
    if(texturename != null) {
      texture = renderer.getTexture(texturename);
      if(texture == null)
        texture = new Texture(texturename);
    }

    renderer.setTexture(null);      
        
    renderer.drawPrimitve(vertices, indices);
    return true;
  }
  
  public int getVertexCount()
  {
    return vertices.length;
  }
  
  public int getTriangleCount()
  {
    int vcount = (indices.length == 0) ? getVertexCount() : indices.length;
    switch(primitivetype) {
      case PT_TRIANGLELIST:
        return vcount / 3;
      case PT_TRIANGLESTRIP:
        return vcount - 2;
    }

    return 0;    
  }
  
  public void generateNormals()
  {
    for(int l = 0; l < vertices.length; l ++)
      vertices[l].normal = new Vect3(0, 0, 0);

	  for(int j = 0; j < indices.length; j += 3) {
      
	  	Vect3 a = vertices[indices[j + 0]].position.sub(vertices[indices[j + 1]].position),
    	  		b = vertices[indices[j + 1]].position.sub(vertices[indices[j + 2]].position);
	
      Vect3 normal = a.cross(b);

      vertices[indices[j + 0]].normal.add(normal);
      vertices[indices[j + 1]].normal.add(normal);
      vertices[indices[j + 2]].normal.add(normal);
	  }

	  for(int k = 0; k < vertices.length; k ++)
      vertices[k].normal = vertices[k].normal.normalize();
  }

}