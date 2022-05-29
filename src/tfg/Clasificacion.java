/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tfg;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Antonio
 */
public class Clasificacion {

    public static void generar(int idCompeticion) {
        ArrayList equipos = BaseDeDatos.getBD().ConsultaSQL("SELECT E.idEquipo FROM equipo E, compite C WHERE (C.idCompeticion = " + idCompeticion + ") AND (C.idEquipo = E.idEquipo) ORDER BY E.nombre");
        int totalEquipos = equipos.size();

        if (BaseDeDatos.getBD().ConsultaSQL("SELECT * FROM clasifica WHERE idCompeticion = " + idCompeticion).isEmpty()) {
            //No existe la clasificacion y se inserta
            for (int i = 0; i < totalEquipos; i++) {
                String[] registro = (String[]) equipos.get(i);
                BaseDeDatos.getBD().ConsultaSQL("INSERT INTO clasifica VALUES (" + idCompeticion + "," + registro[0] + ",0,0,0,0,0,0,0," + (i + 1) + ")");
            }
        }
        //Modificamos la clasificacion existente
        ArrayList partidos = BaseDeDatos.getBD().ConsultaSQL("SELECT id_equipoLocal, id_equipoVisitante, golesLocal, golesVisitante, estado FROM partido WHERE idCompeticion = " + idCompeticion);
        HashMap<Integer, Integer> posEquipos = new HashMap<>();
        int[] id = new int[totalEquipos];
        int[] puntos = new int[totalEquipos];
        int[] golesAfavor = new int[totalEquipos];
        int[] golesEnContra = new int[totalEquipos];
        int[] partidosJugados = new int[totalEquipos];
        int[] partidosGanados = new int[totalEquipos];
        int[] partidosEmpatados = new int[totalEquipos];
        int[] partidosPerdidos = new int[totalEquipos];
        int[] posicion = new int[totalEquipos];

        for (int i = 0; i < partidos.size(); i++) {
            String[] registro = (String[]) partidos.get(i);

            //Se obtienen los equipos que forman el partido
            int idEquipoLocal = Integer.parseInt(registro[0]);
            int idEquipoVisitante = Integer.parseInt(registro[1]);

            int posEquipoLocal;
            int posEquipoVisitante;

            if (posEquipos.containsKey(idEquipoLocal)) {
                posEquipoLocal = posEquipos.get(idEquipoLocal);
            } else {
                posEquipoLocal = posEquipos.size();
                posEquipos.put(idEquipoLocal, posEquipoLocal);
                id[posEquipoLocal] = idEquipoLocal; //Se guarda el id asociado a la posicion del equipo local
            }

            if (posEquipos.containsKey(idEquipoVisitante)) {
                posEquipoVisitante = posEquipos.get(idEquipoVisitante);
            } else {
                posEquipoVisitante = posEquipos.size();
                posEquipos.put(idEquipoVisitante, posEquipoVisitante);
                id[posEquipoVisitante] = idEquipoVisitante; //Se guarda el id asociado a la posicion del equipo visitante
            }
            
            //Se comprueba que el partido esté finalizado y que el número de goles hayan sido introducidos para contabilizar el partido
            if (registro[2] != null && registro[3] != null && registro[4].equals("Finalizado")) {
                int golesLocal = Integer.parseInt(registro[2]);
                int golesVisitante = Integer.parseInt(registro[3]);

                //Se suman los goles a favor y en contra tanto del equipo local como del visitante
                golesAfavor[posEquipoLocal] += golesLocal;
                golesEnContra[posEquipoLocal] += golesVisitante;
                golesAfavor[posEquipoVisitante] += golesVisitante;
                golesEnContra[posEquipoVisitante] += golesLocal;

                //Se suma un partido jugado tanto al equipo local como al visitante
                partidosJugados[posEquipoLocal]++;
                partidosJugados[posEquipoVisitante]++;

                if (golesLocal > golesVisitante) {
                    //Gana el equipo local
                    partidosGanados[posEquipoLocal]++;
                    puntos[posEquipoLocal] += 3;
                    partidosPerdidos[posEquipoVisitante]++;
                } else if (golesLocal == golesVisitante) {
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
            }
        }

        //Ordenar clasificacion
        ArrayList<SimpleEntry<Integer, Integer>> puntosEquipos = new ArrayList<>();
        for (int i = 0; i < puntos.length; i++) {
            puntosEquipos.add(new SimpleEntry(i, puntos[i]));
        }

        Collections.sort(puntosEquipos, new Comparator<SimpleEntry<Integer, Integer>>() {
            @Override
            public int compare(SimpleEntry<Integer, Integer> equipo1, SimpleEntry<Integer, Integer> equipo2) {
                int comparar = equipo2.getValue().compareTo(equipo1.getValue()); //Comparamos el segundo valor con el primero para que ordene los valores de mayor a menor
                if (comparar == 0) {
                    //Los puntos son iguales y decide la clasificación por la diferencia de goles
                    Integer diferencia1 = golesAfavor[equipo1.getKey()] - golesEnContra[equipo1.getKey()];
                    Integer diferencia2 = golesAfavor[equipo2.getKey()] - golesEnContra[equipo2.getKey()];
                    comparar = diferencia2.compareTo(diferencia1);
                    if (comparar == 0) {
                        //La diferencia de goles es igual y decide la clasificación por el mayor número de goles a favor
                        Integer aFavor1 = golesAfavor[equipo1.getKey()];
                        Integer aFavor2 = golesAfavor[equipo2.getKey()];
                        return aFavor2.compareTo(aFavor1);
                    }
                }
                return comparar;
            }
        });

        //Se obtienen las posiciones ordenadas
        for (int i = 0; i < totalEquipos; i++) {
            posicion[(int) puntosEquipos.get(i).getKey()] = i + 1;
        }

        //Actualizamos la base de datos
        for (int i = 0; i < totalEquipos; i++) {
            BaseDeDatos.getBD().ConsultaSQL("UPDATE clasifica SET puntos=" + puntos[i] + ",golesAfavor=" + golesAfavor[i] + ",golesEnContra=" + golesEnContra[i]
                    + ",partidosJugados=" + partidosJugados[i] + ",partidosGanados=" + partidosGanados[i] + ",partidosEmpatados=" + partidosEmpatados[i] + ",partidosPerdidos=" + partidosPerdidos[i] + ",posicion=" + posicion[i]
                    + " WHERE (idCompeticion=" + idCompeticion + ") AND (idEquipo=" + id[i] + ")");
        }
    }
}
