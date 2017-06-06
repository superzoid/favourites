package fav.ws.spring.boot.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Favourites implements Serializable{
    private String customerNo;
    private List<Favourite> favs = Collections.emptyList();

    public Favourites() {
    }

    public Favourites(String customerNo, List<Favourite> favs) {
        this.customerNo = customerNo;
        this.favs = favs;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public List<Favourite> getFavs() {
        return favs;
    }

    public void setFavs(List<Favourite> favs) {
        this.favs = favs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favourites that = (Favourites) o;

        if (customerNo != null ? !customerNo.equals(that.customerNo) : that.customerNo != null) return false;
        return favs != null ? favs.equals(that.favs) : that.favs == null;
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
