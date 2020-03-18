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

import ca.uqac.lif.synthia.replay.Playback;
import virussim.Patient;
import virussim.Patient.Health;

public class StepCounter extends Playback<Patient.Health>
{
  public StepCounter(int num_steps)
  {
    super(createArray(num_steps));
  }
  
  protected static Patient.Health[] createArray(int num_steps)
  {
    Patient.Health[] out = new Patient.Health[num_steps];
    for (int i = 0; i < num_steps - 1; i++)
    {
      out[i] = Health.INFECTED;
    }
    out[num_steps - 1] = Health.RECOVERED;
    return out;
  }
}
