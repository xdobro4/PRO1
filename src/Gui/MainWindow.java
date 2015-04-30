package Gui;

import PhoneBoook.Item;
import PhoneBoook.ItemsManager;

import javax.swing.*;
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

        return pnlItems;
    }

    private JList createItemList() {
        JList<Item> list = new JList<>(this.itemsManager.getList());

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(3);
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
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Pole jméno a telefonní číslo jsou povinná!", "Warning", JOptionPane.WARNING_MESSAGE);

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
        for(Item item: this.itemsManager.getList()) {
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