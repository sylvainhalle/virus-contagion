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
package coronaarena.picker;

import ca.uqac.lif.synthia.Picker;
import coronaarena.Player;
import coronaarena.Vector2d;

/**
 * A picker that generates instances of {@link Player}s.
 */
public class PlayerPicker implements Picker<Player>
{
  /**
   * A picker for the position of a new player
   */
  protected Vector2dPicker m_pickerPosition;
  
  /**
   * A picker for the initial velocity vector of the player
   */
  protected Vector2dPicker m_pickerVelocity;
  
  /**
   * A picker for the "movable"
   */
  protected Picker<Boolean> m_pickerMovable;
  
  /**
   * Creates a new player picker
   * @param picker_position A picker for the position of a new player
   * @param picker_velocity A picker for the initial velocity vector of the player
   * @param picker_movable A picker for the "movable"
   */
  public PlayerPicker(Vector2dPicker picker_position,
      Vector2dPicker picker_velocity, Picker<Boolean> picker_movable)
  {
    super();
    m_pickerPosition = picker_position;
    m_pickerVelocity = picker_velocity;
    m_pickerMovable = picker_movable;
  }
  
  @Override
  public Picker<Player> duplicate(boolean with_state)
  {
    return new PlayerPicker(m_pickerPosition.duplicate(with_state),
        m_pickerVelocity.duplicate(with_state), m_pickerMovable.duplicate(with_state));
  }

  @Override
  public Player pick()
  {
    Vector2d position = m_pickerPosition.pick();
    Vector2d velocity = m_pickerVelocity.pick();
    boolean movable = m_pickerMovable.pick();
    return new Player(position, velocity, movable);
  }

  @Override
  public void reset()
  {
    m_pickerPosition.reset();
    m_pickerVelocity.reset();
    m_pickerMovable.reset();
  }
}
