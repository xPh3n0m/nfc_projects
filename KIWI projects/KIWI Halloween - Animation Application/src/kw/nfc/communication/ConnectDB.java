package kw.nfc.communication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectDB {

	private Connection conn;
	
	public ConnectDB() {
		
	}
	
	public void connect() throws SQLException {
			this.conn = DriverManager.getConnection(Utility.DB_ONLINE_URL, Utility.DB_USER, Utility.DB_PASSWORD);
	}
	
	public void reconnect() throws SQLException {
		if(!conn.isValid(500)) {
			conn = DriverManager.getConnection(Utility.DB_ONLINE_URL, Utility.DB_USER, Utility.DB_PASSWORD);
		}
	}
	
	public void updateCloackId(int gid, int cid) throws SQLException {
		Statement state;
		state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		state.setQueryTimeout(3);
		String query = ("SELECT * FROM guest WHERE gid=" + gid);
        ResultSet res = state.executeQuery(query);
        res.first();
        
        res.updateInt("cid", cid);
        res.updateRow();
        
        res.close();
        state.close();
	}
	
	public Guest getGuestInfo(int gid) throws SQLException {
        Statement state;
		state = conn.createStatement();
		state.setQueryTimeout(3);
		String query = "SELECT guest_name, tid, cid FROM guest\n";
        query += ("WHERE gid=" + gid);
        ResultSet res = state.executeQuery(query);
        
        res.next();
        
        String name = res.getString("guest_name");
        int tid = res.getInt("tid");
        int cid = res.getInt("cid");

        Guest g = new Guest(gid, name, tid, cid);
        
        res.close();
        state.close();
        
        return g;
	}
	
	public void newOrder(int gid, int nbBeers, int nbSpirits) throws SQLException {
		String query = "INSERT INTO orders (gid, nbbeers, nbspirits) VALUES (?, ?, ?)";
		PreparedStatement psm = conn.prepareStatement(query);
		psm.setQueryTimeout(3);
		psm.setInt(1, gid);
		psm.setInt(2, nbBeers);
		psm.setInt(3, nbSpirits);
		
		psm.executeUpdate();
		
		
        System.out.println("Order added");
	}
	
	public Order guestOrderStatus(int gid) throws SQLException {
		Statement state;
		state = conn.createStatement();
		String query = "SELECT nbbeers, nbspirits FROM orders\n";
        query += ("WHERE gid=" + gid);
        ResultSet res = state.executeQuery(query);
        
        int nBbeers = 0;
        int nbSpirits = 0;
        
        while(res.next()) {
        	nBbeers += res.getInt("nbbeers");
        	nbSpirits += res.getInt("nbspirits");
        }
        res.close();
        state.close();
        
        return new Order(gid, nBbeers, nbSpirits);
	}
	
	public void insertAnimation(int gid, int animation) throws SQLException {
		String query = "INSERT INTO animation (gid, result) VALUES (?, ?)";
		PreparedStatement psm = conn.prepareStatement(query);
		psm.setInt(1, gid);
		psm.setInt(2, animation);
		
		psm.executeUpdate();
		
		
        System.out.println("Animation added");
	}
}
