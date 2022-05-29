/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import tfg.Alta.Alta_Jornada;
import tfg.InfoModificar.Modificar_Jornada;

/**
 *
 * @author abc
 */
public class Principal extends javax.swing.JPanel {
    
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
    
    private class miTablaModel extends DefaultTableModel{
        public boolean isCellEditable (int row, int column){
            return false;
        }
    }
    
    public Principal() {
        initComponents();
        estiloJLabel();
        cargarTablaClasificacion();
        cargarJornadas();
        cargarTablaResultados();
        cargarTemporadas();
        cargarCompeticiones();
    }
    
    public void estiloJLabel(){
        jLabel_Clasificacion.setFont(new Font("Verdana", 0, 50));
        jLabel_Clasificacion.setOpaque(true);
        jLabel_Clasificacion.setBackground(Color.yellow);
    }
    
    public void cargarTablaClasificacion(){
        String[] registro = null;
        
        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Posición");
        dtm.addColumn("Equipo");
        dtm.addColumn("Partidos Jugados");
        dtm.addColumn("Partidos ganados");
        dtm.addColumn("Partidos empatados");
        dtm.addColumn("Partidos perdidos");
        dtm.addColumn("Goles a favor");
        dtm.addColumn("Goles en contra");
        dtm.addColumn("Puntos");
        
        datosClasificacion = BaseDeDatos.getBD().ConsultaSQL("SELECT C.posicion, E.nombre, C.partidosJugados, C.partidosGanados, C.partidosEmpatados, C.partidosPerdidos, C.golesAfavor, C.golesEnContra, C.puntos FROM clasifica C, equipo E WHERE (C.idCompeticion = " +idCompeticion +") AND (C.idEquipo = E.idEquipo) ORDER BY C.posicion");
        
        if(datosClasificacion != null){
            int n = datosClasificacion.size();
            for(int i = 0; i < n; i++){
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
        
        //Cambiar tamaño columna
        TableColumn tc = jTable_Clasificacion.getColumn("Posición");  
        tc.setPreferredWidth(20);
        tc = jTable_Clasificacion.getColumn("Puntos");
        tc.setPreferredWidth(20);
        tc = jTable_Clasificacion.getColumn("Equipo");
        tc.setPreferredWidth(200);
        
        //Centrar contenido de columnas
        //alinear encabezados
        ((DefaultTableCellRenderer) jTable_Clasificacion.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
        for(int i=0; i<jTable_Clasificacion.getColumnCount(); i++){
            //alinear campos
            alinear.setHorizontalAlignment(SwingConstants.CENTER);
            jTable_Clasificacion.getColumnModel().getColumn(i).setCellRenderer(alinear);
        }
    }
    
    public void cargarJornadas(){
        String[] registro = null;
        datosJornada = BaseDeDatos.getBD().ConsultaSQL("SELECT DISTINCT jornada FROM partido");
        DefaultComboBoxModel dbcm = new DefaultComboBoxModel();
        for(int i=0; i<datosJornada.size();i++){
            registro = (String[]) datosJornada.get(i);
            dbcm.addElement("Jornada " +registro[0]); 
        }
        jComboBox_Jornadas.setModel(dbcm);
        
        jButton_Anterior.setEnabled(false);
        if(jComboBox_Jornadas.getItemCount() < 2){
            jButton_Siguiente.setEnabled(false);
        }
    }
    
    public void cargarTablaResultados(){
        String[] registro = null;
        
        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Hora");
        dtm.addColumn("Fecha");
        dtm.addColumn("Equipo Local");
        dtm.addColumn("Resultado");
        dtm.addColumn("Equipo Visitante");
        dtm.addColumn("Estado");
        
        //filtrar por jornada
        if(jComboBox_Jornadas.getSelectedIndex() != -1){
            registro = (String[]) datosJornada.get(jComboBox_Jornadas.getSelectedIndex());
            jornadaActual = Integer.parseInt(registro[0]);

            datosPartidos = BaseDeDatos.getBD().ConsultaSQL("SELECT P.jornada, P.hora, P.fecha, E.nombre, P.golesLocal, P.golesVisitante, E1.nombre, P.estado FROM partido P, equipo E, equipo E1 WHERE (P.id_equipoLocal = E.idEquipo) AND (P.id_equipoVisitante = E1.idEquipo) AND (P.jornada = " +jornadaActual +") AND (P.idCompeticion = " +idCompeticion +")");

            if(datosPartidos != null){
                int n = datosPartidos.size();
                for(int i = 0; i < n; i++){
                    registro = (String[]) datosPartidos.get(i);
                    if(registro[4] == null)
                        registro[4] = "";
                    if(registro[5] == null)
                        registro[5] = "";
                    Object[] fila = new Object[]{
                        registro[1],
                        registro[2],
                        registro[3],
                        registro[4] + " - " + registro[5],
                        registro[6],
                        registro[7]
                    };
                    dtm.addRow(fila); 
                }
            }
        }
        
        //Establecer modelo
        jTable_Resultados.setModel(dtm);
        
        //Cambiar tamaño columna
        TableColumn tc = jTable_Resultados.getColumn("Resultado");  
        tc.setPreferredWidth(100);
        
        //Centrar contenido de columnas
        //alinear encabezados
        ((DefaultTableCellRenderer) jTable_Resultados.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
        for(int i=0; i<jTable_Resultados.getColumnCount(); i++){
            //alinear campos
            alinear.setHorizontalAlignment(SwingConstants.CENTER);
            jTable_Resultados.getColumnModel().getColumn(i).setCellRenderer(alinear);
        }
    }
    
    public void cargarTemporadas(){
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String[] registro = null;
        datosTemporada = BaseDeDatos.getBD().ConsultaSQL("SELECT idTemporada, anio FROM temporada");
        
        if(datosTemporada != null){
            int n = datosTemporada.size();
            for(int i=0; i<n; i++){
                registro = (String[]) datosTemporada.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Temporadas.setModel(dcbm);
    }
    
    public void cargarCompeticiones(){
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String[] registro = null;
        datosCompeticion = BaseDeDatos.getBD().ConsultaSQL("SELECT idCompeticion, nombre FROM competicion WHERE idTemporada = " +idTemporada);
        
        if(datosCompeticion != null){
            int n = datosCompeticion.size();
            for(int i=0; i<n; i++){
                registro = (String[]) datosCompeticion.get(i);
                dcbm.addElement(registro[1]);
            }
        }
        jComboBox_Competiciones.setModel(dcbm);
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
        jComboBox_Temporadas = new javax.swing.JComboBox<>();
        jLabel_Temporadas = new javax.swing.JLabel();
        jComboBox_Competiciones = new javax.swing.JComboBox<>();
        jLabel_Competiciones = new javax.swing.JLabel();
        jButton_Estadisticas = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton_Anterior = new javax.swing.JButton();
        jComboBox_Jornadas = new javax.swing.JComboBox<>();
        jButton_Siguiente = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Resultados = new javax.swing.JTable();
        jButton_ModificarJornada = new javax.swing.JButton();
        jButton_AnadirJornada = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel_Clasificacion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Clasificacion = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 51, 51));
        setMinimumSize(new java.awt.Dimension(20, 20));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 60));

        jComboBox_Temporadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Temporadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TemporadasActionPerformed(evt);
            }
        });

        jLabel_Temporadas.setText("Temporadas");

        jComboBox_Competiciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Competiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_CompeticionesActionPerformed(evt);
            }
        });

        jLabel_Competiciones.setText("Competiciones");

        jButton_Estadisticas.setText("ESTADÍSTICAS");
        jButton_Estadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EstadisticasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox_Temporadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Temporadas))
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_Competiciones)
                        .addContainerGap(142, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox_Competiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Estadisticas)
                        .addGap(27, 27, 27))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Temporadas)
                    .addComponent(jLabel_Competiciones))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_Temporadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_Competiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Estadisticas))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel4.setMinimumSize(new java.awt.Dimension(0, 60));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jButton_Anterior.setText("Anterior");
        jButton_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnteriorActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Anterior);

        jComboBox_Jornadas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Jornadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_JornadasActionPerformed(evt);
            }
        });
        jPanel4.add(jComboBox_Jornadas);

        jButton_Siguiente.setText("Siguiente");
        jButton_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SiguienteActionPerformed(evt);
            }
        });
        jPanel4.add(jButton_Siguiente);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel2.add(jPanel4, gridBagConstraints);

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
        jScrollPane2.setViewportView(jTable_Resultados);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jButton_ModificarJornada.setText("MODIFICAR JORNADA");
        jButton_ModificarJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarJornadaActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_ModificarJornada, new java.awt.GridBagConstraints());

        jButton_AnadirJornada.setText("AÑADIR JORNADA");
        jButton_AnadirJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirJornadaActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_AnadirJornada, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.weighty = 100.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        add(jPanel2, gridBagConstraints);

        jLabel_Clasificacion.setBackground(new java.awt.Color(255, 255, 51));
        jLabel_Clasificacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Clasificacion.setText("Clasificación");
        jLabel_Clasificacion.setMaximumSize(new java.awt.Dimension(2300, 300));

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
        jScrollPane1.setViewportView(jTable_Clasificacion);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Clasificacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel_Clasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 80.0;
        gridBagConstraints.weighty = 100.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 8);
        add(jPanel3, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnteriorActionPerformed
        if(jComboBox_Jornadas.getSelectedIndex() > 0){
            jComboBox_Jornadas.setSelectedIndex(jComboBox_Jornadas.getSelectedIndex() - 1);
            jButton_Siguiente.setEnabled(true);
            if(jComboBox_Jornadas.getSelectedIndex() == 0){
                jButton_Anterior.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jButton_AnteriorActionPerformed

    private void jButton_AnadirJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirJornadaActionPerformed
        Alta_Jornada ventana =  new Alta_Jornada((JFrame) this.getRootPane().getParent(), true, idCompeticion);
        ventana.setTitle("Nueva jornada");
        ventana.setSize(new Dimension(900, 700));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        //Se actualiza la clasificación después de añadir una jornada
        cargarTablaClasificacion();
    }//GEN-LAST:event_jButton_AnadirJornadaActionPerformed

    private void jComboBox_JornadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_JornadasActionPerformed
        cargarTablaResultados();
    }//GEN-LAST:event_jComboBox_JornadasActionPerformed

    private void jButton_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SiguienteActionPerformed
        if(jComboBox_Jornadas.getSelectedIndex() < jComboBox_Jornadas.getItemCount() - 1){
            jComboBox_Jornadas.setSelectedIndex(jComboBox_Jornadas.getSelectedIndex() + 1);
            jButton_Anterior.setEnabled(true);
            if(jComboBox_Jornadas.getSelectedIndex() == jComboBox_Jornadas.getItemCount() - 1){
                jButton_Siguiente.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jButton_SiguienteActionPerformed

    private void jComboBox_TemporadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TemporadasActionPerformed
        String[] registro = null;
        registro = (String[]) datosTemporada.get(jComboBox_Temporadas.getSelectedIndex());
        idTemporada = Integer.parseInt(registro[0]);
        cargarCompeticiones();
        jComboBox_CompeticionesActionPerformed(evt); //se fuerza a que se carguen los resultados
    }//GEN-LAST:event_jComboBox_TemporadasActionPerformed

    private void jComboBox_CompeticionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_CompeticionesActionPerformed
        String[] registro = null;
        registro = (String[]) datosCompeticion.get(jComboBox_Competiciones.getSelectedIndex());
        idCompeticion = Integer.parseInt(registro[0]);
        cargarTablaResultados();
    }//GEN-LAST:event_jComboBox_CompeticionesActionPerformed

    private void jButton_ModificarJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarJornadaActionPerformed
        Modificar_Jornada ventana =  new Modificar_Jornada((JFrame) this.getRootPane().getParent(), true, idCompeticion, jornadaActual);
        ventana.setTitle("Modificar jornada");
        ventana.setSize(new Dimension(900, 700));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
        //Se actualizan los resultados y la clasificación después de modificar una jornada
        cargarTablaResultados();
        cargarTablaClasificacion();
    }//GEN-LAST:event_jButton_ModificarJornadaActionPerformed

    private void jButton_EstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EstadisticasActionPerformed
        Estadisticas ventana =  new Estadisticas((JFrame) this.getRootPane().getParent(), true, idCompeticion);
        ventana.setTitle("Estadísticas");
        ventana.setSize(new Dimension(1000, 700));
        ventana.setLocationRelativeTo(null);
        ventana.setModal(true);
        ventana.setVisible(true);
    }//GEN-LAST:event_jButton_EstadisticasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AnadirJornada;
    private javax.swing.JButton jButton_Anterior;
    private javax.swing.JButton jButton_Estadisticas;
    private javax.swing.JButton jButton_ModificarJornada;
    private javax.swing.JButton jButton_Siguiente;
    private javax.swing.JComboBox<String> jComboBox_Competiciones;
    private javax.swing.JComboBox<String> jComboBox_Jornadas;
    private javax.swing.JComboBox<String> jComboBox_Temporadas;
    private javax.swing.JLabel jLabel_Clasificacion;
    private javax.swing.JLabel jLabel_Competiciones;
    private javax.swing.JLabel jLabel_Temporadas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Clasificacion;
    private javax.swing.JTable jTable_Resultados;
    // End of variables declaration//GEN-END:variables
}
