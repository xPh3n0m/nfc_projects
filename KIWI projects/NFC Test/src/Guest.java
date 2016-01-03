import org.json.simple.JSONObject;


public class Guest {
	
	private int gid;
	private String name;
	private int teamId;
	private int cloakId;
	private double balance;
	
	public Guest(int gid, String name) {
		this.setGid(gid);
		this.setName(name);
	}

	public Guest(int gid, String name, int tid, int cid) {
		this.setGid(gid);
		this.setName(name);
		this.setTeamId(tid);
		this.setCloakId(cid);
		this.setBalance(0.0);
	}
	
	public JSONObject getJSONString() {
		JSONObject j = new JSONObject();
		j.put("gid", gid);
		j.put("guest_name", name);
		j.put("tid", teamId);
		j.put("cid", cloakId);
		
		return j;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getCloakId() {
		return cloakId;
	}

	public void setCloakId(int cloakId) {
		this.cloakId = cloakId;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double b) {
		this.balance = b;
	}
	
	/**
	 * Process a payment
	 * Returns true and updates the balance if the balance is sufficient
	 * Returns false otherwise
	 * @param amount
	 * @return
	 */
	public boolean pay(double amount) {
		if(balance - amount < 0) {
			return false;
		} else {
			setBalance(balance - amount);
			return true;
		}
	}
	
	public void recharge(double amount) {
		setBalance(balance + amount);
	}

}
