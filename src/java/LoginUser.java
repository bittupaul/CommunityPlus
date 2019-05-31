import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try  {
            
            ServletContext context = getServletContext();
           
            
            String uemail = request.getParameter("email");
            String upassword = request.getParameter("password");
 
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
            String validateLogin="select \"PASSWORD\" from \"userinfo\" where \"EMAIL\" = ?";
            PreparedStatement ps = cn.prepareStatement(validateLogin,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ps.setString(1,uemail);
            ResultSet rs = ps.executeQuery();
            
            
            if(rs.isBeforeFirst())
            {
                rs.next();
                if(upassword.equals(rs.getString(1)))
                {
                    rs.close();
                    ps.close();
                    cn.close();
                    HttpSession userSession  = request.getSession();
                    userSession.setAttribute("email",uemail);
                    RequestDispatcher rd = request.getRequestDispatcher("homepage");
                    rd.forward(request,response);
                }
                else
                {
                    rs.close();
                    ps.close();
                    cn.close();
                    out.println("Incorrect Password!!");
                    RequestDispatcher rd = request.getRequestDispatcher("index.html");
                    rd.include(request,response);
                }
            }
            else
            {
                    rs.close();
                    ps.close();
                    cn.close();
                out.println("Incorrect email!!");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request,response);
            }

        }
        catch(Exception e)
        {
            out.println(e);
            out.println("Due to some technical issues, we couldn't process your request at the moment. Please try again after sometime. We are extremely sorry for the inconvenience.");
        }
        
    }
}
