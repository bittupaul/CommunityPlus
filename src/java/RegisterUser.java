import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RegisterUser extends HttpServlet {

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try  {
            
            ServletContext context = getServletContext();
            
            
            long umobile = Long.parseLong(request.getParameter("mobile"));
            String uname = request.getParameter("name");
            String uemail = request.getParameter("email");
            String ucity = request.getParameter("city");
            String uhelpdomain = request.getParameter("domain preference");
            String upassword = request.getParameter("password");
            int uage = Integer.parseInt(request.getParameter("age"));     
            int matchFound = 0;
            
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
           
            String checkDuplication = "select email,mobile from \"userinfo\"";
            PreparedStatement ps = cn.prepareStatement(checkDuplication);
            ResultSet rs= (ResultSet) ps.executeQuery();
            while(rs.next())
            {
                String testMail = rs.getString("EMAIL");
                if(testMail.equals(uemail))
                {
                    cn.close();
                    rs.close();
                    ps.close();
                    
                    matchFound = 1;
                    out.println("<h4>The email which you have been using for registration is already in use.<br> If it's your email, avoid creating another account.<br> If not, don't try to create an account with someone else's email as we do verification at later stage and we strongly condemn such unfair use of others' credentials. <br></h4>");
                    RequestDispatcher rd = request.getRequestDispatcher("/register.html");
                    rd.include(request, response);
                    break;
                }
                Long testMobile = rs.getLong("MOBILE");
                if(testMobile == umobile)
                    {
                    cn.close();
                    rs.close();
                    ps.close();
                    
                    matchFound = 1;
                    out.println("<h4>The mobile number which you have been using for registration is already in use.<br> If it's your mobile number, avoid creating another account.<br> If not, don't try to create an account with someone else's mobile number as we do verification at later stage and we strongly condemn such unfair use of others' credentials. <br></h4>");
                    RequestDispatcher rd = request.getRequestDispatcher("/register.html");
                    rd.include(request, response);
                    break;
                }
            }
            
                if(matchFound==0)
                {     
                    String addUser = "insert into \"userinfo\"( USERNAME,MOBILE,EMAIL,CITY,AGE,HELPDOMAIN,PASSWORD) values(?,?,?,?,?,?,?)";   
                    PreparedStatement ps2 = cn.prepareStatement(addUser);
                    ps2.setLong(2, umobile);
                    ps2.setInt(5, uage);
                    ps2.setString(1, uname);
                    ps2.setString(3, uemail);
                    ps2.setString(4, ucity);
                    ps2.setString(6, uhelpdomain);
                    ps2.setString(7, upassword);
                    ps2.executeUpdate();
                    
                    cn.close();
                    ps2.close();
                    rs.close();
                    out.println("Registration Successful.");
                    RequestDispatcher rd = request.getRequestDispatcher("homepage");
                    rd.include(request, response);
                }
        }
        catch (ClassNotFoundException | SQLException e)
        {
            out.println(e);
            out.println("Due to some technical issues, we couldn't process your request at the moment. Please try again after sometime. We are extremely sorry for the inconvenience.");
        }
    }
}


