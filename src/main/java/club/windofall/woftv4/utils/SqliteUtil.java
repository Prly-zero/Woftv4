package club.windofall.woftv4.utils;
import club.windofall.woftv4.Woftv4;

import java.sql.*;
import java.util.*;

public class SqliteUtil {
    private static final String CLASS_NAME = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:plugins/WofT/DB/WofTv4.db";
    public static void Init() throws SQLException, ClassNotFoundException {
        Class.forName(CLASS_NAME);
        try(Connection connection = DriverManager.getConnection(DB_URL)){
            Statement statement = connection.createStatement();
            HashSet<String> set = new HashSet<>();
            set.add("CREATE TABLE IF NOT EXISTS CML"+
                    "(UUID TEXT PRIMARY KEY NOT NULL,"+
                    "LEVEL INT NOT NULL)");
            set.add("CREATE TABLE IF NOT EXISTS ANCHOR"+
                    "(NAME TEXT PRIMARY KEY NOT NULL,"+
                    "INFO TEXT NOT NULL)");
            for (String sql : set){
                statement.executeUpdate(sql);
            }
        }
    }
    public static void Update(String sql) throws SQLException, ClassNotFoundException {
        if ((sql.startsWith("INSERT") || sql.startsWith("UPDATE") || sql.startsWith("DELETE")) && sql.endsWith(";")){
            Class.forName(CLASS_NAME);
            try(Connection connection = DriverManager.getConnection(DB_URL)){
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
        }
    }
    public static Object[][] Query(String sql) throws SQLException, ClassNotFoundException {
        if (sql.startsWith("SELECT") && sql.endsWith(";")){
            Class.forName(CLASS_NAME);
            try(Connection connection = DriverManager.getConnection(DB_URL)){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                List<Object[]> rows = new ArrayList<>();
                while (resultSet.next()){
                    Object[] rowData = new Object[columnCount];
                    for (int i=1;i<=columnCount;i++){
                        rowData[i-1] = resultSet.getObject(i);
                    }
                    rows.add(rowData);
                }
                return rows.toArray(new Object[0][]);
            }
        }
        return null;
    }
    public static void UpdateLC() throws ClassNotFoundException, SQLException {
        Object[][] res = Query("SELECT * FROM CML;");
        if (res != null) {
            HashMap<UUID,Integer> liveChannel = new HashMap<>();
            for (Object[] re: res){
                liveChannel.put(UUID.fromString((String) re[0]),(int)re[1]);
            }
            Woftv4.setLiveChannel(liveChannel);
        }
    }
    public static void  UpdateAC() throws SQLException, ClassNotFoundException {
        Object[][] res = SqliteUtil.Query("SELECT NAME FROM ANCHOR;");
        if (res != null) {
            HashSet<String> hashSet = new HashSet<>();
            for (Object[] re : res) {
                hashSet.add((String) re[0]);
            }
            Woftv4.setAnchorSet(hashSet);
        }
    }
}
