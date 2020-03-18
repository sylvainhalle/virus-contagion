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
 * Two-dimensional physical object that can move and collide with
 * walls and other objects.
 */
public class Ball
{
  /**
   * Restitution
   */
  public static final double s_restitution = 1d;

  /**
   * A counter to give each player a unique ID
   */
  protected static transient int s_idCounter = 0;

  /**
   * The ball's unique ID
   */
  protected int m_id;

  /**
   * The ball's position in the arena
   */
  /*@ non_null @*/ protected Vector2d m_position;

  /**
   * The ball's velocity in the arena
   */
  /*@ non_null @*/ protected Vector2d m_velocity;
  
  /**
   * The radius of the player when displayed as a ball
   */
  protected double m_radius = 5d;
  
  /**
   * Whether this ball is fixed in the arena
   */
  protected boolean m_fixed = false;
  
  /**
   * Gets the player's radius when displayed as a ball
   * @return The radius
   */
  public double getRadius()
  {
    return m_radius;
  }
  
  /**
   * Sets whether this ball is fixed in the arena
   * @param b <tt>true</tt> to set this ball as fixed, <tt>false</tt>
   * otherwise
   */
  public void setFixed(boolean b)
  {
    m_fixed = b;
  }
  
  /**
   * Determines if a ball is fixed
   * @return <tt>true</tt> if the player is fixed, <tt>false</tt> otherwise
   */
  public boolean isFixed()
  {
    return m_fixed;
  }

  /**
   * Gets the player's position
   * @return The player's position
   */
  public Vector2d getPosition()
  {
    return m_position;
  }
  
  /**
   * Sets the ball's position
   * @param p The position
   */
  public void setPosition(/*@ non_null @*/ Vector2d p)
  {
    m_position = p;
  }

  /**
   * Gets the player's velocity
   * @return The player's velocity
   */
  public Vector2d getVelocity()
  {
    return m_velocity;
  }
  
  /**
   * Sets the ball's velocity
   * @param v The velocity
   */
  public void setVelocity(/*@ non_null @*/ Vector2d v)
  {
    m_velocity = v;
  }
  
  /**
   * Manages the interaction between the current ball and
   * another one
   * @param p The other player
   */
  public boolean interactWith(Ball p)
  {
    Vector2d delta = Vector2d.subtract(m_position, p.getPosition());
    double r = getRadius() + p.getRadius();
    double dist2 = Vector2d.dotProduct(delta, delta);
    if (dist2 > r * r)
    {
      // No collision
      return false;
    }
    // Update physics
    double d = delta.getModulus();
    Vector2d mtd;
    if (d != 0.0d)
    {
      // minimum translation distance to push balls apart after intersecting
      mtd = Vector2d.multiply(delta, ((getRadius() + p.getRadius())-d)/d);
    }
    else
    {
      // Special case. Balls are exactly on top of eachother.  Don't want to divide by zero.
      d = p.getRadius() + getRadius() - 1.0f;
      delta = new Vector2d(p.getRadius() + getRadius(), 0.0f);
      mtd = Vector2d.multiply(delta, ((getRadius() + p.getRadius())-d)/d);
    }
    // resolve intersection
    float im1 = 1;
    float im2 = 1;
    // push-pull them apart
    m_position = Vector2d.add(m_position, Vector2d.multiply(mtd, im1 / (im1 + im2)));
    p.m_position = Vector2d.subtract(p.m_position, Vector2d.multiply(mtd, im2 / (im1 + im2)));
    // impact speed
    Vector2d v = Vector2d.subtract(m_velocity, p.m_velocity);
    double vn = Vector2d.dotProduct(v, mtd.normalize());
    // sphere intersecting but moving away from each other already
    if (vn > 0.0f)
    {
      return true;
    }
    // collision impulse
    double i = (-(1.0f + s_restitution) * vn) / (im1 + im2);
    Vector2d impulse = Vector2d.multiply(mtd, i);
    // change in momentum
    m_velocity = Vector2d.add(m_velocity, Vector2d.multiply(impulse, im1));
    p.m_velocity = Vector2d.subtract(p.m_velocity, Vector2d.multiply(impulse, im2));
    return true;
  }
  
  /**
   * Updates the internal state of the ball for the next iteration
   */
  public void tick()
  {
    // Nothing to do
  }
}
