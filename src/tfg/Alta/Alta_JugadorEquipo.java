/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.Alta;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import misqlhsqldb.MiSQLhSQLDB;

/**
 *
 * @author Antonio
 */
public class Alta_JugadorEquipo extends javax.swing.JDialog {

    MiSQLhSQLDB bbdd = new MiSQLhSQLDB("SA", "SA");
    
    int idCompeticion = -1;
    int idEquipo = -1;
    ArrayList datos = null;
    
    public Alta_JugadorEquipo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public Alta_JugadorEquipo(java.awt.Frame parent, boolean modal, int idCompeticion, int idEquipo){
        super(parent, modal);
        initComponents();
        this.idCompeticion = idCompeticion;
        this.idEquipo = idEquipo;
        cargarJugadores();
    }
    
    public void cargarJugadores(){
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String[] registro = null;
        datos = bbdd.ConsultaSQL("SELECT idJugador, nombre, apellido1, apellido2 FROM jugador");
        
        if(datos != null){
            int n = datos.size();
            for(int i=0; i<n; i++){
                registro = (String[]) datos.get(i);
                dcbm.addElement(registro[1] +" " +registro[2] +" " +registro[3]);
            }
        }
        jComboBox_Jugadores.setModel(dcbm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox_Jugadores = new javax.swing.JComboBox<>();
        jLabel_Jugadores = new javax.swing.JLabel();
        jButton_Anadir = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox_Jugadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel_Jugadores.setText("Jugadores");

        jButton_Anadir.setText("AÑADIR");
        jButton_Anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirActionPerformed(evt);
            }
        });

        jButton_Cancelar.setText("CANCELAR");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_Jugadores)
                    .addComponent(jComboBox_Jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(235, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_Cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Anadir)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel_Jugadores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_Jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Anadir)
                    .addComponent(jButton_Cancelar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_AnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirActionPerformed
        String[] registro = (String[]) datos.get(jComboBox_Jugadores.getSelectedIndex());
        int idJugador = Integer.parseInt(registro[0]);
        bbdd.ConsultaSQL("INSERT INTO pertenece VALUES ("+idJugador +", " +idEquipo +", " +idCompeticion +", '2022-07-01');");
    }//GEN-LAST:event_jButton_AnadirActionPerformed

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
            java.util.logging.Logger.getLogger(Alta_JugadorEquipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alta_JugadorEquipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alta_JugadorEquipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alta_JugadorEquipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Alta_JugadorEquipo dialog = new Alta_JugadorEquipo(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBox_Jugadores;
    private javax.swing.JLabel jLabel_Jugadores;
    // End of variables declaration//GEN-END:variables
}