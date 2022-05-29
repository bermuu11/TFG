/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg;

import tfg.BaseDeDatos;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */
public class Estadisticas extends javax.swing.JDialog {

    int idCompeticion;
    ArrayList estadisticas = null;
    
    public Estadisticas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public Estadisticas(java.awt.Frame parent, boolean modal, int idCompeticion){
        super(parent, modal);
        initComponents();
        this.idCompeticion = idCompeticion;
        estiloJLabel();
        cargarEstadisticas();
    }
    
    public void estiloJLabel(){
        jLabel_Estadisticas.setFont(new Font("Verdana", 0, 20));
        jLabel_Estadisticas.setOpaque(true);
        jLabel_Estadisticas.setBackground(Color.yellow);
    }
    
    private void ejecutarConsultaEquipoMasMenosGoles(String tipo, String orden){
        ///////Ajustar para cuando hay varios registros
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E.nombre, " +tipo +"(C.golesAfavor) AS maximo FROM clasifica C, equipo E "
                + "WHERE (C.idEquipo = E.idEquipo) AND (C.idCompeticion = " +idCompeticion +") GROUP BY E.nombre ORDER BY maximo " +orden +" LIMIT 1");
    }
    
    private void ejecutarConsultaEquipoMasMenosGolesLocalVisitante(String localVisitante, String orden){
        ///////Ajustar para cuando hay varios registros
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT E.nombre, SUM(P.goles" +localVisitante +") AS total FROM partido P, equipo E "
                + "WHERE (P.id_equipo" +localVisitante +" = E.idEquipo) AND (P.idCompeticion = " +idCompeticion +") GROUP BY E.nombre ORDER BY total " +orden +" LIMIT 1");
    }
    
    private void ejecutarConsultaTotalGolesLocalVisitante(String localVisitante){
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT SUM(goles" +localVisitante +") AS total FROM partido "
                + "WHERE (idCompeticion = " +idCompeticion +")");
    }
    
    private void ejecutarConsultaTotalVictoriasEmpates(String victoriasEmpates){
        estadisticas = BaseDeDatos.getBD().ConsultaSQL("SELECT " +victoriasEmpates +" AS total FROM clasifica "
                + "WHERE (idCompeticion = " +idCompeticion +")");
    }
    
    public void cargarEstadisticas(){
        String[] registro = null;
        //Obtener equipo más goleador
        ejecutarConsultaEquipoMasMenosGoles("MAX", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleador.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener equipo menos goleador
        ejecutarConsultaEquipoMasMenosGoles("MIN", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleador.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener equipo que más goles anota como local
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Local", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleadorLocal.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener equipo que menos goles anota como local
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Local", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleadorLocal.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener equipo que más goles anota como visitante
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Visitante", "DESC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMasGoleadorVisitante.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener equipo que menos goles anota como visitante
        ejecutarConsultaEquipoMasMenosGolesLocalVisitante("Visitante", "ASC");
        registro = (String[]) estadisticas.get(0);
        jLabel_EquipoMenosGoleadorVisitante.setText(registro[0] +": " +registro[1] +" goles a favor.");
        
        //Obtener total de goles locales
        ejecutarConsultaTotalGolesLocalVisitante("Local");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalGolesLocal.setText(registro[0] +" goles.");
        
        //Obtener total de goles visitantes
        ejecutarConsultaTotalGolesLocalVisitante("Visitante");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalGolesVisitante.setText(registro[0] +" goles.");
        
        //Obtener total de victorias
        ejecutarConsultaTotalVictoriasEmpates("SUM(partidosGanados)");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalVictorias.setText(registro[0] +" victorias.");
        
        //Obtener total de empates
        ejecutarConsultaTotalVictoriasEmpates("SUM(partidosEmpatados) / 2");
        registro = (String[]) estadisticas.get(0);
        jLabel_TotalEmpates.setText(registro[0] +" empates.");
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

        jLabel_Estadisticas = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleador = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleador = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorLocal = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorLocalTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorLocal = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMasGoleadorVisitante = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorVisitanteTitulo = new javax.swing.JLabel();
        jLabel_EquipoMenosGoleadorVisitante = new javax.swing.JLabel();
        jLabel_TotalGolesLocalTitulo = new javax.swing.JLabel();
        jLabel_TotalGolesVisitanteTitulo = new javax.swing.JLabel();
        jLabel_TotalGolesLocal = new javax.swing.JLabel();
        jLabel_TotalGolesVisitante = new javax.swing.JLabel();
        jLabel_TotalVictoriasTitulo = new javax.swing.JLabel();
        jLabel_TotalEmpatesTitulo = new javax.swing.JLabel();
        jLabel_TotalVictorias = new javax.swing.JLabel();
        jLabel_TotalEmpates = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel_Estadisticas.setText("Estadísticas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_Estadisticas, gridBagConstraints);

        jLabel_EquipoMasGoleadorTitulo.setText("Equipo más goleador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleador.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleador, gridBagConstraints);

        jLabel_EquipoMenosGoleadorTitulo.setText("Equipo menos goleador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleador.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleador, gridBagConstraints);

        jLabel_EquipoMasGoleadorLocalTitulo.setText("Equipo más goleador como local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorLocalTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleadorLocal.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorLocal, gridBagConstraints);

        jLabel_EquipoMenosGoleadorLocalTitulo.setText("Equipo menos goleador como local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorLocalTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleadorLocal.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorLocal, gridBagConstraints);

        jLabel_EquipoMasGoleadorVisitanteTitulo.setText("Equipo más goleador como visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMasGoleadorVisitante.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMasGoleadorVisitante, gridBagConstraints);

        jLabel_EquipoMenosGoleadorVisitanteTitulo.setText("Equipo menos goleador como visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorVisitanteTitulo, gridBagConstraints);

        jLabel_EquipoMenosGoleadorVisitante.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoMenosGoleadorVisitante, gridBagConstraints);

        jLabel_TotalGolesLocalTitulo.setText("Total de goles local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesLocalTitulo, gridBagConstraints);

        jLabel_TotalGolesVisitanteTitulo.setText("Total de goles visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesVisitanteTitulo, gridBagConstraints);

        jLabel_TotalGolesLocal.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesLocal, gridBagConstraints);

        jLabel_TotalGolesVisitante.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalGolesVisitante, gridBagConstraints);

        jLabel_TotalVictoriasTitulo.setText("Total de victorias");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalVictoriasTitulo, gridBagConstraints);

        jLabel_TotalEmpatesTitulo.setText("Total de empates");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalEmpatesTitulo, gridBagConstraints);

        jLabel_TotalVictorias.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalVictorias, gridBagConstraints);

        jLabel_TotalEmpates.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_TotalEmpates, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Estadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Estadisticas dialog = new Estadisticas(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel_EquipoMasGoleador;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorLocal;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorTitulo;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorVisitante;
    private javax.swing.JLabel jLabel_EquipoMasGoleadorVisitanteTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleador;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorLocal;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorLocalTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorTitulo;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorVisitante;
    private javax.swing.JLabel jLabel_EquipoMenosGoleadorVisitanteTitulo;
    private javax.swing.JLabel jLabel_Estadisticas;
    private javax.swing.JLabel jLabel_TotalEmpates;
    private javax.swing.JLabel jLabel_TotalEmpatesTitulo;
    private javax.swing.JLabel jLabel_TotalGolesLocal;
    private javax.swing.JLabel jLabel_TotalGolesLocalTitulo;
    private javax.swing.JLabel jLabel_TotalGolesVisitante;
    private javax.swing.JLabel jLabel_TotalGolesVisitanteTitulo;
    private javax.swing.JLabel jLabel_TotalVictorias;
    private javax.swing.JLabel jLabel_TotalVictoriasTitulo;
    // End of variables declaration//GEN-END:variables
}
