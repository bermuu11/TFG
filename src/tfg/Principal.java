/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import tfg.Alta.Alta_Jornada;
import tfg.InfoModificar.Modificar_Jornada;

/**
 *
 * @author Antonio
 */
public class Principal extends javax.swing.JPanel {

    private static final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat formatoHoraBD = new SimpleDateFormat("HH:mm:ss");

    ArrayList datos = null;
    ArrayList datosTemporada = null;
    ArrayList datosCompeticion = null;
    ArrayList datosPartidos = null;
    ArrayList datosJornada = null;
    ArrayList datosClasificacion = null;

    int idTemporada;
    int idLiga;
    int idCompeticion;
    int jornadaActual;

    private class miTablaModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public Principal() {
        initComponents();
        Image imagen = new ImageIcon(getClass().getClassLoader().getResource("imagenes/estadisticas.png")).getImage();
        jButton_Estadisticas.setIcon(new ImageIcon(imagen.getScaledInstance(28, 28, java.awt.Image.SCALE_SMOOTH)));

        estiloJLabel();
        estiloJTable();
        try {
            cargarTemporadas();
            cargarTemporadaActual();
            cargarCompeticiones();
            cargarCompeticionActual();
            cargarJornadas();
            cargarTablaResultados();
            cargarTablaClasificacion();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Se ha producido un error al acceder a los datos. Asegure que la base de datos está creada y el servidor activo.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void estiloJLabel() {
        jLabel_Competicion.setFont(new Font("Segoe UI", Font.BOLD, 50));

        jLabel_Resultados.setFont(new Font("Segoe UI", Font.BOLD, 36));

        jLabel_Clasificacion.setFont(new Font("Segoe UI", Font.BOLD, 36));
    }

    public void estiloJTable() {
        JTableHeader header = jTable_Resultados.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTable_Resultados.setFillsViewportHeight(true); // para que el fondo se use en todo

        header = jTable_Clasificacion.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTable_Clasificacion.setFillsViewportHeight(true); // para que el fondo se use en todo
    }

    public void temporadaModificada() {
        cargarTemporadas();

        int idCompeticionActual = idCompeticion;
        //Comprueba si la temporada actual sigue existiendo para seleccionarla
        int i = 0;
        boolean buscando = true;
        while (buscando && i < datosTemporada.size()) {
            String[] registro = (String[]) datosTemporada.get(i);
            if (Integer.parseInt(registro[0]) == idTemporada) {
                //La ha encontrado, la selecciona y deja de buscar
                jComboBox_Temporadas.setSelectedIndex(i);
                buscando = false;
            }
            i++;
        }

        if (buscando) {
            //Ha habido cambio de temporada y se deben cargar todos sus datos
            cargarTemporadaActual();
            cargarCompeticiones();
            jComboBox_CompeticionesActionPerformed(null); //se fuerza a que se carguen los resultados
        } else {
            //Comprueba si la competición actual sigue existiendo para seleccionarla
            i = 0;
            buscando = true;
            while (buscando && i < datosCompeticion.size()) {
                String[] registro = (String[]) datosCompeticion.get(i);
                if (Integer.parseInt(registro[0]) == idCompeticionActual) {
                    //La ha encontrado, la selecciona y deja de buscar
                    jComboBox_Competiciones.setSelectedIndex(i);
                    buscando = false;
                }
                i++;
            }
        }
    }

    public void ligaModificada() {
        //Recarga la competición por si la liga a la que corresponde ha cambiado
        competicionModificada();
    }

    public void competicionModificada() {
        cargarCompeticiones();

        //Comprueba si la competición actual sigue existiendo para seleccionarla
        int i = 0;
        boolean buscando = true;
        while (buscando && i < datosCompeticion.size()) {
            String[] registro = (String[]) datosCompeticion.get(i);
            if (Integer.parseInt(registro[0]) == idCompeticion) {
                //La ha encontrado, la selecciona y deja de buscar
                jComboBox_Competiciones.setSelectedIndex(i);
                buscando = false;
            }
            i++;
        }

        if (buscando) {
            //Ha habido cambio de competición y se deben cargar todos sus datos
            cargarCompeticionActual();
            cargarJornadas();
            cargarTablaResultados();
            cargarTablaClasificacion();
        }
    }

    public void cargarTablaClasificacion() {
        String[] registro = null;

        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Pos.");
        dtm.addColumn("Equipo");
        dtm.addColumn("J.");
        dtm.addColumn("G.");
        dtm.addColumn("E.");
        dtm.addColumn("P.");
        dtm.addColumn("G.F.");
        dtm.addColumn("G.C.");
        dtm.addColumn("Puntos");

        datosClasificacion = BaseDeDatos.getBD().ConsultaSQL("SELECT C.posicion, E.nombre, C.partidosJugados, C.partidosGanados, C.partidosEmpatados, C.partidosPerdidos, C.golesAfavor, C.golesEnContra, C.puntos FROM clasifica C, equipo E WHERE (C.idCompeticion = " + idCompeticion + ") AND (C.idEquipo = E.idEquipo) ORDER BY C.posicion");
        if (datosClasificacion != null) {
            int n = datosClasificacion.size();
            for (int i = 0; i < n; i++) {
                registro = (String[]) datosClasificacion.get(i);
                Object[] fila = new Object[]{
                    registro[0],
                    registro[1],
                    registro[2],
                    registro[3],
                    registro[4],
                    registro[5],
                    registro[6],
                    registro[7],
                    registro[8]
                };
                dtm.addRow(fila);
            }
        }
        //Establecer modelo
        jTable_Clasificacion.setModel(dtm);

        //Cambiar tamaño de la columna equipo
        TableColumn tc = jTable_Clasificacion.getColumn("Equipo");
        tc.setPreferredWidth(240);

        //Alinear cabeceras centradas
        DefaultTableCellRenderer alinearCabecera = (DefaultTableCellRenderer) jTable_Clasificacion.getTableHeader().getDefaultRenderer();
        alinearCabecera.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        //Alinear columnas centradas menos la de equipo
        DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
        alinear.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < jTable_Clasificacion.getColumnCount(); i++) {
            if (i != 1) {
                jTable_Clasificacion.getColumnModel().getColumn(i).setCellRenderer(alinear);
            }
        }
    }

    public void cargarJornadas() {
        datosJornada = BaseDeDatos.getBD().ConsultaSQL("SELECT DISTINCT jornada FROM partido WHERE idCompeticion = " + idCompeticion);
        DefaultComboBoxModel<String> dbcm = new DefaultComboBoxModel<>();
        for (int i = 0; i < datosJornada.size(); i++) {
            String[] registro = (String[]) datosJornada.get(i);
            dbcm.addElement("Jornada " + registro[0]);
        }
        jComboBox_Jornadas.setModel(dbcm);

        jButton_Anterior.setEnabled(false);
        jButton_Siguiente.setEnabled(jComboBox_Jornadas.getItemCount() > 1);

        //Si no hay jornadas se deshabilitan los botones de modificar y eliminar jornada y las estadísticas
        boolean hayJornadas = jComboBox_Jornadas.getItemCount() != 0;
        jButton_ModificarJornada.setEnabled(hayJornadas);
        jButton_EliminarJornada.setEnabled(hayJornadas);
        jButton_Estadisticas.setEnabled(hayJornadas);
    }

    public void cargarTablaResultados() {
        String[] registro = null;

        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Hora");
        dtm.addColumn("Fecha");
        dtm.addColumn("Equipo Local");
        dtm.addColumn("Resultado");
        dtm.addColumn("Equipo Visitante");
        dtm.addColumn("Estado");

        //filtrar por jornada
        if (jComboBox_Jornadas.getSelectedIndex() != -1) {
            registro = (String[]) datosJornada.get(jComboBox_Jornadas.getSelectedIndex());
            jornadaActual = Integer.parseInt(registro[0]);

            datosPartidos = BaseDeDatos.getBD().ConsultaSQL("SELECT P.hora, P.fecha, E1.nombre, P.golesLocal, P.golesVisitante, E2.nombre, P.estado FROM partido P, equipo E1, equipo E2 WHERE (P.id_equipoLocal = E1.idEquipo) AND (P.id_equipoVisitante = E2.idEquipo) AND (P.jornada = " + jornadaActual + ") AND (P.idCompeticion = " + idCompeticion + ")");

            if (datosPartidos != null) {
                int n = datosPartidos.size();
                for (int i = 0; i < n; i++) {
                    registro = (String[]) datosPartidos.get(i);

                    try {
                        Date hora = formatoHoraBD.parse(registro[0]);
                        registro[0] = formatoHora.format(hora);
                    } catch (Exception ex) {
                    }
                    try {
                        Date fecha = formatoBD.parse(registro[1]);
                        registro[1] = formato.format(fecha);
                    } catch (Exception ex) {
                    }
                    if (registro[3] == null) {
                        registro[3] = "";
                    }
                    if (registro[4] == null) {
                        registro[4] = "";
                    }
                    Object[] fila = new Object[]{
                        registro[0],
                        registro[1],
                        registro[2],
                        registro[3] + " - " + registro[4],
                        registro[5],
                        registro[6]
                    };
                    dtm.addRow(fila);
                }
            }
        }

        //Establecer modelo
        jTable_Resultados.setModel(dtm);

        //Cambiar tamaño columna
        TableColumn tc = jTable_Resultados.getColumn("Hora");
        tc.setPreferredWidth(40);
        tc = jTable_Resultados.getColumn("Equipo Local");
        tc.setPreferredWidth(150);
        tc = jTable_Resultados.getColumn("Equipo Visitante");
        tc.setPreferredWidth(150);
        tc = jTable_Resultados.getColumn("Estado");
        tc.setPreferredWidth(90);

        //Alinear cabeceras centradas
        DefaultTableCellRenderer alinearCabecera = (DefaultTableCellRenderer) jTable_Resultados.getTableHeader().getDefaultRenderer();
        alinearCabecera.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        //Alinear columnas centradas menos las de los equipos
        DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
        alinear.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        DefaultTableCellRenderer alinearDerecha = new DefaultTableCellRenderer();
        alinearDerecha.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        for (int i = 0; i < jTable_Resultados.getColumnCount(); i++) {
            if (i == 2) {
                jTable_Resultados.getColumnModel().getColumn(i).setCellRenderer(alinearDerecha);
            } else if (i != 4) {
                jTable_Resultados.getColumnModel().getColumn(i).setCellRenderer(alinear);
            }
        }
    }

    public void cargarTemporadas() {
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        datosTemporada = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, anio FROM temporada");

        if (datosTemporada != null) {
            int n = datosTemporada.size();
            for (int i = 0; i < n; i++) {
                String[] registro = (String[]) datosTemporada.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Temporadas.setModel(dcbm);
    }

    public void cargarTemporadaActual() {
        if (jComboBox_Temporadas.getSelectedIndex() >= 0) {
            String[] registro = (String[]) datosTemporada.get(jComboBox_Temporadas.getSelectedIndex());
            idTemporada = Integer.parseInt(registro[0]);
        }
    }

    public void cargarCompeticiones() {
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        datosCompeticion = BaseDeDatos.getBD().ConsultaSQL("SELECT C.idCompeticion, C.nombre, L.nombre FROM competicion C, liga L WHERE (idTemporada = " + idTemporada + ") AND (C.idLiga = L.idLiga)");

        if (datosCompeticion != null) {
            int n = datosCompeticion.size();
            for (int i = 0; i < n; i++) {
                String[] registro = (String[]) datosCompeticion.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Competiciones.setModel(dcbm);
    }

    public void cargarCompeticionActual() {
        boolean hayEquipos = false;
        try {
            String[] registro = (String[]) datosCompeticion.get(jComboBox_Competiciones.getSelectedIndex());
            idCompeticion = Integer.parseInt(registro[0]);
            String nombre = registro[1];
            String liga = registro[2];
            registro = (String[]) datosTemporada.get(jComboBox_Temporadas.getSelectedIndex());
            int anio = Integer.parseInt(registro[1]);
            jLabel_Competicion.setText(nombre + " " + anio + "-" + (anio + 1));
            jLabel_Liga.setText(liga);

            //Comprueba si la competición tiene equipos para habilitar los botones de jornadas y estadísticas
            ArrayList numeroEquipos = BaseDeDatos.getBD().ConsultaSQL("SELECT COUNT(*) FROM compite WHERE idCompeticion=" + idCompeticion);
            registro = (String[]) numeroEquipos.get(0);
            if (Integer.parseInt(registro[0]) > 1) {
                hayEquipos = true;
            }
        } catch (Exception ex) {
            idCompeticion = -1;
            jLabel_Competicion.setText("(Sin competiciones)");
            jLabel_Liga.setText("-");
        }
        jButton_AnadirJornada.setEnabled(hayEquipos);
        jButton_ModificarJornada.setEnabled(hayEquipos && jButton_ModificarJornada.isEnabled());
        jButton_EliminarJornada.setEnabled(hayEquipos && jButton_EliminarJornada.isEnabled());
        jButton_Estadisticas.setEnabled(hayEquipos && jButton_Estadisticas.isEnabled());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel_Competicion = new javax.swing.JLabel();
        jLabel_Temporadas = new javax.swing.JLabel();
        jComboBox_Temporadas = new javax.swing.JComboBox<>();
        jLabel_Competiciones = new javax.swing.JLabel();
        jComboBox_Competiciones = new javax.swing.JComboBox<>();
        jButton_Estadisticas = new javax.swing.JButton();
        jLabel_Liga = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_Resultados = new javax.swing.JLabel();
        jButton_Anterior = new javax.swing.JButton();
        jComboBox_Jornadas = new javax.swing.JComboBox<>();
        jButton_Siguiente = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Resultados = new javax.swing.JTable();
        jButton_ModificarJornada = new javax.swing.JButton();
        jButton_AnadirJornada = new javax.swing.JButton();
        jButton_EliminarJornada = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel_Clasificacion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Clasificacion = new javax.swing.JTable();

        setBackground(new java.awt.Color(242, 255, 242));
        setMinimumSize(new java.awt.Dimension(20, 20));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(246, 220, 214));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 90));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel_Competicion.setBackground(new java.awt.Color(214, 246, 231));
        jLabel_Competicion.setForeground(new java.awt.Color(156, 76, 44));
        jLabel_Competicion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel_Competicion.setText("Competición");
        jLabel_Competicion.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 16, 0));
        jLabel_Competicion.setMaximumSize(new java.awt.Dimension(2300, 300));
        jLabel_Competicion.setMinimumSize(new java.awt.Dimension(300, 60));
        jLabel_Competicion.setPreferredSize(new java.awt.Dimension(300, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        jPanel1.add(jLabel_Competicion, gridBagConstraints);

        jLabel_Temporadas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_Temporadas.setText("Temporadas");
        jLabel_Temporadas.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel_Temporadas.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel_Temporadas.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, -20, 0);
        jPanel1.add(jLabel_Temporadas, gridBagConstraints);

        jComboBox_Temporadas.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jComboBox_Temporadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Temporadas.setMinimumSize(new java.awt.Dimension(100, 32));
        jComboBox_Temporadas.setPreferredSize(new java.awt.Dimension(100, 32));
        jComboBox_Temporadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TemporadasActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(22, 16, 0, 0);
        jPanel1.add(jComboBox_Temporadas, gridBagConstraints);

        jLabel_Competiciones.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_Competiciones.setText("Competiciones");
        jLabel_Competiciones.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel_Competiciones.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel_Competiciones.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, -20, 0);
        jPanel1.add(jLabel_Competiciones, gridBagConstraints);

        jComboBox_Competiciones.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jComboBox_Competiciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Competiciones.setMinimumSize(new java.awt.Dimension(200, 32));
        jComboBox_Competiciones.setPreferredSize(new java.awt.Dimension(200, 32));
        jComboBox_Competiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_CompeticionesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(22, 16, 0, 0);
        jPanel1.add(jComboBox_Competiciones, gridBagConstraints);

        jButton_Estadisticas.setFont(new java.awt.Font("Microsoft JhengHei", 1, 16)); // NOI18N
        jButton_Estadisticas.setText("ESTADÍSTICAS");
        jButton_Estadisticas.setIconTextGap(12);
        jButton_Estadisticas.setMaximumSize(new java.awt.Dimension(200, 40));
        jButton_Estadisticas.setMinimumSize(new java.awt.Dimension(200, 40));
        jButton_Estadisticas.setPreferredSize(new java.awt.Dimension(200, 40));
        jButton_Estadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EstadisticasActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 0, 16);
        jPanel1.add(jButton_Estadisticas, gridBagConstraints);

        jLabel_Liga.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_Liga.setText("Liga");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 6, 0);
        jPanel1.add(jLabel_Liga, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(214, 239, 246));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 400));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 464));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel_Resultados.setBackground(new java.awt.Color(214, 239, 246));
        jLabel_Resultados.setForeground(new java.awt.Color(44, 123, 156));
        jLabel_Resultados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Resultados.setText("Resultados");
        jLabel_Resultados.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 16, 0));
        jLabel_Resultados.setMaximumSize(new java.awt.Dimension(2300, 300));
        jLabel_Resultados.setMinimumSize(new java.awt.Dimension(300, 60));
        jLabel_Resultados.setPreferredSize(new java.awt.Dimension(300, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabel_Resultados, gridBagConstraints);

        jButton_Anterior.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Anterior.setText("Anterior");
        jButton_Anterior.setMaximumSize(new java.awt.Dimension(71, 32));
        jButton_Anterior.setMinimumSize(new java.awt.Dimension(71, 32));
        jButton_Anterior.setPreferredSize(new java.awt.Dimension(80, 32));
        jButton_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnteriorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        jPanel2.add(jButton_Anterior, gridBagConstraints);

        jComboBox_Jornadas.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jComboBox_Jornadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Jornadas.setMaximumSize(new java.awt.Dimension(71, 32));
        jComboBox_Jornadas.setMinimumSize(new java.awt.Dimension(71, 32));
        jComboBox_Jornadas.setPreferredSize(new java.awt.Dimension(80, 32));
        jComboBox_Jornadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_JornadasActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        jPanel2.add(jComboBox_Jornadas, gridBagConstraints);

        jButton_Siguiente.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Siguiente.setText("Siguiente");
        jButton_Siguiente.setMaximumSize(new java.awt.Dimension(71, 32));
        jButton_Siguiente.setMinimumSize(new java.awt.Dimension(71, 32));
        jButton_Siguiente.setPreferredSize(new java.awt.Dimension(80, 32));
        jButton_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SiguienteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        jPanel2.add(jButton_Siguiente, gridBagConstraints);

        jTable_Resultados.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable_Resultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable_Resultados.setRowHeight(24);
        jScrollPane2.setViewportView(jTable_Resultados);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jButton_ModificarJornada.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_ModificarJornada.setText("MODIFICAR JORNADA");
        jButton_ModificarJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarJornadaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 16, 16);
        jPanel2.add(jButton_ModificarJornada, gridBagConstraints);

        jButton_AnadirJornada.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_AnadirJornada.setText("AÑADIR JORNADA");
        jButton_AnadirJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirJornadaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 16, 16);
        jPanel2.add(jButton_AnadirJornada, gridBagConstraints);

        jButton_EliminarJornada.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_EliminarJornada.setText("ELIMINAR JORNADA");
        jButton_EliminarJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarJornadaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 16, 16);
        jPanel2.add(jButton_EliminarJornada, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(214, 246, 231));
        jPanel3.setMinimumSize(new java.awt.Dimension(300, 400));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 610));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel_Clasificacion.setBackground(new java.awt.Color(214, 246, 231));
        jLabel_Clasificacion.setForeground(new java.awt.Color(44, 156, 115));
        jLabel_Clasificacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Clasificacion.setText("Clasificación");
        jLabel_Clasificacion.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 16, 0));
        jLabel_Clasificacion.setMaximumSize(new java.awt.Dimension(2300, 300));
        jLabel_Clasificacion.setMinimumSize(new java.awt.Dimension(300, 60));
        jLabel_Clasificacion.setPreferredSize(new java.awt.Dimension(300, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabel_Clasificacion, gridBagConstraints);

        jTable_Clasificacion.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable_Clasificacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable_Clasificacion.setRowHeight(24);
        jScrollPane1.setViewportView(jTable_Clasificacion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 8);
        add(jPanel3, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnteriorActionPerformed
        if (jComboBox_Jornadas.getSelectedIndex() > 0) {
            jComboBox_Jornadas.setSelectedIndex(jComboBox_Jornadas.getSelectedIndex() - 1);
            jButton_Siguiente.setEnabled(true);
            if (jComboBox_Jornadas.getSelectedIndex() == 0) {
                jButton_Anterior.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jButton_AnteriorActionPerformed

    private void jButton_AnadirJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirJornadaActionPerformed
        Alta_Jornada ventana = new Alta_Jornada((JFrame) this.getRootPane().getParent(), true, idCompeticion);
        ventana.setTitle("Nueva jornada");
        ventana.setSize(new Dimension(1000, 600));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        //Se actualizan las jornadas, los resultados y la clasificación después de añadir una jornada
        cargarJornadas();
        jComboBox_Jornadas.setSelectedIndex(jComboBox_Jornadas.getItemCount() - 1);
        cargarTablaResultados();
        cargarTablaClasificacion();
    }//GEN-LAST:event_jButton_AnadirJornadaActionPerformed

    private void jComboBox_JornadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_JornadasActionPerformed
        jButton_Anterior.setEnabled(jComboBox_Jornadas.getSelectedIndex() > 0);
        jButton_Siguiente.setEnabled(jComboBox_Jornadas.getSelectedIndex() < jComboBox_Jornadas.getItemCount() - 1);
        cargarTablaResultados();
    }//GEN-LAST:event_jComboBox_JornadasActionPerformed

    private void jButton_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SiguienteActionPerformed
        if (jComboBox_Jornadas.getSelectedIndex() < jComboBox_Jornadas.getItemCount() - 1) {
            jComboBox_Jornadas.setSelectedIndex(jComboBox_Jornadas.getSelectedIndex() + 1);
            jButton_Anterior.setEnabled(true);
            if (jComboBox_Jornadas.getSelectedIndex() == jComboBox_Jornadas.getItemCount() - 1) {
                jButton_Siguiente.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jButton_SiguienteActionPerformed

    private void jComboBox_TemporadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TemporadasActionPerformed
        String[] registro = (String[]) datosTemporada.get(jComboBox_Temporadas.getSelectedIndex());
        idTemporada = Integer.parseInt(registro[0]);
        cargarCompeticiones();
        jComboBox_CompeticionesActionPerformed(evt); //se fuerza a que se carguen los resultados
    }//GEN-LAST:event_jComboBox_TemporadasActionPerformed

    private void jComboBox_CompeticionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_CompeticionesActionPerformed
        cargarCompeticionActual();
        cargarJornadas();
        cargarTablaResultados();
        cargarTablaClasificacion();
    }//GEN-LAST:event_jComboBox_CompeticionesActionPerformed

    private void jButton_ModificarJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarJornadaActionPerformed
        Modificar_Jornada ventana = new Modificar_Jornada((JFrame) this.getRootPane().getParent(), true, idCompeticion, jornadaActual);
        ventana.setTitle("Modificar jornada");
        ventana.setSize(new Dimension(1000, 600));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        //Se actualizan las jornadas, los resultados y la clasificación después de modificar una jornada
        int posJornada = jComboBox_Jornadas.getSelectedIndex();
        cargarJornadas();
        jComboBox_Jornadas.setSelectedIndex(posJornada);
        cargarTablaResultados();
        cargarTablaClasificacion();
    }//GEN-LAST:event_jButton_ModificarJornadaActionPerformed

    private void jButton_EstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EstadisticasActionPerformed
        Estadisticas ventana = new Estadisticas((JFrame) this.getRootPane().getParent(), true, idCompeticion);
        ventana.setTitle("Estadísticas");
        ventana.setSize(new Dimension(1100, 560));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
    }//GEN-LAST:event_jButton_EstadisticasActionPerformed

    private void jButton_EliminarJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarJornadaActionPerformed
        int respuesta = JOptionPane.showOptionDialog(this, "¿Seguro que quiere eliminar esta jornada de la competición?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"SÍ", "NO"}, "NO");
        if (respuesta == 0) {
            BaseDeDatos.getBD().ConsultaSQL("DELETE FROM partido WHERE (idCompeticion = " + idCompeticion + ") AND (jornada = " + jornadaActual + ")");

            //Al eliminar una jornada es necesario generar la clasificación actualizada
            Clasificacion.generar(idCompeticion);

            //Se actualizan las jornadas, los resultados y la clasificación después de eliminar una jornada
            cargarJornadas();
            cargarTablaResultados();
            cargarTablaClasificacion();
        }
    }//GEN-LAST:event_jButton_EliminarJornadaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AnadirJornada;
    private javax.swing.JButton jButton_Anterior;
    private javax.swing.JButton jButton_EliminarJornada;
    private javax.swing.JButton jButton_Estadisticas;
    private javax.swing.JButton jButton_ModificarJornada;
    private javax.swing.JButton jButton_Siguiente;
    private javax.swing.JComboBox<String> jComboBox_Competiciones;
    private javax.swing.JComboBox<String> jComboBox_Jornadas;
    private javax.swing.JComboBox<String> jComboBox_Temporadas;
    private javax.swing.JLabel jLabel_Clasificacion;
    private javax.swing.JLabel jLabel_Competicion;
    private javax.swing.JLabel jLabel_Competiciones;
    private javax.swing.JLabel jLabel_Liga;
    private javax.swing.JLabel jLabel_Resultados;
    private javax.swing.JLabel jLabel_Temporadas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Clasificacion;
    private javax.swing.JTable jTable_Resultados;
    // End of variables declaration//GEN-END:variables
}
