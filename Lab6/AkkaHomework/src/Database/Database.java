package Database;

import java.sql.*;

public class Database {
    // SQLite connection string
    private String url = "jdbc:sqlite:data/homework.db";
    //private Connection conn;

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS asks (\n"
                + "	id text PRIMARY KEY,\n"
                + "	asks int\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean insert(String name) {
        String sql = "INSERT INTO asks(id,asks) VALUES(?,1)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void selectAll(){
        String sql = "SELECT id, asks FROM asks";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("id") +  "\t" +
                        rs.getInt("asks"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getAsks(String name){
        String sql = "SELECT asks "
                + "FROM asks WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,name);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                return rs.getInt("asks");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void update(String name) {
        String sql = "UPDATE asks SET asks = asks + 1 where id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */ /*
    public static void main(String[] args) {
        Database db = new Database();
        db.createNewTable();
        db.insert("bu");
        db.selectAll();
        db.getAsks("bu");
        db.update("bu");
        db.getAsks("bu");
    }*/
}
