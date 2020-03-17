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
package coronaarena;

public class Player
{
  /**
   * The health state of the player
   */
  public enum HealthState {HEALTHY, INFECTED, RECOVERED}

  /**
   * Restitution
   */
  public static final double s_restitution = 0.85d;

  /**
   * A counter to give each player a unique ID
   */
  protected static transient int s_idCounter = 0;

  /**
   * The minimum distance between two players to be considered
   * a collision
   */
  protected static float s_collisionRadius = 1f;

  /**
   * The player's unique ID
   */
  protected int m_id;

  /**
   * The player's position in the arena
   */
  /*@ non_null @*/ protected Vector2d m_position;

  /**
   * The player's velocity in the arena
   */
  /*@ non_null @*/ protected Vector2d m_velocity;

  /**
   * The player's health state
   */
  protected HealthState m_healthState;

  /**
   * The number of update cycles since the player is infected
   */
  protected int m_infectedLoops;

  /**
   * Flag deciding whether the player is allowed to move in the
   * arena
   */
  protected boolean m_isMoving;

  /**
   * The radius of the player when displayed as a ball
   */
  protected double m_radius = 5d;
  
  public static int s_recoverySteps = 200;

  public Player(Vector2d position, Vector2d velocity, boolean is_moving) 
  {
    super();
    m_id = s_idCounter++;
    m_position = position;
    m_velocity = velocity;
    m_isMoving = is_moving;
    m_healthState = HealthState.HEALTHY;
    m_infectedLoops = 0;
  }

  /**
   * Gets the player's radius when displayed as a ball
   * @return The radius
   */
  public double getRadius()
  {
    return m_radius;
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
   * Gets the player's velocity
   * @return The player's velocity
   */
  public Vector2d getVelocity()
  {
    return m_velocity;
  }

  /**
   * Gets the player's health state
   * @return The health state
   */
  public HealthState getHealthState()
  {
    return m_healthState;
  }
  
  public void setHealthState(HealthState s)
  {
    if (m_healthState == HealthState.RECOVERED)
    {
      return;
    }
    if (m_healthState == HealthState.HEALTHY && s == HealthState.INFECTED)
    {
      m_healthState = HealthState.INFECTED;
    }
  }
  
  public void tick()
  {
    if (m_healthState == HealthState.INFECTED)
    {
      m_infectedLoops++;
    }
    if (m_infectedLoops > s_recoverySteps)
    {
      m_healthState = HealthState.RECOVERED;
    }
  }
  
  /**
   * Determines if a player is moving
   * @return <tt>true</tt> if the player is moving, <tt>false</tt> otherwise
   */
  public boolean isMoving()
  {
    return m_isMoving;
  }

  /**
   * Manages the interaction between the current player and
   * another one
   * @param p The other player
   */
  public void interactWith(Player p)
  {
    Vector2d delta = Vector2d.subtract(m_position, p.getPosition());
    double r = getRadius() + p.getRadius();
    double dist2 = Vector2d.dotProduct(delta, delta);
    if (dist2 > r * r)
    {
      // No collision
      return;
    }
    // Handle infection
    if (p.getHealthState() == HealthState.INFECTED)
    {
      setHealthState(HealthState.INFECTED);
    }
    if (m_healthState == HealthState.INFECTED)
    {
      p.setHealthState(HealthState.INFECTED);
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
      return;
    }
    // collision impulse
    double i = (-(1.0f + s_restitution) * vn) / (im1 + im2);
    Vector2d impulse = Vector2d.multiply(mtd, i);
    // change in momentum
    m_velocity = Vector2d.add(m_velocity, Vector2d.multiply(impulse, im1));
    p.m_velocity = Vector2d.subtract(p.m_velocity, Vector2d.multiply(impulse, im2));
  }
  
  @Override
  public String toString()
  {
    StringBuilder out = new StringBuilder();
    out.append("<");
    out.append("p:").append(m_position).append(",").append("v:").append(m_velocity);
    out.append("m:").append(m_isMoving).append("h:").append(m_healthState);
    out.append(">");
    return out.toString();
  }
}
