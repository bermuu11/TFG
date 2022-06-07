/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */
public class Estadisticas extends javax.swing.JDialog {

    int idCompeticion;
    ArrayList estadisticas = null;

    public Estadisticas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public Estadisticas(java.awt.Frame parent, boolean modal, int idCompeticion) {
        super(parent, modal);
        initComponents();
        getContentPane().setBackground(new Color(214, 246, 231));
        this.idCompeticion = idCompeticion;
        estiloJLabel();
        cargarEstadisticas();
    }

    public void estiloJLabel() {
        jLabel_Estadisticas.setFont(new Font("Segoe UI", Font.BOLD, 50));
        jLabel_Estadisticas.setOpaque(true);
    }

    private void ejecutarConsultaEquipoMasMenosGoles(String tipo, String orden) {
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E.nombre, " + tipo + "(C.golesAfavor) AS maximo FROM clasifica C, equipo E "
                + "WHERE (C.idEquipo = E.idEquipo) AND (C.idCompeticion = " + idCompeticion + ") GROUP BY E.nombre ORDER BY maximo " + orden + " LIMIT 1");
    }

    private void ejecutarConsultaEquipoMasMenosGolesLocalVisitante(String localVisitante, String orden) {
        //Solo se tienen en cuenta los partidos finalizados y con un resultado correcto
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E.nombre, SUM(P.goles" + localVisitante + ") AS total FROM partido P, equipo E "
                + "WHERE (P.golesLocal IS NOT NULL) AND (P.golesVisitante IS NOT NULL) AND (P.estado = 'Finalizado') AND (P.id_equipo" + localVisitante + " = E.idEquipo) AND (P.idCompeticion = " + idCompeticion + ") GROUP BY E.nombre ORDER BY total " + orden + " LIMIT 1");
        if (estadisticas.isEmpty()) {
            estadisticas.add(new String[]{"(todos)", "0"});
        }
    }

    private void ejecutarConsultaTotalGolesLocalVisitante(String localVisitante) {
        //Solo se tienen en cuenta los partidos finalizados y con un resultado correcto
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT SUM(goles" + localVisitante + ") AS total FROM partido "
                + "WHERE (golesLocal IS NOT NULL) AND (golesVisitante IS NOT NULL) AND (estado = 'Finalizado') AND (idCompeticion = " + idCompeticion + ")");
    }

    private void ejecutarConsultaTotalVictoriasEmpates(String victoriasEmpates) {
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT " + victoriasEmpates + " AS total FROM clasifica "
                + "WHERE (idCompeticion = " + idCompeticion + ")");
    }

    private void ejecutarConsultaMayoresGoleadas() {
        //Solo se tienen en cuenta los partidos finalizados y con un resultado correcto
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E1.nombre, P.golesLocal, P.golesVisitante, E2.nombre, ABS(golesLocal-golesVisitante) AS diferencia, golesLocal+golesVisitante AS suma FROM partido P, equipo E1, equipo E2 "
                + "WHERE (P.golesLocal IS NOT NULL) AND (P.golesVisitante IS NOT NULL) AND (P.estado = 'Finalizado') AND (P.id_equipoLocal = E1.idEquipo) AND (P.id_equipoVisitante = E2.idEquipo) AND (P.idCompeticion = " + idCompeticion + ") ORDER BY diferencia DESC, suma DESC LIMIT 3");
    }

    private void ejecutarConsultaMasMenosPuntosLocalVisitante(String tipo, String orden) {
        String tipoContrario;

        if (tipo.equals("Local")) {
            tipoContrario = "Visitante";
        } else {
            tipoContrario = "Local";
        }
        //Solo se tienen en cuenta los partidos finalizados y con un resultado correcto
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E.nombre, SUM(CASE WHEN P.goles" + tipo + " > P.goles" + tipoContrario + " THEN 3 WHEN P.golesLocal = P.golesVisitante THEN 1 ELSE 0 END) AS puntos, SUM(goles" + tipo + "-goles" + tipoContrario + ") AS diferencia, SUM(goles" + tipo + ") AS suma FROM partido P, equipo E "
                + "WHERE (P.golesLocal IS NOT NULL) AND (P.golesVisitante IS NOT NULL) AND (P.estado = 'Finalizado') AND (P.id_equipo" + tipo + " = E.idEquipo) AND (P.idCompeticion = " + idCompeticion + ") GROUP BY P.id_equipo" + tipo + ", E.nombre ORDER BY puntos " + orden + ", diferencia " + orden + ", suma " + orden + " LIMIT 1");
    }

    private String obtenerValor(String valor) {
        if (valor == null) {
            return "0";
        }
        return valor;
    }

    private String textoValorPlural(String valor, String texto, String plural) {
        int numero;
        String cadena = texto + plural;
        try {
            numero = Integer.parseInt(valor);
            if (numero == 1) {
                cadena = texto; // se pone en singular
            }
        } catch (Exception ex) {
        }

        return valor + " " + cadena;
    }

    public void cargarEstadisticas() {
        //Obtener equipo más goleador
        ejecutarConsultaEquipoMasMenosGoles("MAX", "DESC");
        String[] registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleador.setText(registro[0] + ": " + textoValorPlural(registro[1], "gol", "es") + " a favor.");

        //Obtener equipo menos goleador
        ejecutarConsultaEquipoMasMenosGoles("MIN", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleador.setText(registro[0] + ": " + textoValorPlural(registro[1], "gol", "es") + " a favor.");

        //Obtener equipo que más goles anota como local
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Local", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleadorLocal.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "gol", "es") + " a favor.");

        //Obtener equipo que menos goles anota como local
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Local", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleadorLocal.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "gol", "es") + " a favor.");

        //Obtener equipo que más goles anota como visitante
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Visitante", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleadorVisitante.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "gol", "es") + " a favor.");

        //Obtener equipo que menos goles anota como visitante
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Visitante", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleadorVisitante.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "gol", "es") + " a favor.");

        //Obtener total de goles locales
        ejecutarConsultaTotalGolesLocalVisitante("Local");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalGolesLocal.setText(textoValorPlural(obtenerValor(registro[0]), "gol", "es") + ".");

        //Obtener total de goles visitantes
        ejecutarConsultaTotalGolesLocalVisitante("Visitante");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalGolesVisitante.setText(textoValorPlural(obtenerValor(registro[0]), "gol", "es") + ".");

        //Obtener total de victorias
        ejecutarConsultaTotalVictoriasEmpates("SUM(partidosGanados)");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalVictorias.setText(textoValorPlural(registro[0], "victoria", "s") + ".");

        //Obtener total de empates
        ejecutarConsultaTotalVictoriasEmpates("SUM(partidosEmpatados) / 2");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalEmpates.setText(textoValorPlural(registro[0], "empate", "s") + ".");

        //Obtener mayores goleadas
        ejecutarConsultaMayoresGoleadas();
        String texto = "<html><style>div {text-align: center; white-space: nowrap}</style>";
        for (int i = 0; i < estadisticas.size(); i++) {
            registro = (String[]) estadisticas.get(i);
            texto += "<div>" + registro[0] + " " + registro[1] + "-" + registro[2] + " " + registro[3] + "</div>";
        }
        texto += "</html>";
        jLabel_MayoresGoleadas.setText(texto);

        //Obtener equipo que más puntos tiene como local
        ejecutarConsultaMasMenosPuntosLocalVisitante("Local", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasPuntosLocal.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "punto", "s") + ".");

        //Obtener equipo que menos puntos tiene como local
        ejecutarConsultaMasMenosPuntosLocalVisitante("Local", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosPuntosLocal.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "punto", "s") + ".");

        //Obtener equipo que más puntos tiene como visitante
        ejecutarConsultaMasMenosPuntosLocalVisitante("Visitante", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasPuntosVisitante.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "punto", "s") + ".");

        //Obtener equipo que menos puntos tiene como visitante
        ejecutarConsultaMasMenosPuntosLocalVisitante("Visitante", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosPuntosVisitante.setText(registro[0] + ": " + textoValorPlural(obtenerValor(registro[1]), "punto", "s") + ".");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel_Estadisticas = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleador = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleador = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorLocal = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorLocal = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorVisitante = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorVisitante = new javax.swing.JLabel();
        jLabel_TotalGolesLocalTitulo = new javax.swing.JLabel();
        jLabel_TotalGolesVisitanteTitulo = new javax.swing.JLabel();
        jLabel_TotalGolesLocal = new javax.swing.JLabel();
        jLabel_TotalGolesVisitante = new javax.swing.JLabel();
        jLabel_TotalVictoriasTitulo = new javax.swing.JLabel();
        jLabel_TotalEmpatesTitulo = new javax.swing.JLabel();
        jLabel_TotalVictorias = new javax.swing.JLabel();
        jLabel_TotalEmpates = new javax.swing.JLabel();
        jButton_Cerrar = new javax.swing.JButton();
        jLabel_MayoresGoleadasTitulo = new javax.swing.JLabel();
        jLabel_MayoresGoleadas = new javax.swing.JLabel();
        jLabel_EquipoMasPuntosLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasPuntosLocal = new javax.swing.JLabel();
        jLabel_EquipoMenosPuntosLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosPuntosLocal = new javax.swing.JLabel();
        jLabel_EquipoMasPuntosVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasPuntosVisitante = new javax.swing.JLabel();
        jLabel_EquipoMenosPuntosVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosPuntosVisitante = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel_Estadisticas.setBackground(new java.awt.Color(246, 244, 214));
        jLabel_Estadisticas.setForeground(new java.awt.Color(152, 156, 44));
        jLabel_Estadisticas.setText("Estadísticas");
        jLabel_Estadisticas.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 8, 8));
        jLabel_Estadisticas.setMinimumSize(new java.awt.Dimension(72, 80));
        jLabel_Estadisticas.setPreferredSize(new java.awt.Dimension(800, 80));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        getContentPane().add(jLabel_Estadisticas, gridBagConstraints);

        jLabel_EquipoMasGoleadorTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMasGoleadorTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorTitulo.setText("Equipo más goleador");
        jLabel_EquipoMasGoleadorTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMasGoleador.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMasGoleador, gridBagConstraints);

        jLabel_EquipoMenosGoleadorTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMenosGoleadorTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosGoleadorTitulo.setText("Equipo menos goleador");
        jLabel_EquipoMenosGoleadorTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosGoleadorTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMenosGoleador.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMenosGoleador, gridBagConstraints);

        jLabel_EquipoMasGoleadorLocalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMasGoleadorLocalTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorLocalTitulo.setText("Equipo más goleador como local");
        jLabel_EquipoMasGoleadorLocalTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorLocalTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorLocalTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleadorLocal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMasGoleadorLocal.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorLocal, gridBagConstraints);

        jLabel_EquipoMenosGoleadorLocalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMenosGoleadorLocalTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosGoleadorLocalTitulo.setText("Equipo menos goleador como local");
        jLabel_EquipoMenosGoleadorLocalTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosGoleadorLocalTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorLocalTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleadorLocal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMenosGoleadorLocal.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorLocal, gridBagConstraints);

        jLabel_EquipoMasGoleadorVisitanteTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMasGoleadorVisitanteTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorVisitanteTitulo.setText("Equipo más goleador como visitante");
        jLabel_EquipoMasGoleadorVisitanteTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasGoleadorVisitanteTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleadorVisitante.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMasGoleadorVisitante.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorVisitante, gridBagConstraints);

        jLabel_EquipoMenosGoleadorVisitanteTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMenosGoleadorVisitanteTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosGoleadorVisitanteTitulo.setText("Equipo menos goleador como visitante");
        jLabel_EquipoMenosGoleadorVisitanteTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleadorVisitante.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMenosGoleadorVisitante.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorVisitante, gridBagConstraints);

        jLabel_TotalGolesLocalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_TotalGolesLocalTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_TotalGolesLocalTitulo.setText("Total de goles local");
        jLabel_TotalGolesLocalTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_TotalGolesLocalTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesLocalTitulo, gridBagConstraints);

        jLabel_TotalGolesVisitanteTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_TotalGolesVisitanteTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_TotalGolesVisitanteTitulo.setText("Total de goles visitante");
        jLabel_TotalGolesVisitanteTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_TotalGolesVisitanteTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesVisitanteTitulo, gridBagConstraints);

        jLabel_TotalGolesLocal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_TotalGolesLocal.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_TotalGolesLocal, gridBagConstraints);

        jLabel_TotalGolesVisitante.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_TotalGolesVisitante.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_TotalGolesVisitante, gridBagConstraints);

        jLabel_TotalVictoriasTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_TotalVictoriasTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_TotalVictoriasTitulo.setText("Total de victorias");
        jLabel_TotalVictoriasTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_TotalVictoriasTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalVictoriasTitulo, gridBagConstraints);

        jLabel_TotalEmpatesTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_TotalEmpatesTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_TotalEmpatesTitulo.setText("Total de empates");
        jLabel_TotalEmpatesTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_TotalEmpatesTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalEmpatesTitulo, gridBagConstraints);

        jLabel_TotalVictorias.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_TotalVictorias.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_TotalVictorias, gridBagConstraints);

        jLabel_TotalEmpates.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_TotalEmpates.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_TotalEmpates, gridBagConstraints);

        jButton_Cerrar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Cerrar.setText("CERRAR");
        jButton_Cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CerrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 16, 16);
        getContentPane().add(jButton_Cerrar, gridBagConstraints);

        jLabel_MayoresGoleadasTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_MayoresGoleadasTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_MayoresGoleadasTitulo.setText("Mayores goleadas");
        jLabel_MayoresGoleadasTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_MayoresGoleadasTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_MayoresGoleadasTitulo, gridBagConstraints);

        jLabel_MayoresGoleadas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_MayoresGoleadas.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_MayoresGoleadas, gridBagConstraints);

        jLabel_EquipoMasPuntosLocalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMasPuntosLocalTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasPuntosLocalTitulo.setText("Equipo con más puntos como local");
        jLabel_EquipoMasPuntosLocalTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasPuntosLocalTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasPuntosLocalTitulo, gridBagConstraints);

        jLabel_EquipoMasPuntosLocal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMasPuntosLocal.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMasPuntosLocal, gridBagConstraints);

        jLabel_EquipoMenosPuntosLocalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMenosPuntosLocalTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosPuntosLocalTitulo.setText("Equipo con menos puntos como local");
        jLabel_EquipoMenosPuntosLocalTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosPuntosLocalTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosPuntosLocalTitulo, gridBagConstraints);

        jLabel_EquipoMenosPuntosLocal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMenosPuntosLocal.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMenosPuntosLocal, gridBagConstraints);

        jLabel_EquipoMasPuntosVisitanteTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMasPuntosVisitanteTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasPuntosVisitanteTitulo.setText("Equipo con más puntos como visitante");
        jLabel_EquipoMasPuntosVisitanteTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMasPuntosVisitanteTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasPuntosVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMasPuntosVisitante.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMasPuntosVisitante.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMasPuntosVisitante, gridBagConstraints);

        jLabel_EquipoMenosPuntosVisitanteTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_EquipoMenosPuntosVisitanteTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosPuntosVisitanteTitulo.setText("Equipo con menos puntos como visitante");
        jLabel_EquipoMenosPuntosVisitanteTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoMenosPuntosVisitanteTitulo.setPreferredSize(new java.awt.Dimension(262, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosPuntosVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMenosPuntosVisitante.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_EquipoMenosPuntosVisitante.setText("jLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel_EquipoMenosPuntosVisitante, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CerrarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CerrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Estadisticas dialog = new Estadisticas(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cerrar;
    private javax.swing.JLabel jLabel_EquipoMasGoleador;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorLocal;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorTitulo;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorVisitante;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorVisitanteTitulo;
    private javax.swing.JLabel jLabel_EquipoMasPuntosLocal;
    private javax.swing.JLabel jLabel_EquipoMasPuntosLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMasPuntosVisitante;
    private javax.swing.JLabel jLabel_EquipoMasPuntosVisitanteTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleador;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorLocal;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorVisitante;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorVisitanteTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosPuntosLocal;
    private javax.swing.JLabel jLabel_EquipoMenosPuntosLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosPuntosVisitante;
    private javax.swing.JLabel jLabel_EquipoMenosPuntosVisitanteTitulo;
    private javax.swing.JLabel jLabel_Estadisticas;
    private javax.swing.JLabel jLabel_MayoresGoleadas;
    private javax.swing.JLabel jLabel_MayoresGoleadasTitulo;
    private javax.swing.JLabel jLabel_TotalEmpates;
    private javax.swing.JLabel jLabel_TotalEmpatesTitulo;
    private javax.swing.JLabel jLabel_TotalGolesLocal;
    private javax.swing.JLabel jLabel_TotalGolesLocalTitulo;
    private javax.swing.JLabel jLabel_TotalGolesVisitante;
    private javax.swing.JLabel jLabel_TotalGolesVisitanteTitulo;
    private javax.swing.JLabel jLabel_TotalVictorias;
    private javax.swing.JLabel jLabel_TotalVictoriasTitulo;
    // End of variables declaration//GEN-END:variables
}
