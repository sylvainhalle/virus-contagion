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
package virussim.gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.uqac.lif.cep.Processor;

public class ArenaWindow extends JFrame
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The label containing the image in the window
   */
  /*@ non_null @*/ JLabel m_label;
  
  /**
   * The processor to toggle when the mouse is clicked
   */
  /*@ non_null @*/ Processor m_processor;
  
  /**
   * Whether the processor is started or not
   */
  protected boolean m_started = false;
  
  /**
   * Creates a new arena window
   * @param width
   * @param height
   */
  public ArenaWindow(int width, int height, /*@ non_null @*/ Processor p)
  {
    super();
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    add(panel);
    setTitle("Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(width, height);
    m_label = new JLabel();
    panel.add(m_label);
    m_processor = p;
    panel.addMouseListener(new ClickListener());
  }
  
  /**
   * Gets the label containing the image in the window
   * @return The label
   */
  public JLabel getLabel()
  {
    return m_label;
  }
  
  protected class ClickListener implements MouseListener
  {
    @Override
    public void mouseClicked(MouseEvent e)
    {
      if (m_started)
      {
        m_processor.stop();
        m_started = false;
      }
      else
      {
        m_processor.start();
        m_started = true;
      }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      // TODO Auto-generated method stub
      
    }
  }
}
