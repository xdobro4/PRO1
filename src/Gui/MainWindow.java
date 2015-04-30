package Gui;

import PhoneBoook.Item;
import PhoneBoook.ItemsManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {

    public static final String ITEMS_PATH = System.getProperty("user.dir") + "/src/items.csv";

    private ItemsManager itemsManager = new ItemsManager(ITEMS_PATH);

    // form
    private JTextField fieldName = new JTextField("Test", 10);
    private JTextField fieldPhone = new JTextField("123456789;123456789", 10);
    private JTextField fieldAddress = new JTextField(10);
    private JTextField fieldDescription = new JTextField(10);

    // JList
    private JList<Item> list;

    // buttons
    private JButton editButton;
    private JButton deleteButton;

    public MainWindow() {
        setTitle("Evidence kontaktů");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initWindow();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                printItems(); // @todo

                super.windowClosing(e);
            }
        });
    }

    private void initWindow() {
        this.printItems(); // @todo

        // items
        JPanel pnlItems = createPanelItems();
        add(pnlItems, BorderLayout.CENTER);

        // toolbar
        add(createToolbar(), BorderLayout.NORTH);

        pack();
    }

    private JPanel createPanelItems() {
        JPanel pnlItems = new JPanel(new BorderLayout());

        // add panel
        pnlItems.add(addCreatePanel(), BorderLayout.NORTH);

        // list
        pnlItems.add(createItemList(), BorderLayout.CENTER);


        pnlItems.add(createListButtons(), BorderLayout.SOUTH);
        return pnlItems;
    }

    private JPanel createListButtons() {
        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(createEditButton(), BorderLayout.SOUTH);
        buttons.add(createDeleteButton(), BorderLayout.NORTH);

        return buttons;
    }

    private JButton createEditButton() {
        this.editButton = new JButton("Editovat");
        this.editButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        this.editButton.setHorizontalTextPosition(AbstractButton.CENTER);
        this.editButton.setEnabled(false);
        this.editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEdit(e);
            }
        });

        return this.editButton;
    }

    private JButton createDeleteButton() {
        this.deleteButton = new JButton("Smazat");
        this.deleteButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        this.deleteButton.setHorizontalTextPosition(AbstractButton.CENTER);
        this.deleteButton.setEnabled(false);
        this.deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete(e);
            }
        });

        return this.deleteButton;
    }

    private void handleEdit(ActionEvent e) {
        if (list.getSelectedIndex() == -1) {
            return;
        }

        Item item = list.getSelectedValue();

//        item.setName(fieldName.getText());
//        item.setPhone(fieldPhone.getText());
//        item.setAddress(fieldAddress.getText());
//        item.setDescription(fieldDescription.getText());

        fieldName.setText(item.getName());
        fieldPhone.setText(item.getPhone());
        fieldAddress.setText(item.getAddress());
        fieldDescription.setText(item.getDescription());

//        showMessage("Uloženo!");
    }

    private void handleDelete(ActionEvent e) {
        if (this.list.getSelectedIndex() == -1) {
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to Save your Previous Note First?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.NO_OPTION) {
            return;
        }

        Integer indexVal = list.getSelectedIndex();
        Item item = list.getSelectedValue();
        this.itemsManager.remove(item);
        this.showMessage("Odstraněno!");

        if (this.list.getMaxSelectionIndex() == indexVal) {
            this.deleteButton.setEnabled(false);
        }
    }

    private void showMessage(String message) {
        final JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private JList createItemList() {
        this.list = new JList<>(this.itemsManager.getList());

        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setLayoutOrientation(JList.VERTICAL);
        this.list.setVisibleRowCount(3);
        this.list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    if (list.getSelectedIndex() == -1) {
                        //No selection, disable fire button.
                        editButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                    } else {
                        //Selection, enable the fire button.
                        editButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));

        return list;
    }

    private JPanel addCreatePanel() {
        JLabel l;

        JPanel addPanel = new JPanel();
        addPanel.setBorder(BorderFactory.createTitledBorder("Detail kontaktu"));

        l = new JLabel("Jméno: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldName);
        addPanel.add(this.fieldName);

        l = new JLabel("Telefonní čísla (oddělujte středníkem): ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldPhone);
        addPanel.add(this.fieldPhone);

        l = new JLabel("Adresa: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldAddress);
        addPanel.add(this.fieldAddress);

        l = new JLabel("Poznámka: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldDescription);
        addPanel.add(this.fieldDescription);

        JButton addButton = new JButton("Přidat");
        addButton.addActionListener(this.createAddListener());
        addPanel.add(addButton);

        return addPanel;
    }

    private ActionListener createAddListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.handleAdd();
            }
        };
    }

    private void handleAdd() {
        String name = this.fieldName.getText();
        System.out.println(name);

        String phone = this.fieldPhone.getText();
        System.out.println(phone);

        String address = this.fieldAddress.getText();
        System.out.println(address);

        String description = this.fieldDescription.getText();
        System.out.println(description);

        if (name.isEmpty() || phone.isEmpty()) {
            this.showMessage("Pole jméno a telefonní číslo jsou povinná!");

            return;
        }

        Item item = new Item(name, address, phone, description);
        this.itemsManager.add(item);
        this.itemsManager.flush();
    }

    private JToolBar createToolbar() {
        JToolBar tb = new JToolBar();

//        JButton btPridat = new JButton("Pridat");
//        tb.add(btPridat);
//        btPridat.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                pridatListek();
//            }
//
//        });
//
//        JButton btVypsat = new JButton("Vypsat");
//        tb.add(btVypsat);
//        btVypsat.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                vypsatListky();
//            }
//        });
//
//        JButton btUlozit = new JButton("Ulo�it");
//        tb.add(btUlozit);
//        btUlozit.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                ulozit();
//            }
//        });
//
//        JButton btNacist = new JButton("Na��st");
//        tb.add(btNacist);
//        btNacist.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                nacist();
//            }
//        });

        return tb;
    }

    // DATA
    public boolean writeItem() {
        this.itemsManager.add(new Item("e", "c", "d", "b"));
        return this.itemsManager.flush();
    }

    public void printItems() {
        for (Item item : this.itemsManager.getList()) {
            System.out.println(item.toString());
        }
    }

    // RUN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

}