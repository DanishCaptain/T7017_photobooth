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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.DatabaseBoothException;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;

public class CustomerPanel extends JPanel implements CustomerUpdatesListener, ListSelectionListener, ActionListener
{
  private static final long serialVersionUID = 1224821954460879840L;
  private DefaultListModel<Customer> mCustomers = new DefaultListModel<Customer>();
  private JList<Customer> lCustomers = new JList<Customer>(mCustomers);
  private BoothController controller;
  private Customer active;
  private JLabel lCustomerOid = new JLabel();
  private JTextField tfCustomerName = new JTextField(45);
  private JTextField tfTeamNumber = new JTextField(6);
  private JTextField tfEmailAddress = new JTextField(50);
  private JButton bNewCustomer = new JButton("New Customer");
  private JButton bSave = new JButton("Save");

  public CustomerPanel(CustomerManager cMgr, BoothController controller)
  {
    cMgr.addCustomerUpdatesListener(this);
    this.controller = controller;
    
    setLayout(new BorderLayout());
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(0, 1));
    add(p, BorderLayout.CENTER);
    
    JPanel p0 = new JPanel();
    p0.setBorder(new TitledBorder("Customers: "));;
    p.add(p0);
    
    addCombined(p0, new JLabel(""), new JScrollPane(lCustomers));
    lCustomers.addListSelectionListener(this);
    lCustomers.setVisibleRowCount(5);
    
    addCombined(p0, new JLabel(""), bNewCustomer);
    
    addCombined(p, new JLabel("Id: "), lCustomerOid);

    addCombined(p, new JLabel("Name: "), tfCustomerName);

    addCombined(p, new JLabel("Team Number: "), tfTeamNumber);

    addCombined(p, new JLabel("Email: "), tfEmailAddress);

    p.add(bSave);
    tfCustomerName.setEditable(false);
    tfTeamNumber.setEditable(false);
    tfEmailAddress.setEditable(false);
    bNewCustomer.addActionListener(this);
    bSave.addActionListener(this);
    bSave.setEnabled(false);
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
      bSave.setEnabled(false);
      bNewCustomer.setEnabled(true);
      lCustomerOid.setText("");
      tfCustomerName.setText("");
      tfTeamNumber.setText("");
      tfEmailAddress.setText("");
      tfCustomerName.setEditable(false);
      tfTeamNumber.setEditable(false);
      tfEmailAddress.setEditable(false);
    } else {
      bSave.setEnabled(true);
      bNewCustomer.setEnabled(false);
      if (active.getOid() == null) {
        lCustomerOid.setText("--<new>--");
      } else {
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
    if (e.getSource() == bNewCustomer) {
      this.lCustomers.setSelectedIndex(-1);
      active = new Customer();
      
      updateDisplay();
    } else if (e.getSource() == bSave) {
      if (active != null) {
        lCustomers.setSelectedIndex(-1);
        if (active.getOid() == null) {
          try
          {
            controller.addNew(tfCustomerName.getText(), tfTeamNumber.getText(), tfEmailAddress.getText());
          }
          catch (DatabaseBoothException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } else {
          try
          {
            controller.update(lCustomerOid.getText(), tfCustomerName.getText(), tfTeamNumber.getText(), tfEmailAddress.getText());
          }
          catch (DatabaseBoothException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        active = null;
        updateDisplay();
      }
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
