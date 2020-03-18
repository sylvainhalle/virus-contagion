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
package virussim;

import virussim.physics.Ball;
import virussim.physics.Vector2d;
import virussim.picker.HealthMarkovChain;

/**
 * A special type of {@link Ball} that can catch a virus when in contact
 * with another infected ball.
 */
public class Patient extends Ball
{
  /**
   * The health state of the player
   */
  public enum Health {HEALTHY, INFECTED, RECOVERED, DEAD}

  /**
   * The player's health state
   */
  protected Health m_health;
  
  /**
   * The player's Markov chain for its health state
   */
  protected HealthMarkovChain m_healthChain;

  /**
   * Flag deciding whether the player is allowed to move in the
   * arena
   */
  protected boolean m_isMoving;

  public Patient(Vector2d position, Vector2d velocity, boolean is_moving) 
  {
    super();
    m_id = s_idCounter++;
    m_position = position;
    m_velocity = velocity;
    m_isMoving = is_moving;
    if (!is_moving)
    {
      // Ignore speed vector
      //m_velocity = Vector2d.NULL;
      setFixed(true);
    }
    m_health = Health.HEALTHY;
  }
  
  /**
   * Sets the Markov chain defining the health status of this
   * patient.
   * @param c The chain
   */
  public void setHealthChain(HealthMarkovChain c)
  {
    m_healthChain = c;
  }

  /**
   * Gets the player's health state
   * @return The health state
   */
  public Health getHealthState()
  {
    return m_health;
  }
  
  public void setHealthState(Health s)
  {
    if (m_health == Health.RECOVERED)
    {
      return;
    }
    if (m_health == Health.HEALTHY && s == Health.INFECTED)
    {
      m_health = Health.INFECTED;
    }
  }
  
  /**
   * Updates the state of the player for the next iteration
   */
  public void tick()
  {
    // If infected, pick next state in Markov chain
    if (m_health == Health.INFECTED)
    {
      m_health = m_healthChain.pick();
    }
  }

  /**
   * Manages the interaction between the current player and
   * another one
   * @param b The other player
   */
  @Override
  public boolean interactWith(Ball b)
  {
    Patient p = (Patient) b;
    if (super.interactWith(p))
    {
      // Collision
      if (p.getHealthState() == Health.INFECTED)
      {
        setHealthState(Health.INFECTED);
      }
      if (m_health == Health.INFECTED)
      {
        p.setHealthState(Health.INFECTED);
      }
    }
    return false;
  }
  
  @Override
  public String toString()
  {
    StringBuilder out = new StringBuilder();
    out.append("<");
    out.append("p:").append(m_position).append(",").append("v:").append(m_velocity);
    out.append("m:").append(m_isMoving).append("h:").append(m_health);
    out.append(">");
    return out.toString();
  }
}
