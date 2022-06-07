/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package tfg.InfoModificar;

import java.awt.Color;
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
public class Modificar_Jornada extends javax.swing.JDialog {

    private static final int MAX_PARTIDOS = 15;

    private static final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat formatoHoraBD = new SimpleDateFormat("HH:mm:ss");

    private HashMap<String, Component> mapaComponentes;

    ArrayList equipos = null;
    int totalEquipos;
    int totalPartidos;

    HashMap<String, Integer> posEquipos = new HashMap<>();

    int idCompeticion;
    int jornada;

    ArrayList partidos = null;

    public Modificar_Jornada(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public Modificar_Jornada(java.awt.Frame parent, boolean modal, int idCompeticion, int jornada) {
        super(parent, modal);
        initComponents();
        this.idCompeticion = idCompeticion;
        this.jornada = jornada;
        getContentPane().setBackground(new Color(214, 239, 246));
        crearMapaComponentes();
        cargarEquipos();
        cargarEstados();
        ocultarPartidos();
        cargarDatos();
    }

    //Crea una tabla hash en la que se asocian los componentes con cadenas que los identifican
    public void crearMapaComponentes() {
        mapaComponentes = new HashMap<>();
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
            String[] listaEquipos = new String[totalEquipos];
            for (int i = 0; i < totalEquipos; i++) {
                String[] registro = (String[]) equipos.get(i);
                listaEquipos[i] = registro[1];
                //Asocia el id del equipo con la posición dentro de la lista
                posEquipos.put(registro[0], i);
            }

            //Rellena los combos del equipo local y visitante de los partidos
            for (int i = 0; i < totalPartidos; i++) {
                //Obtiene el componente asociado al equipo local del partido actual
                JComboBox<String> combo = (JComboBox<String>) getComponentePorNombre("EL" + i);
                //Asigna el modelo creado a partir de la lista de equipos
                if (combo != null) {
                    combo.setModel(new DefaultComboBoxModel<>(listaEquipos));
                }
                //Obtiene el componente asociado al equipo visitante del partido actual
                combo = (JComboBox) getComponentePorNombre("EV" + i);
                //Asigna el modelo creado a partir de la lista de equipos
                if (combo != null) {
                    combo.setModel(new DefaultComboBoxModel<>(listaEquipos));
                }
            }
        }
    }

    public void cargarEstados() {
        //Crea una lista con todos los estados
        String[] listaEstados = new String[]{"Sin comenzar", "Finalizado", "Suspendido", "Aplazado"};

        for (int i = 0; i < totalPartidos; i++) {
            //Obtiene el componente asociado al estado del partido actual
            JComboBox<String> combo = (JComboBox<String>) getComponentePorNombre("ES" + i);
            //Asigna el modelo creado a partir de la lista de estados
            if (combo != null) {
                combo.setModel(new DefaultComboBoxModel<>(listaEstados));
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

    public void cargarDatos() {
        partidos = BaseDeDatos.getBD().ConsultaSQL("SELECT * FROM partido WHERE (idCompeticion = " + idCompeticion + ") AND (jornada = " + jornada + ")");

        if (partidos != null) {
            //Establece la jornada
            jTextField_Jornada.setText(String.valueOf(jornada));
            for (int i = 0; i < partidos.size(); i++) {
                String[] registro = (String[]) partidos.get(i);
                //Establece la hora
                try {
                    Date hora = formatoHoraBD.parse(registro[3]);
                    registro[3] = formatoHora.format(hora);
                    ((JTextField) getComponentePorNombre("HO" + i)).setText(registro[3]);
                } catch (Exception ex) {
                }
                //Establece la fecha
                try {
                    Date fecha = formatoBD.parse(registro[4]);
                    registro[4] = formato.format(fecha);
                    ((JTextField) getComponentePorNombre("FE" + i)).setText(registro[4]);
                } catch (Exception ex) {
                }
                //Establece el equipo local
                ((JComboBox) getComponentePorNombre("EL" + i)).setSelectedIndex(posEquipos.get(registro[5]));
                //Establece el equipo visitante
                ((JComboBox) getComponentePorNombre("EV" + i)).setSelectedIndex(posEquipos.get(registro[6]));
                //Establece los goles del equipo local
                ((JTextField) getComponentePorNombre("GL" + i)).setText(registro[7]);
                //Establece los goles del equipo visitante
                ((JTextField) getComponentePorNombre("GV" + i)).setText(registro[8]);
                //Establece el estado del partido
                ((JComboBox) getComponentePorNombre("ES" + i)).setSelectedItem(registro[9]);
            }
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
        jTextField_Hora7 = new javax.swing.JTextField();
        jTextField_GolesLocal1 = new javax.swing.JTextField();
        jTextField_GolesVisitante1 = new javax.swing.JTextField();
        jTextField_Hora2 = new javax.swing.JTextField();
        jTextField_Fecha2 = new javax.swing.JTextField();
        jTextField_Hora6 = new javax.swing.JTextField();
        jTextField_Fecha6 = new javax.swing.JTextField();
        jTextField_Hora3 = new javax.swing.JTextField();
        jTextField_Fecha3 = new javax.swing.JTextField();
        jTextField_Hora5 = new javax.swing.JTextField();
        jTextField_Fecha5 = new javax.swing.JTextField();
        jTextField_GolesLocal3 = new javax.swing.JTextField();
        jTextField_GolesVisitante2 = new javax.swing.JTextField();
        jTextField_GolesVisitante6 = new javax.swing.JTextField();
        jTextField_Hora4 = new javax.swing.JTextField();
        jTextField_Fecha4 = new javax.swing.JTextField();
        jTextField_GolesLocal2 = new javax.swing.JTextField();
        jTextField_GolesLocal5 = new javax.swing.JTextField();
        jTextField_GolesVisitante4 = new javax.swing.JTextField();
        jTextField_GolesVisitante3 = new javax.swing.JTextField();
        jTextField_Fecha7 = new javax.swing.JTextField();
        jTextField_GolesLocal7 = new javax.swing.JTextField();
        jTextField_GolesLocal6 = new javax.swing.JTextField();
        jTextField_GolesVisitante5 = new javax.swing.JTextField();
        jTextField_GolesLocal4 = new javax.swing.JTextField();
        jTextField_Hora8 = new javax.swing.JTextField();
        jTextField_Fecha8 = new javax.swing.JTextField();
        jTextField_GolesVisitante7 = new javax.swing.JTextField();
        jTextField_GolesVisitante8 = new javax.swing.JTextField();
        jTextField_GolesLocal8 = new javax.swing.JTextField();
        jTextField_Hora9 = new javax.swing.JTextField();
        jTextField_Fecha9 = new javax.swing.JTextField();
        jTextField_GolesVisitante9 = new javax.swing.JTextField();
        jTextField_GolesLocal9 = new javax.swing.JTextField();
        jTextField_Hora10 = new javax.swing.JTextField();
        jTextField_Hora11 = new javax.swing.JTextField();
        jTextField_Fecha10 = new javax.swing.JTextField();
        jTextField_GolesVisitante10 = new javax.swing.JTextField();
        jTextField_GolesLocal10 = new javax.swing.JTextField();
        jTextField_Fecha11 = new javax.swing.JTextField();
        jTextField_GolesVisitante11 = new javax.swing.JTextField();
        jTextField_Hora12 = new javax.swing.JTextField();
        jTextField_GolesLocal12 = new javax.swing.JTextField();
        jTextField_Fecha12 = new javax.swing.JTextField();
        jTextField_GolesVisitante12 = new javax.swing.JTextField();
        jTextField_GolesLocal11 = new javax.swing.JTextField();
        jTextField_Hora13 = new javax.swing.JTextField();
        jTextField_Fecha13 = new javax.swing.JTextField();
        jTextField_GolesVisitante13 = new javax.swing.JTextField();
        jTextField_GolesLocal13 = new javax.swing.JTextField();
        jTextField_Hora14 = new javax.swing.JTextField();
        jTextField_Fecha14 = new javax.swing.JTextField();
        jTextField_Hora15 = new javax.swing.JTextField();
        jTextField_GolesVisitante14 = new javax.swing.JTextField();
        jTextField_GolesLocal14 = new javax.swing.JTextField();
        jTextField_Fecha15 = new javax.swing.JTextField();
        jTextField_GolesVisitante15 = new javax.swing.JTextField();
        jTextField_GolesLocal15 = new javax.swing.JTextField();
        jComboBox_EquipoLocal2 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante2 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal1 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal3 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante1 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante3 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal4 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante5 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal5 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante14 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal6 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal7 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal8 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal11 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal10 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal9 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal12 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal13 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal14 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante6 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante7 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante15 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante8 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante10 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante11 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante9 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante12 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante4 = new javax.swing.JComboBox<>();
        jComboBox_EquipoVisitante13 = new javax.swing.JComboBox<>();
        jComboBox_EquipoLocal15 = new javax.swing.JComboBox<>();
        jComboBox_Estado1 = new javax.swing.JComboBox<>();
        jComboBox_Estado2 = new javax.swing.JComboBox<>();
        jComboBox_Estado3 = new javax.swing.JComboBox<>();
        jComboBox_Estado4 = new javax.swing.JComboBox<>();
        jComboBox_Estado5 = new javax.swing.JComboBox<>();
        jComboBox_Estado6 = new javax.swing.JComboBox<>();
        jComboBox_Estado7 = new javax.swing.JComboBox<>();
        jComboBox_Estado8 = new javax.swing.JComboBox<>();
        jComboBox_Estado9 = new javax.swing.JComboBox<>();
        jComboBox_Estado10 = new javax.swing.JComboBox<>();
        jTextField_Jornada = new javax.swing.JTextField();
        jComboBox_Estado11 = new javax.swing.JComboBox<>();
        jComboBox_Estado13 = new javax.swing.JComboBox<>();
        jComboBox_Estado12 = new javax.swing.JComboBox<>();
        jComboBox_Estado15 = new javax.swing.JComboBox<>();
        jComboBox_Estado14 = new javax.swing.JComboBox<>();
        jButton_Cancelar = new javax.swing.JButton();
        jButton_Modificar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setForeground(new java.awt.Color(214, 239, 246));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel_Jornada.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_Jornada.setText("Jornada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_Jornada, gridBagConstraints);

        jTextField_Hora1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora1.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora1.setName("HO0"); // NOI18N
        jTextField_Hora1.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora1, gridBagConstraints);

        jLabel_Hora.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_Hora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Hora.setText("Hora");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_Hora, gridBagConstraints);

        jLabel_Fecha.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_Fecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Fecha.setText("Fecha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_Fecha, gridBagConstraints);

        jLabel_EquipoLocal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_EquipoLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoLocal.setText("Equipo Local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoLocal, gridBagConstraints);

        jLabel_GolesLocal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_GolesLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_GolesLocal.setText("Goles Local");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_GolesLocal, gridBagConstraints);

        jLabel_GolesVisitante.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_GolesVisitante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_GolesVisitante.setText("Goles Visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_GolesVisitante, gridBagConstraints);

        jLabel_EquipoVisitante.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_EquipoVisitante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_EquipoVisitante.setText("Equipo Visitante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_EquipoVisitante, gridBagConstraints);

        jLabel_Estado.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel_Estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Estado.setText("Estado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel_Estado, gridBagConstraints);

        jTextField_Fecha1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha1.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha1.setName("FE0"); // NOI18N
        jTextField_Fecha1.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha1, gridBagConstraints);

        jTextField_Hora7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora7.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora7.setName("HO6"); // NOI18N
        jTextField_Hora7.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora7, gridBagConstraints);

        jTextField_GolesLocal1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal1.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal1.setName("GL0"); // NOI18N
        jTextField_GolesLocal1.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal1, gridBagConstraints);

        jTextField_GolesVisitante1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante1.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante1.setName("GV0"); // NOI18N
        jTextField_GolesVisitante1.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante1, gridBagConstraints);

        jTextField_Hora2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora2.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora2.setName("HO1"); // NOI18N
        jTextField_Hora2.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora2, gridBagConstraints);

        jTextField_Fecha2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha2.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha2.setName("FE1"); // NOI18N
        jTextField_Fecha2.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha2, gridBagConstraints);

        jTextField_Hora6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora6.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora6.setName("HO5"); // NOI18N
        jTextField_Hora6.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora6, gridBagConstraints);

        jTextField_Fecha6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha6.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha6.setName("FE5"); // NOI18N
        jTextField_Fecha6.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha6, gridBagConstraints);

        jTextField_Hora3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora3.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora3.setName("HO2"); // NOI18N
        jTextField_Hora3.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora3, gridBagConstraints);

        jTextField_Fecha3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha3.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha3.setName("FE2"); // NOI18N
        jTextField_Fecha3.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha3, gridBagConstraints);

        jTextField_Hora5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora5.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora5.setName("HO4"); // NOI18N
        jTextField_Hora5.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora5, gridBagConstraints);

        jTextField_Fecha5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha5.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha5.setName("FE4"); // NOI18N
        jTextField_Fecha5.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha5, gridBagConstraints);

        jTextField_GolesLocal3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal3.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal3.setName("GL2"); // NOI18N
        jTextField_GolesLocal3.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal3, gridBagConstraints);

        jTextField_GolesVisitante2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante2.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante2.setName("GV1"); // NOI18N
        jTextField_GolesVisitante2.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante2, gridBagConstraints);

        jTextField_GolesVisitante6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante6.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante6.setName("GV5"); // NOI18N
        jTextField_GolesVisitante6.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante6, gridBagConstraints);

        jTextField_Hora4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora4.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora4.setName("HO3"); // NOI18N
        jTextField_Hora4.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora4, gridBagConstraints);

        jTextField_Fecha4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha4.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha4.setName("FE3"); // NOI18N
        jTextField_Fecha4.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha4, gridBagConstraints);

        jTextField_GolesLocal2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal2.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal2.setName("GL1"); // NOI18N
        jTextField_GolesLocal2.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal2, gridBagConstraints);

        jTextField_GolesLocal5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal5.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal5.setName("GL4"); // NOI18N
        jTextField_GolesLocal5.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal5, gridBagConstraints);

        jTextField_GolesVisitante4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante4.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante4.setName("GV3"); // NOI18N
        jTextField_GolesVisitante4.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante4, gridBagConstraints);

        jTextField_GolesVisitante3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante3.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante3.setName("GV2"); // NOI18N
        jTextField_GolesVisitante3.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante3, gridBagConstraints);

        jTextField_Fecha7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha7.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha7.setName("FE6"); // NOI18N
        jTextField_Fecha7.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha7, gridBagConstraints);

        jTextField_GolesLocal7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal7.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal7.setName("GL6"); // NOI18N
        jTextField_GolesLocal7.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal7, gridBagConstraints);

        jTextField_GolesLocal6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal6.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal6.setName("GL5"); // NOI18N
        jTextField_GolesLocal6.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal6, gridBagConstraints);

        jTextField_GolesVisitante5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante5.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante5.setName("GV4"); // NOI18N
        jTextField_GolesVisitante5.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante5, gridBagConstraints);

        jTextField_GolesLocal4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal4.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal4.setName("GL3"); // NOI18N
        jTextField_GolesLocal4.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal4, gridBagConstraints);

        jTextField_Hora8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora8.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora8.setName("HO7"); // NOI18N
        jTextField_Hora8.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora8, gridBagConstraints);

        jTextField_Fecha8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha8.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha8.setName("FE7"); // NOI18N
        jTextField_Fecha8.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha8, gridBagConstraints);

        jTextField_GolesVisitante7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante7.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante7.setName("GV6"); // NOI18N
        jTextField_GolesVisitante7.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante7, gridBagConstraints);

        jTextField_GolesVisitante8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante8.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante8.setName("GV7"); // NOI18N
        jTextField_GolesVisitante8.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante8, gridBagConstraints);

        jTextField_GolesLocal8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal8.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal8.setName("GL7"); // NOI18N
        jTextField_GolesLocal8.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal8, gridBagConstraints);

        jTextField_Hora9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora9.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora9.setName("HO8"); // NOI18N
        jTextField_Hora9.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora9, gridBagConstraints);

        jTextField_Fecha9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha9.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha9.setName("FE8"); // NOI18N
        jTextField_Fecha9.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha9, gridBagConstraints);

        jTextField_GolesVisitante9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante9.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante9.setName("GV8"); // NOI18N
        jTextField_GolesVisitante9.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante9, gridBagConstraints);

        jTextField_GolesLocal9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal9.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal9.setName("GL8"); // NOI18N
        jTextField_GolesLocal9.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal9, gridBagConstraints);

        jTextField_Hora10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora10.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora10.setName("HO9"); // NOI18N
        jTextField_Hora10.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora10, gridBagConstraints);

        jTextField_Hora11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora11.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora11.setName("HO10"); // NOI18N
        jTextField_Hora11.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora11, gridBagConstraints);

        jTextField_Fecha10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha10.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha10.setName("FE9"); // NOI18N
        jTextField_Fecha10.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha10, gridBagConstraints);

        jTextField_GolesVisitante10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante10.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante10.setName("GV9"); // NOI18N
        jTextField_GolesVisitante10.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante10, gridBagConstraints);

        jTextField_GolesLocal10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal10.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal10.setName("GL9"); // NOI18N
        jTextField_GolesLocal10.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal10, gridBagConstraints);

        jTextField_Fecha11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha11.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha11.setName("FE10"); // NOI18N
        jTextField_Fecha11.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha11, gridBagConstraints);

        jTextField_GolesVisitante11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante11.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante11.setName("GV10"); // NOI18N
        jTextField_GolesVisitante11.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante11, gridBagConstraints);

        jTextField_Hora12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora12.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora12.setName("HO11"); // NOI18N
        jTextField_Hora12.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora12, gridBagConstraints);

        jTextField_GolesLocal12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal12.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal12.setName("GL11"); // NOI18N
        jTextField_GolesLocal12.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal12, gridBagConstraints);

        jTextField_Fecha12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha12.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha12.setName("FE11"); // NOI18N
        jTextField_Fecha12.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha12, gridBagConstraints);

        jTextField_GolesVisitante12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante12.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante12.setName("GV11"); // NOI18N
        jTextField_GolesVisitante12.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante12, gridBagConstraints);

        jTextField_GolesLocal11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal11.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal11.setName("GL10"); // NOI18N
        jTextField_GolesLocal11.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal11, gridBagConstraints);

        jTextField_Hora13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora13.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora13.setName("HO12"); // NOI18N
        jTextField_Hora13.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora13, gridBagConstraints);

        jTextField_Fecha13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha13.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha13.setName("FE12"); // NOI18N
        jTextField_Fecha13.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha13, gridBagConstraints);

        jTextField_GolesVisitante13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante13.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante13.setName("GV12"); // NOI18N
        jTextField_GolesVisitante13.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante13, gridBagConstraints);

        jTextField_GolesLocal13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal13.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal13.setName("GL12"); // NOI18N
        jTextField_GolesLocal13.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal13, gridBagConstraints);

        jTextField_Hora14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora14.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora14.setName("HO13"); // NOI18N
        jTextField_Hora14.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora14, gridBagConstraints);

        jTextField_Fecha14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha14.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha14.setName("FE13"); // NOI18N
        jTextField_Fecha14.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha14, gridBagConstraints);

        jTextField_Hora15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Hora15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Hora15.setMinimumSize(new java.awt.Dimension(70, 30));
        jTextField_Hora15.setName("HO14"); // NOI18N
        jTextField_Hora15.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Hora15, gridBagConstraints);

        jTextField_GolesVisitante14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante14.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante14.setName("GV13"); // NOI18N
        jTextField_GolesVisitante14.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante14, gridBagConstraints);

        jTextField_GolesLocal14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal14.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal14.setName("GL13"); // NOI18N
        jTextField_GolesLocal14.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal14, gridBagConstraints);

        jTextField_Fecha15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Fecha15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_Fecha15.setMinimumSize(new java.awt.Dimension(90, 30));
        jTextField_Fecha15.setName("FE14"); // NOI18N
        jTextField_Fecha15.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Fecha15, gridBagConstraints);

        jTextField_GolesVisitante15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesVisitante15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesVisitante15.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesVisitante15.setName("GV14"); // NOI18N
        jTextField_GolesVisitante15.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesVisitante15, gridBagConstraints);

        jTextField_GolesLocal15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_GolesLocal15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_GolesLocal15.setMinimumSize(new java.awt.Dimension(50, 30));
        jTextField_GolesLocal15.setName("GL14"); // NOI18N
        jTextField_GolesLocal15.setPreferredSize(new java.awt.Dimension(50, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_GolesLocal15, gridBagConstraints);

        jComboBox_EquipoLocal2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal2.setName("EL1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal2, gridBagConstraints);

        jComboBox_EquipoVisitante2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante2.setName("EV1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante2, gridBagConstraints);

        jComboBox_EquipoLocal1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal1.setName("EL0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal1, gridBagConstraints);

        jComboBox_EquipoLocal3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal3.setName("EL2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal3, gridBagConstraints);

        jComboBox_EquipoVisitante1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante1.setName("EV0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante1, gridBagConstraints);

        jComboBox_EquipoVisitante3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante3.setName("EV2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante3, gridBagConstraints);

        jComboBox_EquipoLocal4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal4.setName("EL3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal4, gridBagConstraints);

        jComboBox_EquipoVisitante5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante5.setName("EV4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante5, gridBagConstraints);

        jComboBox_EquipoLocal5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal5.setName("EL4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal5, gridBagConstraints);

        jComboBox_EquipoVisitante14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante14.setName("EV13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante14, gridBagConstraints);

        jComboBox_EquipoLocal6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal6.setName("EL5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal6, gridBagConstraints);

        jComboBox_EquipoLocal7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal7.setName("EL6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal7, gridBagConstraints);

        jComboBox_EquipoLocal8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal8.setName("EL7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal8, gridBagConstraints);

        jComboBox_EquipoLocal11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal11.setName("EL10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal11, gridBagConstraints);

        jComboBox_EquipoLocal10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal10.setName("EL9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal10, gridBagConstraints);

        jComboBox_EquipoLocal9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal9.setName("EL8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal9, gridBagConstraints);

        jComboBox_EquipoLocal12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal12.setName("EL11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal12, gridBagConstraints);

        jComboBox_EquipoLocal13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal13.setName("EL12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal13, gridBagConstraints);

        jComboBox_EquipoLocal14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal14.setName("EL13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal14, gridBagConstraints);

        jComboBox_EquipoVisitante6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante6.setName("EV5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante6, gridBagConstraints);

        jComboBox_EquipoVisitante7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante7.setName("EV6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante7, gridBagConstraints);

        jComboBox_EquipoVisitante15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante15.setName("EV14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante15, gridBagConstraints);

        jComboBox_EquipoVisitante8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante8.setName("EV7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante8, gridBagConstraints);

        jComboBox_EquipoVisitante10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante10.setName("EV9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante10, gridBagConstraints);

        jComboBox_EquipoVisitante11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante11.setName("EV10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante11, gridBagConstraints);

        jComboBox_EquipoVisitante9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante9.setName("EV8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante9, gridBagConstraints);

        jComboBox_EquipoVisitante12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante12.setName("EV11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante12, gridBagConstraints);

        jComboBox_EquipoVisitante4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante4.setName("EV3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante4, gridBagConstraints);

        jComboBox_EquipoVisitante13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoVisitante13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoVisitante13.setName("EV12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoVisitante13, gridBagConstraints);

        jComboBox_EquipoLocal15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_EquipoLocal15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_EquipoLocal15.setName("EL14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_EquipoLocal15, gridBagConstraints);

        jComboBox_Estado1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado1.setName("ES0"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado1, gridBagConstraints);

        jComboBox_Estado2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado2.setName("ES1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado2, gridBagConstraints);

        jComboBox_Estado3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado3.setName("ES2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado3, gridBagConstraints);

        jComboBox_Estado4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado4.setName("ES3"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado4, gridBagConstraints);

        jComboBox_Estado5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado5.setName("ES4"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado5, gridBagConstraints);

        jComboBox_Estado6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado6.setName("ES5"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado6, gridBagConstraints);

        jComboBox_Estado7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado7.setName("ES6"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado7, gridBagConstraints);

        jComboBox_Estado8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado8.setName("ES7"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado8, gridBagConstraints);

        jComboBox_Estado9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado9.setName("ES8"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado9, gridBagConstraints);

        jComboBox_Estado10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado10.setName("ES9"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado10, gridBagConstraints);

        jTextField_Jornada.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField_Jornada.setMinimumSize(new java.awt.Dimension(100, 30));
        jTextField_Jornada.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTextField_Jornada, gridBagConstraints);

        jComboBox_Estado11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado11.setName("ES10"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado11, gridBagConstraints);

        jComboBox_Estado13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado13.setName("ES12"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado13, gridBagConstraints);

        jComboBox_Estado12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado12.setName("ES11"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado12, gridBagConstraints);

        jComboBox_Estado15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado15.setName("ES14"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado15, gridBagConstraints);

        jComboBox_Estado14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_Estado14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Estado14.setName("ES13"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox_Estado14, gridBagConstraints);

        jButton_Cancelar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Cancelar.setText("CANCELAR");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton_Cancelar, gridBagConstraints);

        jButton_Modificar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jButton_Modificar.setText("MODIFICAR");
        jButton_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ModificarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton_Modificar, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jButton_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ModificarActionPerformed
        String[] registro = null;
        ArrayList<String> consultas = new ArrayList<>();
        boolean[] equiposUsados = new boolean[totalEquipos];
        int nuevaJornada;

        //Obtiene el número de jornada
        try {
            nuevaJornada = Integer.parseInt(jTextField_Jornada.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Debe introducir un número de jornada válido.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //Comprueba si ya existe la jornada introducida (salvo que sea la actual)
        if (nuevaJornada != jornada) {
            ArrayList numeroJornada = BaseDeDatos.getBD().ConsultaSQL("SELECT COUNT(*) FROM partido WHERE (idCompeticion=" + idCompeticion + ") AND (jornada=" + nuevaJornada + ")");
            registro = (String[]) numeroJornada.get(0);
            if (Integer.parseInt(registro[0]) > 0) {
                JOptionPane.showMessageDialog(this, "La jornada introducida ya existe en esta competición.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int numPartido = 0;
        boolean esCorrecto = true;
        while (numPartido < totalPartidos && esCorrecto) {
            String consulta = "UPDATE partido SET jornada = " + nuevaJornada + ", ";
            //Obtiene la hora del partido
            String horaPartido = ((JTextField) getComponentePorNombre("HO" + numPartido)).getText();
            if (horaPartido.equals("")) {
                consulta += "hora = null, ";
            } else {
                try {
                    Date hora = formatoHora.parse(horaPartido);
                    horaPartido = formatoHoraBD.format(hora);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "El formato de la hora introducida en la fila " + (numPartido + 1) + " no es correcto. Debe ser 'hh:mm'.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
                consulta += "hora = '" + horaPartido + "', ";
            }

            //Obtiene la fecha del partido
            String fechaPartido = ((JTextField) getComponentePorNombre("FE" + numPartido)).getText();
            if (fechaPartido.equals("")) {
                consulta += "fecha = null, ";
            } else {
                try {
                    Date fecha = formato.parse(fechaPartido);
                    fechaPartido = formatoBD.format(fecha);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "El formato de la fecha introducida en la fila " + (numPartido + 1) + " no es correcto. Debe ser 'dd/mm/aaaa'.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
                consulta += "fecha = '" + fechaPartido + "', ";
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
                consulta += "id_equipoLocal = " + idEquipoLocal + ", ";
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
                consulta += "id_equipoVisitante = " + idEquipoVisitante + ", ";
                equiposUsados[combo.getSelectedIndex()] = true;
            }

            //Obtiene los goles del equipo local
            String golesLocal = ((JTextField) getComponentePorNombre("GL" + numPartido)).getText();
            if (golesLocal.equals("")) {
                consulta += "golesLocal = null, ";
            } else {
                try {
                    consulta += "golesLocal = " + Integer.parseInt(golesLocal) + ", ";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "El formato de los goles locales en la fila " + (numPartido + 1) + " no es correcto.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
            }

            //Obtiene los goles del equipo visitante
            String golesVisitante = ((JTextField) getComponentePorNombre("GV" + numPartido)).getText();
            if (golesVisitante.equals("")) {
                consulta += "golesVisitante = null, ";
            } else {
                try {
                    consulta += "golesVisitante = " + Integer.parseInt(golesVisitante) + ", ";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "El formato de los goles visitantes en la fila " + (numPartido + 1) + " no es correcto.", "Error", JOptionPane.WARNING_MESSAGE);
                    esCorrecto = false;
                    break;
                }
            }

            //Obtiene el estado del partido
            combo = (JComboBox) getComponentePorNombre("ES" + numPartido);
            String estado = combo.getSelectedItem().toString();
            consulta += "estado = '" + estado + "' ";

            //Añade la condición con el identificador del partido a modificar
            registro = (String[]) partidos.get(numPartido);
            consulta += "WHERE idPartido = " + registro[0];

            consultas.add(consulta);

            //Pasa al siguiente partido
            numPartido++;
        }

        if (esCorrecto) {
            //Se ejecutan las consultas que modifican los partidos de la jornada
            for (String sql : consultas) {
                BaseDeDatos.getBD().ConsultaSQL(sql);
            }

            //Se genera la clasificación actualizada
            Clasificacion.generar(idCompeticion);

            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton_ModificarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //Cuando se muestra la ventana se redimensiona para adaptarse al número de partidos
        setSize(getWidth(), getHeight() - ((10 - totalPartidos) * 40));
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Modificar_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Modificar_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Modificar_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Modificar_Jornada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Modificar_Jornada dialog = new Modificar_Jornada(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_Modificar;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal1;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal10;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal11;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal12;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal13;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal14;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal15;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal2;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal3;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal4;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal5;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal6;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal7;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal8;
    private javax.swing.JComboBox<String> jComboBox_EquipoLocal9;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante1;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante10;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante11;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante12;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante13;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante14;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante15;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante2;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante3;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante4;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante5;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante6;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante7;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante8;
    private javax.swing.JComboBox<String> jComboBox_EquipoVisitante9;
    private javax.swing.JComboBox<String> jComboBox_Estado1;
    private javax.swing.JComboBox<String> jComboBox_Estado10;
    private javax.swing.JComboBox<String> jComboBox_Estado11;
    private javax.swing.JComboBox<String> jComboBox_Estado12;
    private javax.swing.JComboBox<String> jComboBox_Estado13;
    private javax.swing.JComboBox<String> jComboBox_Estado14;
    private javax.swing.JComboBox<String> jComboBox_Estado15;
    private javax.swing.JComboBox<String> jComboBox_Estado2;
    private javax.swing.JComboBox<String> jComboBox_Estado3;
    private javax.swing.JComboBox<String> jComboBox_Estado4;
    private javax.swing.JComboBox<String> jComboBox_Estado5;
    private javax.swing.JComboBox<String> jComboBox_Estado6;
    private javax.swing.JComboBox<String> jComboBox_Estado7;
    private javax.swing.JComboBox<String> jComboBox_Estado8;
    private javax.swing.JComboBox<String> jComboBox_Estado9;
    private javax.swing.JLabel jLabel_EquipoLocal;
    private javax.swing.JLabel jLabel_EquipoVisitante;
    private javax.swing.JLabel jLabel_Estado;
    private javax.swing.JLabel jLabel_Fecha;
    private javax.swing.JLabel jLabel_GolesLocal;
    private javax.swing.JLabel jLabel_GolesVisitante;
    private javax.swing.JLabel jLabel_Hora;
    private javax.swing.JLabel jLabel_Jornada;
    private javax.swing.JTextField jTextField_Fecha1;
    private javax.swing.JTextField jTextField_Fecha10;
    private javax.swing.JTextField jTextField_Fecha11;
    private javax.swing.JTextField jTextField_Fecha12;
    private javax.swing.JTextField jTextField_Fecha13;
    private javax.swing.JTextField jTextField_Fecha14;
    private javax.swing.JTextField jTextField_Fecha15;
    private javax.swing.JTextField jTextField_Fecha2;
    private javax.swing.JTextField jTextField_Fecha3;
    private javax.swing.JTextField jTextField_Fecha4;
    private javax.swing.JTextField jTextField_Fecha5;
    private javax.swing.JTextField jTextField_Fecha6;
    private javax.swing.JTextField jTextField_Fecha7;
    private javax.swing.JTextField jTextField_Fecha8;
    private javax.swing.JTextField jTextField_Fecha9;
    private javax.swing.JTextField jTextField_GolesLocal1;
    private javax.swing.JTextField jTextField_GolesLocal10;
    private javax.swing.JTextField jTextField_GolesLocal11;
    private javax.swing.JTextField jTextField_GolesLocal12;
    private javax.swing.JTextField jTextField_GolesLocal13;
    private javax.swing.JTextField jTextField_GolesLocal14;
    private javax.swing.JTextField jTextField_GolesLocal15;
    private javax.swing.JTextField jTextField_GolesLocal2;
    private javax.swing.JTextField jTextField_GolesLocal3;
    private javax.swing.JTextField jTextField_GolesLocal4;
    private javax.swing.JTextField jTextField_GolesLocal5;
    private javax.swing.JTextField jTextField_GolesLocal6;
    private javax.swing.JTextField jTextField_GolesLocal7;
    private javax.swing.JTextField jTextField_GolesLocal8;
    private javax.swing.JTextField jTextField_GolesLocal9;
    private javax.swing.JTextField jTextField_GolesVisitante1;
    private javax.swing.JTextField jTextField_GolesVisitante10;
    private javax.swing.JTextField jTextField_GolesVisitante11;
    private javax.swing.JTextField jTextField_GolesVisitante12;
    private javax.swing.JTextField jTextField_GolesVisitante13;
    private javax.swing.JTextField jTextField_GolesVisitante14;
    private javax.swing.JTextField jTextField_GolesVisitante15;
    private javax.swing.JTextField jTextField_GolesVisitante2;
    private javax.swing.JTextField jTextField_GolesVisitante3;
    private javax.swing.JTextField jTextField_GolesVisitante4;
    private javax.swing.JTextField jTextField_GolesVisitante5;
    private javax.swing.JTextField jTextField_GolesVisitante6;
    private javax.swing.JTextField jTextField_GolesVisitante7;
    private javax.swing.JTextField jTextField_GolesVisitante8;
    private javax.swing.JTextField jTextField_GolesVisitante9;
    private javax.swing.JTextField jTextField_Hora1;
    private javax.swing.JTextField jTextField_Hora10;
    private javax.swing.JTextField jTextField_Hora11;
    private javax.swing.JTextField jTextField_Hora12;
    private javax.swing.JTextField jTextField_Hora13;
    private javax.swing.JTextField jTextField_Hora14;
    private javax.swing.JTextField jTextField_Hora15;
    private javax.swing.JTextField jTextField_Hora2;
    private javax.swing.JTextField jTextField_Hora3;
    private javax.swing.JTextField jTextField_Hora4;
    private javax.swing.JTextField jTextField_Hora5;
    private javax.swing.JTextField jTextField_Hora6;
    private javax.swing.JTextField jTextField_Hora7;
    private javax.swing.JTextField jTextField_Hora8;
    private javax.swing.JTextField jTextField_Hora9;
    private javax.swing.JTextField jTextField_Jornada;
    // End of variables declaration//GEN-END:variables
}
