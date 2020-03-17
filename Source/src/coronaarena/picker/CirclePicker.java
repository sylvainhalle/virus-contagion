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
 * A picker that returns a random 2D vector of fixed modulus. In
 * other words, the output of this picker is a randomly selected point
 * somewhere on a circle of a given radius (hence the name "circle
 * picker"). 
 */
public class CirclePicker implements Vector2dPicker
{
  /**
   * The radius of the circle
   */
  protected float m_radius;
  
  /**
   * A picker used to select an angle (in radians)
   */
  protected Picker<Float> m_anglePicker;
  
  /**
   * Creates a new circle picker
   * @param radius The radius of the circle 
   * @param angle_picker A picker used to select an angle (in radians)
   */
  public CirclePicker(float radius, Picker<Float> angle_picker)
  {
    super();
    m_radius = radius;
    m_anglePicker = angle_picker;
  }
  
  @Override
  public CirclePicker duplicate(boolean with_state)
  {
    return new CirclePicker(m_radius, m_anglePicker.duplicate(with_state));
  }

  @Override
  public Vector2d pick()
  {
    float angle = m_anglePicker.pick();
    double x = m_radius * Math.cos(angle);
    double y = m_radius * Math.sin(angle);
    return new Vector2d(x, y);
  }

  @Override
  public void reset()
  {
    m_anglePicker.reset();    
  }
}
