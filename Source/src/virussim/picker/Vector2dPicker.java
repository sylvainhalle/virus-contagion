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
package virussim.picker;

import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.vector.VectorPicker;
import virussim.physics.Vector2d;

/**
 * A picker that generates 2D vectors. This class only wraps around
 * Synthia's {@link VectorPicker}s.
 */
public class Vector2dPicker implements Picker<Vector2d>
{
  /**
   * The vector picker provided by Synthia
   */
  protected VectorPicker m_picker;
  
  /**
   * Creates a new Vector2d picker
   * @param p The {@link VectorPicker} used to create 2D vectors
   */
  public Vector2dPicker(VectorPicker p)
  {
    super();
    m_picker = p;
  }
  
  @Override
  public Vector2dPicker duplicate(boolean with_state)
  {
    return new Vector2dPicker((VectorPicker) m_picker.duplicate(with_state));
  }

  @Override
  public void reset()
  {
    m_picker.reset();
  }

  @Override
  public Vector2d pick()
  {
    return new Vector2d(m_picker.pick());
  }
}
