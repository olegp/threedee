package com.ionsquare.threedee;


/**
 * A collection of functions that allow you to treat a 32-bit
 * integer as a fixed point real value.<br>
 * The resolution of both the integer and fraction parts is
 * 16 bits.
 */
public class Fixed
{
  public final static float FIXED_FACTOR = 65535;
  public final static float FLOAT_FACTOR = 1 / FIXED_FACTOR;
  public final static int FRAC_FACTOR = 65535 / 100;
  
  public static float toFloat(int a)
  {
    return ((float)a) * FLOAT_FACTOR;
  }
  
  public static int toInt(int a)
  {
    return a >> 16;
  }
  
  public static short toShort(int a)
  {
    return (short)(a >> 16); 
  }
  
  public static int toFixed(float a)
  {
    return (int)(a * FIXED_FACTOR);
  }
    
  // the fraction is in percent
  public static int toFixed(int integer, int fraction)
  {
    return (integer << 16) | (fraction * FRAC_FACTOR);
  }
    
  public static int div(int a, int b)
  {
    return (int)((((long)a) << 16) / ((long)b));
  }
  
  public static int mul(int a, int b)
  {
    return (int)((((long)a) * ((long)b)) >> 16);
  }
}
