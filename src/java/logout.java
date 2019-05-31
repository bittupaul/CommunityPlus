import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class logout extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HttpSession userSession = request.getSession();
            userSession.invalidate();
            out.println("You have been logged out successfully. Hope you have had a pleasant experience.");
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.include(request,response);
          
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

}
