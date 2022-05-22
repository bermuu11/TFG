/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.InfoModificar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import misqlhsqldb.MiSQLhSQLDB;

/**
 *
 * @author Antonio
 */
public class InfoModificar_Jugador extends javax.swing.JDialog {

    MiSQLhSQLDB bbdd = new MiSQLhSQLDB("SA", "SA");
    
    boolean modificar = false;
    int id = -1;
    ArrayList datos = null;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");

    private String nombre = "";
    private String apellido1 = "";
    private String apellido2 = "";
    private int dorsal = -1;
    private String fechaNacimiento;
    private String posicion = "";
    private String pais = "";
    
    public InfoModificar_Jugador(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public InfoModificar_Jugador(java.awt.Frame parent, boolean modal, int id) {
        super(parent, modal);
        initComponents();
        this.id = id;
        modificar();
        cargarDatos();
        jButton_Cancelar.setVisible(false);
    }
    
    public void modificar(){
        if(modificar == false){
            jTextField_Nombre.setEnabled(false);
            jTextField_Dorsal.setEnabled(false);
            jTextField_Posicion.setEnabled(false);
            jTextField_Pais.setEnabled(false);
            jTextField_Apellido1.setEnabled(false);
            jTextField_Apellido2.setEnabled(false);
            jTextField_FechaNacimiento.setEnabled(false);
            jButton_Modificar.setText("MODIFICAR");
        } else {
            jTextField_Nombre.setEnabled(true);
            jTextField_Dorsal.setEnabled(true);
            jTextField_Posicion.setEnabled(true);
            jTextField_Pais.setEnabled(true);
            jTextField_Apellido1.setEnabled(true);
            jTextField_Apellido2.setEnabled(true);
            jTextField_FechaNacimiento.setEnabled(true);
            jButton_Modificar.setText("GUARDAR");
            jButton_Cancelar.setVisible(true);
        }
    }
    
    public void cargarDatos(){
        datos = bbdd.ConsultaSQL("SELECT nombre, apellido1, apellido2, dorsal, fechaNacimiento, posicion, pais\n" +
        "FROM jugador WHERE idJugador = '"+id +"'");
        
        String[] registro = (String[]) datos.get(0);
        jTextField_Nombre.setText(registro[0]);
        jTextField_Apellido1.setText(registro[1]);
        jTextField_Apellido2.setText(registro[2]);
        jTextField_Dorsal.setText(registro[3]);
        try {
            Date fecha = formatoBD.parse(registro[4]);
            registro[4] = formato.format(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(InfoModificar_Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextField_FechaNacimiento.setText(registro[4]);
        jTextField_Posicion.setText(registro[5]);
        jTextField_Pais.setText(registro[6]);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_Modificar = new javax.swing.JButton();
        jLabel_Nombre = new javax.swing.JLabel();
        jTextField_Nombre = new javax.swing.JTextField();
        jTextField_Apellido1 = new javax.swing.JTextField();
        jTextField_Apellido2 = new javax.swing.JTextField();
        jTextField_Dorsal = new javax.swing.JTextField();
        jLabel_Apellido1 = new javax.swing.JLabel();
        jLabel_Apellido2 = new javax.swing.JLabel();
        jLabel_Dorsal = new javax.swing.JLabel();
        jTextField_FechaNacimiento = new javax.swing.JTextField();
        jLabel_FechaNacimiento = new javax.swing.JLabel();
        jTextField_Posicion = new javax.swing.JTextField();
        jLabel_Posicion = new javax.swing.JLabel();
        jTextField_Pais = new javax.swing.JTextField();
        jLabel_Pais = new javax.swing.JLabel();
        jButton_Cerrar = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton_Modificar.setText("MODIFICAR");
        jButton_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarActionPerformed(evt);
            }
        });

        jLabel_Nombre.setText("Nombre");

        jLabel_Apellido1.setText("Primer Apellido");

        jLabel_Apellido2.setText("Segundo apellido");

        jLabel_Dorsal.setText("Dorsal");

        jLabel_FechaNacimiento.setText("Fecha de nacimiento");

        jLabel_Posicion.setText("Posición");

        jLabel_Pais.setText("País");

        jButton_Cerrar.setText("CERRAR");
        jButton_Cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CerrarActionPerformed(evt);
            }
        });

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
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton_Cerrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Modificar)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_Dorsal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Dorsal)
                            .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Nombre))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Apellido1)
                            .addComponent(jTextField_Apellido1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_Posicion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Posicion))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Pais)
                            .addComponent(jTextField_Pais, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_Apellido2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_Apellido2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_FechaNacimiento)
                                    .addComponent(jTextField_FechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(179, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nombre)
                    .addComponent(jLabel_Apellido1)
                    .addComponent(jLabel_Apellido2)
                    .addComponent(jLabel_FechaNacimiento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Apellido1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Apellido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_FechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Dorsal)
                    .addComponent(jLabel_Posicion)
                    .addComponent(jLabel_Pais))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Dorsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Posicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Modificar)
                    .addComponent(jButton_Cerrar)
                    .addComponent(jButton_Cancelar))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CerrarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CerrarActionPerformed

    private void jButton_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarActionPerformed
        if(modificar == false){
            modificar = true;
            modificar();
        } else {
            modificar = false;
            modificar();
            nombre = jTextField_Nombre.getText();
            apellido1 = jTextField_Apellido1.getText();
            apellido2 = jTextField_Apellido2.getText();
            dorsal = Integer.parseInt(jTextField_Dorsal.getText());
            try {
                Date fecha = formato.parse(jTextField_FechaNacimiento.getText());
                fechaNacimiento = formatoBD.format(fecha);
            } catch (ParseException ex) {
                //Logger.getLogger(Alta_Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }
            posicion = jTextField_Posicion.getText();
            pais = jTextField_Pais.getText();
            datos = bbdd.ConsultaSQL("UPDATE jugador SET nombre = '" +nombre +"', apellido1 = '" +apellido1 +"', apellido2 = '" +apellido2 +"', dorsal = " +dorsal +", fechaNacimiento = '" +fechaNacimiento +"', posicion = '" +posicion +"', pais = '" +pais +"' WHERE idJugador = '" +id +"'");
        }
    }//GEN-LAST:event_jButton_ModificarActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        modificar = false;
        modificar();
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
            java.util.logging.Logger.getLogger(InfoModificar_Jugador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Jugador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Jugador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfoModificar_Jugador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InfoModificar_Jugador dialog = new InfoModificar_Jugador(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Cerrar;
    private javax.swing.JButton jButton_Modificar;
    private javax.swing.JLabel jLabel_Apellido1;
    private javax.swing.JLabel jLabel_Apellido2;
    private javax.swing.JLabel jLabel_Dorsal;
    private javax.swing.JLabel jLabel_FechaNacimiento;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Pais;
    private javax.swing.JLabel jLabel_Posicion;
    private javax.swing.JTextField jTextField_Apellido1;
    private javax.swing.JTextField jTextField_Apellido2;
    private javax.swing.JTextField jTextField_Dorsal;
    private javax.swing.JTextField jTextField_FechaNacimiento;
    private javax.swing.JTextField jTextField_Nombre;
    private javax.swing.JTextField jTextField_Pais;
    private javax.swing.JTextField jTextField_Posicion;
    // End of variables declaration//GEN-END:variables
}
