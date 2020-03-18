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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A rectangular region where balls can move and collide. The state
 * of an arena is given by the individual state (position and velocity)
 * of each of the balls it contains. The state of the balls is updated
 * by calling {@link #update()}.
 */
public class Arena
{
  /**
   * The set of players in the arena
   */
  /*@ non_null @*/ protected Map<Integer,Ball> m_balls;

  /**
   * The width of the arena
   */
  protected double m_width;

  /**
   * The height of the arena
   */
  protected double m_height;

  /**
   * Creates a new arena
   * @param width The width of the arena
   * @param height The height of the arena
   * @param balls The collection of players to put inside the arena
   */
  public Arena(int width, int height, /*@ non_null @*/ Collection<? extends Ball> balls)
  {
    super();
    m_width = width;
    m_height = height;
    m_balls = new HashMap<Integer,Ball>(balls.size());
    for (Ball b : balls)
    {
      m_balls.put(b.m_id, (Ball) b);
    }
  }

  /**
   * Gets the width of the arena
   * @return The width
   */
  public double getWidth()
  {
    return m_width;
  }

  /**
   * Gets the height of the arena
   * @return The height
   */
  public double getHeight()
  {
    return m_height;
  }

  

  /**
   * Gets the current state of each ball in the arena
   * @return A map between ball IDs and ball instances
   */
  public Map<Integer,Ball> getBalls()
  {
    return m_balls;
  }

  /**
   * Updates the state of each ball in the arena
   */
  public void update()
  {
    // Step the position of movable objects based off their velocity/gravity and elapsedTime
    for (int i = 0; i < m_balls.size(); i++)
    {
      Ball p1 = m_balls.get(i);
      if (!p1.isFixed())
      {
        p1.getPosition().setX(p1.getPosition().getX() + p1.getVelocity().getX());
        p1.getPosition().setY(p1.getPosition().getY() + p1.getVelocity().getY());
      }
    }
    // Check for collision with walls
    for (int i = 0; i < m_balls.size(); i++)
    {
      Ball p1 = m_balls.get(i);
      if (p1.getPosition().getX() - p1.getRadius() < 0)
      {
        p1.getPosition().setX(p1.getRadius()); // Place ball against edge
        p1.getVelocity().setX(-(p1.getVelocity().getX() * Ball.s_restitution)); // Reverse direction and account for friction
        p1.getVelocity().setY(p1.getVelocity().getY() * Ball.s_restitution);
      }
      else if (p1.getPosition().getX() + p1.getRadius() > getWidth()) // Right Wall
      {
        p1.getPosition().setX(getWidth() - p1.getRadius());   // Place ball against edge
        p1.getVelocity().setX(-(p1.getVelocity().getX() * Ball.s_restitution)); // Reverse direction and account for friction
        p1.getVelocity().setY((p1.getVelocity().getY() * Ball.s_restitution));
      }
      if (p1.getPosition().getY() - p1.getRadius() < 0)       // Top Wall
      {
        p1.getPosition().setY(p1.getRadius());        // Place ball against edge
        p1.getVelocity().setY(-(p1.getVelocity().getY() * Ball.s_restitution)); // Reverse direction and account for friction
        p1.getVelocity().setX((p1.getVelocity().getX() * Ball.s_restitution));
      }
      else if (p1.getPosition().getY() + p1.getRadius() > getHeight()) // Bottom Wall
      {
        p1.getPosition().setY(getHeight() - p1.getRadius());    // Place ball against edge
        p1.getVelocity().setY(-(p1.getVelocity().getY() * Ball.s_restitution));    // Reverse direction and account for friction
        p1.getVelocity().setX((p1.getVelocity().getX() * Ball.s_restitution));
      }
      // Player to player collision
      for (int j = 1 + 1; j < m_balls.size(); j++)
      {
        Ball p2 = m_balls.get(j);
        p1.interactWith(p2);
      }
      p1.tick();
    }
  }
}
