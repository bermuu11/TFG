/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package tfg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author abc
 */
public class Principal extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    private class miTablaModel extends DefaultTableModel{
        public boolean isCellEditable (int row, int column){
            return false;
        }
    }
    
    public Principal() {
        initComponents();
        estiloJLabel();
        cargarTablaClasificacion();
        cargarTablaResultados();
    }
    
    public void estiloJLabel(){
        jLabel_Clasificacion.setFont(new Font("Verdana", 0, 50));
        jLabel_Clasificacion.setOpaque(true);
        jLabel_Clasificacion.setBackground(Color.yellow);
    }
    
    public void cargarTablaClasificacion(){
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
        Object[] fila = new Object[]{
                    1,
                    "Real Madrid",
                    34,
                    30,
                    2,
                    2,
                    73,
                    21,
                    92,
                };
                dtm.addRow(fila);
        
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
    
    public void cargarTablaResultados(){
        miTablaModel dtm = new miTablaModel();
        dtm.addColumn("Equipo Local");
        dtm.addColumn("Resultado");
        dtm.addColumn("Equipo Visitante");
        Object[] fila = new Object[]{
                    "Real Madrid",
                    "2-1",
                    "Granada",
                };
                dtm.addRow(fila);
        
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
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton_Anterior = new javax.swing.JButton();
        jComboBox_Jornadas = new javax.swing.JComboBox<>();
        jButton_Siguiente = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Resultados = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel_Clasificacion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Clasificacion = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 51, 51));
        setMinimumSize(new java.awt.Dimension(20, 20));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 60));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
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
        jPanel4.add(jComboBox_Jornadas);

        jButton_Siguiente.setText("Siguiente");
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel_Clasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(230, Short.MAX_VALUE))
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
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_AnteriorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Anterior;
    private javax.swing.JButton jButton_Siguiente;
    private javax.swing.JComboBox<String> jComboBox_Jornadas;
    private javax.swing.JLabel jLabel_Clasificacion;
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
