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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A {@link JFrame} containing a bitmap
 */
public class BitmapJFrame extends JFrame
{
  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The label containing the image in the window
   */
  /*@ non_null @*/ protected JLabel m_label;
  
  /**
   * The panel containing the window contents
   */
  /*@ non_null @*/ protected JPanel m_panel;
  
  public BitmapJFrame(int width, int height, String title)
  {
    super();
    m_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    add(m_panel);
    setTitle(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(width, height);
    m_label = new JLabel();
    m_panel.add(m_label);
  }
  
  /**
   * Gets the label containing the image in the window
   * @return The label
   */
  public JLabel getLabel()
  {
    return m_label;
  }
}
