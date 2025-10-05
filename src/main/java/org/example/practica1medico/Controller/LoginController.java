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
import java.sql.*;

public class LoginController {

    @FXML private TextField txtEmailID;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;


    @FXML
    private void handleLogin(ActionEvent event) {
        String email = txtEmailID.getText().trim();
        String pass = txtPassword.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                mostrarAlerta("No se pudo conectar con la base de datos.");
                return;
            }

            // Comprobamos si el email y la contraseña (SHA2) son correctos
            String sql = """
                    SELECT emailID, DNI, nombre, direccion, telefono
                    FROM Paciente
                    WHERE emailID = ? AND pass = SHA2(?, 256)
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Si encuentra al paciente, creamos el objeto Paciente
                    Paciente paciente = new Paciente(
                            rs.getString("emailID"),
                            rs.getString("DNI"),
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("telefono")
                    );

                    // Cargar formulario principal
                    abrirFormularioPrincipal(paciente);
                } else {
                    mostrarAlerta("Credenciales incorrectas. Intente de nuevo.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la conexión o consulta.");
        }
    }

    private void abrirFormularioPrincipal(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Formulario.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtenemos el controlador del formulario y le pasamos el paciente
            FormularioController controller = loader.getController();
            controller.setPaciente(paciente);

            Stage stage = new Stage();
            stage.setTitle("Centro Médico - Formulario del Paciente");
            stage.setScene(scene);
            stage.show();

            // Cerramos la ventana de login
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


