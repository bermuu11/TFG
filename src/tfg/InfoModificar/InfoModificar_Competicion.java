/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.InfoModificar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import tfg.Alta.Alta_EquipoCompeticion;
import tfg.BaseDeDatos;
import tfg.Inicio;

/**
 *
 * @author Antonio
 */
public class InfoModificar_Competicion extends javax.swing.JDialog {

    private class miTablaModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    ArrayList datosTemporadas = null;
    ArrayList datosLigas = null;
    ArrayList datosCompeticion = null;
    ArrayList datosEquipos = null;
    int idCompeticion;
    int idTemporada;
    int idLiga;
    boolean modificar = false;

    public InfoModificar_Competicion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public InfoModificar_Competicion(java.awt.Frame parent, boolean modal, int idCompeticion) {
        super(parent, modal);
        initComponents();
        getContentPane().setBackground(new Color(214, 246, 231));
        this.idCompeticion = idCompeticion;
        estiloJLabel();
        estiloJTable();
        modificar();
        estadoBotones();
        cargarDatos();
        cargarTemporadas();
        cargarLigas();
        cargarTabla(idCompeticion);
    }

    public void estiloJLabel() {
        jTextField_Nombre.setDisabledTextColor(Color.DARK_GRAY);
    }

    public void estiloJTable() {
        JTableHeader header = jTable_Equipos.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTable_Equipos.setFillsViewportHeight(true); // para que el fondo se use en todo
    }

    public void modificar() {
        if (modificar == false) {
            jTextField_Nombre.setEnabled(false);
            jComboBox_Temporadas.setEnabled(false);
            jComboBox_Ligas.setEnabled(false);
            jButton_Cancelar.setVisible(false);
            jButton_AnadirEquipo.setEnabled(false);
            jButton_ModificarGuardar.setText("MODIFICAR");
            setTitle("Información de la competición");
        } else {
            jTextField_Nombre.setEnabled(true);
            jComboBox_Temporadas.setEnabled(true);
            jComboBox_Ligas.setEnabled(true);
            jButton_ModificarGuardar.setText("GUARDAR");
            jButton_Cancelar.setVisible(true);
            jButton_AnadirEquipo.setEnabled(true);
            setTitle("Información de la competición (modificando)");
        }
        estadoBotones();
    }

    public void estadoBotones() {
        boolean habilitado = jTable_Equipos.getSelectedRow() != -1 && modificar;
        jButton_EliminarEquipo.setEnabled(habilitado);
    }

    public void cargarTemporadas() {
        int posicionSeleccionar = -1;
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        datosTemporadas = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, anio FROM temporada;");

        if (datosTemporadas != null) {
            int n = datosTemporadas.size();
            for (int i = 0; i < n; i++) {
                String[] registro = (String[]) datosTemporadas.get(i);
                dcbm.addElement(registro[1]);
                if (Integer.parseInt(registro[0]) == idTemporada) {
                    posicionSeleccionar = i;
                }
            }
        }
        jComboBox_Temporadas.setModel(dcbm);
        jComboBox_Temporadas.setSelectedIndex(posicionSeleccionar);
    }

    public void cargarLigas() {
        int posicionSeleccionar = -1;
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        datosLigas = BaseDeDatos.getBD().ConsultaSQL("SELECT idLiga, nombre FROM liga;");

        if (datosLigas != null) {
            int n = datosLigas.size();
            for (int i = 0; i < n; i++) {
                String[] registro = (String[]) datosLigas.get(i);
                dcbm.addElement(registro[1]);
                if (Integer.parseInt(registro[0]) == idLiga) {
                    posicionSeleccionar = i;
                }
            }
        }
        jComboBox_Ligas.setModel(dcbm);
        jComboBox_Ligas.setSelectedIndex(posicionSeleccionar);
    }

    public void cargarDatos() {
        datosCompeticion = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, idLiga, nombre FROM competicion WHERE idCompeticion=" + idCompeticion);
        String[] registro = (String[]) datosCompeticion.get(0);
        idTemporada = Integer.parseInt(registro[0]);
        idLiga = Integer.parseInt(registro[1]);
        jTextField_Nombre.setText(registro[2]);
    }

    //MIRAR
    public void mostrarTemporadaLigaActual() {
        String[] registro = null;
        datosCompeticion = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, idLiga FROM competicion WHERE idCompeticion = " + idCompeticion + ";");
        jComboBox_Temporadas.setSelectedIndex(Integer.parseInt(registro[0]));
        jComboBox_Ligas.setSelectedIndex(Integer.parseInt(registro[1]));
    }

    public void cargarTabla(int idCompeticion) {
        String[] registro = null;

        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Nombre");
        dtm.addColumn("Estadio");
        dtm.addColumn("Año de fundación");
        dtm.addColumn("Ciudad");
        dtm.addColumn("Entrenador");
        dtm.addColumn("Presidente");

        datosEquipos = BaseDeDatos.getBD().ConsultaSQL("SELECT E.idEquipo, E.nombre, E.estadio, E.anioFundacion, E.ciudad, E.entrenador, E.presidente FROM equipo E, compite C WHERE (C.idEquipo = E.idEquipo) AND (C.idCompeticion = " + idCompeticion + ");");

        if (datosEquipos != null) {
            int n = datosEquipos.size();
            for (int i = 0; i < n; i++) {
                registro = (String[]) datosEquipos.get(i);
                Object[] fila = new Object[]{
                    registro[1],
                    registro[2],
                    registro[3],
                    registro[4],
                    registro[5],
                    registro[6]
                };
                dtm.addRow(fila);
            }
        }
        jTable_Equipos.setModel(dtm);
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
        jLabel_Temporada = new javax.swing.JLabel();
        jComboBox_Temporadas = new javax.swing.JComboBox<>();
        jLabel_Liga = new javax.swing.JLabel();
        jComboBox_Ligas = new javax.swing.JComboBox<>();
        jLabel_Nombre = new javax.swing.JLabel();
        jTextField_Nombre = new javax.swing.JTextField();
        jLabel_Equipos = new javax.swing.JLabel();
        jButton_AnadirEquipo = new javax.swing.JButton();
        jButton_EliminarEquipo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Equipos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Cerrar = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();
        jButton_ModificarGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel_Temporada.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Temporada.setText("Temporada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel_Temporada, gridBagConstraints);

        jComboBox_Temporadas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Temporadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Temporadas.setMinimumSize(new java.awt.Dimension(72, 30));
        jComboBox_Temporadas.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        jPanel1.add(jComboBox_Temporadas, gridBagConstraints);

        jLabel_Liga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Liga.setText("Liga");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jLabel_Liga, gridBagConstraints);

        jComboBox_Ligas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Ligas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Ligas.setMinimumSize(new java.awt.Dimension(200, 30));
        jComboBox_Ligas.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 100, 0, 0);
        jPanel1.add(jComboBox_Ligas, gridBagConstraints);

        jLabel_Nombre.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Nombre.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jLabel_Nombre, gridBagConstraints);

        jTextField_Nombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Nombre.setMinimumSize(new java.awt.Dimension(200, 30));
        jTextField_Nombre.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 100, 0, 0);
        jPanel1.add(jTextField_Nombre, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(32, 0, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel_Equipos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_Equipos.setText("Equipos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 16, 0, 0);
        getContentPane().add(jLabel_Equipos, gridBagConstraints);

        jButton_AnadirEquipo.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_AnadirEquipo.setText("AÑADIR EQUIPO");
        jButton_AnadirEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirEquipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 8, 16);
        getContentPane().add(jButton_AnadirEquipo, gridBagConstraints);

        jButton_EliminarEquipo.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_EliminarEquipo.setText("ELIMINAR EQUIPO");
        jButton_EliminarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarEquipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 8, 16);
        getContentPane().add(jButton_EliminarEquipo, gridBagConstraints);

        jTable_Equipos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable_Equipos.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_Equipos.setRowHeight(24);
        jTable_Equipos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable_Equipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_EquiposMouseClicked(evt);
            }
        });
        jTable_Equipos.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable_EquiposPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Equipos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
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

        jButton_ModificarGuardar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_ModificarGuardar.setText("MODIFICAR");
        jButton_ModificarGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarGuardarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 16, 16);
        jPanel2.add(jButton_ModificarGuardar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ModificarGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarGuardarActionPerformed
        if (modificar == false) {
            modificar = true;
            modificar();
        } else {
            String nombre = jTextField_Nombre.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo nombre es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] registro = (String[]) datosTemporadas.get(jComboBox_Temporadas.getSelectedIndex());
            idTemporada = Integer.parseInt(registro[0]);
            registro = (String[]) datosLigas.get(jComboBox_Ligas.getSelectedIndex());
            idLiga = Integer.parseInt(registro[0]);

            //Comprueba si la competición ya existe antes de modificarla
            ArrayList consulta = BaseDeDatos.getBD().ConsultaSQL("SELECT idCompeticion FROM competicion WHERE (idTemporada=" + idTemporada + ") AND (idLiga=" + idLiga + ") AND (nombre='" + nombre + "') AND (idCompeticion!=" + idCompeticion + ")");
            if (consulta.isEmpty()) {
                BaseDeDatos.getBD().ConsultaSQL("UPDATE competicion SET idTemporada=" + idTemporada + ",idLiga=" + idLiga + ",nombre='" + nombre + "' WHERE idCompeticion=" + idCompeticion);
                modificar = false;
                modificar();
                cargarDatos();
                //Informa a los otros módulos del cambio realizado
                Inicio.competicionModificada();
            } else {
                JOptionPane.showMessageDialog(this, "La competición " + nombre + " de esa liga ya existe en esa temporada.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton_ModificarGuardarActionPerformed

    private void jButton_AnadirEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirEquipoActionPerformed
        Alta_EquipoCompeticion ventana = new Alta_EquipoCompeticion((JFrame) this.getRootPane().getParent().getParent(), true, idCompeticion);
        ventana.setTitle("Añadir equipo");
        ventana.setSize(new Dimension(300, 200));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla(idCompeticion);
    }//GEN-LAST:event_jButton_AnadirEquipoActionPerformed

    private void jButton_EliminarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarEquipoActionPerformed
        String[] registro = (String[]) datosEquipos.get(jTable_Equipos.getSelectedRow());
        int idEquipo = Integer.parseInt(registro[0]);

        ArrayList numeroPartidos = BaseDeDatos.getBD().ConsultaSQL("SELECT COUNT(*) FROM partido WHERE (idCompeticion=" + idCompeticion + ") AND ((id_equipoLocal=" + idEquipo + ") OR (id_equipoVisitante=" + idEquipo + "))");
        registro = (String[]) numeroPartidos.get(0);
        if (Integer.parseInt(registro[0]) > 0) {
            JOptionPane.showMessageDialog(this, "No se puede borrar este equipo porque tiene partidos jugados en esta competición.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(this, "¿Seguro que quiere eliminar este equipo de la competición?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"SÍ", "NO"}, "NO");
        if (respuesta == 0) {
            //Se borra el equipo de la competición
            BaseDeDatos.getBD().ConsultaSQL("DELETE FROM compite WHERE (idEquipo = " + idEquipo + ") AND (idCompeticion = " + idCompeticion + ");");
            //Se borra la pertencia de los jugadores del equipo borrado en la competición
            BaseDeDatos.getBD().ConsultaSQL("DELETE FROM pertenece WHERE (idEquipo = " + idEquipo + ") AND (idCompeticion = " + idCompeticion + ");");
            //Se borra la clasificación del equipo de la competición
            BaseDeDatos.getBD().ConsultaSQL("DELETE FROM clasifica WHERE (idEquipo = " + idEquipo + ") AND (idCompeticion = " + idCompeticion + ");");
            cargarTabla(idCompeticion);
        }
    }//GEN-LAST:event_jButton_EliminarEquipoActionPerformed

    private void jButton_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CerrarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CerrarActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        modificar = false;
        modificar();
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jTable_EquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_EquiposMouseClicked
        if (evt.getClickCount() == 1 && modificar == true) {
            estadoBotones();
        }
    }//GEN-LAST:event_jTable_EquiposMouseClicked

    private void jTable_EquiposPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable_EquiposPropertyChange
        estadoBotones();
    }//GEN-LAST:event_jTable_EquiposPropertyChange

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
            java.util.logging.Logger.getLogger(InfoModificar_Competicion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Competicion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Competicion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Competicion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InfoModificar_Competicion dialog = new InfoModificar_Competicion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_AnadirEquipo;
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Cerrar;
    private javax.swing.JButton jButton_EliminarEquipo;
    private javax.swing.JButton jButton_ModificarGuardar;
    private javax.swing.JComboBox<String> jComboBox_Ligas;
    private javax.swing.JComboBox<String> jComboBox_Temporadas;
    private javax.swing.JLabel jLabel_Equipos;
    private javax.swing.JLabel jLabel_Liga;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Temporada;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Equipos;
    private javax.swing.JTextField jTextField_Nombre;
    // End of variables declaration//GEN-END:variables
}
