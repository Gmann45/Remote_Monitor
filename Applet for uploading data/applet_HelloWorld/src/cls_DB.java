import java.sql.*;
import gnu.*;
import gnu.io.*;

public class cls_DB
{

    private Connection con;
    private String dbType;
    private String dbName;
    private String dbUser;
    private String dbPass;

    // Constructor
    cls_DB(String dbType, String dbName, String dbUser, String dbPass)
    {
        this.dbType=dbType;
        this.dbName=dbName;
        this.dbUser=dbUser;
        this.dbPass=dbPass;
    }

    public boolean connect()
    {
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost/pokus?characterEncoding=UTF-8");//"jdbc:"+dbType+"://localhost/"+dbName+"?characterEncoding=UTF-8",dbUser,dbPass);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }

    }

    public ResultSet runSql(String sql)
    {

        ResultSet rset;
        System.out.println(sql);

        try
        {
            Statement stmt = con.createStatement();
            rset = stmt.executeQuery(sql);
        }
        catch (Exception ex)
        {

        }
        
        
        return null;

    }


}
