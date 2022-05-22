/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tfg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import misqlhsqldb.MiSQLhSQLDB;

/**
 *
 * @author Antonio
 */
public class BaseDeDatos {
    static Connection conexion = null;
    
    public static void conectarBD(){
        if(conexion == null){
            try {
             Class.forName("org.hsqldb.jdbcDriver");
             conexion = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost", "SA", "SA");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Equipos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Equipos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void desconectarBD() throws SQLException{
        conexion.close();
        conexion = null;
    }
}
