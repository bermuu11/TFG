/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.InfoModificar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import tfg.Alta.Alta_JugadorEquipo;
import tfg.BaseDeDatos;

/**
 *
 * @author Antonio
 */
public class InfoModificar_Equipo extends javax.swing.JDialog {

    private static final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");

    private class miTablaModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    boolean modificar = false;
    int idEquipo;
    int idCompeticion;

    ArrayList datos = null;
    ArrayList datosCompeticion = null;
    ArrayList datosJugador = null;

    public InfoModificar_Equipo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public InfoModificar_Equipo(java.awt.Frame parent, boolean modal, int idEquipo) {
        super(parent, modal);
        initComponents();
        getContentPane().setBackground(new Color(246, 244, 214));
        this.idEquipo = idEquipo;
        estiloJLabel();
        estiloJTable();
        modificar();
        cargarDatos();
        cargarCompeticiones();
        cargarTabla(idCompeticion, idEquipo);
        if (idCompeticion != -1) {
            jComboBox_Competicion.setSelectedIndex(jComboBox_Competicion.getItemCount() - 1);
            jButton_Cancelar.setVisible(false);
        } else {
            jButton_AnadirJugador.setEnabled(false);
        }
        estadoBotones();
    }

    public void estiloJLabel() {
        jTextField_Nombre.setDisabledTextColor(Color.DARK_GRAY);
        jTextField_Estadio.setDisabledTextColor(Color.DARK_GRAY);
        jTextField_Ciudad.setDisabledTextColor(Color.DARK_GRAY);
        jTextField_Entrenador.setDisabledTextColor(Color.DARK_GRAY);
        jTextField_Presidente.setDisabledTextColor(Color.DARK_GRAY);
        jTextField_AnoFundacion.setDisabledTextColor(Color.DARK_GRAY);
    }

    public void estiloJTable() {
        JTableHeader header = jTable_Jugadores.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTable_Jugadores.setFillsViewportHeight(true); // para que el fondo se use en todo
    }

    public void estadoBotones() {
        boolean habilitado = jTable_Jugadores.getSelectedRow() != -1 && modificar == true;
        jButton_EliminarJugador.setEnabled(habilitado);
    }

    public void modificar() {
        if (modificar == false) {
            jTextField_Nombre.setEnabled(false);
            jTextField_Estadio.setEnabled(false);
            jTextField_Ciudad.setEnabled(false);
            jTextField_AnoFundacion.setEnabled(false);
            jTextField_Entrenador.setEnabled(false);
            jTextField_Presidente.setEnabled(false);
            jButton_Cancelar.setVisible(false);
            jButton_AnadirJugador.setEnabled(false);
            jButton_Modificar.setText("MODIFICAR");
            setTitle("Información del equipo");
        } else {
            jTextField_Nombre.setEnabled(true);
            jTextField_Estadio.setEnabled(true);
            jTextField_Ciudad.setEnabled(true);
            jTextField_AnoFundacion.setEnabled(true);
            jTextField_Entrenador.setEnabled(true);
            jTextField_Presidente.setEnabled(true);
            jButton_Modificar.setText("GUARDAR");
            jButton_Cancelar.setVisible(true);
            jButton_AnadirJugador.setEnabled(datosCompeticion.size() > 0); // solo se puede añadir si hay competición
            setTitle("Información del equipo (modificando)");
        }
        estadoBotones();
    }

    public void cargarDatos() {
        datos = BaseDeDatos.getBD().ConsultaSQL("SELECT nombre, estadio, anioFundacion, ciudad, entrenador, presidente\n"
                + "FROM equipo WHERE idEquipo = " + idEquipo);

        String[] registro = (String[]) datos.get(0);
        jTextField_Nombre.setText(registro[0]);
        jTextField_Estadio.setText(registro[1]);
        jTextField_AnoFundacion.setText(registro[2]);
        jTextField_Ciudad.setText(registro[3]);
        jTextField_Entrenador.setText(registro[4]);
        jTextField_Presidente.setText(registro[5]);
    }

    public void cargarCompeticiones() {
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        String[] registro = null;
        datosCompeticion = BaseDeDatos.getBD().ConsultaSQL("SELECT C.idCompeticion, C.nombre, T.anio FROM competicion C, compite CE, temporada T WHERE (CE.idCompeticion = C.idCompeticion) AND (CE.idEquipo = " + idEquipo + ") AND (C.idTemporada = T.idTemporada);");

        if (datosCompeticion != null) {
            int n = datosCompeticion.size();
            for (int i = 0; i < n; i++) {
                registro = (String[]) datosCompeticion.get(i);
                int anio = Integer.parseInt(registro[2]);
                dcbm.addElement(registro[1] +" " + anio + "-" + (anio + 1));
            }
        }
        jComboBox_Competicion.setModel(dcbm);
        if (datosCompeticion.isEmpty()) {
            idCompeticion = -1;
        } else {
            idCompeticion = Integer.parseInt(registro[0]); //identificador de la ultima competicion leida
        }
    }

    public void cargarTabla(int idCompeticion, int idEquipo) {
        String[] registro = null;

        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Nombre");
        dtm.addColumn("Apellidos");
        dtm.addColumn("Fecha de nacimiento");
        dtm.addColumn("Posición");
        dtm.addColumn("País");
        dtm.addColumn("Fecha traspaso");

        if (idCompeticion != -1) {
            datosJugador = BaseDeDatos.getBD().ConsultaSQL("SELECT J.idJugador, J.nombre, J.apellido1, J.apellido2, J.fechaNacimiento, J.posicion, J.pais, P.fechaTraspaso FROM jugador J, pertenece P WHERE (P.idJugador = J.idJugador) AND (P.idEquipo = " + idEquipo + ") AND (P.idCompeticion = " + idCompeticion + ");");

            if (datosJugador != null) {
                int n = datosJugador.size();
                for (int i = 0; i < n; i++) {
                    registro = (String[]) datosJugador.get(i);

                    try {
                        Date fecha = formatoBD.parse(registro[4]);
                        registro[4] = formato.format(fecha);
                    } catch (Exception ex) {
                    }
                    try {
                        Date fecha = formatoBD.parse(registro[7]);
                        registro[7] = formato.format(fecha);
                    } catch (Exception ex) {
                    }

                    Object[] fila = new Object[]{
                        registro[1],
                        registro[2] + " " + registro[3],
                        registro[4],
                        registro[5],
                        registro[6],
                        registro[7]
                    };
                    dtm.addRow(fila);
                }
            }
        }
        jTable_Jugadores.setModel(dtm);

        //Se cambia el tamaño de la columna Fecha de nacimiento
        TableColumn tc = jTable_Jugadores.getColumn("Fecha de nacimiento");
        tc.setPreferredWidth(100);
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
        jLabel_Nombre = new javax.swing.JLabel();
        jTextField_Nombre = new javax.swing.JTextField();
        jLabel_Estadio = new javax.swing.JLabel();
        jTextField_Estadio = new javax.swing.JTextField();
        jLabel_Ciudad = new javax.swing.JLabel();
        jTextField_Ciudad = new javax.swing.JTextField();
        jLabel_Entrenador = new javax.swing.JLabel();
        jTextField_Entrenador = new javax.swing.JTextField();
        jLabel_Presidente = new javax.swing.JLabel();
        jTextField_Presidente = new javax.swing.JTextField();
        jLabel_AnoFundacion = new javax.swing.JLabel();
        jTextField_AnoFundacion = new javax.swing.JTextField();
        jLabel_Jugadores = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel_Competicion = new javax.swing.JLabel();
        jComboBox_Competicion = new javax.swing.JComboBox<>();
        jButton_EliminarJugador = new javax.swing.JButton();
        jButton_AnadirJugador = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Jugadores = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Cerrar = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();
        jButton_Modificar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel_Nombre.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Nombre.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 208, 0, 0);
        jPanel1.add(jLabel_Nombre, gridBagConstraints);

        jTextField_Nombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Nombre.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_Nombre.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 208, 0, 0);
        jPanel1.add(jTextField_Nombre, gridBagConstraints);

        jLabel_Estadio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Estadio.setText("Estadio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanel1.add(jLabel_Estadio, gridBagConstraints);

        jTextField_Estadio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Estadio.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_Estadio.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 24, 0, 0);
        jPanel1.add(jTextField_Estadio, gridBagConstraints);

        jLabel_Ciudad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Ciudad.setText("Ciudad");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanel1.add(jLabel_Ciudad, gridBagConstraints);

        jTextField_Ciudad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Ciudad.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_Ciudad.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 24, 0, 0);
        jPanel1.add(jTextField_Ciudad, gridBagConstraints);

        jLabel_Entrenador.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Entrenador.setText("Entrenador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanel1.add(jLabel_Entrenador, gridBagConstraints);

        jTextField_Entrenador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Entrenador.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_Entrenador.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 24, 0, 0);
        jPanel1.add(jTextField_Entrenador, gridBagConstraints);

        jLabel_Presidente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Presidente.setText("Presidente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanel1.add(jLabel_Presidente, gridBagConstraints);

        jTextField_Presidente.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Presidente.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_Presidente.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 24, 0, 0);
        jPanel1.add(jTextField_Presidente, gridBagConstraints);

        jLabel_AnoFundacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_AnoFundacion.setText("Año fundación");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 208);
        jPanel1.add(jLabel_AnoFundacion, gridBagConstraints);

        jTextField_AnoFundacion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_AnoFundacion.setMinimumSize(new java.awt.Dimension(136, 32));
        jTextField_AnoFundacion.setPreferredSize(new java.awt.Dimension(136, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 24, 0, 208);
        jPanel1.add(jTextField_AnoFundacion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(32, 0, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel_Jugadores.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Jugadores.setText("Jugadores");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 20, 0, 0);
        getContentPane().add(jLabel_Jugadores, gridBagConstraints);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel_Competicion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Competicion.setText("Competición");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        jPanel3.add(jLabel_Competicion, gridBagConstraints);

        jComboBox_Competicion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Competicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Competicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_CompeticionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel3.add(jComboBox_Competicion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 0, 0);
        getContentPane().add(jPanel3, gridBagConstraints);

        jButton_EliminarJugador.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_EliminarJugador.setText("ELIMINAR JUGADOR");
        jButton_EliminarJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarJugadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 8, 16);
        getContentPane().add(jButton_EliminarJugador, gridBagConstraints);

        jButton_AnadirJugador.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_AnadirJugador.setText("AÑADIR JUGADOR");
        jButton_AnadirJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirJugadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 8, 16);
        getContentPane().add(jButton_AnadirJugador, gridBagConstraints);

        jTable_Jugadores.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable_Jugadores.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_Jugadores.setRowHeight(24);
        jTable_Jugadores.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable_Jugadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_JugadoresMouseClicked(evt);
            }
        });
        jTable_Jugadores.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable_JugadoresPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Jugadores);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 16);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButton_Cerrar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Cerrar.setText("CERRAR");
        jButton_Cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CerrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 16, 0);
        jPanel2.add(jButton_Cerrar, gridBagConstraints);

        jButton_Cancelar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Cancelar.setText("CANCELAR");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 16, 16);
        jPanel2.add(jButton_Cancelar, gridBagConstraints);

        jButton_Modificar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Modificar.setText("MODIFICAR");
        jButton_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 16, 16);
        jPanel2.add(jButton_Modificar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean hayError(String nombre, String campo) {
        if (campo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo " + nombre + " es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private void jButton_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarActionPerformed
        if (modificar == false) {
            modificar = true;
            modificar();
        } else {
            String nombre = jTextField_Nombre.getText();
            if (hayError("nombre", nombre)) {
                return;
            }

            //Comprueba si el equipo ya existe antes de modificarlo (solo compara el nombre)
            ArrayList consulta = BaseDeDatos.getBD().ConsultaSQL("SELECT idEquipo FROM equipo WHERE (nombre='" + nombre + "') AND (idEquipo!=" + idEquipo + ")");
            if (!consulta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ya existe un equipo con el mismo nombre.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String estadio = jTextField_Estadio.getText();
            if (hayError("estadio", estadio)) {
                return;
            }
            String ciudad = jTextField_Ciudad.getText();
            if (hayError("ciudad", ciudad)) {
                return;
            }
            String entrenador = jTextField_Entrenador.getText();
            if (hayError("entrenador", entrenador)) {
                return;
            }
            String presidente = jTextField_Presidente.getText();
            int anioFundacion;
            try {
                anioFundacion = Integer.parseInt(jTextField_AnoFundacion.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "El año de fundación es incorrecto.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //Si todo ha ido bien actualiza el equipo y finaliza el modo modificación
            BaseDeDatos.getBD().ConsultaSQL("UPDATE equipo SET nombre='" + nombre + "',estadio='" + estadio + "',anioFundacion=" + anioFundacion + ",ciudad='" + ciudad + "',entrenador='" + entrenador + "',presidente='" + presidente + "' WHERE idEquipo=" + idEquipo);
            modificar = false;
            modificar();
            cargarDatos();
        }
    }//GEN-LAST:event_jButton_ModificarActionPerformed

    private void jButton_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CerrarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CerrarActionPerformed

    private void jTable_JugadoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_JugadoresMouseClicked
        if (evt.getClickCount() == 1 && modificar == true) {
            estadoBotones();
        }
    }//GEN-LAST:event_jTable_JugadoresMouseClicked

    private void jButton_AnadirJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirJugadorActionPerformed
        String[] registro = (String[]) datosCompeticion.get(jComboBox_Competicion.getSelectedIndex());
        idCompeticion = Integer.parseInt(registro[0]);

        Alta_JugadorEquipo ventana = new Alta_JugadorEquipo((JFrame) this.getRootPane().getParent().getParent(), true, idCompeticion, idEquipo);
        ventana.setTitle("Añadir jugador");
        ventana.setSize(new Dimension(500, 225));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla(idCompeticion, idEquipo);
    }//GEN-LAST:event_jButton_AnadirJugadorActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        modificar = false;
        modificar();
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jButton_EliminarJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarJugadorActionPerformed
        int respuesta = JOptionPane.showOptionDialog(this, "¿Seguro que quiere eliminar este jugador del equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"SÍ", "NO"}, "NO");
        if (respuesta == 0) {
            String[] registro = (String[]) datosJugador.get(jTable_Jugadores.getSelectedRow());
            int idJugador = Integer.parseInt(registro[0]);
            BaseDeDatos.getBD().ConsultaSQL("DELETE FROM pertenece WHERE (idJugador = '" + idJugador + "') AND (idCompeticion = " + idCompeticion + ") AND (idEquipo = " + idEquipo + ");");
            cargarTabla(idCompeticion, idEquipo);
        }
    }//GEN-LAST:event_jButton_EliminarJugadorActionPerformed

    private void jComboBox_CompeticionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_CompeticionActionPerformed
        String[] registro = (String[]) datosCompeticion.get(jComboBox_Competicion.getSelectedIndex());
        idCompeticion = Integer.parseInt(registro[0]);
        cargarTabla(idCompeticion, idEquipo);
    }//GEN-LAST:event_jComboBox_CompeticionActionPerformed

    private void jTable_JugadoresPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable_JugadoresPropertyChange
        estadoBotones();
    }//GEN-LAST:event_jTable_JugadoresPropertyChange

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
            java.util.logging.Logger.getLogger(InfoModificar_Equipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Equipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Equipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Equipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InfoModificar_Equipo dialog = new InfoModificar_Equipo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_AnadirJugador;
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Cerrar;
    private javax.swing.JButton jButton_EliminarJugador;
    private javax.swing.JButton jButton_Modificar;
    private javax.swing.JComboBox<String> jComboBox_Competicion;
    private javax.swing.JLabel jLabel_AnoFundacion;
    private javax.swing.JLabel jLabel_Ciudad;
    private javax.swing.JLabel jLabel_Competicion;
    private javax.swing.JLabel jLabel_Entrenador;
    private javax.swing.JLabel jLabel_Estadio;
    private javax.swing.JLabel jLabel_Jugadores;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Presidente;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Jugadores;
    private javax.swing.JTextField jTextField_AnoFundacion;
    private javax.swing.JTextField jTextField_Ciudad;
    private javax.swing.JTextField jTextField_Entrenador;
    private javax.swing.JTextField jTextField_Estadio;
    private javax.swing.JTextField jTextField_Nombre;
    private javax.swing.JTextField jTextField_Presidente;
    // End of variables declaration//GEN-END:variables
}
