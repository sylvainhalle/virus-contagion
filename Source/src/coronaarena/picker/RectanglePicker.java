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
import coronaarena.Vector2d;

/**
 * Generates a 2D vector by independently picking a value for the
 * "x" and the "y" coordinate of the vector.
 */
public class RectanglePicker implements Vector2dPicker
{
  /**
   * A picker for the "x" position of the vector
   */
  protected Picker<Integer> m_pickerX;
  
  /**
   * A picker for the "y" position of the vector
   */
  protected Picker<Integer> m_pickerY;
  
  /**
   * Creates a new rectangle picker
   * @param picker_x A picker for the "x" position of the vector
   * @param picker_y A picker for the "y" position of the vector
   */
  public RectanglePicker(Picker<Integer> picker_x, Picker<Integer> picker_y)
  {
    super();
    m_pickerX = picker_x;
    m_pickerY = picker_y;
  }
  
  @Override
  public Vector2d pick()
  {
    return new Vector2d(m_pickerX.pick(), m_pickerY.pick());
  }

  @Override
  public void reset()
  {
    m_pickerX.reset();
    m_pickerY.reset();
  }

  @Override
  public RectanglePicker duplicate(boolean with_state)
  {
    return new RectanglePicker(m_pickerX.duplicate(with_state), m_pickerY.duplicate(with_state));
  }
}
