import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class userposts extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            ServletContext context = getServletContext();
 
            out.println("<div align=\"center\">");
            out.println("<a href=\"publish.html\">Publish</a>");
            out.println("&nbsp");
            out.println("<a href=\"logout\">Logout</a>");
            out.println("</div>");
            
            HttpSession userSession = request.getSession();
            String user = (String)userSession.getAttribute("email");
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
            
            //1005 = scrollable sensitive result set
            //1008 = updatable result set
            out.println("<br/><br/><br/>");
            Statement st = cn.createStatement(1005,1008);
            ResultSet rs =  st.executeQuery("select * from \"postinfo\" where email = '"+user+"'");
            if(!rs.isBeforeFirst())
            {
                out.println("<div align=\"center\">");
                out.println("It's great to see that you don't have any post. Everything seems to be wonderful with you. Cheers. :)");
                out.println("</div>"); 
            }
            else
            {
                while(rs.next())
                {
                    int postid = rs.getInt("ID");
                    String helpdomain = rs.getString("HELPDOMAIN");
                    String subject = rs.getString("SUBJECT");
                    String location = rs.getString("LOCATION");
                    Date d1 = rs.getDate("PDATE");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(d1);
                
                    
                    out.println("<div align=\"center\">");
                    out.println("----------------------------------------------------<br/>");                 
                    out.println("Post ID: "+postid+"<br/>");
                    out.println("Type of help required: "+helpdomain+"<br/>");
                    out.println("Location: "+location+"<br/>");
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
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
