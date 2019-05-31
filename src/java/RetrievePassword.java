
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RetrievePassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try  {
            
            ServletContext context = getServletContext();
            
            String user = request.getParameter("email");
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
            String getPassword = "select PASSWORD from \"userinfo\" where EMAIL = ?";
            PreparedStatement ps = cn.prepareStatement(getPassword,1005,1008);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst())
            {
            rs.next();
            
            
            String from = context.getInitParameter("from");
            String host = "smtp.gmail.com"; 
            String password=context.getInitParameter("password");
  
      
            Properties properties = System.getProperties();  
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.host", host);
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {  
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(from,password);}  
            }); 
            
        String newline = System.getProperty("line.separator");
      
         MimeMessage message = new MimeMessage(session);  
         message.setFrom(new InternetAddress(from));  
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(user));  
         message.setSubject("Community+ Account Password");  
         String msg = "Hey there..."+newline+"Your Community+ account password is:"+newline+rs.getString("PASSWORD")+newline+newline+"Thanks,"+" - Community+ Team";
         message.setText(msg); 
         Transport.send(message);  
         
         out.println("We have sent you a mail with your password.");
         ps.close();
         rs.close();
         cn.close();
         }
         
         else
         {
            ps.close();
            rs.close();
            cn.close();
            out.println("It seems that you don't have an account with the enetered email. Please register yourself.");
            RequestDispatcher rd = request.getRequestDispatcher("register.html");
            rd.include(request, response);
         }
            
         
        }
        catch(IOException | ClassNotFoundException | SQLException | MessagingException | ServletException e)
        {
            out.println(e);
            out.println("Due to some technical issues, we couldn't process your request at the moment. Please try again after sometime. We are extremely sorry for the inconvenience.");
        }
    }

    
}
