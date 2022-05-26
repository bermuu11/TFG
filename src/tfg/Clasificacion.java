/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedMap;
import javax.swing.RowFilter.Entry;

/**
 *
 * @author Antonio
 */
public class Clasificacion {
    
    public static void generar(int idCompeticion){
        ArrayList equipos = BaseDeDatos.getBD().ConsultaSQL("SELECT E.idEquipo FROM equipo E, compite C WHERE (C.idCompeticion = " +idCompeticion +") AND (C.idEquipo = E.idEquipo) ORDER BY E.nombre");
        
        if(BaseDeDatos.getBD().ConsultaSQL("SELECT * FROM clasifica WHERE idCompeticion = " +idCompeticion).isEmpty()){
            //No existe la clasificacion y se inserta
            for(int i=0; i<equipos.size(); i++){
                String[] registro = (String[]) equipos.get(i);
                BaseDeDatos.getBD().ConsultaSQL("INSERT INTO clasifica VALUES (" +idCompeticion +"," +registro[0] +",0,0,0,0,0,0,0," +(i+1) +")");
            }
        }
        //Modificamos la clasificacion
        ArrayList partidos = BaseDeDatos.getBD().ConsultaSQL("SELECT id_equipoLocal, id_equipoVisitante, golesLocal, golesVisitante, estado FROM partido WHERE idCompeticion = " +idCompeticion);
        HashMap<Integer, Integer> posEquipos = new HashMap<>();
        int[] partidosJugados = new int[equipos.size()];
        int[] partidosGanados = new int[equipos.size()];
        int[] partidosEmpatados = new int[equipos.size()];
        int[] partidosPerdidos = new int[equipos.size()];
        int[] golesAfavor = new int[equipos.size()];
        int[] golesEnContra = new int[equipos.size()];
        int[] puntos = new int[equipos.size()];
        int[] posicion = new int[equipos.size()];
        
        for(int i=0; i<partidos.size(); i++){
            String[] registro = (String[]) partidos.get(i);
            
            //Se comprueba que el partido esté finalizado y que el número de goles hayan sido introducidos para contabilizar el partido
            if(!registro[2].isEmpty() && !registro[3].isEmpty() && registro[4].equals("Finalizado")){
                int idEquipoLocal = Integer.parseInt(registro[0]);
                int idEquipoVisitante = Integer.parseInt(registro[1]);
                int golesLocal = Integer.parseInt(registro[2]);
                int golesVisitante = Integer.parseInt(registro[3]);
                int posEquipoLocal;
                int posEquipoVisitante;
                
                if(posEquipos.containsKey(idEquipoLocal)){
                    posEquipoLocal = posEquipos.get(idEquipoLocal);
                } else {
                    posEquipoLocal = posEquipos.size();
                    posEquipos.put(idEquipoLocal, posEquipoLocal);
                }
                
                if(posEquipos.containsKey(idEquipoVisitante)){
                    posEquipoVisitante = posEquipos.get(idEquipoVisitante);
                } else {
                    posEquipoVisitante = posEquipos.size();
                    posEquipos.put(idEquipoVisitante, posEquipoVisitante);
                }
                
                //Se suma un partido jugado tanto al equipo local como al visitante
                partidosJugados[posEquipoLocal]++;
                partidosJugados[posEquipoVisitante]++;
                
                //Se suman los goles a favor y en contra tanto del equipo local como del visitante
                golesAfavor[posEquipoLocal] += golesLocal;
                golesEnContra[posEquipoLocal] += golesVisitante;
                golesAfavor[posEquipoVisitante] += golesVisitante;
                golesEnContra[posEquipoVisitante] += golesLocal;
                
                if(golesLocal > golesVisitante){
                    //Gana el equipo local
                    partidosGanados[posEquipoLocal]++;
                    puntos[posEquipoLocal] += 3;
                    partidosPerdidos[posEquipoVisitante]++;
                } else if (golesLocal == golesVisitante){
                    //Empate entre los dos equipos
                    partidosEmpatados[posEquipoLocal]++; 
                    puntos[posEquipoLocal]++;
                    partidosEmpatados[posEquipoVisitante]++;
                    puntos[posEquipoVisitante]++;
                } else {
                    //Gana el equipo visitante
                    partidosGanados[posEquipoVisitante]++;
                    puntos[posEquipoVisitante] += 3;
                    partidosPerdidos[posEquipoLocal]++;
                }
                
                //
                
            } 
        }
    }
}
