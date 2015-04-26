package PhoneBoook;

public class Item {
    private String name = null;

    private String address = null;

    private String phone = null;

    private String description = null;

    public Item(String name, String address, String phone, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getName() + ItemsManager.SEPARATOR + this.getAddress() + ItemsManager.SEPARATOR + this.getPhone() + ItemsManager.SEPARATOR + this.getDescription();
    }
}
