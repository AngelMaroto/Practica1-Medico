package org.example.practica1medico.Controller;

import com.mysql.cj.protocol.Resultset;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML
    public void initialize(){
        colNumero.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getNCita()).asObject());
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getFecha()));
        colEspecialidad.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getEspecialidad()));


    }

    public void setPaciente(Paciente p){
        this.pac = p;
        txtDni.setText(p.getDni());
        txtNombre.setText(p.getNombre());
        txtDireccion.setText(p.getDireccion());
        txtTelefono.setText(p.getTelefono());
    }

    private void cargaEspecialidad(){
        cmbEspecialidad.getItems().clear();
        try(Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ESPECIALIDAD")){
            while(rs.next()){
                cmbEspecialidad.getItems().add(new Especialidad(rs.getInt("idE"),rs.getString("tipo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargaCitas(){
        listaCitas.clear();
        String sql = "SELECT c.NCita, c.fecha, e.tipo " +
                "FROM Cita c " +
                "JOIN Especialidad e ON c.fk_idE_Especialidad = e.idE " +
                "WHERE c.fk_emailID_Paciente = ?;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, pac.getEmailID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                listaCitas.add(new Cita(rs.getDate("fecha").toLocalDate(),rs.getString("tipo")));
            }
            tableCitas.setItems(listaCitas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void nuevaCita(){
        Especialidad esp = cmbEspecialidad.getValue();
        LocalDate fecha = dpFechCita.getValue();
        if (esp == null || fecha == null){
            System.out.println("Debe seleccionar fehca y especialidad.");
            return;
        }
        String sql = "INSERT INTO CITA (fecha,fk_idE_Especialidad, fk_emailID_Paciente)" +
                "VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDate(1,Date.valueOf(fecha));
            ps.setInt(2,esp.getId());
            ps.setString(3, pac.getEmailID());
            ps.executeUpdate();
            cargaCitas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void borrarCita(){
        Cita c = tableCitas.getSelectionModel().getSelectedItem();
        if (c==null){
            System.out.println("Seleccione una cita para borrar");
            return;
        }
        String sql = "DELETE FROM CITA WHERE NCITA=?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, c.getNCita());
            ps.executeUpdate();
            cargaCitas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificarCita(){
        Cita c = tableCitas.getSelectionModel().getSelectedItem();
        if (c==null){
            System.out.println("seleccione una cita para modificar");
            return;
        }
        LocalDate nuevaFecha = dpFechCita.getValue();
        Especialidad esp = cmbEspecialidad.getValue();

        if (nuevaFecha == null || esp == null){
            System.out.println("selecciona fecha y especialidad para modificar");
            return;
        }
        String sql  = "UPDATE CITA SET FECHA = ?, fk_idE_Especialidad = ? WHERE NCITA = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(nuevaFecha));
            ps.setInt(2, esp.getId());
            ps.setInt(3,c.getNCita());
            ps.executeUpdate();
            cargaCitas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
