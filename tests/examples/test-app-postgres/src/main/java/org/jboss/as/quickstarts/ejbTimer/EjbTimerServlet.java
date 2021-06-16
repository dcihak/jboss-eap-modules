package org.jboss.as.quickstarts.ejbTimer;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@WebServlet(name = "EjbTimerServlet", urlPatterns = {"/EjbTimerServlet"})
public class EjbTimerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String DATASOURCE_NAME = "PostgreSQLDS";

    @EJB
    private EjbTimerInterface ejbTimerBean;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain");
        final PrintWriter writer = resp.getWriter();
        Connection con = null;

        ejbTimerBean.createTimer();

        try {
            InitialContext iniCtx = new InitialContext();
            DataSource ds = (DataSource) iniCtx.lookup("java:jboss/datasources/" + DATASOURCE_NAME);
            con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select next_date from jboss_ejb_timer");

            Timestamp nextDate = null;
            while (rs.next()) {
                nextDate = rs.getTimestamp("next_date");
            }
            writer.print("next_date:" + nextDate);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(writer);
        } catch (NamingException e) {
            e.printStackTrace(writer);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
