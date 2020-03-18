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


import static ca.uqac.lif.cep.Connector.connect;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.Cumulate;
import ca.uqac.lif.cep.functions.CumulativeFunction;
import ca.uqac.lif.cep.functions.FunctionTree;
import ca.uqac.lif.cep.functions.TurnInto;
import ca.uqac.lif.cep.mtnp.DrawPlot;
import ca.uqac.lif.cep.mtnp.UpdateTableMap;
import ca.uqac.lif.cep.tmf.CountDecimate;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.cep.tuples.MapToTuple;
import ca.uqac.lif.cep.tuples.MergeTuples;
import ca.uqac.lif.cep.tuples.ScalarIntoTuple;
import ca.uqac.lif.cep.util.Maps;
import ca.uqac.lif.cep.util.Multiset;
import ca.uqac.lif.cep.util.Numbers;
import ca.uqac.lif.cep.widgets.WidgetSink;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.random.AffineTransform;
import ca.uqac.lif.synthia.random.GaussianFloat;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.random.RandomIntervalFloat;
import ca.uqac.lif.synthia.vector.HyperspherePicker;
import ca.uqac.lif.synthia.vector.PrismPicker;
import virussim.Patient;
import virussim.Patient.Health;
import virussim.cep.DrawArena;
import virussim.cep.GetHealth;
import virussim.gui.BitmapJFrame;
import virussim.gui.ProcessorClickListener;
import virussim.physics.Arena;
import virussim.picker.HealthMarkovChain;
import virussim.picker.PlayerPicker;
import virussim.picker.StepCounter;
import virussim.picker.Vector2dPicker;

public class Main
{
  @SuppressWarnings("unchecked")
  public static void main(String[] args)
  {
    // The starting seed for all RNGs
    int seed = 0;
    
    // Whether to use the Markov model for patients, or
    // a fixed number of steps
    boolean use_markov = false;
    
    // Whether to use a Gaussian distribution for patient positions,
    // or a uniform distribution
    boolean gaussian_positions = false;
    
    // The width and height of the arena
    int width = 640, height = 320;
    
    // The initial velocity of each player
    float velocity = 3;
    
    // The number of players in the arena
    int num_players = 200;
    
    // The probability of a player being movable
    float movable_probability = 1f;
    
    // The number of simulation steps after which a player recovers
    // (in the fixed model)
    int recovery_steps = 75;
    
    // The probability of dying when infected (in the Markov model)
    float p_die = 0f;
    
    // The probability of staying infected (in the Markov model)
    float p_infected = 0.999f;
    
    // The position pickers
    Picker<Float> r_w = null, r_h = null;
    if (gaussian_positions)
    {
      GaussianFloat gf_w = new GaussianFloat();
      gf_w.setSeed(seed++);
      r_w = new AffineTransform.AffineTransformFloat(gf_w, width / 6, width / 2);
      GaussianFloat gf_h = new GaussianFloat();
      gf_h.setSeed(seed++);
      r_h = new AffineTransform.AffineTransformFloat(gf_h, height / 6, height / 2);
    }
    else
    {
      RandomFloat rf_w = new RandomFloat();
      rf_w.setSeed(seed++);
      r_w = new AffineTransform.AffineTransformFloat(rf_w, width, 0);
      RandomFloat rf_h = new RandomFloat();
      rf_h.setSeed(seed++);
      r_h = new AffineTransform.AffineTransformFloat(rf_h, height, 0);
    }
    
    // Create a collection of randomly generated players, and add them
    // to the arena
    Vector2dPicker p_position = new Vector2dPicker(new PrismPicker(r_w, r_h));
    RandomIntervalFloat rif = new RandomIntervalFloat(0, 2 * Math.PI);
    rif.setSeed(seed++);
    Vector2dPicker p_velocity = new Vector2dPicker(new HyperspherePicker(velocity, rif));
    RandomBoolean p_movable = new RandomBoolean(movable_probability);
    p_movable.setSeed(seed++);
    PlayerPicker p_player = new PlayerPicker(p_position, p_velocity, p_movable);
    Set<Patient> players = new HashSet<Patient>(num_players);
    for (int i = 0; i < num_players; i++)
    {
      Patient p = p_player.pick();
      if (i == 0)
      {
        // Set a single player as infected
        p.setHealthState(Health.INFECTED);
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
    
    // Connect arena to pump and to fork
    Pump pump = new Pump(50);
    connect(arena, pump);
    Fork fork = new Fork(2);
    connect(pump, fork);
    
    {
      // Branch 1: render arena and draw in window
      ApplyFunction draw = new ApplyFunction(new DrawArena(width, height));
      connect(fork, 0, draw, 0);
      BitmapJFrame window = new BitmapJFrame(width, height, "Simulation");
      window.getLabel().addMouseListener(new ProcessorClickListener(pump));
      WidgetSink ws = new WidgetSink(window.getLabel());
      connect(draw, ws);
      window.setLocation(50, 50);
      window.setVisible(true);
    }
    
    {
      // Branch 2: compute infected
      int decim_interval = 25;
      CountDecimate decim = new CountDecimate(decim_interval);
      connect(fork, 1, decim, 0);
      Fork f = new Fork(2);
      connect(decim, f);
      TurnInto one = new TurnInto(decim_interval);
      connect(f, 0, one, 0);
      Cumulate sum = new Cumulate(new CumulativeFunction<Number>(Numbers.addition));
      connect(one, sum);
      ApplyFunction stt = new ApplyFunction(new ScalarIntoTuple("t"));
      connect(sum, stt);
      ApplyFunction count = new ApplyFunction(new FunctionTree(
          MapToTuple.instance,
            new FunctionTree(Multiset.getCardinalities,
                new FunctionTree(Maps.multiValues, new Maps.ApplyAll(GetHealth.instance)))));
      connect(f, 1, count, 0);
      ApplyFunction merge = new ApplyFunction(new MergeTuples());
      connect(stt, 0, merge, 0);
      connect(count, 0, merge, 1);
      UpdateTableMap table = new UpdateTableMap("t", "HEALTHY", "INFECTED", "RECOVERED");
      connect(merge, table);
      Scatterplot plot = new Scatterplot();
      
      DrawPlot draw = new DrawPlot(plot);
      connect(table, draw);
      BitmapJFrame window = new BitmapJFrame(640, 480, "Evolution");
      WidgetSink ws = new WidgetSink(window.getLabel());
      connect(draw, ws);
      window.setLocation(100, 200);
      window.setVisible(true);
    }
    
    // Ready
    pump.turn();
  }

}
