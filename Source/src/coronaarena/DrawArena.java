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
package coronaarena;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * From a map of players, a BeepBeep {@link Function} object that produces 
 * an image showing their state and their position.
 */
@SuppressWarnings("rawtypes")
public class DrawArena extends UnaryFunction<Map,BufferedImage>
{
  /**
   * The width of the image
   */
  protected int m_width;

  /**
   * The height of the image
   */
  protected int m_height;
  
  /**
   * The color given to "infected" players
   */
  protected static Color s_infectedColor = new Color(176, 103, 48);
  
  /**
   * The color given to "healthy" players
   */
  protected static Color s_healthyColor = new Color(176, 197, 202);
  
  /**
   * The color given to "recovered" players
   */
  protected static Color s_recoveredColor = new Color(194, 142, 190);

  /**
   * Creates a new instance of the function for a specific width and height.
   * @param width The width of the image
   * @param height The height of the image
   */
  public DrawArena(int width, int height)
  {
    super(Map.class, BufferedImage.class);
    m_width = width;
    m_height = height;
  }

  @Override
  public BufferedImage getValue(Map x)
  {
    BufferedImage img = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) img.getGraphics();
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, m_width, m_height);
    for (Object o : x.values())
    {
      render(g2, (Player) o);
    }
    return img;
  }

  /**
   * Draws a player on the image
   * @param p The player to draw
   */
  protected void render(Graphics2D g2, Player p)
  {
    switch (p.getHealthState())
    {
    case HEALTHY:
      g2.setColor(s_healthyColor);
      break;
    case INFECTED:
      g2.setColor(s_infectedColor);
      break;
    default:
      g2.setColor(s_recoveredColor);
      break;
    }
    g2.fillOval((int) (p.getPosition().getX() - p.getRadius()), 
        (int) (p.getPosition().getY() - p.getRadius()),
        (int) (2 * p.getRadius()) , (int) (2 * p.getRadius()));
  }
}
