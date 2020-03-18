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

import java.util.Map;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.tuples.Tuple;
import ca.uqac.lif.cep.tuples.TupleMap;
import virussim.Patient;

/**
 * In the state of an arena, counts
 * @author sylvain
 *
 */
@SuppressWarnings("rawtypes")
public class CountStatus extends UnaryFunction<Map,Tuple>
{
  /**
   * The keys of the resulting tuples
   */
  protected String[] m_keys;
  
  /**
   * Creates a new instance of the function
   * @param keys The keys
   */
  public CountStatus(Object ... keys)
  {
    super(Map.class, Tuple.class);
    m_keys = new String[keys.length];
    for (int i = 0; i < keys.length; i++)
    {
      m_keys[i] = keys[i].toString();
    }
  }

  @Override
  public Tuple getValue(Map m)
  {
    TupleMap t = new TupleMap();
    for (String k : m_keys)
    {
      t.put(k, 0);
    }
    for (Object o : m.values())
    {
      Patient p = (Patient) o;
      String s = p.getHealthState().toString();
      t.put(s, ((int) t.get(s)) + 1);
    }
    return t;
  }
}
