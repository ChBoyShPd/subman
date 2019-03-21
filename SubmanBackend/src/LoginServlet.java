
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "Servlet for user login, verifies password and assigns cookies", urlPatterns = {
		"/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String key = request.getParameter("key");
		boolean success = false;
		try {	// Ask database for login data verification
			DBA data = new DBA();
			success = data.login(name, key);
			data.close();
		} catch (SQLException e) {
			// TODO log ERROR
			e.printStackTrace();
			response.sendError(500, e.getMessage() + "\n\n" + e.getSQLState());
			return;
		}
		if (!success) {
			response.sendError(401, "Authentication for user " + name + " has failed.");
			return;
			// TODO specify what's wrong, may need to rewrite DBA.login()
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("username", name);
		response.getWriter().append("success");
		//TODO Is this good enough? What does the frontend need?

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		doGet(request, response);
	}

}
