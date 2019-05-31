import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class homepage extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            ServletContext context = getServletContext();
            
            String user = request.getParameter("email");
            
            out.println("<div align=\"center\">");
            out.println("<a href=\"publish.html\">Publish</a>");
            out.println("&nbsp");
            out.println("<a href=\"userposts\">My posts</a>");
            out.println("&nbsp");
            out.println("<a href=\"logout\">Logout</a>");
            out.println("</div>");
            
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
            
            //1005 = scrollable sensitive result set
            //1008 = updatable result set
            out.println("<br/><br/><br/>");
            out.println("<div align=\"center\">");
            out.println("<h2>Feed</h2>");
            out.println("</div>");
            Statement st = cn.createStatement(1005,1008);
            ResultSet rs =  st.executeQuery("select * from \"postinfo\" where pdate between cast({fn timestampadd(sql_tsi_day,-6,current_date)}as date) AND current_date order by pdate desc");
            if(!rs.isBeforeFirst())
            {
                rs=st.executeQuery("select * from \"postinfo\" order by pdate desc");
            }
            if(!rs.isBeforeFirst())
            {
                out.println("<div align=\"center\">");
                out.println("Everything and everyone is at peace right now... :)");
                out.println("</div>");
            }
            else
            {
                while(rs.next())
                {
                    int postid = rs.getInt("ID");
                    String username = rs.getString("USERNAME");
                    String helpdomain = rs.getString("HELPDOMAIN");
                    String subject = rs.getString("SUBJECT");
                    String location = rs.getString("LOCATION");
                    String email = rs.getString("EMAIL");
                    Date d1 = rs.getDate("PDATE");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(d1);
                    long mobile = rs.getLong("MOBILE");
                    
                    
                    
                    out.println("<div align=\"center\">");
                    out.println("----------------------------------------------------<br/>");                 
                    out.println("Post ID: "+postid+"<br/>");
                    out.println("Posted By: "+username+"<br/>");
                    out.println("Type of help required: "+helpdomain+"<br/>");
                    out.println("Location: "+location+"<br/>");
                    out.println("Mobile: "+mobile+"<br/>");
                    out.println("email: "+email+"<br/>");
                    out.println("Date: "+date+"<br/>");
                    out.println("Subject: "+subject+"<br/>");
                    out.println("----------------------------------------------------<br/>");
                    out.println("</div>");
                             
                }
            }
            rs.close();
            st.close();
            cn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            out.println(e);
        }   
    }
}
