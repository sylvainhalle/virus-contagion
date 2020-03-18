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

import ca.uqac.lif.cep.functions.UnaryFunction;
import virussim.Patient;

/**
 * Function that extracts the health status of a given patient
 */
public class GetHealth extends UnaryFunction<Patient,String>
{
  /**
   * A single visible instance of the function
   */
  public static final transient GetHealth instance = new GetHealth();
  
  /**
   * Creates a new instance of the function
   */
  protected GetHealth()
  {
    super(Patient.class, String.class);
  }

  @Override
  public String getValue(Patient p)
  {
    return p.getHealthState().toString();
  }
}
