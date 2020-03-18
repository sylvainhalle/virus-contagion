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
package virussim.cep;

import java.util.Queue;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.tmf.Source;
import virussim.physics.Arena;

public class ArenaSource extends Source
{
  /**
   * The arena to use as a source
   */
  /*@ non_null @*/ protected Arena m_arena;
  
  /**
   * Creates a new arena source
   * @param a The arena to use as a source
   */
  public ArenaSource(/*@ non_null @*/ Arena a)
  {
    super(1);
    m_arena = a;
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    outputs.add(new Object[] {m_arena.getBalls()});
    m_arena.update();
    return true;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    throw new UnsupportedOperationException("This source cannot be duplicated");
  }
}
