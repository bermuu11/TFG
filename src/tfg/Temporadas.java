/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import tfg.Alta.Alta_Temporada;
import tfg.InfoModificar.Modificar_Temporada;

/**
 *
 * @author Antonio
 */
public class Temporadas extends javax.swing.JPanel {

    private class miTablaModel extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    ArrayList datos = null;

    int id = -1;
    int anio = -1;

    public Temporadas() {
        initComponents();
        cargarTabla();
        estiloJLabel();
        estiloJTable();
        estadoBotones();
    }

    public void estiloJLabel() {
        jLabel_Temporadas.setFont(new Font("Segoe UI", Font.BOLD, 50));
        jLabel_Temporadas.setOpaque(true);
    }

    public void estiloJTable() {
        JTableHeader header = jTable_Temporadas.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTable_Temporadas.setFillsViewportHeight(true); // para que el fondo se use en todo
    }

    public void estadoBotones() {
        boolean habilitado = jTable_Temporadas.getSelectedRow() != -1;
        jButton_Modificar.setEnabled(habilitado);
        jButton_Eliminar.setEnabled(habilitado);
    }

    public void cargarTabla() {
        String[] registro = null;

        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Año");

        datos = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, anio FROM temporada");

        if (datos != null) {
            int n = datos.size();
            for (int i = 0; i < n; i++) {
                registro = (String[]) datos.get(i);
                Object[] fila = new Object[]{
                    registro[1]
                };
                dtm.addRow(fila);
            }
        }

        jTable_Temporadas.setModel(dtm);
    }

    private void modificar() {
        String[] registro = (String[]) datos.get(jTable_Temporadas.getSelectedRow());
        id = Integer.parseInt(registro[0]);
        anio = Integer.parseInt(registro[1]);

        Modificar_Temporada ventana = new Modificar_Temporada((JFrame) this.getRootPane().getParent(), true, id, anio);
        ventana.setTitle("Modificar temporada");
        ventana.setSize(new Dimension(300, 200));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla();
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

        jButton_Crear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Temporadas = new javax.swing.JTable();
        jLabel_Temporadas = new javax.swing.JLabel();
        jButton_Eliminar = new javax.swing.JButton();
        jButton_Modificar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(243, 255, 252));
        setLayout(new java.awt.GridBagLayout());

        jButton_Crear.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Crear.setText("CREAR NUEVA");
        jButton_Crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CrearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 48, 0);
        add(jButton_Crear, gridBagConstraints);

        jTable_Temporadas.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable_Temporadas.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_Temporadas.setRowHeight(24);
        jTable_Temporadas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable_Temporadas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_TemporadasMouseClicked(evt);
            }
        });
        jTable_Temporadas.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable_TemporadasPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Temporadas);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 16, 16);
        add(jScrollPane1, gridBagConstraints);

        jLabel_Temporadas.setBackground(new java.awt.Color(214, 216, 246));
        jLabel_Temporadas.setForeground(new java.awt.Color(47, 44, 156));
        jLabel_Temporadas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel_Temporadas.setText("Temporadas");
        jLabel_Temporadas.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 8, 0));
        jLabel_Temporadas.setMaximumSize(new java.awt.Dimension(2300, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(jLabel_Temporadas, gridBagConstraints);

        jButton_Eliminar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Eliminar.setText("ELIMINAR");
        jButton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 48, 0);
        add(jButton_Eliminar, gridBagConstraints);

        jButton_Modificar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Modificar.setText("MODIFICAR");
        jButton_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 16, 48, 0);
        add(jButton_Modificar, gridBagConstraints);

        jPanel1.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CrearActionPerformed
        Alta_Temporada ventana = new Alta_Temporada((JFrame) this.getRootPane().getParent(), true);
        ventana.setTitle("Nueva temporada");
        ventana.setSize(new Dimension(300, 200));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla();
    }//GEN-LAST:event_jButton_CrearActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        //Comprueba si la temporada tiene competiciones para permitir borrar
        String[] registro = (String[]) datos.get(jTable_Temporadas.getSelectedRow());
        id = Integer.parseInt(registro[0]);

        ArrayList numeroCompeticiones = BaseDeDatos.getBD().ConsultaSQL("SELECT COUNT(*) FROM competicion WHERE idTemporada=" + id);
        registro = (String[]) numeroCompeticiones.get(0);
        if (Integer.parseInt(registro[0]) > 0) {
            JOptionPane.showMessageDialog(this, "No se puede borrar esta temporada porque tiene competiciones activas", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(this, "¿Seguro que quiere eliminar esta temporada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"SÍ", "NO"}, "NO");
        if (respuesta == 0) {
            datos = BaseDeDatos.getBD().ConsultaSQL("DELETE FROM temporada WHERE idTemporada=" + id);
            cargarTabla();
            //Informa a los otros módulos del cambio realizado
            Inicio.temporadaModificada();
        }
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void jTable_TemporadasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_TemporadasMouseClicked
        if (evt.getClickCount() == 1) {
            estadoBotones();
        }
        if (evt.getClickCount() == 2) {
            modificar();
        }
    }//GEN-LAST:event_jTable_TemporadasMouseClicked

    private void jButton_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarActionPerformed
        modificar();
    }//GEN-LAST:event_jButton_ModificarActionPerformed

    private void jTable_TemporadasPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable_TemporadasPropertyChange
        estadoBotones();
    }//GEN-LAST:event_jTable_TemporadasPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Crear;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JButton jButton_Modificar;
    private javax.swing.JLabel jLabel_Temporadas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Temporadas;
    // End of variables declaration//GEN-END:variables
}
