/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tfg;

import misqlhsqldb.MiSQLhSQLDB;

/**
 *
 * @author Antonio
 */
public class BaseDeDatos {

    private static MiSQLhSQLDB bbdd = null;

    public static MiSQLhSQLDB getBD() {
        if (bbdd == null) {
            bbdd = new MiSQLhSQLDB("SA", "");
        }
        return bbdd;
    }
}
