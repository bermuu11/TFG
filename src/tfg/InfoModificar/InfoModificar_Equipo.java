/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.InfoModificar;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import misqlhsqldb.MiSQLhSQLDB;
import tfg.InfoModificar.InfoModificar_Jugador;
import tfg.Alta.Alta_Jugador;
import tfg.Alta.Alta_JugadorEquipo;
import tfg.Equipos;
import static tfg.BaseDeDatos.conectarBD;

/**
 *
 * @author Antonio
 */
public class InfoModificar_Equipo extends javax.swing.JDialog {
    private class miTablaModel extends DefaultTableModel{
        public boolean isCellEditable (int row, int column){
            return false;
        }
    }
    
    MiSQLhSQLDB bbdd = new MiSQLhSQLDB("SA", "SA");
    
    boolean modificar = false;
    int idEquipo;
    int idCompeticion;
    int idJugador;
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
        this.idEquipo = idEquipo;
        modificar();
        cargarDatos();
        cargarCompeticiones();
        cargarTabla(idCompeticion, idEquipo);
        if(idCompeticion != -1){
            jComboBox_Competicion.setSelectedIndex(jComboBox_Competicion.getItemCount() - 1);
            jButton_Cancelar.setVisible(false);
        } else {
            jButton_AnadirJugador.setEnabled(false);
        }
        jButton_EliminarJugador.setEnabled(false);
    }
    
    public void modificar(){
        if(modificar == false){
            jTextField_Nombre.setEnabled(false);
            jTextField_Estadio.setEnabled(false);
            jTextField_Ciudad.setEnabled(false);
            jTextField_AnoFundacion.setEnabled(false);
            jTextField_Entrenador.setEnabled(false);
            jTextField_Presidente.setEnabled(false);
            jButton_Cancelar.setVisible(false);
            jButton_Modificar.setText("MODIFICAR");
        } else {
            jTextField_Nombre.setEnabled(true);
            jTextField_Estadio.setEnabled(true);
            jTextField_Ciudad.setEnabled(true);
            jTextField_AnoFundacion.setEnabled(true);
            jTextField_Entrenador.setEnabled(true);
            jTextField_Presidente.setEnabled(true);
            jButton_Modificar.setText("GUARDAR");
            jButton_Cancelar.setVisible(true);
        }
    }
    
    public void cargarDatos(){
        datos = bbdd.ConsultaSQL("SELECT nombre, estadio, anioFundacion, ciudad, entrenador, presidente\n" +
        "FROM equipo WHERE idEquipo = '"+idEquipo +"'");
        
        String[] registro = (String[]) datos.get(0);
        jTextField_Nombre.setText(registro[0]);
        jTextField_Estadio.setText(registro[1]);
        jTextField_AnoFundacion.setText(registro[2]);
        jTextField_Ciudad.setText(registro[3]);
        jTextField_Entrenador.setText(registro[4]);
        jTextField_Presidente.setText(registro[5]);
    }
    
    public void cargarCompeticiones(){
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String[] registro = null;
        datosCompeticion = bbdd.ConsultaSQL("SELECT idCompeticion, nombre FROM competicion C, compite CE WHERE (CE.idCompeticion = C.idCompeticion) AND (CE.idEquipo = " +idEquipo +");");
        
        if(datosCompeticion != null){
            int n = datosCompeticion.size();
            for(int i=0; i<n; i++){
                registro = (String[]) datosCompeticion.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Competicion.setModel(dcbm);
        if(datosCompeticion.size() > 0){
            idCompeticion = Integer.parseInt(registro[0]); //identificador de la ultima competicion leida
        } else {
            idCompeticion = -1;
        }
    }
    
    public void cargarTabla(int idCompeticion, int idEquipo){
        String[] registro = null;
        
        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Nombre");
        dtm.addColumn("Apellidos");
        dtm.addColumn("Fecha de nacimiento");
        dtm.addColumn("Posición");
        dtm.addColumn("País");
        dtm.addColumn("Fecha traspaso");
        
        if(idCompeticion != -1){
            datosJugador = bbdd.ConsultaSQL("SELECT J.idJugador, J.nombre, J.apellido1, J.apellido2, J.fechaNacimiento, J.posicion, J.pais, P.fechaTraspaso FROM jugador J, pertenece P WHERE (P.idJugador = J.idJugador) AND (P.idEquipo = " +idEquipo +") AND (P.idCompeticion = " +idCompeticion +");");

            if(datosJugador != null){
                int n = datosJugador.size();
                for(int i = 0; i < n; i++){
                    registro = (String[]) datosJugador.get(i);
                    Object[] fila = new Object[]{
                        registro[1],
                        registro[2] +" " +registro[3],
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField_Nombre = new javax.swing.JTextField();
        jTextField_Estadio = new javax.swing.JTextField();
        jTextField_Ciudad = new javax.swing.JTextField();
        jLabel_Nombre = new javax.swing.JLabel();
        jTextField_Entrenador = new javax.swing.JTextField();
        jTextField_Presidente = new javax.swing.JTextField();
        jTextField_AnoFundacion = new javax.swing.JTextField();
        jLabel_Estadio = new javax.swing.JLabel();
        jLabel_Ciudad = new javax.swing.JLabel();
        jLabel_Entrenador = new javax.swing.JLabel();
        jLabel_Presidente = new javax.swing.JLabel();
        jLabel_AnoFundacion = new javax.swing.JLabel();
        jButton_Modificar = new javax.swing.JButton();
        jButton_Cerrar = new javax.swing.JButton();
        jLabel_Jugadores = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Jugadores = new javax.swing.JTable();
        jButton_AnadirJugador = new javax.swing.JButton();
        jButton_EliminarJugador = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();
        jComboBox_Competicion = new javax.swing.JComboBox<>();
        jLabel_Competicion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel_Nombre.setText("Nombre");

        jLabel_Estadio.setText("Estadio");

        jLabel_Ciudad.setText("Ciudad");

        jLabel_Entrenador.setText("Entrenador");

        jLabel_Presidente.setText("Presidente");

        jLabel_AnoFundacion.setText("Año fundación");

        jButton_Modificar.setText("MODIFICAR");
        jButton_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarActionPerformed(evt);
            }
        });

        jButton_Cerrar.setText("CERRAR");
        jButton_Cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CerrarActionPerformed(evt);
            }
        });

        jLabel_Jugadores.setText("Jugadores");

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
        jTable_Jugadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_JugadoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_Jugadores);

        jButton_AnadirJugador.setText("AÑADIR JUGADOR");
        jButton_AnadirJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirJugadorActionPerformed(evt);
            }
        });

        jButton_EliminarJugador.setText("ELIMINAR JUGADOR");
        jButton_EliminarJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarJugadorActionPerformed(evt);
            }
        });

        jButton_Cancelar.setText("CANCELAR");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });

        jComboBox_Competicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Competicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_CompeticionActionPerformed(evt);
            }
        });

        jLabel_Competicion.setText("Competicion");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jButton_Cerrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_Cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Modificar)
                .addGap(25, 25, 25))
            .addGroup(layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Nombre)
                            .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Estadio)
                            .addComponent(jTextField_Estadio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Ciudad)
                            .addComponent(jTextField_Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Entrenador)
                            .addComponent(jTextField_Entrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Presidente)
                            .addComponent(jTextField_Presidente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Competicion)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox_Competicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton_EliminarJugador)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_AnoFundacion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_AnoFundacion)
                    .addComponent(jButton_AnadirJugador))
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_Jugadores)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nombre)
                    .addComponent(jLabel_Estadio)
                    .addComponent(jLabel_Ciudad)
                    .addComponent(jLabel_Entrenador)
                    .addComponent(jLabel_Presidente)
                    .addComponent(jLabel_AnoFundacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Estadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Entrenador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_Presidente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_AnoFundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Jugadores)
                    .addComponent(jLabel_Competicion))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_AnadirJugador)
                    .addComponent(jButton_EliminarJugador)
                    .addComponent(jComboBox_Competicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Modificar)
                    .addComponent(jButton_Cerrar)
                    .addComponent(jButton_Cancelar))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarActionPerformed
        if(modificar == false){
            modificar = true;
            modificar();
        } else {
            modificar = false;
            modificar();
            datos = bbdd.ConsultaSQL("UPDATE equipo SET nombre = '" +jTextField_Nombre.getText() +"', estadio = '" +jTextField_Estadio.getText() +"', ciudad = '" +jTextField_Ciudad.getText() +"', anioFundacion = '" +jTextField_AnoFundacion.getText() +"', entrenador = '" +jTextField_Entrenador.getText() +"', presidente = '" +jTextField_Presidente.getText() +"' WHERE idEquipo = '" +idEquipo +"'");
            cargarDatos();
        }
    }//GEN-LAST:event_jButton_ModificarActionPerformed

    private void jButton_CerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CerrarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CerrarActionPerformed

    private void jTable_JugadoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_JugadoresMouseClicked
        if(evt.getClickCount()==1){
            jButton_EliminarJugador.setEnabled(true);
        }
        if(evt.getClickCount()==2){
            InfoModificar_Jugador ventana =  new InfoModificar_Jugador((JFrame) this.getRootPane().getParent(), true);
            ventana.setTitle("Información jugador");
            ventana.setSize(new Dimension(700, 500));
            ventana.setLocationRelativeTo(null);
            ventana.setModal(true);
            ventana.setVisible(true);
        }
    }//GEN-LAST:event_jTable_JugadoresMouseClicked

    private void jButton_AnadirJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirJugadorActionPerformed
        String[] registro = (String[]) datosCompeticion.get(jComboBox_Competicion.getSelectedIndex());
        idCompeticion = Integer.parseInt(registro[0]);
        
        Alta_JugadorEquipo ventana =  new Alta_JugadorEquipo((JFrame) this.getRootPane().getParent().getParent(), true, idCompeticion, idEquipo);
        ventana.setTitle("Añadir jugador");
        ventana.setSize(new Dimension(500, 300));
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
        String[] registro = (String[]) datosJugador.get(jTable_Jugadores.getSelectedRow());
        idJugador = Integer.parseInt(registro[0]);
        bbdd.ConsultaSQL("DELETE FROM pertenece WHERE (idJugador = '" +idJugador +"') AND (idCompeticion = " +idCompeticion +") AND (idEquipo = " +idEquipo +");");
        cargarTabla(idCompeticion, idEquipo);
    }//GEN-LAST:event_jButton_EliminarJugadorActionPerformed

    private void jComboBox_CompeticionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_CompeticionActionPerformed
        String[] registro = (String[]) datosCompeticion.get(jComboBox_Competicion.getSelectedIndex());
        idCompeticion = Integer.parseInt(registro[0]);
        cargarTabla(idCompeticion, idEquipo);
    }//GEN-LAST:event_jComboBox_CompeticionActionPerformed

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
