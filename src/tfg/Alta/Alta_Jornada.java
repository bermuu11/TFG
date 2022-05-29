/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.Alta;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import tfg.BaseDeDatos;
import tfg.Clasificacion;
        
/**
 *
 * @author Antonio
 */
public class Alta_Jornada extends javax.swing.JDialog {
    
    private static final int MAX_PARTIDOS = 15;

    private static final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm");
    private static final SimpleDateFormat formatoHoraBD = new SimpleDateFormat("hh:mm:ss");
    
    private HashMap mapaComponentes;
    
    ArrayList equipos = null;
    int totalEquipos;
    int totalPartidos;
    
    int idCompeticion;
    
    public Alta_Jornada(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public Alta_Jornada(java.awt.Frame parent, boolean modal, int idCompeticion){
        super(parent, modal);
        initComponents();
        this.idCompeticion = idCompeticion;
        crearMapaComponentes();
        cargarEquipos();
        cargarEstados();
        ocultarPartidos();
    }
    
    //Crea una tabla hash en la que se asocian los componentes con cadenas que los identifican
    public void crearMapaComponentes() {
        mapaComponentes = new HashMap<String, Component>();
        Component[] componentes = getContentPane().getComponents();
        for (Component componente : componentes) {
            mapaComponentes.put(componente.getName(), componente);
        }
    }

    //Obtiene de la tabla hash un componente a partir de su identificador
    public Component getComponentePorNombre(String nombre) {
        if (mapaComponentes.containsKey(nombre)) {
            return (Component) mapaComponentes.get(nombre);
        }
        return null;
    }

    public void cargarEquipos() {
        equipos = BaseDeDatos.getBD().ConsultaSQL("SELECT E.idEquipo, E.nombre FROM compite C, equipo E WHERE (C.idCompeticion = " + idCompeticion + ") AND (C.idEquipo = E.idEquipo);");
        totalEquipos = equipos.size();
        totalPartidos = totalEquipos / 2; //el número de partidos es la mitad que el de equipos

        if (equipos != null) {
            //Crea una lista de todos los equipos
            ArrayList<String> listaEquipos = new ArrayList<>();
            for (int i = 0; i < totalEquipos; i++) {
                String[] registro = (String[]) equipos.get(i);
                listaEquipos.add(registro[1]);
            }

            //Rellena los combos del equipo local y visitante de los partidos
            for (int i = 0; i < totalPartidos; i++) {
                //Obtiene el componente asociado al equipo local del partido actual
                JComboBox combo = (JComboBox) getComponentePorNombre("EL" + i);
                //Asigna el modelo creado a partir de la lista de equipos
                if (combo != null) {
                    combo.setModel(new DefaultComboBoxModel(listaEquipos.toArray()));
                }
                //Obtiene el componente asociado al equipo visitante del partido actual
                combo = (JComboBox) getComponentePorNombre("EV" + i);
                //Asigna el modelo creado a partir de la lista de equipos
                if (combo != null) {
                    combo.setModel(new DefaultComboBoxModel(listaEquipos.toArray()));
                }
            }
        }
    }
    
    public void cargarEstados() {
        //Crea una lista con todos los estados
        ArrayList<String> listaEstados = new ArrayList<>();
        listaEstados.add("Sin comenzar");
        listaEstados.add("Finalizado");
        listaEstados.add("Suspendido");
        listaEstados.add("Aplazado");

        for (int i = 0; i < totalPartidos; i++) {
            //Obtiene el componente asociado al estado del partido actual
            JComboBox combo = (JComboBox) getComponentePorNombre("ES" + i);
            //Asigna el modelo creado a partir de la lista de estados
            if (combo != null) {
                combo.setModel(new DefaultComboBoxModel(listaEstados.toArray()));
            }
        }
    }
    
    public void ocultarPartidos() {
        //Oculta todos los componentes de los partidos que no son necesarios
        for (int i = totalPartidos; i < MAX_PARTIDOS; i++) {
            getComponentePorNombre("HO" + i).setVisible(false);
            getComponentePorNombre("FE" + i).setVisible(false);
            getComponentePorNombre("EL" + i).setVisible(false);
            getComponentePorNombre("EV" + i).setVisible(false);
            getComponentePorNombre("GL" + i).setVisible(false);
            getComponentePorNombre("GV" + i).setVisible(false);
            getComponentePorNombre("ES" + i).setVisible(false);
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

        jLabel_Jornada = new javax.swing.JLabel();
        jTextField_Hora1 = new javax.swing.JTextField();
        jLabel_Hora = new javax.swing.JLabel();
        jLabel_Fecha = new javax.swing.JLabel();
        jLabel_EquipoLocal = new javax.swing.JLabel();
        jLabel_GolesLocal = new javax.swing.JLabel();
        jLabel_GolesVisitante = new javax.swing.JLabel();
        jLabel_EquipoVisitante = new javax.swing.JLabel();
        jLabel_Estado = new javax.swing.JLabel();
        jTextField_Fecha1 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField_GolesLocal1 = new javax.swing.JTextField();
        jTextField_GolesVisitante1 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jTextField34 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField37 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        jTextField39 = new javax.swing.JTextField();
        jTextField40 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jTextField47 = new javax.swing.JTextField();
        jTextField48 = new javax.swing.JTextField();
        jTextField49 = new javax.swing.JTextField();
        jTextField53 = new javax.swing.JTextField();
        jTextField54 = new javax.swing.JTextField();
        jTextField55 = new javax.swing.JTextField();
        jTextField56 = new javax.swing.JTextField();
        jTextField57 = new javax.swing.JTextField();
        jTextField59 = new javax.swing.JTextField();
        jTextField60 = new javax.swing.JTextField();
        jTextField62 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jTextField63 = new javax.swing.JTextField();
        jTextField64 = new javax.swing.JTextField();
        jTextField67 = new javax.swing.JTextField();
        jTextField68 = new javax.swing.JTextField();
        jTextField69 = new javax.swing.JTextField();
        jTextField72 = new javax.swing.JTextField();
        jTextField73 = new javax.swing.JTextField();
        jTextField74 = new javax.swing.JTextField();
        jTextField75 = new javax.swing.JTextField();
        jTextField78 = new javax.swing.JTextField();
        jTextField79 = new javax.swing.JTextField();
        jTextField80 = new javax.swing.JTextField();
        jTextField81 = new javax.swing.JTextField();
        jTextField82 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jTextField85 = new javax.swing.JTextField();
        jTextField86 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal1 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante1 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jComboBox9 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        jComboBox11 = new javax.swing.JComboBox<>();
        jComboBox12 = new javax.swing.JComboBox<>();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jComboBox15 = new javax.swing.JComboBox<>();
        jComboBox16 = new javax.swing.JComboBox<>();
        jComboBox17 = new javax.swing.JComboBox<>();
        jComboBox18 = new javax.swing.JComboBox<>();
        jComboBox19 = new javax.swing.JComboBox<>();
        jComboBox20 = new javax.swing.JComboBox<>();
        jComboBox21 = new javax.swing.JComboBox<>();
        jComboBox22 = new javax.swing.JComboBox<>();
        jComboBox23 = new javax.swing.JComboBox<>();
        jComboBox24 = new javax.swing.JComboBox<>();
        jComboBox25 = new javax.swing.JComboBox<>();
        jComboBox26 = new javax.swing.JComboBox<>();
        jComboBox27 = new javax.swing.JComboBox<>();
        jComboBox28 = new javax.swing.JComboBox<>();
        jComboBox29 = new javax.swing.JComboBox<>();
        jComboBox30 = new javax.swing.JComboBox<>();
        jComboBox_Estado1 = new javax.swing.JComboBox<>();
        jComboBox32 = new javax.swing.JComboBox<>();
        jComboBox33 = new javax.swing.JComboBox<>();
        jComboBox34 = new javax.swing.JComboBox<>();
        jComboBox35 = new javax.swing.JComboBox<>();
        jComboBox36 = new javax.swing.JComboBox<>();
        jComboBox37 = new javax.swing.JComboBox<>();
        jComboBox38 = new javax.swing.JComboBox<>();
        jComboBox39 = new javax.swing.JComboBox<>();
        jComboBox40 = new javax.swing.JComboBox<>();
        jTextField_Jornada = new javax.swing.JTextField();
        jComboBox41 = new javax.swing.JComboBox<>();
        jComboBox42 = new javax.swing.JComboBox<>();
        jComboBox43 = new javax.swing.JComboBox<>();
        jComboBox44 = new javax.swing.JComboBox<>();
        jComboBox45 = new javax.swing.JComboBox<>();
        jButton_Cancelar = new javax.swing.JButton();
        jButton_Anadir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel_Jornada.setText("Jornada");
        getContentPane().add(jLabel_Jornada, new java.awt.GridBagConstraints());

        jTextField_Hora1.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField_Hora1.setName("HO0"); // NOI18N
        jTextField_Hora1.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField_Hora1, gridBagConstraints);

        jLabel_Hora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Hora.setText("Hora");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        getContentPane().add(jLabel_Hora, gridBagConstraints);

        jLabel_Fecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Fecha.setText("Fecha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        getContentPane().add(jLabel_Fecha, gridBagConstraints);

        jLabel_EquipoLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoLocal.setText("Equipo Local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        getContentPane().add(jLabel_EquipoLocal, gridBagConstraints);

        jLabel_GolesLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_GolesLocal.setText("Goles Local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        getContentPane().add(jLabel_GolesLocal, gridBagConstraints);

        jLabel_GolesVisitante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_GolesVisitante.setText("Goles Visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 20;
        getContentPane().add(jLabel_GolesVisitante, gridBagConstraints);

        jLabel_EquipoVisitante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoVisitante.setText("Equipo Visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        getContentPane().add(jLabel_EquipoVisitante, gridBagConstraints);

        jLabel_Estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Estado.setText("Estado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        getContentPane().add(jLabel_Estado, gridBagConstraints);

        jTextField_Fecha1.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField_Fecha1.setName("FE0"); // NOI18N
        jTextField_Fecha1.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField_Fecha1, gridBagConstraints);

        jTextField25.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField25.setName("HO6"); // NOI18N
        jTextField25.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField25, gridBagConstraints);

        jTextField_GolesLocal1.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField_GolesLocal1.setName("GL0"); // NOI18N
        jTextField_GolesLocal1.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField_GolesLocal1, gridBagConstraints);

        jTextField_GolesVisitante1.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField_GolesVisitante1.setName("GV0"); // NOI18N
        jTextField_GolesVisitante1.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField_GolesVisitante1, gridBagConstraints);

        jTextField5.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField5.setName("HO1"); // NOI18N
        jTextField5.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField5, gridBagConstraints);

        jTextField6.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField6.setName("FE1"); // NOI18N
        jTextField6.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField6, gridBagConstraints);

        jTextField11.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField11.setName("HO5"); // NOI18N
        jTextField11.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField11, gridBagConstraints);

        jTextField12.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField12.setName("FE5"); // NOI18N
        jTextField12.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField12, gridBagConstraints);

        jTextField13.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField13.setName("HO2"); // NOI18N
        jTextField13.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField13, gridBagConstraints);

        jTextField14.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField14.setName("FE2"); // NOI18N
        jTextField14.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField14, gridBagConstraints);

        jTextField18.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField18.setName("HO4"); // NOI18N
        jTextField18.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField18, gridBagConstraints);

        jTextField19.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField19.setName("FE4"); // NOI18N
        jTextField19.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField19, gridBagConstraints);

        jTextField22.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField22.setName("GL2"); // NOI18N
        jTextField22.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField22, gridBagConstraints);

        jTextField23.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField23.setName("GV1"); // NOI18N
        jTextField23.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField23, gridBagConstraints);

        jTextField26.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField26.setName("GV5"); // NOI18N
        jTextField26.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField26, gridBagConstraints);

        jTextField27.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField27.setName("HO3"); // NOI18N
        jTextField27.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField27, gridBagConstraints);

        jTextField28.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField28.setName("FE3"); // NOI18N
        jTextField28.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField28, gridBagConstraints);

        jTextField32.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField32.setName("GL1"); // NOI18N
        jTextField32.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField32, gridBagConstraints);

        jTextField34.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField34.setName("GL4"); // NOI18N
        jTextField34.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField34, gridBagConstraints);

        jTextField35.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField35.setName("GV3"); // NOI18N
        jTextField35.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField35, gridBagConstraints);

        jTextField36.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField36.setName("GV2"); // NOI18N
        jTextField36.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField36, gridBagConstraints);

        jTextField37.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField37.setName("FE6"); // NOI18N
        jTextField37.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField37, gridBagConstraints);

        jTextField38.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField38.setName("GL6"); // NOI18N
        jTextField38.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField38, gridBagConstraints);

        jTextField39.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField39.setName("GL5"); // NOI18N
        jTextField39.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField39, gridBagConstraints);

        jTextField40.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField40.setName("GV4"); // NOI18N
        jTextField40.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField40, gridBagConstraints);

        jTextField41.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField41.setName("GL3"); // NOI18N
        jTextField41.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField41, gridBagConstraints);

        jTextField42.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField42.setName("HO7"); // NOI18N
        jTextField42.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField42, gridBagConstraints);

        jTextField43.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField43.setName("FE7"); // NOI18N
        jTextField43.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField43, gridBagConstraints);

        jTextField45.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField45.setName("GV6"); // NOI18N
        jTextField45.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField45, gridBagConstraints);

        jTextField46.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField46.setName("GV7"); // NOI18N
        jTextField46.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField46, gridBagConstraints);

        jTextField47.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField47.setName("GL7"); // NOI18N
        jTextField47.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField47, gridBagConstraints);

        jTextField48.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField48.setName("HO8"); // NOI18N
        jTextField48.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField48, gridBagConstraints);

        jTextField49.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField49.setName("FE8"); // NOI18N
        jTextField49.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField49, gridBagConstraints);

        jTextField53.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField53.setName("GV8"); // NOI18N
        jTextField53.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField53, gridBagConstraints);

        jTextField54.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField54.setName("GL8"); // NOI18N
        jTextField54.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField54, gridBagConstraints);

        jTextField55.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField55.setName("HO9"); // NOI18N
        jTextField55.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField55, gridBagConstraints);

        jTextField56.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField56.setName("HO10"); // NOI18N
        jTextField56.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField56, gridBagConstraints);

        jTextField57.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField57.setName("FE9"); // NOI18N
        jTextField57.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField57, gridBagConstraints);

        jTextField59.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField59.setName("GV9"); // NOI18N
        jTextField59.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField59, gridBagConstraints);

        jTextField60.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField60.setName("GL9"); // NOI18N
        jTextField60.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField60, gridBagConstraints);

        jTextField62.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField62.setName("FE10"); // NOI18N
        jTextField62.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField62, gridBagConstraints);

        jTextField50.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField50.setName("GV10"); // NOI18N
        jTextField50.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField50, gridBagConstraints);

        jTextField63.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField63.setName("HO11"); // NOI18N
        jTextField63.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField63, gridBagConstraints);

        jTextField64.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField64.setName("GL11"); // NOI18N
        jTextField64.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField64, gridBagConstraints);

        jTextField67.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField67.setName("FE11"); // NOI18N
        jTextField67.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField67, gridBagConstraints);

        jTextField68.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField68.setName("GV11"); // NOI18N
        jTextField68.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField68, gridBagConstraints);

        jTextField69.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField69.setName("GL10"); // NOI18N
        jTextField69.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField69, gridBagConstraints);

        jTextField72.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField72.setName("HO12"); // NOI18N
        jTextField72.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField72, gridBagConstraints);

        jTextField73.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField73.setName("FE12"); // NOI18N
        jTextField73.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField73, gridBagConstraints);

        jTextField74.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField74.setName("GV12"); // NOI18N
        jTextField74.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField74, gridBagConstraints);

        jTextField75.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField75.setName("GL12"); // NOI18N
        jTextField75.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField75, gridBagConstraints);

        jTextField78.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField78.setName("HO13"); // NOI18N
        jTextField78.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField78, gridBagConstraints);

        jTextField79.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField79.setName("FE13"); // NOI18N
        jTextField79.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField79, gridBagConstraints);

        jTextField80.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField80.setName("HO14"); // NOI18N
        jTextField80.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField80, gridBagConstraints);

        jTextField81.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField81.setName("GV13"); // NOI18N
        jTextField81.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField81, gridBagConstraints);

        jTextField82.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField82.setName("GL13"); // NOI18N
        jTextField82.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField82, gridBagConstraints);

        jTextField29.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextField29.setName("FE14"); // NOI18N
        jTextField29.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField29, gridBagConstraints);

        jTextField85.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField85.setName("GV14"); // NOI18N
        jTextField85.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField85, gridBagConstraints);

        jTextField86.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField86.setName("GL14"); // NOI18N
        jTextField86.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jTextField86, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("EL1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        getContentPane().add(jComboBox1, gridBagConstraints);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setName("EV1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        getContentPane().add(jComboBox2, gridBagConstraints);

        jComboBox_EquipoLocal1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal1.setName("EL0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jComboBox_EquipoLocal1, gridBagConstraints);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.setName("EL2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jComboBox4, gridBagConstraints);

        jComboBox_EquipoVisitante1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante1.setName("EV0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jComboBox_EquipoVisitante1, gridBagConstraints);

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.setName("EV2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jComboBox6, gridBagConstraints);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox7.setName("EL3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        getContentPane().add(jComboBox7, gridBagConstraints);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox8.setName("EV4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        getContentPane().add(jComboBox8, gridBagConstraints);

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox9.setName("EL4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        getContentPane().add(jComboBox9, gridBagConstraints);

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox10.setName("EV13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 15;
        getContentPane().add(jComboBox10, gridBagConstraints);

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox11.setName("EL5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        getContentPane().add(jComboBox11, gridBagConstraints);

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox12.setName("EL6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        getContentPane().add(jComboBox12, gridBagConstraints);

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox13.setName("EL7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        getContentPane().add(jComboBox13, gridBagConstraints);

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox14.setName("EL10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        getContentPane().add(jComboBox14, gridBagConstraints);

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox15.setName("EL9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        getContentPane().add(jComboBox15, gridBagConstraints);

        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox16.setName("EL8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        getContentPane().add(jComboBox16, gridBagConstraints);

        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox17.setName("EL11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        getContentPane().add(jComboBox17, gridBagConstraints);

        jComboBox18.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox18.setName("EL12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        getContentPane().add(jComboBox18, gridBagConstraints);

        jComboBox19.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox19.setName("EL13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        getContentPane().add(jComboBox19, gridBagConstraints);

        jComboBox20.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox20.setName("EV5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        getContentPane().add(jComboBox20, gridBagConstraints);

        jComboBox21.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox21.setName("EV6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        getContentPane().add(jComboBox21, gridBagConstraints);

        jComboBox22.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox22.setName("EV14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 16;
        getContentPane().add(jComboBox22, gridBagConstraints);

        jComboBox23.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox23.setName("EV7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        getContentPane().add(jComboBox23, gridBagConstraints);

        jComboBox24.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox24.setName("EV9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        getContentPane().add(jComboBox24, gridBagConstraints);

        jComboBox25.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox25.setName("EV10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        getContentPane().add(jComboBox25, gridBagConstraints);

        jComboBox26.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox26.setName("EV8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        getContentPane().add(jComboBox26, gridBagConstraints);

        jComboBox27.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox27.setName("EV11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        getContentPane().add(jComboBox27, gridBagConstraints);

        jComboBox28.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox28.setName("EV3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        getContentPane().add(jComboBox28, gridBagConstraints);

        jComboBox29.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox29.setName("EV12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 14;
        getContentPane().add(jComboBox29, gridBagConstraints);

        jComboBox30.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox30.setName("EL14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        getContentPane().add(jComboBox30, gridBagConstraints);

        jComboBox_Estado1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado1.setName("ES0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jComboBox_Estado1, gridBagConstraints);

        jComboBox32.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox32.setName("ES1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        getContentPane().add(jComboBox32, gridBagConstraints);

        jComboBox33.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox33.setName("ES2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jComboBox33, gridBagConstraints);

        jComboBox34.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox34.setName("ES3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        getContentPane().add(jComboBox34, gridBagConstraints);

        jComboBox35.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox35.setName("ES4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        getContentPane().add(jComboBox35, gridBagConstraints);

        jComboBox36.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox36.setName("ES5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        getContentPane().add(jComboBox36, gridBagConstraints);

        jComboBox37.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox37.setName("ES6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        getContentPane().add(jComboBox37, gridBagConstraints);

        jComboBox38.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox38.setName("ES7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        getContentPane().add(jComboBox38, gridBagConstraints);

        jComboBox39.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox39.setName("ES8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        getContentPane().add(jComboBox39, gridBagConstraints);

        jComboBox40.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox40.setName("ES9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        getContentPane().add(jComboBox40, gridBagConstraints);

        jTextField_Jornada.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextField_Jornada.setPreferredSize(new java.awt.Dimension(70, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 5;
        getContentPane().add(jTextField_Jornada, gridBagConstraints);

        jComboBox41.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox41.setName("ES10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        getContentPane().add(jComboBox41, gridBagConstraints);

        jComboBox42.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox42.setName("ES12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        getContentPane().add(jComboBox42, gridBagConstraints);

        jComboBox43.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox43.setName("ES11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        getContentPane().add(jComboBox43, gridBagConstraints);

        jComboBox44.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox44.setName("ES14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 16;
        getContentPane().add(jComboBox44, gridBagConstraints);

        jComboBox45.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox45.setName("ES13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        getContentPane().add(jComboBox45, gridBagConstraints);

        jButton_Cancelar.setText("CANCELAR");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        getContentPane().add(jButton_Cancelar, gridBagConstraints);

        jButton_Anadir.setText("AÑADIR");
        jButton_Anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AnadirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 18;
        getContentPane().add(jButton_Anadir, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jButton_AnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AnadirActionPerformed
        String[] registro = null;
        ArrayList<String> consultas = new ArrayList<>();
        boolean[] equiposUsados = new boolean[totalEquipos];

        int numPartido = 0;
        boolean esCorrecto = true;
        while (numPartido < totalPartidos && esCorrecto) {
            String consulta = "INSERT INTO partido OVERRIDING SYSTEM VALUE VALUES (null," + idCompeticion + ",";
            //Obtiene el número de jornada
            try {
                int jornada = Integer.parseInt(jTextField_Jornada.getText());
                consulta += jornada + ",";
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Debe introducir un número de jornada válido.", "Error", JOptionPane.WARNING_MESSAGE);
                esCorrecto = false;
                break;
            }

            //Obtiene la hora del partido
            String horaPartido = ((JTextField) getComponentePorNombre("HO" + numPartido)).getText();
            if (horaPartido.equals("")) {
                consulta += "null,";
            } else {
                try {
                    Date hora = formatoHora.parse(horaPartido);
                    horaPartido = formatoHoraBD.format(hora);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "El formato de la hora introducida en la fila " + (numPartido + 1) + " no es correcto. Debe ser 'hh:mm'.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
                consulta += "'" + horaPartido + "',";
            }

            //Obtiene la fecha del partido
            String fechaPartido = ((JTextField) getComponentePorNombre("FE" + numPartido)).getText();
            if (fechaPartido.equals("")) {
                consulta += "null,";
            } else {
                try {
                    Date fecha = formato.parse(fechaPartido);
                    fechaPartido = formatoBD.format(fecha);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "El formato de la fecha introducida en la fila " + (numPartido + 1) + " no es correcto. Debe ser 'dd/mm/aaaa'.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
                consulta += "'" + fechaPartido + "',";
            }

            //Obtiene el equipo local seleccionado
            JComboBox combo = (JComboBox) getComponentePorNombre("EL" + numPartido);
            if (equiposUsados[combo.getSelectedIndex()] == true) {
                JOptionPane.showMessageDialog(this, "El equipo local de la fila " + (numPartido + 1) + " ya está en uso.", "Error", JOptionPane.WARNING_MESSAGE);
                esCorrecto = false;
                break;
            } else {
                registro = (String[]) equipos.get(combo.getSelectedIndex());
                int idEquipoLocal = Integer.parseInt(registro[0]);
                consulta += idEquipoLocal + ",";
                equiposUsados[combo.getSelectedIndex()] = true;
            }

            //Obtiene el equipo visitante seleccionado
            combo = (JComboBox) getComponentePorNombre("EV" + numPartido);
            if (equiposUsados[combo.getSelectedIndex()] == true) {
                JOptionPane.showMessageDialog(this, "El equipo visitante de la fila " + (numPartido + 1) + " ya está en uso.", "Error", JOptionPane.WARNING_MESSAGE);
                esCorrecto = false;
                break;
            } else {
                registro = (String[]) equipos.get(combo.getSelectedIndex());
                int idEquipoVisitante = Integer.parseInt(registro[0]);
                consulta += idEquipoVisitante + ",";
                equiposUsados[combo.getSelectedIndex()] = true;
            }

            //Obtiene los goles del equipo local
            String golesLocal = ((JTextField) getComponentePorNombre("GL" + numPartido)).getText();
            if (golesLocal.equals("")) {
                consulta += "null,";
            } else {
                try {
                    consulta += Integer.parseInt(golesLocal) + ",";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "El formato de los goles locales en la fila " + (numPartido + 1) + " no es correcto.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
            }

            //Obtiene los goles del equipo visitante
            String golesVisitante = ((JTextField) getComponentePorNombre("GV" + numPartido)).getText();
            if (golesVisitante.equals("")) {
                consulta += "null,";
            } else {
                try {
                    consulta += Integer.parseInt(golesVisitante) + ",";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "El formato de los goles visitantes en la fila " + (numPartido + 1) + " no es correcto.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
            }

            //Obtiene el estado del partido
            combo = (JComboBox) getComponentePorNombre("ES" + numPartido);
            String estado = combo.getSelectedItem().toString();
            consulta += "'" + estado + "')";

            consultas.add(consulta);

            //Pasa al siguiente partido
            numPartido++;
        }

        if (esCorrecto) {
            //Se ejecutan las consultas que insertan los partidos de la jornada
            for (String sql : consultas) {
                BaseDeDatos.getBD().ConsultaSQL(sql);
            }

            //Se genera la clasificación actualizada
            Clasificacion.generar(idCompeticion);
        }
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
            java.util.logging.Logger.getLogger(Alta_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alta_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alta_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alta_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Alta_Jornada dialog = new Alta_Jornada(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox18;
    private javax.swing.JComboBox<String> jComboBox19;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox20;
    private javax.swing.JComboBox<String> jComboBox21;
    private javax.swing.JComboBox<String> jComboBox22;
    private javax.swing.JComboBox<String> jComboBox23;
    private javax.swing.JComboBox<String> jComboBox24;
    private javax.swing.JComboBox<String> jComboBox25;
    private javax.swing.JComboBox<String> jComboBox26;
    private javax.swing.JComboBox<String> jComboBox27;
    private javax.swing.JComboBox<String> jComboBox28;
    private javax.swing.JComboBox<String> jComboBox29;
    private javax.swing.JComboBox<String> jComboBox30;
    private javax.swing.JComboBox<String> jComboBox32;
    private javax.swing.JComboBox<String> jComboBox33;
    private javax.swing.JComboBox<String> jComboBox34;
    private javax.swing.JComboBox<String> jComboBox35;
    private javax.swing.JComboBox<String> jComboBox36;
    private javax.swing.JComboBox<String> jComboBox37;
    private javax.swing.JComboBox<String> jComboBox38;
    private javax.swing.JComboBox<String> jComboBox39;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox40;
    private javax.swing.JComboBox<String> jComboBox41;
    private javax.swing.JComboBox<String> jComboBox42;
    private javax.swing.JComboBox<String> jComboBox43;
    private javax.swing.JComboBox<String> jComboBox44;
    private javax.swing.JComboBox<String> jComboBox45;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal1;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante1;
    private javax.swing.JComboBox<String> jComboBox_Estado1;
    private javax.swing.JLabel jLabel_EquipoLocal;
    private javax.swing.JLabel jLabel_EquipoVisitante;
    private javax.swing.JLabel jLabel_Estado;
    private javax.swing.JLabel jLabel_Fecha;
    private javax.swing.JLabel jLabel_GolesLocal;
    private javax.swing.JLabel jLabel_GolesVisitante;
    private javax.swing.JLabel jLabel_Hora;
    private javax.swing.JLabel jLabel_Jornada;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField63;
    private javax.swing.JTextField jTextField64;
    private javax.swing.JTextField jTextField67;
    private javax.swing.JTextField jTextField68;
    private javax.swing.JTextField jTextField69;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField73;
    private javax.swing.JTextField jTextField74;
    private javax.swing.JTextField jTextField75;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    private javax.swing.JTextField jTextField82;
    private javax.swing.JTextField jTextField85;
    private javax.swing.JTextField jTextField86;
    private javax.swing.JTextField jTextField_Fecha1;
    private javax.swing.JTextField jTextField_GolesLocal1;
    private javax.swing.JTextField jTextField_GolesVisitante1;
    private javax.swing.JTextField jTextField_Hora1;
    private javax.swing.JTextField jTextField_Jornada;
    // End of variables declaration//GEN-END:variables
}
