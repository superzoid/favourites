package model;

public class Favourite {
    private String sku;
    private int purchaseCount;

    private Favourite(String sku, int purchaseCount) {
        this.sku = sku;
        this.purchaseCount = purchaseCount;
    }

    public static Favourite fav(String sku, int count) {
        return new Favourite(sku,count);
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favourite favourite = (Favourite) o;

        return purchaseCount == favourite.purchaseCount && (sku != null ? sku.equals(favourite.sku) : favourite.sku == null);
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + purchaseCount;
        return result;
    }

    @Override
    public String toString() {
        return "Favourite{" +
            "sku='" + sku + '\'' +
            ", purchaseCount=" + purchaseCount +
            '}';
    }
}
