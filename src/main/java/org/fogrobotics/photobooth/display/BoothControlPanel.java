package org.fogrobotics.photobooth.display;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
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
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;
import org.fogrobotics.photobooth.model.email.EmailMemoListener;
import org.fogrobotics.photobooth.model.photo.Photo;
import org.fogrobotics.photobooth.model.photo.PhotoMemoListener;
import org.fogrobotics.photobooth.model.photo.PhotoUpdatesListener;

public class BoothControlPanel extends JPanel implements CustomerUpdatesListener, ListSelectionListener, ActionListener,
    PhotoUpdatesListener, EmailMemoListener, PhotoMemoListener
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
  private TextField tfMemo = new TextField();
  private PhotoPanel photoPanel = new PhotoPanel();

  public BoothControlPanel(BoothModel model, BoothController controller)
  {
    model.addCustomerUpdatesListener(this);
    model.addPhotoUpdatesListener(this);
    model.addPhotoMemoListener(this);
    model.addEmailMemoListener(this);
    this.controller = controller;

    setLayout(new BorderLayout());
    JPanel main = new JPanel();
    add(main, BorderLayout.CENTER);
    add(tfMemo, BorderLayout.SOUTH);
    tfMemo.setEditable(false);

    main.add(new JLabel("Customers: "));
    main.add(lCustomers);
    lCustomers.addListSelectionListener(this);
    lCustomers.setVisibleRowCount(5);

    JPanel p = new JPanel();
    p.setLayout(new GridLayout(0, 1));
    main.add(p, BorderLayout.CENTER);

    addCombined(p, new JLabel("Name: "), lbCustomerName);

    addCombined(p, new JLabel("Team Number: "), lbTeamNumber);

    addCombined(p, new JLabel("Email: "), lbEmailAddress);

    main.add(bTakePicture);
    bTakePicture.addActionListener(this);
    bTakePicture.setEnabled(false);
    main.add(bSendEmail);
    bSendEmail.addActionListener(this);
    bSendEmail.setEnabled(false);

    main.add(photoPanel);
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
    if (active == null)
    {
      bTakePicture.setEnabled(true);
      lbCustomerName.setText("");
      lbTeamNumber.setText("");
      lbEmailAddress.setText("");
    }
    else
    {
      bTakePicture.setEnabled(true);
      lbCustomerName.setText(active.getName());
      lbTeamNumber.setText(active.getTeamNumber());
      lbEmailAddress.setText(active.getEmailAddress());
    }
    if (activePhoto != null)
    {
      bSendEmail.setEnabled(true);

    }
    else
    {
      bSendEmail.setEnabled(false);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == bSendEmail)
    {
      controller.sendEmail(active, activePhoto);
      updateDisplay();
    }
    else if (e.getSource() == bTakePicture)
    {
      controller.takePhoto(active);
      updateDisplay();
    }
  }

  @Override
  public void update(Photo p)
  {
    activePhoto = p;
    photoPanel.set(p);
    updateDisplay();
  }

  @Override
  public void emailMemoChange(String memo)
  {
    tfMemo.setText(memo);
  }

  @Override
  public void photoMemoChange(String memo)
  {
    tfMemo.setText(memo);
  }
}
