package org.fogrobotics.photobooth.display;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;
import org.fogrobotics.photobooth.model.photo.Photo;

public class CustomerPanel extends JPanel implements CustomerUpdatesListener, ListSelectionListener, ActionListener
{
  private static final long serialVersionUID = 1224821954460879840L;
  private DefaultListModel<Customer> mCustomers = new DefaultListModel<Customer>();
  private JList<Customer> lCustomers = new JList<Customer>(mCustomers);
  private BoothModel model;
  private BoothController controller;
  private Customer active;
  private JLabel lCustomerOid = new JLabel();
  private JTextField tfCustomerName = new JTextField(45);
  
  private NumberFormat format = NumberFormat.getInstance();
  private NumberFormatter formatter = new NumberFormatter(format);
  private JFormattedTextField tfTeamNumber = new JFormattedTextField(formatter){
    @Override
    protected void processKeyEvent(KeyEvent ev) {
      if (ev.getID() == KeyEvent.KEY_PRESSED && getText().length() <= 1) {
        if ((ev.getKeyCode() == KeyEvent.VK_BACK_SPACE && this.getCaretPosition() == 1) ||
            (ev.getKeyCode() == KeyEvent.VK_DELETE && this.getCaretPosition() == 0)) {
              formatter.setAllowsInvalid(true);
              setText("");
              formatter.setAllowsInvalid(false);
              ev.consume();
              return;
        }
      }
      super.processKeyEvent(ev);
    }
  };

  private JTextField tfEmailAddress = new JTextField(50);
  private JButton bNewCustomer = new JButton("New Customer");
  private JButton bSave = new JButton("Save");
  private JButton bCancel = new JButton("Cancel");
  private DefaultListModel<Photo> mPhotos = new DefaultListModel<Photo>();
  private JList<Photo> lPhotos = new JList<Photo>(mPhotos);
  private PhotoPanel photoPanel = new PhotoPanel();

  public CustomerPanel(BoothModel model, BoothController controller)
  {
    model.addCustomerUpdatesListener(this);
    this.model = model;
    this.controller = controller;

    setLayout(new BorderLayout());
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(0, 1));
    add(p, BorderLayout.CENTER);

    JPanel p0 = new JPanel();
    p0.setBorder(new TitledBorder("Customers: "));

    p.add(p0);

    addCombined(p0, new JLabel(""), new JScrollPane(lCustomers));
    lCustomers.addListSelectionListener(this);
    lCustomers.setVisibleRowCount(5);

    addCombined(p0, new JLabel(""), bNewCustomer);

    addCombined(p, new JLabel("Id: "), lCustomerOid);

    addCombined(p, new JLabel("Name: "), tfCustomerName);

    addCombined(p, new JLabel("Team Number: "), tfTeamNumber);
    tfTeamNumber.setColumns(4);
    format.setGroupingUsed(false);
    formatter.setValueClass(Integer.class);
    formatter.setMinimum(0);
    formatter.setMaximum(9999);
    formatter.setAllowsInvalid(false);
    // If you want the value to be committed on each keystroke instead of focus lost
    formatter.setCommitsOnValidEdit(true);
    
    
    addCombined(p, new JLabel("Email: "), tfEmailAddress);

    JPanel pButtons = new JPanel();
    p.add(pButtons);
    pButtons.add(bSave);
    pButtons.add(bCancel);
    tfCustomerName.setEditable(false);
    tfTeamNumber.setEditable(false);
    tfEmailAddress.setEditable(false);
    bNewCustomer.addActionListener(this);

    bSave.addActionListener(this);
    bSave.setEnabled(false);
    bCancel.addActionListener(this);
    bCancel.setEnabled(false);
    
    p0 = new JPanel();
    p0.setLayout(new BorderLayout());
    p0.setBorder(new TitledBorder("Photos: "));
    add(p0, BorderLayout.EAST);
    p0.add(new JScrollPane(lPhotos), BorderLayout.NORTH);
    p0.add(photoPanel, BorderLayout.CENTER);
    lPhotos.setVisibleRowCount(3);
    lPhotos.addListSelectionListener(this);
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
    if (e.getSource() == lCustomers) {
      active = lCustomers.getSelectedValue();
      mPhotos.clear();
      try
      {
        List<Photo> list = model.getPhotoList(active);
        for (Photo p : list) {
          mPhotos.addElement(p);
        }
        photoPanel.repaint();
      }
      catch (DatabaseBoothException e1)
      {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      updateDisplay();
    } else if (e.getSource() == lPhotos) {
      photoPanel.set(lPhotos.getSelectedValue());
    }
  }

  private void updateDisplay()
  {
    if (active == null)
    {
      bSave.setEnabled(false);
      bCancel.setEnabled(false);
      bNewCustomer.setEnabled(true);
      lCustomerOid.setText("");
      tfCustomerName.setText("");
      tfTeamNumber.setText("");
      tfEmailAddress.setText("");
      tfCustomerName.setEditable(false);
      tfTeamNumber.setEditable(false);
      tfEmailAddress.setEditable(false);
    }
    else
    {
      bSave.setEnabled(true);
      bCancel.setEnabled(true);
      bNewCustomer.setEnabled(false);
      if (active.getOid() == null)
      {
        lCustomerOid.setText("--<new>--");
      }
      else
      {
        lCustomerOid.setText(active.getOid().toString());
      }
      tfCustomerName.setText(active.getName());
      tfTeamNumber.setText(active.getTeamNumber());
      tfEmailAddress.setText(active.getEmailAddress());
      tfCustomerName.setEditable(true);
      tfTeamNumber.setEditable(true);
      tfEmailAddress.setEditable(true);
      tfCustomerName.grabFocus();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == bNewCustomer)
    {
      this.lCustomers.setSelectedIndex(-1);
      active = new Customer();

      updateDisplay();
    }
    else if (e.getSource() == bSave)
    {
      if (active != null)
      {
        if (active.getOid() == null)
        {
          try
          {
            controller.addNew(tfCustomerName.getText(), tfTeamNumber.getText(), tfEmailAddress.getText());
          }
          catch (DatabaseBoothException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        else
        {
          try
          {
            controller.update(lCustomerOid.getText(), tfCustomerName.getText(), tfTeamNumber.getText(),
                tfEmailAddress.getText());
          }
          catch (DatabaseBoothException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        active = null;
        lCustomers.setSelectedIndex(-1);
        updateDisplay();
      }
    }
    else if (e.getSource() == bCancel)
    {
      active = null;
      lCustomers.setSelectedIndex(-1);
      updateDisplay();
    }
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

}
