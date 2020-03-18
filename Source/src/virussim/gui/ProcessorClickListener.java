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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ca.uqac.lif.cep.Processor;

/**
 * Click listener that simply starts/stops the associated processor
 */
public class ProcessorClickListener implements MouseListener
{
  /**
   * The processor to toggle when the mouse is clicked
   */
  /*@ non_null @*/ Processor m_processor;

  /**
   * Whether the processor is started or not
   */
  protected boolean m_started = false;
  
  public ProcessorClickListener(/*@ non_null @*/ Processor p)
  {
    super();
    m_processor = p;
  }
  
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
    // Nothing to do
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    // Nothing to do
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    // Nothing to do
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    // Nothing to do
  }
}
