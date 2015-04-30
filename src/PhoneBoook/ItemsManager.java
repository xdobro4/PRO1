package PhoneBoook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Vector;

public class ItemsManager {

    public static final String SEPARATOR = ",";

    private Vector<Item> list;

    private String filePath;

    public ItemsManager(String filePath) {
        this.filePath = filePath;
    }

    public Vector<Item> readCsv() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = SEPARATOR;

        this.list = new Vector<>();

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] item = line.split(cvsSplitBy);

                if (item.length == 4)
                    this.list.add(new Item(item[0], item[1], item[2], item[3]));
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
            for (Item item : this.getList()) {

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

    public Vector<Item> getList() {
        if (this.list == null) {
            this.readCsv();
        }

        return this.list;
    }

    public ItemsManager add(Item item) {
        this.getList().add(item);

        return this;
    }

    public ItemsManager remove(Item item) {
        this.getList().remove(item);

        return this;
    }
}
