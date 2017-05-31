package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favourites {
    private final String customerNo;
    private List<Favourite> favs = new ArrayList<Favourite>();

    public Favourites(String customerNo, Favourite... favouritesToAdd) {
        this.customerNo = customerNo;
        add(favouritesToAdd);
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void add(Favourite... favouritesToAdd) {
        Collections.addAll(this.favs, favouritesToAdd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favourites that = (Favourites) o;

        return (customerNo != null ? customerNo.equals(that.customerNo) : that.customerNo == null) && (favs != null ? favs.equals(that.favs) : that.favs == null);
    }

    @Override
    public int hashCode() {
        int result = customerNo != null ? customerNo.hashCode() : 0;
        result = 31 * result + (favs != null ? favs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Favourites{" +
            "customerNo='" + customerNo + '\'' +
            ", favs=" + favs +
            '}';
    }
}
