/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.sql.Connection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import misqlhsqldb.MiSQLhSQLDB;
import static tfg.BaseDeDatos.conectarBD;
import tfg.Alta.Alta_Liga;

/**
 *
 * @author Antonio
 */
public class Ligas extends javax.swing.JPanel {

    /**
     * Creates new form Ligas
     */
    MiSQLhSQLDB bbdd = new MiSQLhSQLDB("SA", "SA");
    
    ArrayList datos = null;
    
    int id = -1;
    String nombre = "";
    String pais = "";
    
    public Ligas() {
        initComponents();
        conectarBD();
        cargarTabla();
        estiloJLabel();
        jButton_Eliminar.setEnabled(false);
    }
    
    public void cargarTabla(){
        String[] registro = null;
        
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("ID");
        dtm.addColumn("Nombre");
        dtm.addColumn("País");
                
        datos = bbdd.ConsultaSQL("SELECT idLiga, nombre, pais\n" +
        "FROM liga");
        
        if(datos != null){
            int n = datos.size();
            for(int i = 0; i < n; i++){
                registro = (String[]) datos.get(i);
                Object[] fila = new Object[]{
                    registro[0],
                    registro[1],
                    registro[2]
                };
                dtm.addRow(fila); 
            }
        }
        
        jTable_Ligas.setModel(dtm);
    }
    
    public void estiloJLabel(){
        jLabel_Ligas.setFont(new Font("Verdana", 0, 50));
        jLabel_Ligas.setOpaque(true);
        jLabel_Ligas.setBackground(Color.yellow);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_Ligas = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Ligas = new javax.swing.JTable();
        jButton_Crear = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_Ligas.setBackground(new java.awt.Color(255, 255, 51));
        jLabel_Ligas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Ligas.setText("Ligas");
        jLabel_Ligas.setMaximumSize(new java.awt.Dimension(2300, 300));
        add(jLabel_Ligas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTable_Ligas.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_Ligas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_LigasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Ligas);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jButton_Crear.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Crear.setText("CREAR NUEVA");
        jButton_Crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CrearActionPerformed(evt);
            }
        });
        add(jButton_Crear, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, -1, -1));

        jButton_Eliminar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Eliminar.setText("ELIMINAR");
        jButton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarActionPerformed(evt);
            }
        });
        add(jButton_Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 490, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CrearActionPerformed
        Alta_Liga ventana =  new Alta_Liga((JFrame) this.getRootPane().getParent(), true);
        ventana.setTitle("Nueva liga");
        ventana.setSize(new Dimension(500, 300));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        cargarTabla();
    }//GEN-LAST:event_jButton_CrearActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        String[] registro = (String[]) datos.get(jTable_Ligas.getSelectedRow());
        id = Integer.parseInt(registro[0]);
        datos = bbdd.ConsultaSQL("DELETE FROM liga WHERE idLiga = '" +id +"'");
        cargarTabla();
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void jTable_LigasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_LigasMouseClicked
        if(evt.getClickCount()==1){
            jButton_Eliminar.setEnabled(true);
        }
    }//GEN-LAST:event_jTable_LigasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Crear;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JLabel jLabel_Ligas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Ligas;
    // End of variables declaration//GEN-END:variables
}
