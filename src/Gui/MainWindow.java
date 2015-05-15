package Gui;

import PhoneBoook.Item;
import PhoneBoook.ItemsManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

public class MainWindow extends JFrame {

    public static final String SEARCH_STRING = "Vyhledat";

    private ItemsManager itemsManager = null;
    private JPanel pnlItems = new JPanel(new BorderLayout());

    // form
    private JTextField fieldName = new JTextField("Ales", 10); // @todo
    private JTextField fieldPhone = new JTextField("123456789", 10); // @todo
    private JTextField fieldAddress = new JTextField("doma", 10); // @todo

    // search
    private JTextField search = new JTextField(SEARCH_STRING, 15);

    // JList
    private JList<Item> list;

    // buttons
    private JButton editButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton addButton;

    // construct
    public MainWindow() {
        JFileChooser fc = setupFileChooser();

        File file = null;
        Integer i = 0;
        do {
            Integer returnVal = fc.showOpenDialog(MainWindow.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            } else {
                showMessage("Nejprve je třeba vybrat CSV soubor!");
            }

        } while (++i < 2 && file == null); // pokud uzivatel nevybere soubor 2x program konci

        if (file == null) {
            showMessage("Nebyl zvolen žádný soubor!");
            System.exit(0);
            return;
        }

        this.itemsManager = new ItemsManager(file.getAbsoluteFile().toString());

        setTitle("Evidence kontaktů");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // items
        this.add(createPanel(), BorderLayout.CENTER);
        // add panel
        this.add(addCreatePanel(), BorderLayout.NORTH);

        this.pack();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
    }

    // GUI
    private JPanel createPanel() {
        // list
        pnlItems.add(createSortBar(), BorderLayout.NORTH);

        pnlItems.add(createItemList(), BorderLayout.CENTER);
        pnlItems.add(new JScrollPane(list));

        pnlItems.add(createListButtons(), BorderLayout.SOUTH);

        return pnlItems;
    }

    private JPanel createSortBar() {
        JPanel panel = new JPanel();

        JButton btPridat = new JButton("Řadit A-Z");
        panel.add(btPridat);
        btPridat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemsManager.sort();
                list.setListData(itemsManager.getVectorList());
                handleSearch();
            }

        });


        JButton btVypsat = new JButton("Řadit Z-A");
        panel.add(btVypsat);
        btVypsat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemsManager.sort(true);
                list.setListData(itemsManager.getVectorList());
                handleSearch();
            }
        });

        panel.add(search);
        search.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleSearch();
            }

        });
        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (search.getText().equals(SEARCH_STRING)) {
                    search.setText("");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (search.getText().equals("")) {
                    clearSearch();
                }
            }
        });

        return panel;
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
                handleSearch();
            }
        });

        return this.deleteButton;
    }

    private JList createItemList() {
        this.list = new JList<>(this.itemsManager.getList());

        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setLayoutOrientation(JList.VERTICAL);
        this.list.setVisibleRowCount(10);
        this.list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (list.getSelectedIndex() == -1) {
                        //No selection, disable fire button.
                        editButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                    } else {
                        if (saveButton.isVisible()) {
                            switchAddEditButtons(false, true);
                            emptyInputs();
                        }

                        //Selection, enable the fire button.
                        editButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });

        return list;
    }

    private JToolBar addCreatePanel() {
        JLabel l;

        JToolBar addPanel = new JToolBar();
        addPanel.setBorder(BorderFactory.createTitledBorder("Detail kontaktu"));

        l = new JLabel("Jméno: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldName);
        addPanel.add(this.fieldName);

        l = new JLabel("Telefonní čísla: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldPhone);
        addPanel.add(this.fieldPhone);

        l = new JLabel("Adresa: ", JLabel.TRAILING);
        addPanel.add(l);
        l.setLabelFor(this.fieldAddress);
        addPanel.add(this.fieldAddress);

        this.addButton = new JButton("Přidat");
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd(e);
                clearSearch();
            }
        });
        addPanel.add(this.addButton);

        this.saveButton = new JButton("Uložit");
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave(e);
                handleSearch();
            }
        });
        this.saveButton.setVisible(false);
        addPanel.add(this.saveButton);

        return addPanel;
    }

    private void clearSearch() {
        this.search.setText(SEARCH_STRING);
    }

    // ACTIONS
    private void handleAdd(ActionEvent e) {
        String name = this.fieldName.getText();
        String phone = this.fieldPhone.getText();
        String address = this.fieldAddress.getText();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            this.showMessage("Všechna pole jsou povinná!");

            return;
        }

        Item item = new Item(name, address, phone);
        this.itemsManager.add(item);
        this.itemsManager.flush();
        this.list.setListData(itemsManager.getVectorList());
    }

    private void handleSave(ActionEvent e) {
        if (this.list.getSelectedIndex() == -1) {
            return;
        }

        Item item = this.list.getSelectedValue();
        item.setName(this.fieldName.getText());
        item.setPhone(this.fieldPhone.getText());
        item.setAddress(this.fieldAddress.getText());

        this.emptyInputs();

        this.itemsManager.flush();
        this.list.repaint();

        switchAddEditButtons(false, true);

        this.itemsManager.flush();
    }

    private void handleEdit(ActionEvent e) {
        if (this.list.getSelectedIndex() == -1) {
            return;
        }

        Item item = this.list.getSelectedValue();
        this.fieldName.setText(item.getName());
        this.fieldPhone.setText(item.getPhone());
        this.fieldAddress.setText(item.getAddress());

        switchAddEditButtons(true, false);
    }

    private void handleDelete(ActionEvent e) {
        if (this.list.getSelectedIndex() == -1) {
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null, "Opravdu odstranit záznam?", "", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.NO_OPTION) {
            return;
        }

        Item item = this.list.getSelectedValue();
        this.itemsManager.remove(item);
        this.itemsManager.flush();
        this.list.repaint();
        this.showMessage("Odstraněno!");

        if (this.list.getSelectedIndex() >= this.list.getModel().getSize()) {
            this.deleteButton.setEnabled(false);
        }
    }

    private void handleSearch() {
        if (search.getText().isEmpty() || search.getText().equals(SEARCH_STRING)) {
            list.setListData(itemsManager.getVectorList());
        } else {
            Vector<Item> items = itemsManager.getVectorList();
            for (Item item : itemsManager.getVectorList()) {
                if (!item.toString().contains(search.getText())) {
                    items.remove(item);
                }
            }
            list.setListData(items);
        }
    }

    // UTILS
    private JFileChooser setupFileChooser() {
        JFileChooser fc = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV soubor", "csv");
        fc.setFileFilter(filter);

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(workingDirectory);

        return fc;
    }

    private void showMessage(String message) {
        final JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void emptyInputs() {
        this.fieldName.setText("");
        this.fieldPhone.setText("");
        this.fieldAddress.setText("");
    }

    private void switchAddEditButtons(boolean save, boolean edit) {
        saveButton.setVisible(save);
        addButton.setVisible(edit);
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

