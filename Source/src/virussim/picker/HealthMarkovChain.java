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
import ca.uqac.lif.synthia.sequence.MarkovChain;
import ca.uqac.lif.synthia.util.Constant;
import virussim.Patient;

public class HealthMarkovChain extends MarkovChain<Patient.Health>
{
  /**
   * The constant picker representing the "INFECTED" health state
   */
  protected static final Constant<Patient.Health> s_infected = new Constant<Patient.Health>(Patient.Health.INFECTED);
  
  /**
   * The constant picker representing the "HEALTHY" health state
   */
  protected static final Constant<Patient.Health> s_healthy = new Constant<Patient.Health>(Patient.Health.HEALTHY);
  
  /**
   * The constant picker representing the "RECOVERED" health state
   */
  protected static final Constant<Patient.Health> s_recovered = new Constant<Patient.Health>(Patient.Health.RECOVERED);
  
  /**
   * The constant picker representing the "DEAD" health state
   */
  protected static final Constant<Patient.Health> s_dead = new Constant<Patient.Health>(Patient.Health.DEAD);
  
  /**
   * Creates a new instance of the health Markov chain
   * @param p_infected The probability of staying infected in the
   * next iteration
   * @param p_dead The probability of dying in the next iteration
   * @param float_source A random source to pick the states
   */
  public HealthMarkovChain(float p_infected, float p_dead, Picker<Float> float_source)
  {
    super(float_source);
    add(0, s_infected);
    add(1, s_recovered);
    add(2, s_dead);
    add(0, 0, p_infected);
    add(0, 2, p_dead);
    add(0, 1, 1 - p_infected - p_dead);
    add(1, 1, 1);
    add(2, 2, 1);
  }

}
