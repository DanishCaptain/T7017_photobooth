package org.fogrobotics.photobooth.display;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.BoothModel;

public class PhotoBoothDisplay
{

  private JFrame frame;

  public PhotoBoothDisplay(BoothModel model, BoothController controller)
  {
    frame = new JFrame("7017 Photo Both");
    JTabbedPane tabs = new JTabbedPane();
    frame.add(tabs, BorderLayout.CENTER);

    tabs.add("Photo Booth", new BoothControlPanel(model, controller));
    tabs.add("Customers", new CustomerPanel(model, controller));
    frame.setSize(800, 700);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    // frame.pack();
  }

}
