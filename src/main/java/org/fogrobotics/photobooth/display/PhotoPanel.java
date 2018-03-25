package org.fogrobotics.photobooth.display;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.fogrobotics.photobooth.model.photo.Photo;

public class PhotoPanel extends JPanel
{
  private static final long serialVersionUID = -2308114501875313218L;
  private BufferedImage img;
  
  public PhotoPanel()
  {
    setSize(500, 500);
    setPreferredSize(getSize());
  }

  @Override
  public void paintComponent (Graphics g)
  {
    super.paintComponent(g);
    if (img != null) {
      g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
  }

  public void set(Photo p)
  {
    img = null;
    if (p != null) {
      File file = p.getArtiface();
      if (file.exists()) {
        try
        {
          img = ImageIO.read(file);
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    repaint();
  }

}
