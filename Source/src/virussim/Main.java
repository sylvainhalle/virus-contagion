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

import static ca.uqac.lif.cep.Connector.connect;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.cep.widgets.WidgetSink;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.AffineTransform;
import ca.uqac.lif.synthia.random.GaussianFloat;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.random.RandomIntervalFloat;
import virussim.Patient.Health;
import virussim.cep.DrawArena;
import virussim.gui.ArenaWindow;
import virussim.physics.Arena;
import virussim.picker.CirclePicker;
import virussim.picker.HealthMarkovChain;
import virussim.picker.PlayerPicker;
import virussim.picker.RectanglePicker;
import virussim.picker.StepCounter;

public class Main
{
  public static void main(String[] args)
  {
    // Whether to use the Markov model for patients, or
    // a fixed number of steps
    boolean use_markov = false;
    
    // Whether to use a Gaussian distribution for patient positions,
    // or a uniform distribution
    boolean gaussian_positions = false;
    
    // The width and height of the arena
    int width = 640, height = 320;
    
    // The initial velocity of each player
    float velocity = 2;
    
    // The number of players in the arena
    int num_players = 200;
    
    // The probability of a player being movable
    float movable_probability = 0.25f;
    
    // The number of simulation steps after which a player recovers
    // (in the fixed model)
    int recovery_steps = 100;
    
    // The probability of dying when infected (in the Markov model)
    float p_die = 0f;
    
    // The probability of staying infected (in the Markov model)
    float p_infected = 0.999f;
    
    // The position pickers
    Picker<Float> r_w = null, r_h = null;
    if (gaussian_positions)
    {
      r_w = new AffineTransform.AffineTransformFloat(new GaussianFloat(), width / 6, width / 2);
      r_h = new AffineTransform.AffineTransformFloat(new GaussianFloat(), height / 6, height / 2);
    }
    else
    {
      r_w = new AffineTransform.AffineTransformFloat(new RandomFloat(), width, 0);
      r_h = new AffineTransform.AffineTransformFloat(new RandomFloat(), height, 0);
    }
    
    // Create a collection of randomly generated players
    RectanglePicker p_position = new RectanglePicker(r_w, r_h);
    CirclePicker p_velocity = new CirclePicker(
        velocity, new RandomIntervalFloat(0, 2 * Math.PI));
    RandomBoolean p_movable = new RandomBoolean(movable_probability);
    PlayerPicker p_player = new PlayerPicker(p_position, p_velocity, p_movable);
    Set<Patient> players = new HashSet<Patient>(num_players);
    for (int i = 0; i < num_players; i++)
    {
      Patient p = p_player.pick();
      if (i == 0)
      {
        // Set a single player as infected
        p.m_health = Health.INFECTED;
      }
      if (use_markov)
      {
        p.setHealthPicker(new HealthMarkovChain(p_infected, p_die, new RandomFloat()));
      }
      else
      {
        p.setHealthPicker(new StepCounter(recovery_steps));
      }
      players.add(p);
    }
    Arena arena = new Arena(width, height, players);
    
    // Create a widget sink
    Pump pump = new Pump(25);
    ArenaWindow win = new ArenaWindow(width, height, pump);
    WidgetSink w_sink = new WidgetSink(win.getLabel());
    
    // Connect arena to sink, with a pump in between
    connect(arena, pump);
    ApplyFunction draw = new ApplyFunction(new DrawArena(width, height));
    connect(pump, draw);
    connect(draw, w_sink);
    
    // Ready
    win.setVisible(true);
    pump.turn();
  }

}
