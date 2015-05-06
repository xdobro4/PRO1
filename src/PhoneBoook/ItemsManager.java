package PhoneBoook;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Collections;
import java.util.Vector;

public class ItemsManager {

    public static final String SEPARATOR = ",";

    private DefaultListModel<Item> list;

    private String filePath;

    public ItemsManager(String filePath) {
        this.filePath = filePath;
    }

    public DefaultListModel<Item> readCsv() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = SEPARATOR;

        this.list = new DefaultListModel<>();

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] item = line.split(cvsSplitBy);

                if (item.length == 3)
                    this.list.addElement(new Item(item[0], item[1], item[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return this.list;
    }

    public boolean flush() {

        try {
            FileWriter writer = new FileWriter(this.filePath);
            for (Item item : this.getVectorList()) {

                writer.append(item.toString());
                writer.append("\n");

                writer.flush();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public DefaultListModel<Item> getList() {
        if (this.list == null) {
            this.readCsv();
        }

        return this.list;
    }

    public ItemsManager add(Item item) {
        this.getList().addElement(item);

        return this;
    }

    public ItemsManager remove(Item item) {
        this.getList().removeElement(item);

        return this;
    }

    public void sort(boolean reverse) {
        Vector<Item> list = this.getVectorList();
        Collections.sort(list);

        if (reverse) {
            Collections.reverse(list);
        }

        this.list.removeAllElements();
        for (Item item : list) {
            this.list.addElement(item);
        }
    }

    public void sort() {
        this.sort(false);
    }


    public Vector<Item> getVectorList() {
        Vector<Item> items = new Vector<>();
        for (Object item : this.getList().toArray()) {
            items.add((Item) item);
        }

        return items;
    }
}
