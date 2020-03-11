package com.be.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

	private final static String driverName = "com.mysql.jdbc.Driver";    //驱动
    private final static String url = "jdbc:mysql://localhost:3306/sxl?useUnicode=true&characterEncoding=UTF8";
    private final static String userName = "root";
    private final static String pwd = "123456";
    
    public final static int pageSize = 50;


    public static Connection getMysqlConnection()
    {
        Connection connection = null;
        try{
            Class.forName(driverName);
            connection = DriverManager.getConnection(url,userName,pwd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * 获取结果集数量
     * @param sql
     * @return
     */
    public static int findCountBySql(String sql){
    	
		int count = 0;
		sql = "select count(*) from ( " + sql + ") a ";
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			connection = getMysqlConnection();
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
			while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    count =  rs.getInt(i);
                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(rs, st, connection);
		}
		return count;
    }
    
    /**
     * 获取list结果集,分页
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> findListBySql(String sql,int pageNumber){
    	
    	sql = "select * from ( " + sql + ") a limit " + (pageNumber - 1) * pageSize + "," + pageSize;
    	return findListBySql(sql);
    }
    
    /**
     * 获取list结果集
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> findListBySql(String sql) {
    	System.out.println(sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            connection = getMysqlConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs,st,connection);
        }
        return list;
    }


    /**
     * 获取map结果集
     * @param sql
     * @return
     */
    public static Map<String, Object> findMapBySql(String sql) {
        Map<String, Object> map = new HashMap<String, Object>();
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = getMysqlConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs,st,connection);
        }
        return map;
    }

    public static void closeResources(ResultSet rs,Statement st,Connection connection)
    {
        try
        {
            if (rs != null)
            {
                rs.close();
            }
            if (st != null)
            {
                st.close();
            }
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
