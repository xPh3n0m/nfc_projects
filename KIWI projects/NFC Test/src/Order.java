
public class Order {
	
	private int orderId;
	private int guestId;
	private int menuItem;
	private int count;
	
	public Order(int oid, int gid, int mid, int count) {
		orderId = oid;
		guestId = gid;
		menuItem = mid;
		this.count = count;
	}
	
	public Order(int gid, int nB, int nS) {
		orderId = Utility.currentOrder++;
		guestId = gid;
		nbBeers = nB;
		nbSpirits = nS;
	}

	public String toString() {
		return new String("Order " + orderId +" for guest " + guestId + ": " + nbBeers + " beers & " + nbSpirits + " spirits");
	}

}
