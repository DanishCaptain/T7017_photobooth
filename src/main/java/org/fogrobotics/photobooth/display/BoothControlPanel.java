package org.fogrobotics.photobooth.display;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;
import org.fogrobotics.photobooth.photo.Photo;
import org.fogrobotics.photobooth.photo.PhotoManager;
import org.fogrobotics.photobooth.photo.PhotoUpdatesListener;

public class BoothControlPanel extends JPanel implements CustomerUpdatesListener, ListSelectionListener, ActionListener, PhotoUpdatesListener
{
  private static final long serialVersionUID = 8726180160354725859L;
  private DefaultListModel<Customer> mCustomers = new DefaultListModel<Customer>();
  private JList<Customer> lCustomers = new JList<Customer>(mCustomers);
  private JLabel lbCustomerName = new JLabel();
  private JLabel lbTeamNumber = new JLabel();
  private JLabel lbEmailAddress = new JLabel();
  private JButton bTakePicture = new JButton("Take Picture");
  private JButton bSendEmail = new JButton("Send Email");
  private Customer active;
  private BoothController controller;
  private Photo activePhoto;

  public BoothControlPanel(CustomerManager cMgr, PhotoManager pMgr, BoothController controller)
  {
    cMgr.addCustomerUpdatesListener(this);
    pMgr.addPhotoUpdatesListener(this);
    this.controller = controller;
    add(new JLabel("Customers: "));
    add(lCustomers);
    lCustomers.addListSelectionListener(this);
    lCustomers.setVisibleRowCount(5);
    
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(0, 1));
    add(p, BorderLayout.CENTER);

    addCombined(p, new JLabel("Name: "), lbCustomerName);

    addCombined(p, new JLabel("Team Number: "), lbTeamNumber);

    addCombined(p, new JLabel("Email: "), lbEmailAddress);

    add(bTakePicture);
    bTakePicture.addActionListener(this);
    bTakePicture.setEnabled(false);
    add(bSendEmail);
    bSendEmail.addActionListener(this);
    bSendEmail.setEnabled(false);
  }

  @Override
  public void addNew(Customer c)
  {
    mCustomers.addElement(c);
    repaint();
  }
  @Override
  public void update(Customer c)
  {
    repaint();
  }
  
  private void addCombined(JPanel panel, JComponent c1, JComponent c2)
  {
    JPanel p0 = new JPanel();
    p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));
    JPanel p = new JPanel();
    panel.add(p);
    p.add(p0);
    p0.add(c1);
    p0.add(c2);
  }

  @Override
  public void valueChanged(ListSelectionEvent e)
  {
    active = lCustomers.getSelectedValue();
    updateDisplay();
  }

  private void updateDisplay()
  {
    if (active == null) {
      bTakePicture.setEnabled(true);
      lbCustomerName.setText("");
      lbTeamNumber.setText("");
      lbEmailAddress.setText("");
    } else {
      bTakePicture.setEnabled(true);
      lbCustomerName.setText(active.getName());
      lbTeamNumber.setText(active.getTeamNumber());
      lbEmailAddress.setText(active.getEmailAddress());
    }
    if (activePhoto != null) {
      bSendEmail.setEnabled(true);
      
    } else {
      bSendEmail.setEnabled(false);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == bSendEmail) {
      controller.sendEmail(active, activePhoto);
      updateDisplay();
    } else if (e.getSource() == bTakePicture) {
      controller.takePhoto(active);
      updateDisplay();
    }
  }

  @Override
  public void update(Photo p)
  {
    this.activePhoto = p;
    updateDisplay();
  }
}
