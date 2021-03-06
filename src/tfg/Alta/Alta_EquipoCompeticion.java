/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.Alta;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import tfg.BaseDeDatos;

/**
 *
 * @author Antonio
 */
public class Alta_EquipoCompeticion extends javax.swing.JDialog {

    int idCompeticion = -1;
    int idEquipo = -1;

    ArrayList datos = null;

    public Alta_EquipoCompeticion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public Alta_EquipoCompeticion(java.awt.Frame parent, boolean modal, int idCompeticion) {
        super(parent, modal);
        initComponents();
        getContentPane().setBackground(new Color(214, 246, 231));
        this.idCompeticion = idCompeticion;
        this.idEquipo = idEquipo;
        cargarEquipos();
    }

    public void cargarEquipos() {
        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>();
        String[] registro = null;
        datos = BaseDeDatos.getBD().ConsultaSQL("SELECT idEquipo, nombre FROM equipo");

        if (datos != null) {
            int n = datos.size();
            for (int i = 0; i < n; i++) {
                registro = (String[]) datos.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Equipos.setModel(dcbm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox_Equipos = new javax.swing.JComboBox<>();
        jLabel_Equipos = new javax.swing.JLabel();
        jButton_Anadir = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox_Equipos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Equipos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel_Equipos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel_Equipos.setText("Equipos");

        jButton_Anadir.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Anadir.setText("A??ADIR");
        jButton_Anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirActionPerformed(evt);
            }
        });

        jButton_Cancelar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Cancelar.setText("CANCELAR");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(19, Short.MAX_VALUE)
                        .addComponent(jButton_Cancelar)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Anadir))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox_Equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Equipos))
                        .addGap(24, 24, 24)))
                .addContainerGap(267, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel_Equipos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_Equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Anadir)
                    .addComponent(jButton_Cancelar))
                .addContainerGap(156, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_AnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirActionPerformed
        String[] registro = (String[]) datos.get(jComboBox_Equipos.getSelectedIndex());
        int idEquipo = Integer.parseInt(registro[0]);

        //Comprueba si el equipo ya est?? en la competici??n antes de a??adirlo
        ArrayList consulta = BaseDeDatos.getBD().ConsultaSQL("SELECT idEquipo FROM compite WHERE (idCompeticion=" + idCompeticion + ") AND (idEquipo=" + idEquipo + ")");
        if (consulta.isEmpty()) {
            BaseDeDatos.getBD().ConsultaSQL("INSERT INTO compite VALUES (" + idCompeticion + ", " + idEquipo + ");");
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "El equipo " + jComboBox_Equipos.getSelectedItem() + " ya est?? en la competici??n.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton_AnadirActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CancelarActionPerformed

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
            java.util.logging.Logger.getLogger(Alta_EquipoCompeticion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alta_EquipoCompeticion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alta_EquipoCompeticion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alta_EquipoCompeticion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Alta_EquipoCompeticion dialog = new Alta_EquipoCompeticion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_Anadir;
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JComboBox<String> jComboBox_Equipos;
    private javax.swing.JLabel jLabel_Equipos;
    // End of variables declaration//GEN-END:variables
}
