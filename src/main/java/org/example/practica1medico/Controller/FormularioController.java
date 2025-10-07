package org.example.practica1medico.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.practica1medico.Model.Cita;
import org.example.practica1medico.Model.Especialidad;
import org.example.practica1medico.Model.Paciente;
import org.example.practica1medico.UTIL.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public class FormularioController {

       /*TextField	txtDni
TextField	txtNombre
TextField	txtDireccion
TextField	txtTelefono
DatePicker	dpFechCita
ComboBox	cmbEspecialidad
TableView tableCitas
TableColumn	colNumero
TableColumn	colFecha
TableColumn	colEspecialidad
Button	btnVer
Button	btnNueva
Button	btnBorrar
Button	btnModificar*/

    @FXML private TextField txtDni, txtNombre, txtDireccion, txtTelefono;
    @FXML private DatePicker dpFechCita;
    @FXML private ComboBox<Especialidad> cmbEspecialidad;
    @FXML private TableView<Cita> tableCitas;
    @FXML private TableColumn<Cita, Integer> colNumero;
    @FXML private TableColumn<Cita, LocalDate> colFecha;
    @FXML private TableColumn<Cita, String> colEspecialidad;
    @FXML private Button btnVer, btnNueva, btnBorrar, btnModificar;

    private Paciente pac;
    private ObservableList<Cita> listaCitas = FXCollections.observableArrayList();

    private Connection conn;

    @FXML
    public void initialize() {
        //la conexión global
        conn = DBConnection.getConnection();

        colNumero.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getNCita()).asObject());
        colFecha.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getFecha()));
        colEspecialidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEspecialidad()));

        //carga especialidades al iniciar
        cargaEspecialidad();
    }

    public void setPaciente(Paciente p) {
        this.pac = p;
        txtDni.setText(p.getDni());
        txtNombre.setText(p.getNombre());
        txtDireccion.setText(p.getDireccion());
        txtTelefono.setText(p.getTelefono());
    }

    private void cargaEspecialidad() {
        cmbEspecialidad.getItems().clear();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM ESPECIALIDAD")) {
            while (rs.next()) {
                cmbEspecialidad.getItems().add(new Especialidad(rs.getInt("idE"), rs.getString("tipo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //método para llamar al metodo real, y asignarlo al boton
    @FXML
    private void cargaCitas(ActionEvent event) {
        cargaCitasGlobal();
    }

    private void cargaCitasGlobal() {
        listaCitas.clear();
        String sql = "SELECT c.NCita, c.fecha, e.tipo " +
                "FROM Cita c " +
                "JOIN Especialidad e ON c.fk_idE_Especialidad = e.idE " +
                "WHERE c.fk_emailID_Paciente = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pac.getEmailID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listaCitas.add(new Cita(rs.getInt("NCita"), rs.getDate("fecha").toLocalDate(), rs.getString("tipo")));
            }
            tableCitas.setItems(listaCitas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void nuevaCita(ActionEvent event) {
        Especialidad esp = cmbEspecialidad.getValue();
        LocalDate fecha = dpFechCita.getValue();
        if (esp == null || fecha == null) {
            System.out.println("Debe seleccionar fecha y especialidad.");
            return;
        }

        String sql = "INSERT INTO CITA (fecha, fk_idE_Especialidad, fk_emailID_Paciente) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setInt(2, esp.getId());
            ps.setString(3, pac.getEmailID());
            ps.executeUpdate();
            cargaCitasGlobal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void borrarCita(ActionEvent event) {
        Cita c = tableCitas.getSelectionModel().getSelectedItem();
        if (c == null) {
            System.out.println("Seleccione una cita para borrar");
            return;
        }

        String sql = "DELETE FROM CITA WHERE NCITA=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getNCita());
            ps.executeUpdate();
            cargaCitasGlobal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void modificarCita(ActionEvent event) {
        Cita c = tableCitas.getSelectionModel().getSelectedItem();
        if (c == null) {
            System.out.println("Seleccione una cita para modificar");
            return;
        }

        LocalDate nuevaFecha = dpFechCita.getValue();
        Especialidad esp = cmbEspecialidad.getValue();
        if (nuevaFecha == null || esp == null) {
            System.out.println("Seleccione fecha y especialidad para modificar");
            return;
        }

        String sql = "UPDATE CITA SET FECHA = ?, fk_idE_Especialidad = ? WHERE NCITA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(nuevaFecha));
            ps.setInt(2, esp.getId());
            ps.setInt(3, c.getNCita());
            ps.executeUpdate();
            cargaCitasGlobal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
