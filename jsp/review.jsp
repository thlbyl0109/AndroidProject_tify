<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String url_mysql = "jdbc:mysql://tifyproject.cmh5zgollnjh.us-east-2.rds.amazonaws.com/tify?serverTimezone=Asia/Seoul&characterEncoding=utf8&useSSL=false";
 	String id_mysql = "root";
    String pw_mysql = "qwer1234";

    String store_sSeqNo = request.getParameter("store_sSeqNo");

    String WhereDefault = "SELECT * from review where store_sSeqNo = " + store_sSeqNo;
    int count = 0;


    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn_mysql = DriverManager.getConnection(url_mysql, id_mysql, pw_mysql);
        Statement stmt_mysql = conn_mysql.createStatement();

        ResultSet rs = stmt_mysql.executeQuery(WhereDefault); 
%>
		{ 
  			"review"  : [ 
<%
        while (rs.next()) {
            if (count == 0) {

            }else{
%>
            , 
<%
            }
%>            
			{
            "rNo" : "<%=rs.getInt(1) %>",   
			"rContent" : "<%=rs.getString(2) %>", 
			"rImage" : "<%=rs.getString(3) %>",   
            "rOwnerComment" : "<%=rs.getString(4) %>",  
            "rDeletedate" : "<%=rs.getString(5) %>",  
            "rInsertDate" : "<%=rs.getString(6) %>",
            "user_uNo" : "<%=rs.getInt(7) %>",
            "store_sSeqNo" : "<%=rs.getInt(8) %>"
			}

<%		
        count++;
        }
%>
		  ] 
		} 
<%		
        conn_mysql.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
	
%>