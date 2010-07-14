package com.ionsquare.threedee;

import java.io.*;

public class VisualLoader
{
  
  public static String getName(String filename)
  {
    // extract the name
    String name = filename;
    int i = filename.lastIndexOf('\\');
    int j = filename.lastIndexOf('/');
      
    if(i != -1 && j != -1) name = name.substring(Math.max(i, j) + 1);
    i = name.lastIndexOf('.');
    if(i != -1) name = name.substring(0, i);
    
    return name;        
  }
  
  static int readInt(InputStream in) throws IOException
  {
    int a, b, c, d;
    a = in.read(); b = in.read(); 
    c = in.read(); d = in.read();
    return (((d & 0xff) << 24) | ((c & 0xff) << 16) |
            ((b & 0xff) << 8) | (a & 0xff));    
  }
  
  static float readFloat(InputStream in) throws IOException
  {
    int i = readInt(in);
    return Float.intBitsToFloat(i);
  }
  
  static String readString(InputStream in) throws IOException
  {
    String s = "";
    char c = ' ';
    
    while(c != '\0') {
      c = (char)in.read();
      s += c;
    }
    
    return s;
  }
  
  static Color readColor(InputStream in) throws IOException
  {
    Color c = new Color();
    c.r = readFloat(in);    
    c.g = readFloat(in);    
    c.b = readFloat(in);    
    c.a = readFloat(in);    
    return c;    
  }
  
  static Vect2 readVect2(InputStream in) throws IOException
  {
    Vect2 v = new Vect2();
    v.x = readFloat(in);
    v.y = readFloat(in);
    return v;
  }
  
  static Vect3 readVect3(InputStream in) throws IOException
  {
    Vect3 v = new Vect3();
    v.x = readFloat(in);
    v.y = readFloat(in);
    v.z = readFloat(in);
    return v;
  }
  
  static Mat4 readMat4(InputStream in) throws IOException
  {
    float m[] = new float[16];
    for(int i = 0; i < m.length; i ++)
      m[i] = readFloat(in);
    
    return new Mat4(m);
  }
  
  static Node readNode(InputStream in, Node parent) throws IOException
  {
    ChunkHeader chunk = new ChunkHeader(in);

    Node node = new Node();
    
    int set = in.available();
    
    if(chunk.type == ChunkHeader.CT_MESH && parent != null)
      readMesh(in, parent);
    else if(chunk.type == ChunkHeader.CT_NODE) {
      node = new Node();

      if(parent != null) parent.childnodes.addElement(node);      
      node.name = readString(in);
      node.toparent = readMat4(in);

      while(set - in.available() < chunk.chunksize)
        readNode(in, node);
    }  

    return node;
  }
  
  static Mesh readMesh(InputStream in, Node parent) throws IOException
  {
    Mesh mesh = new Mesh();

    parent.contents = mesh;
    parent.contentstype = parent.NC_MESH;

    int groupcount = readInt(in);

    for(int i = 0; i < groupcount; i ++) {
      MaterialGroup group = new MaterialGroup();
      
      mesh.groups.addElement(group);

      // set the primitive and vertex types
      // group.vertextype = VT_REFLECTED;
      group.primitivetype = group.PT_TRIANGLELIST;

      // read the vertices    
      Vertex vertices[] = new Vertex[readInt(in)];
      
      for(int j = 0; j < vertices.length; j ++) {
        vertices[j] = new Vertex();
        vertices[j].position = readVect3(in);
        vertices[j].normal = readVect3(in);
        vertices[j].texcoords = readVect2(in);
      }
      
      group.vertices = vertices;
      
      // read the indices
      int indices[] = new int[readInt(in)];
      
      for(int j = 0; j < indices.length; j ++)
        indices[j] = VisualLoader.readInt(in);

      group.indices = indices;
      
      // read the Material
      group.material.diffuse = readColor(in);
      group.material.ambient = readColor(in);
      group.material.specular = readColor(in);
      /*group.material.emissive = */readColor(in);
      group.material.power = readFloat(in);

      group.texturename = readString(in);
      /*group.bumpname = */readString(in);
      group.reflmapname = readString(in);

    }

    mesh.generateNormals();
    return mesh;
  }

  
  static public Visual load(InputStream in)
  {
    if(in == null) return null;
    
    try {
      Visual visual = new Visual();
            
      ChunkHeader header = new ChunkHeader(in);
      
      if(header.type == header.CT_MAIN) {              
        visual.topmost = readNode(in, null);
        return visual;
      }
      
    } catch (IOException e) {
    }    

    return null;
  }
}


class ChunkHeader
{ 
  public final static int CT_MAIN = makeChunkType('3', 'd', 'd', ' '),
                          CT_NODE = makeChunkType('n', 'o', 'd', 'e'),
                          CT_MESH = makeChunkType('m', 'e', 's', 'h');
 
  public int type, version, chunksize, datasize;
      
  static public int makeChunkType(char a, char b, char c, char d)
  {
    return  ((int)d << 24) | ((int)c << 16) | ((int)b << 8) | (int)a;    
  }
  
  public ChunkHeader()
  {
  }
    
  public ChunkHeader(int type, int version, int chunksize, int datasize)
  {
    this.type = type;
    this.version = version;
    this.chunksize = chunksize;
    this.datasize = datasize;
  }
    
  public ChunkHeader(InputStream in) throws IOException
  {
    type = VisualLoader.readInt(in);
    version = VisualLoader.readInt(in);
    chunksize = VisualLoader.readInt(in);
    datasize = VisualLoader.readInt(in);
  }   
}