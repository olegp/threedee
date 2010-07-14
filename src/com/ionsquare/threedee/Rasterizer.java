package com.ionsquare.threedee;

public class Rasterizer
{
  FrameBuffer framebuffer = null;

  public void setFrameBuffer(FrameBuffer framebuffer)
  {
    this.framebuffer = framebuffer;
  }

  public FrameBuffer getFrameBuffer()
  {
    return framebuffer;
  }

    static final float cfactor = 65535 * 255;

  public void drawTriangle(Vertex v1, Vertex v2, Vertex v3, int color)
  {
    // arrange the vertices in ascending order
    Vertex vt;
    if(v3.position.y < v1.position.y) { vt = v3; v3 = v1; v1 = vt; }
    if(v2.position.y < v1.position.y) { vt = v2; v2 = v1; v1 = vt; }
    if(v3.position.y < v2.position.y) { vt = v3; v3 = v2; v2 = vt; }

    // calculate the delta values for each slope
    float dy21 = v2.position.y - v1.position.y,
          dy31 = v3.position.y - v1.position.y,
          dy32 = v3.position.y - v2.position.y;

    float dx21 = (v2.position.x - v1.position.x)/dy21,
          dx31 = (v3.position.x - v1.position.x)/dy31,
          dx32 = (v3.position.x - v2.position.x)/dy32;

    float dz21 = (v2.position.z - v1.position.z)/dy21,
          dz31 = (v3.position.z - v1.position.z)/dy31,
          dz32 = (v3.position.z - v2.position.z)/dy32;


    // set the left and right edge delta values

    float ldx, rdx,	lx, rx;
    float ldz, rdz,	lz, rz;

    lx = rx = v1.position.x;
    lz = rz = v1.position.z;


    if(dx21 < dx31) {
      ldx = dx21;	rdx = dx31;
      ldz = dz21;	rdz = dz31;

    } else if(dx21 > dx31) {
      ldx = dx31;	rdx = dx21;
      ldz = dz31;	rdz = dz21;

    } else
      return;

    // store the y coordinates as integers
    int y1 = (int)v1.position.y, y2 = (int)v2.position.y, y3 = (int)v3.position.y;

    int lxi, rxi, cywidth;
    float cz, dz;

    // retrieve some values from the framebuffer object for faster access
    int width = framebuffer.width;
    int height = framebuffer.height;
    int pbuffer[] = framebuffer.pbuffer;
    float zbuffer[] = framebuffer.zbuffer;

    float widthf = (float)width;

    // get the maximum y value
    int maxy = height < y3 ? height : y3;
    if(maxy < 0) return;

    // at b1 we switch edges or exit the loop
    int bl = y2 < maxy ? y2 : maxy, cy = y1;

    // start drawing the triangle one scanline at a time
    while(true) {

      for(; cy < bl; cy ++) {

        // check if we're on screen
        if(cy >= 0) {

          // draw a scanline
          cywidth = cy * width;

          float dxinv = 1 / (rx - lx);

          dz = (rz - lz) * dxinv;

          if(lx >= 0) {
            lxi = cywidth + (int)lx;
            cz = lz;

          } else {
            // adjust the values if the starting point (on the left) is off screen
            lxi = cywidth;

            // note: this is equivalent to lz + (-lx) * dz;
            cz = lz - dz * lx;
          }

          if(rx < widthf)
            rxi = cywidth + (int)rx;
          else
            // adjust the right x boundary if it's off screen
            rxi = cywidth + width;

          for(int j = lxi; j < rxi; j ++) {
            if(cz < zbuffer[j]) {
              pbuffer[j] = color;
              zbuffer[j] = cz;
            }

            cz += dz;
          }
        }

        // increment the current values
        lx += ldx; rx += rdx;
        lz += ldz; rz += rdz;
      }

      // if we've reached maxy, we're done
      if(cy >= maxy) return;
      // otherwise draw the second half of the triangle
      else bl = maxy;

      if(dx21 < dx31) {
        ldx = dx32; lx = v2.position.x;
        ldz = dz32; lz = v2.position.z;

      } else {
        rdx = dx32; rx = v2.position.x;
        rdz = dz32; rz = v2.position.z;

      }

    }
  }

  public void drawTriangle(Vertex v1, Vertex v2, Vertex v3)
  {
    // arrange the verices in ascending order
    Vertex vt;
    if(v3.position.y < v1.position.y) { vt = v3; v3 = v1; v1 = vt; }
    if(v2.position.y < v1.position.y) { vt = v2; v2 = v1; v1 = vt; }
    if(v3.position.y < v2.position.y) { vt = v3; v3 = v2; v2 = vt; }

    // calculate the delta values for each slope
    float dy21 = v2.position.y - v1.position.y,
          dy31 = v3.position.y - v1.position.y,
          dy32 = v3.position.y - v2.position.y;

    float dx21 = (v2.position.x - v1.position.x)/dy21,
          dx31 = (v3.position.x - v1.position.x)/dy31,
          dx32 = (v3.position.x - v2.position.x)/dy32;

    float dz21 = (v2.position.z - v1.position.z)/dy21,
          dz31 = (v3.position.z - v1.position.z)/dy31,
          dz32 = (v3.position.z - v2.position.z)/dy32;

    float dr21 = (v2.color.r - v1.color.r)/dy21,
          dr31 = (v3.color.r - v1.color.r)/dy31,
          dr32 = (v3.color.r - v2.color.r)/dy32;

    float dg21 = (v2.color.g - v1.color.g)/dy21,
          dg31 = (v3.color.g - v1.color.g)/dy31,
          dg32 = (v3.color.g - v2.color.g)/dy32;

    float db21 = (v2.color.b - v1.color.b)/dy21,
          db31 = (v3.color.b - v1.color.b)/dy31,
          db32 = (v3.color.b - v2.color.b)/dy32;


    // set the left and right edge delta values

    float ldx, rdx,	lx, rx;
    float ldz, rdz,	lz, rz;
    float ldr, rdr,	lr, rr;
    float ldg, rdg,	lg, rg;
    float ldb, rdb,	lb, rb;

    lx = rx = v1.position.x;
    lz = rz = v1.position.z;

    lr = rr = v1.color.r;
    lg = rg = v1.color.g;
    lb = rb = v1.color.b;

    if(dx21 < dx31) {
      ldx = dx21;	rdx = dx31;
      ldz = dz21;	rdz = dz31;

      ldr = dr21; rdr = dr31;
      ldg = dg21; rdg = dg31;
      ldb = db21; rdb = db31;

    } else if(dx21 > dx31) {
      ldx = dx31;	rdx = dx21;
      ldz = dz31;	rdz = dz21;

      ldr = dr31; rdr = dr21;
      ldg = dg31; rdg = dg21;
      ldb = db31; rdb = db21;

    } else
      return;

    // store the y coordinates as integers
    int y1 = (int)v1.position.y, y2 = (int)v2.position.y, y3 = (int)v3.position.y;

    int lxi, rxi, cywidth;
    float cz, dz;
    int hr, hdr, hg, hdg, hb, hdb;

    // retrieve some values from the framebuffer object for faster access
    int width = framebuffer.width;
    int height = framebuffer.height;
    int pbuffer[] = framebuffer.pbuffer;
    float zbuffer[] = framebuffer.zbuffer;

    float widthf = (float)width;

    // get the maximum y value
    int maxy = height < y3 ? height : y3;
    if(maxy < 0) return;

    // at b1 we switch edges or exit the loop
    int bl = y2 < maxy ? y2 : maxy, cy = y1;

    // start drawing the triangle one scanline at a time
    while(true) {

      for(; cy < bl; cy ++) {

        // check if we're on screen
        if(cy >= 0) {

          // draw a scanline
          cywidth = cy * width;

          float dxinv = 1 / (rx - lx);
          float cdxinv = cfactor * dxinv;

          dz = (rz - lz) * dxinv;
          hdr = (int)((rr - lr) * cdxinv);
          hdg = (int)((rg - lg) * cdxinv);
          hdb = (int)((rb - lb) * cdxinv);

          if(lx >= 0) {
            lxi = cywidth + (int)lx;
            cz = lz;

            // convert the color values to fixed point real values
            // the actual 8 byte color value is at 0x00ff0000
            hr = (int)(lr * cfactor);
            hg = (int)(lg * cfactor);
            hb = (int)(lb * cfactor);

          } else {
            // adjust the values if the starting point (on the left) is off screen
            lxi = cywidth;

            // note: this is equivalent to lz + (-lx) * dz;
            cz = lz - dz * lx;

            hr = (int)(lr * cfactor) - hdr * (int)lx;
            hg = (int)(lg * cfactor) - hdg * (int)lx;
            hb = (int)(lb * cfactor) - hdb * (int)lx;
          }

          if(rx < widthf)
            rxi = cywidth + (int)rx;
          else
            // adjust the right x boundary if it's off screen
            rxi = cywidth + width;

          for(int j = lxi; j < rxi; j ++) {
            if(cz < zbuffer[j]) {
              pbuffer[j] = (hr & 0x00ff0000) | ((hg  & 0x00ff0000) >> 8) | (hb >> 16);
              zbuffer[j] = cz;
            }

            hr += hdr;
            hg += hdg;
            hb += hdb;
            cz += dz;
          }
        }

        // increment the current values
        lx += ldx; rx += rdx;
        lz += ldz; rz += rdz;

        lr += ldr; rr += rdr;
        lg += ldg; rg += rdg;
        lb += ldb; rb += rdb;
      }

      // if we've reached maxy, we're done
      if(cy >= maxy) return;
      // otherwise draw the second half of the triangle
      else bl = maxy;

      if(dx21 < dx31) {
        ldx = dx32; lx = v2.position.x;
        ldz = dz32; lz = v2.position.z;

        ldr = dr32; lr = v2.color.r;
        ldg = dg32; lg = v2.color.g;
        ldb = db32; lb = v2.color.b;

      } else {
        rdx = dx32; rx = v2.position.x;
        rdz = dz32; rz = v2.position.z;

        rdr = dr32; rr = v2.color.r;
        rdg = dg32; rg = v2.color.g;
        rdb = db32; rb = v2.color.b;

      }

    }
  }

  public void drawTriangle(Vertex v1, Vertex v2, Vertex v3, Texture texture)
  {
    // arrange the verices in ascending order
    Vertex vt;
    if(v3.position.y < v1.position.y) { vt = v3; v3 = v1; v1 = vt; }
    if(v2.position.y < v1.position.y) { vt = v2; v2 = v1; v1 = vt; }
    if(v3.position.y < v2.position.y) { vt = v3; v3 = v2; v2 = vt; }

    // calculate the delta values for each slope
    float dy21 = v2.position.y - v1.position.y,
          dy31 = v3.position.y - v1.position.y,
          dy32 = v3.position.y - v2.position.y;

    float dx21 = (v2.position.x - v1.position.x)/dy21,
          dx31 = (v3.position.x - v1.position.x)/dy31,
          dx32 = (v3.position.x - v2.position.x)/dy32;

    float dz21 = (v2.position.z - v1.position.z)/dy21,
          dz31 = (v3.position.z - v1.position.z)/dy31,
          dz32 = (v3.position.z - v2.position.z)/dy32;

    float du21 = (v2.texcoords.x - v1.texcoords.x)/dy21,
          du31 = (v3.texcoords.x - v1.texcoords.x)/dy31,
          du32 = (v3.texcoords.x - v2.texcoords.x)/dy32;

    float dv21 = (v2.texcoords.y - v1.texcoords.y)/dy21,
          dv31 = (v3.texcoords.y - v1.texcoords.y)/dy31,
          dv32 = (v3.texcoords.y - v2.texcoords.y)/dy32;

    // set the left and right edge delta values

    float ldx, rdx,	lx, rx;
    float ldz, rdz,	lz, rz;
    float ldu, rdu,	lu, ru;
    float ldv, rdv,	lv, rv;

    lx = rx = v1.position.x;
    lz = rz = v1.position.z;

    lu = ru = v1.texcoords.x;
    lv = rv = v1.texcoords.y;

    if(dx21 < dx31) {
      ldx = dx21;	rdx = dx31;
      ldz = dz21;	rdz = dz31;

      ldu = du21; rdu = du31;
      ldv = dv21; rdv = dv31;

    } else if(dx21 > dx31) {
      ldx = dx31;	rdx = dx21;
      ldz = dz31;	rdz = dz21;

      ldu = du31; rdu = du21;
      ldv = dv31; rdv = dv21;

    } else
      return;

    // store the y coordinates as integers
    int y1 = (int)v1.position.y, y2 = (int)v2.position.y, y3 = (int)v3.position.y;

    int lxi, rxi, cywidth;
    float cz, dz;

    int hu, hdu, hv, hdv;

    // retrieve some values from the framebuffer object for faster access
    int width = framebuffer.width;
    int height = framebuffer.height;
    int pbuffer[] = framebuffer.pbuffer;
    float zbuffer[] = framebuffer.zbuffer;

    float widthf = (float)width;

    int texturewidth = texture.width;
    int texturedata[] = texture.data;

    float texturefactoru = 65535 * (float)texture.width,
          texturefactorv = 65535 * (float)texture.height;

    // get the maximum y value
    int maxy = height < y3 ? height : y3;
    if(maxy < 0) return;

    // at b1 we switch edges or exit the loop
    int bl = y2 < maxy ? y2 : maxy, cy = y1;

    // start drawing the triangle one scanline at a time
    while(true) {

      for(; cy < bl; cy ++) {

        // check if we're on screen
        if(cy >= 0) {

          // draw a scanline
          cywidth = cy * width;

          float dxinv = 1 / (rx - lx);

          dz = (rz - lz) * dxinv;

          hdu = (int)((ru - lu) * texturefactoru * dxinv);
          hdv = (int)((rv - lv) * texturefactorv * dxinv);

          if(lx >= 0) {
            lxi = cywidth + (int)lx;
            cz = lz;

            hu = (int)(lu * texturefactoru);
            hv = (int)(lv * texturefactorv);

          } else {
            // adjust the values if the starting point (on the left) is off screen
            lxi = cywidth;

            // note: this is equivalent to lz + (-lx) * dz;
            cz = lz - dz * lx;

            hu = (int)(lu * texturefactoru) - hdu * (int)lx;
            hv = (int)(lv * texturefactorv) - hdv * (int)lx;
          }

          if(rx < widthf)
            rxi = cywidth + (int)rx;
          else
            // adjust the right x boundary if it's off screen
            rxi = cywidth + width;

          for(int j = lxi; j < rxi; j ++) {
            if(cz < zbuffer[j]) {
              pbuffer[j] = texturedata[texturewidth * (hv >> 16) + (hu >> 16)];
              zbuffer[j] = cz;
            }

            hu += hdu;
            hv += hdv;
            cz += dz;
          }
        }

        // increment the current values
        lx += ldx; rx += rdx;
        lz += ldz; rz += rdz;

        lu += ldu; ru += rdu;
        lv += ldv; rv += rdv;
      }

      // if we've reached maxy, we're done
      if(cy >= maxy) return;
      // otherwise draw the second half of the triangle
      else bl = maxy;

      if(dx21 < dx31) {
        ldx = dx32; lx = v2.position.x;
        ldz = dz32; lz = v2.position.z;

        ldu = du32; lu = v2.texcoords.x;
        ldv = dv32; lv = v2.texcoords.y;

      } else {
        rdx = dx32; rx = v2.position.x;
        rdz = dz32; rz = v2.position.z;

        rdu = du32; ru = v2.texcoords.x;
        rdv = dv32; rv = v2.texcoords.y;
      }

    }
  }

  public void drawPoint(Vertex a, int color)
  {
  }

  public void drawPoint(Vertex a, int color, float size)
  {
  }

}
