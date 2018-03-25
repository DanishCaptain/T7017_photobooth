package org.fogrobotics.photobooth.display;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.photo.PhotoManager;

public class PhotoBoothDisplay
{

  private JFrame frame;

  public PhotoBoothDisplay(CustomerManager cMgr, PhotoManager pMgr, BoothController controller)
  {
    frame = new JFrame("7017 Photo Both");
    JTabbedPane tabs = new JTabbedPane();
    frame.setContentPane(tabs);
    
    tabs.add("Photo Booth", new BoothControlPanel(cMgr, pMgr, controller));
    tabs.add("Customers", new CustomerPanel(cMgr, controller));
    frame.setSize(700,500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
//    frame.pack();
  }

}
