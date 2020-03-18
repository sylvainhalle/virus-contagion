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

import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.tmf.Pump;
import ca.uqac.lif.cep.widgets.WidgetSink;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import ca.uqac.lif.synthia.random.RandomInteger;
import ca.uqac.lif.synthia.random.RandomIntervalFloat;
import virussim.Patient.Health;
import virussim.cep.DrawArena;
import virussim.physics.Arena;
import virussim.picker.CirclePicker;
import virussim.picker.HealthMarkovChain;
import virussim.picker.PlayerPicker;
import virussim.picker.RectanglePicker;

public class Main
{
  public static void main(String[] args)
  {
    // The width and height of the arena
    int width = 640, height = 320;
    
    // The initial velocity of each player
    float velocity = 2;
    
    // The number of players in the arena
    int num_players = 200;
    
    // The probability of a player being movable
    float movable_probability = 0.75f;
    
    // The probability of dying when infected
    float p_die = 0f;
    
    // The probability of staying infected
    float p_infected = 0.999f;
    
    // Create a collection of randomly generated players
    RandomInteger r_w = new RandomInteger(0, width);
    r_w.setSeed(1);
    RandomInteger r_h = new RandomInteger(0, height);
    r_h.setSeed(100);
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
      RandomFloat rf = new RandomFloat();
      rf.setSeed(i);
      p.setHealthChain(new HealthMarkovChain(p_infected, p_die, rf));
      players.add(p);
    }
    
    // Add the players to the arena
    Arena arena = new Arena(width, height, players);

    // Create a window to display the arena
    JFrame arena_window = new JFrame();
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    arena_window.add(panel);
    arena_window.setTitle("Simulation");
    arena_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    arena_window.setSize(width, height);
    JLabel img_label = new JLabel();
    panel.add(img_label);
    
    // Create a widget sink
    WidgetSink w_sink = new WidgetSink(img_label);
    
    // Connect arena to sink, with a pump in between
    Pump pump = new Pump(25);
    connect(arena, pump);
    ApplyFunction draw = new ApplyFunction(new DrawArena(width, height));
    connect(pump, draw);
    connect(draw, w_sink);
    
    // Ready
    arena_window.setVisible(true);
    pump.start();
    //pump.turn();
  }

}
