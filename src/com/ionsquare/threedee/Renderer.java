package com.ionsquare.threedee;

import java.util.*;

public class Renderer
{
  Mat4  model				= Mat4.identity(),
        view				= Mat4.identity(),
        projection	= Mat4.identity(),
        viewport		= Mat4.identity(),

        viewinv			= Mat4.identity(),
        vpvproduct	= Mat4.identity(),  // view * projection * viewport -> for optimization
        product			= Mat4.identity();	// the final matrix product

  public void setModel(Mat4 transform)
  {
    model = new Mat4(transform);
    product = model.mult(vpvproduct);
  }

  public void setView(Mat4 transform)
  {
    view = transform.inverse();
    viewinv = new Mat4(transform);
    vpvproduct = view.mult(projection).mult(viewport);
    product = model.mult(vpvproduct);
  }

  public void setProjection(Mat4 transform)
  {
    projection = new Mat4(transform);
    vpvproduct = view.mult(projection).mult(viewport);
    product = model.mult(vpvproduct);
  }

  public void setViewport(float width, float height)
  {
    viewport = Mat4.viewport(width, height);
    vpvproduct = view.mult(projection).mult(viewport);
    product = model.mult(vpvproduct);
  }

  Material material = null;

  public void setMaterial(Material material)
  {
    this.material = material;
  }

  Vector lights = new Vector();
  public void addLight(Light light)
  {
    lights.addElement(light);
    light.renderer = this;
  }

  public void removeLight(Light light)
  {
    lights.removeElement(light);
    light.renderer = null;
  }


  Rasterizer rasterizer = null;
  FrameBuffer framebuffer = null;

  public void setFrameBuffer(FrameBuffer framebuffer)
  {
    this.framebuffer = framebuffer;
  }

  public void setRasterizer(Rasterizer rasterizer)
  {
    rasterizer.setFrameBuffer(framebuffer);
    this.rasterizer = rasterizer;
  }

  Texture texture = null;

  public void setTexture(Texture texture)
  {
    this.texture = texture;
  }

  Vertex buffer[] = null;

  public void drawPrimitve(Vertex vertices[], int indices[])
  {
    // expand the buffer if necessary
    int length = buffer != null ? buffer.length : 0;
    if(vertices.length > length) {
      buffer = new Vertex[vertices.length];
      for(int i = length; i < buffer.length; i ++)
        buffer[i] = new Vertex();
    }

    // enable lighting only if there's no active texture
    if(texture == null) {
      Mat4 inv = model.inverse();
      for(int i = 0; i < vertices.length; i ++) {

        float R = 0, G = 0, B = 0;

        for(int j = 0; j < lights.size(); j ++) {
          Light light = (Light)lights.elementAt(j);

          Vect3 lightdir = light.position.mult3(inv);
          float intensity = (lightdir.dot(vertices[i].normal.minus()) + 1) * 0.5f;
          float sintensity = (float)Math.pow(vertices[i].normal.dot(viewinv.getPosition().mult(inv).sub(vertices[i].position).normalize().sub(lightdir).normalize()),
                                material.power);

          R += light.ambient.r * material.ambient.r +
               light.diffuse.r * material.diffuse.r * intensity +
               light.specular.r * material.specular.r * sintensity;

          G += light.ambient.g * material.ambient.g +
               light.diffuse.g * material.diffuse.g * intensity +
               light.specular.g * material.specular.g * sintensity;

          B += light.ambient.b * material.ambient.b +
               light.diffuse.b * material.diffuse.b * intensity +
               light.specular.b * material.specular.b * sintensity;
        }

        buffer[i].color = new Color((R > 1) ? 1 : R, (G > 1) ? 1 : G, (B > 1) ? 1 : B);
      }
    }

    Vect4 t = new Vect4();
    for(int i = 0; i < vertices.length; i ++) {
      t.set(vertices[i].position);
      t = t.mult(product);
      // todo: optimize
      float tf = 1.f/Math.abs(t.w);
      buffer[i].position = new Vect3(t.x * tf, t.y * tf, t.z * tf);
    }

    drawBuffer(vertices.length, indices);
  }

  /**
   * Draws a triangle using the specified settings, such as culling,
   * shade mode and texture.
   */
  void drawTriangle(Vertex a, Vertex b, Vertex c)
  {
    // cull the triangle
    float n = c.position.x * a.position.y - a.position.x * c.position.y +
              a.position.x * b.position.y - b.position.x * a.position.y +
              b.position.x * c.position.y - c.position.x * b.position.y;

    if(n > 0) {
      if(!(a.position.z < 0 || b.position.z < 0 || c.position.z < 0)) {
        // if(texture == null)
          rasterizer.drawTriangle(a, b, c);
        //else
        //  rasterizer.texturedZTriangle(a, b, c, texture);
      }
     //rasterizer.flatZTriangle(a, b, c, a.color.getRGB());
    }
  }


  /**
   * Draws the transformed vertices in the vertex buffer. If the indices
   * array is not null the values are used as indices into the vertex buffer.
   */
  void drawBuffer(int vertexcount, int indices[])
  {
    Vertex a, b, c;
    if(indices == null) {
      for(int i = 0; i < vertexcount; i += 3)
        drawTriangle(buffer[i], buffer[i + 1], buffer[i + 2]);
    } else {
      for(int i = 0; i < indices.length; i += 3)
        drawTriangle(buffer[indices[i]], buffer[indices[i + 1]], buffer[indices[i + 2]]);
    }
  }

  Vector textures = new Vector();

  public Texture getTexture(String name)
  {
    for(int i = 0; i < textures.size(); i ++) {
     Texture texture = (Texture)textures.elementAt(i);
     if(name.equalsIgnoreCase(texture.name))
       return texture;
    }

    return null;
  }
}
