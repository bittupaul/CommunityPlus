import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import javax.servlet.http.HttpSession;


public class postit extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try  {
            
            ServletContext  context = getServletContext();
            
            HttpSession userSession =  request.getSession();
            String user = (String) userSession.getAttribute("email");
            LocalDate a = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(a);
            String helpdomain=request.getParameter("domain preference");
            String subject=request.getParameter("subject");
            String location=request.getParameter("location");
            
            
            String driverclass = context.getInitParameter("driverClass");
            String dbusername = context.getInitParameter("dbuserName");
            String dbpassword = context.getInitParameter("dbPassword");
            String databaseurl = context.getInitParameter("databaseUrl")+context.getInitParameter("databaseName");
            Class.forName(driverclass);
            Connection cn = DriverManager.getConnection(databaseurl,dbusername,dbpassword);
            
            String makePost = "insert into \"postinfo\"(HELPDOMAIN, SUBJECT, LOCATION, MOBILE, EMAIL, PDATE, USERNAME) values(?,?,?,?,?,?,?)";
            String getMobileAndUsername = "select mobile,username from \"userinfo\" where email = ?";
            
            PreparedStatement ps = cn.prepareStatement(getMobileAndUsername, 1005, 1008);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Long mobile = rs.getLong(1);
            String username = rs.getString(2);
            rs.close();
            ps.close();
                    
            PreparedStatement ps1 = cn.prepareStatement(makePost);
            ps1.setString(1, helpdomain);
            ps1.setString(2, subject);
            ps1.setString(3, location);
            ps1.setString(5, user);
            ps1.setLong(4,mobile);
            ps1.setDate(6,sqlDate);
            ps1.setString(7, username);
            ps1.execute();
            ps1.close();
            
            String toquery = "select EMAIL from \"userinfo\" where EMAIL <> ? AND HELPDOMAIN IN (?,?)";
            PreparedStatement ps2 = cn.prepareStatement(toquery,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ps2.setString(1, user);
            ps2.setString(2, helpdomain);
            ps2.setString(3, "all");
            ResultSet rs2 = ps2.executeQuery();
            
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
            
            
            if(rs2.isBeforeFirst())
            {
                
                while(rs2.next())
                {
                    String to = rs2.getString("EMAIL");
                   
                    MimeMessage message = new MimeMessage(session);  
                    message.setFrom(new InternetAddress(from));  
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
                    message.setSubject("Required "+helpdomain+" assistance");  
                    String msg ="Hey there,"+newline+user+" on Community+ says,"+newline+subject+newline+newline+"If you feel that you can help here, reach out on "+mobile+" or "+user;
                    msg = msg+newline+" Bring the good in you and be a helping hand."+newline+"Thanks,"+newline+" - Community+ Team";
                    message.setText(msg); 
                    Transport.send(message);  
                }
            }
            
            ps2.close();
            rs2.close();
            cn.close();
            
            out.println("Your problem has been published. Hope you get the required help soon.");
            RequestDispatcher rd = request.getRequestDispatcher("homepage");
            rd.include(request,response);
            
            
        }
        
        catch(ClassNotFoundException | IOException | SQLException | MessagingException | ServletException e)
        {
            out.println(e);
        }
    }
}
