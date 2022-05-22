/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import misqlhsqldb.MiSQLhSQLDB;
import tfg.Alta.Alta_Competicion;
import static tfg.BaseDeDatos.conectarBD;

/**
 *
 * @author Antonio
 */
public class Competiciones extends javax.swing.JPanel {
    private class miTablaModel extends DefaultTableModel{
        public boolean isCellEditable (int row, int column){
            return false;
        }
    }
    
    int id = -1;
    
    ArrayList datos = null;
        
    MiSQLhSQLDB bbdd = new MiSQLhSQLDB("SA", "SA");
    
    public Competiciones() {
        initComponents();
        conectarBD();
        cargarTabla();
        estiloJLabel();
        jButton_Eliminar.setEnabled(false);
    }
    
    public void cargarTabla(){
        String[] registro = null;
        
        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("ID");
        dtm.addColumn("Temporada");
        dtm.addColumn("Liga");
        dtm.addColumn("Nombre");
                
        datos = bbdd.ConsultaSQL("SELECT C.idCompeticion, T.anio, L.nombre, C.nombre\n" +
        "FROM temporada T, liga L, competicion C WHERE T.idTemporada = C.idTemporada AND L.idLiga = C.idLiga");
        
        if(datos != null){
            int n = datos.size();
            for(int i = 0; i < n; i++){
                registro = (String[]) datos.get(i);
                Object[] fila = new Object[]{
                    registro[0],
                    registro[1],
                    registro[2],
                    registro[3]
                };
                dtm.addRow(fila); 
            }
        }
        
        jTable_Competiciones.setModel(dtm);
    }
    
    public void estiloJLabel(){
        jLabel_Competiciones.setFont(new Font("Verdana", 0, 50));
        jLabel_Competiciones.setOpaque(true);
        jLabel_Competiciones.setBackground(Color.yellow);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Competiciones = new javax.swing.JTable();
        jLabel_Competiciones = new javax.swing.JLabel();
        jButton_Crear = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();

        jTable_Competiciones.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_Competiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_CompeticionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Competiciones);

        jLabel_Competiciones.setBackground(new java.awt.Color(255, 255, 51));
        jLabel_Competiciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Competiciones.setText("Competiciones");
        jLabel_Competiciones.setMaximumSize(new java.awt.Dimension(2300, 300));

        jButton_Crear.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Crear.setText("CREAR NUEVA");
        jButton_Crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CrearActionPerformed(evt);
            }
        });

        jButton_Eliminar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Eliminar.setText("ELIMINAR");
        jButton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel_Competiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 873, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton_Crear)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Eliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel_Competiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Crear)
                    .addComponent(jButton_Eliminar))
                .addContainerGap(44, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CrearActionPerformed
        Alta_Competicion ventana =  new Alta_Competicion((JFrame) this.getRootPane().getParent(), true);
        ventana.setTitle("Nueva competición");
        ventana.setSize(new Dimension(500, 300));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla();
    }//GEN-LAST:event_jButton_CrearActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        String[] registro = (String[]) datos.get(jTable_Competiciones.getSelectedRow());
        id = Integer.parseInt(registro[0]);
        datos = bbdd.ConsultaSQL("DELETE FROM competicion WHERE idCompeticion = '" +id +"'");
        cargarTabla();
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void jTable_CompeticionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_CompeticionesMouseClicked
        if(evt.getClickCount()==1){
            jButton_Eliminar.setEnabled(true);
        }
    }//GEN-LAST:event_jTable_CompeticionesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Crear;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JLabel jLabel_Competiciones;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Competiciones;
    // End of variables declaration//GEN-END:variables
}
