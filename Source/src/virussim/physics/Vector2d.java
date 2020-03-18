/*
    A BeepBeep simulation of coronavirus contagion
    Copyright (C) 2020 Sylvain Hallé and friends

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package virussim.physics;

/**
 * A two-dimensional vector that implements elementary operations.
 */
public class Vector2d
{
  /**
   * The null 2D vector
   */
  public static final Vector2d NULL = new Vector2d(0, 0);
  
  /**
   * The first coordinate of the vector
   */
  protected double m_x;
  
  /**
   * The second coordinate of the vector
   */
  protected double m_y;
  
  /**
   * Creates a new 2D vector
   * @param x The first coordinate of the vector
   * @param y The second coordinate of the vector
   */
  public Vector2d(double x, double y)
  {
    super();
    m_x = x;
    m_y = y;
  }
  
  /**
   * Gets the first coordinate of the vector
   * @return The coordinate
   */
  public double getX()
  {
    return m_x;
  }
  
  /**
   * Sets the first coordinate of the vector
   * @param x The coordinate
   * @return This vector
   */
  public Vector2d setX(double x)
  {
    m_x = x;
    return this;
  }
  
  /**
   * Gets the second coordinate of the vector
   * @return The coordinate
   */
  public double getY()
  {
    return m_y;
  }
  
  /**
   * Sets the second coordinate of the vector
   * @param y The coordinate
   * @return This vector
   */
  public Vector2d setY(double y)
  {
    m_y = y;
    return this;
  }
  
  /**
   * Gets the modulus of this vector
   * @return The modulus
   */
  public double getModulus()
  {
    return Math.sqrt(Math.pow(m_x, 2) + Math.pow(m_y, 2));
  }
  
  /**
   * Normalizes this vector
   * @return This vector, normalized
   */
  public Vector2d normalize()
  {
    double len = getModulus();
    if (len != 0f)
    {
      m_x /= len;
      m_y /= len;
    }
    else
    {
      m_x = 0;
      m_y = 0;
    }
    return this;
  }
  
  /**
   * Adds two vectors
   * @param v1 The first vector
   * @param v2 The second vector
   */
  public static Vector2d add(Vector2d v1, Vector2d v2)
  {
    return new Vector2d(v1.m_x + v2.m_x, v1.m_y + v2.m_y);
  }
  
  /**
   * Subtracts two vectors
   * @param v1 The first vector
   * @param v2 The second vector
   */
  public static Vector2d subtract(Vector2d v1, Vector2d v2)
  {
    return new Vector2d(v1.m_x - v2.m_x, v1.m_y - v2.m_y);
  }
  
  /**
   * Multiplies a vector by a scalar
   * @param v The vector
   * @param scale The scalar
   * @return The new vector
   */
  public static Vector2d multiply(Vector2d v, double scale)
  {
    return new Vector2d(v.m_x * scale, v.m_y * scale);
  }

  /**
   * Returns the Euclidean distance between two vectors
   * @param v1 The other vector
   * @param v2 The other vector
   * @return The distance
   */
  public static double distance(Vector2d v1, Vector2d v2)
  {
    return Math.sqrt(Math.pow(v1.m_x - v2.m_x, 2) + Math.pow(v1.m_y - v2.m_y, 2));
  }
  
  /**
   * Returns the dot product of two vectors
   * @param v1 The other vector
   * @param v2 The other vector
   * @return The dot product
   */
  public static double dotProduct(Vector2d v1, Vector2d v2)
  {
    double result = v1.m_x * v2.m_x + v1.m_y * v2.m_y;
    return result;
  }
  
  @Override
  public String toString()
  {
    return "(" + m_x + "," + m_y + ")";
  }
}
