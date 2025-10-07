package org.example.practica1medico.Controller;

import org.example.practica1medico.Model.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.practica1medico.UTIL.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtEmailID;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    @FXML
    private void Login(ActionEvent event) {

        String email = txtEmailID.getText().trim();
        String pass = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            mostrarAlerta("No se pudo conectar con la base de datos.");
            return;
        }

        String sql = """
                SELECT emailID, DNI, nombre, direccion, telefono
                FROM Paciente
                WHERE emailID = ? AND pass = SHA2(?, 256)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = new Paciente(
                            rs.getString("emailID"),
                            rs.getString("DNI"),
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("telefono")
                    );
                    abrirFormularioPrincipal(paciente);
                } else {
                    mostrarAlerta("Credenciales incorrectas. Intente de nuevo.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la conexión");
        }
    }

    private void abrirFormularioPrincipal(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Formulario.fxml"));
            Scene scene = new Scene(loader.load());

            FormularioController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage stage = new Stage();
            stage.setTitle("Formulario del Paciente");
            stage.setScene(scene);
            stage.show();

            //cierra ventana de login
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar la ventana del formulario.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
