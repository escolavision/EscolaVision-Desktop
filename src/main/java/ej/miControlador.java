package ej;

import ej.DAO.GenericDAO;
import ej.DAO.TablasDAO.*;
import ej.Tablas.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

import static java.lang.System.exit;

public class miControlador implements Initializable {

    @FXML
    private Button btnClearTest, btnClearQuest, btnClearAlum, btnClearProf, btnClearTry, btnLogo,
            btnClearAr, btnDelTest, btnDelQuest, btnDelAlum, btnDelProf, btnDelTry, btnDelAr,
            btnLogin, btnSaveTest, btnSaveQuest, btnSaveAlum, btnSaveProf, btnSaveTry, btnSaveAr,
            btnClearPxA, btnDelPxA, btnSavePxA, btnEditarTest, btnEditarPreguntas, btnEditarAlumnos,
            btnEditarProfesores, btnEditarArea, btnEditarPxA, btnEditarIntentos;

    @FXML
    private ListView<String> listViewAlumnos, listViewArea, listViewIntentos, listViewPreguntas,
            listViewProfesores, listViewPxa, listViewTest;

    @FXML
    private CheckBox chkProf;

    @FXML
    private ImageView imglogo, imgViewFoto1, imgViewPicAlum, imgViewPicArea, imageViewLimpiar;

    @FXML
    private Tab tabAlum, tabAr, tabHome, tabProf, tabPxA, tabQuest, tabTest, tabTr;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField txtDNIAlm, txtDNIProf, txtIdAlm, txtIdArea, txtTestTry,
            txtIdProf, txtIdQuest, txtIdTest, txtIdTry, txtNameAlm, txtNameArea, txtNameProf, txtResTry,
            txtSurnameAlm, txtSurnameProf, txtTestName, txtTimeTry, txtUser, txtIdPxA, txtTestQuest;

    @FXML
    private TextArea txtDescripArea, txtEnunQuest, txtEnumPxA;

    @FXML
    private PasswordField txtPassword, txtPasswordAlm, txtPasswordProf;

    @FXML
    private ComboBox<String> cbAreaProf, cbAreaPxA, cbDNITry, cbDNIProfAlm, cbIdQuestPxA;

    @FXML
    private DatePicker txtDateTry;

    @FXML
    private VBox vboxQuest, vboxTry;

    private AlumnoDAO alumnoDAO;
    private TestDAO testDAO;
    private PreguntaDAO preguntaDAO;
    private ProfesorDAO profesorDAO;
    private AreaDAO areaDAO;
    private PxADAO pxaDAO;
    private IntentosDAO intentosDAO;
    private final Configuracion configuracion = new Configuracion();
    private Connection conexion;
    private final Map<String, ListView<String>> listViewMap = new HashMap<>();
    private final ListView<String> listViewDesplegableQuest = new ListView<>();
    private final ListView<String> listViewDesplegableTry = new ListView<>();
    private Map<Tab, Button> tabToButtonMap = new HashMap<>();

    public miControlador() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarMap();
        inicializarConexion();
        inicializarLogin();
        inicializarDAO();
        inicializarTabToButtonMap();
    }

    private void inicializarTabToButtonMap() {
        tabToButtonMap.put(tabTest, btnEditarTest);
        tabToButtonMap.put(tabQuest, btnEditarPreguntas);
        tabToButtonMap.put(tabAlum, btnEditarAlumnos);
        tabToButtonMap.put(tabProf, btnEditarProfesores);
        tabToButtonMap.put(tabAr, btnEditarArea);
        tabToButtonMap.put(tabPxA, btnEditarPxA);
        tabToButtonMap.put(tabTr, btnEditarIntentos);
    }
    private void inicializarMap() {
        listViewMap.put("alumno", listViewAlumnos);
        listViewMap.put("profesor", listViewProfesores);
        listViewMap.put("area", listViewArea);
        listViewMap.put("intentos", listViewIntentos);
        listViewMap.put("pxa", listViewPxa);
        listViewMap.put("pregunta", listViewPreguntas);
        listViewMap.put("test", listViewTest);
    }

    private void inicializarConexion() {
        try {
            conexion = getConnection();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void inicializarLogin() {
        if (conexion != null) {
            imglogo.setImage(new Image("escolavision.png"));
            pantallaPrincipal();
            inicializarEventos();
        }
    }

    private void inicializarDAO() {
        this.alumnoDAO = new AlumnoDAO(conexion);
        this.testDAO = new TestDAO(conexion);
        this.preguntaDAO = new PreguntaDAO(conexion);
        this.profesorDAO = new ProfesorDAO(conexion);
        this.areaDAO = new AreaDAO(conexion);
        this.pxaDAO = new PxADAO(conexion);
        this.intentosDAO = new IntentosDAO(conexion);
    }

    private void pantallaPrincipal() {
        tabPane.getTabs().remove(tabAlum);
        tabPane.getTabs().remove(tabAr);
        tabPane.getTabs().remove(tabProf);
        tabPane.getTabs().remove(tabQuest);
        tabPane.getTabs().remove(tabPxA);
        tabPane.getTabs().remove(tabTest);
        tabPane.getTabs().remove(tabTr);
        btnLogo.setVisible(false);
        btnLogo.setManaged(false);
    }

    private void rolAdmin() {
        tabPane.getTabs().add(tabTest);
        tabPane.getTabs().add(tabQuest);
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabProf);
        tabPane.getTabs().add(tabAr);
        tabPane.getTabs().add(tabPxA);
        tabPane.getTabs().add(tabTr);
        tabPane.getTabs().remove(tabHome);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
    }

    private void rolAlumno() {
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabPxA);
        tabPane.getTabs().add(tabTr);
        tabPane.getTabs().remove(tabHome);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
    }

    private void rolProfesor() {
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabProf);
        tabPane.getTabs().add(tabPxA);
        tabPane.getTabs().add(tabTr);
        tabPane.getTabs().remove(tabHome);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
    }

    private void inicializarEventos() {

        btnLogin.setOnAction(e -> login());

        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnLogin.fire();
            }
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                Button botonEditar = tabToButtonMap.get(oldTab);
                // cambio tap a tap

                /*if (botonEditar != null && botonEditar.getText().equals("Cancelar")) {
                    tabPane.getSelectionModel().select(oldTab);

                    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacion.setTitle("Confirmación de cambio de pestaña");
                    confirmacion.setHeaderText("¿Está seguro de que desea cambiar de pestaña?");
                    confirmacion.setContentText("Se perderán todos los datos no guardados.");

                    Optional<ButtonType> resultado = confirmacion.showAndWait();

                    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                            tabPane.getSelectionModel().select(newTab);
                            limpiar(newTab);
                            cargar(newTab);
                            cargarComboBox(newTab);
                    }
                } else {*/
                    limpiar(newTab);
                    cargar(newTab);
                    cargarComboBox(newTab);
                //}
            }
        });




        cbIdQuestPxA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (!newValue.trim().isEmpty()) {
                    Pregunta pregunta = buscarPreguntaPorId(Integer.parseInt(newValue));
                    if (pregunta != null) {
                        txtEnumPxA.setText(pregunta.getEnunciado());
                    }
                }
            }
        });


        listViewMap.forEach((tipo, listView) -> listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                comprobarEditar(tipo);
                switch (tipo) {
                    case "test": {
                        cambiarBotonEditar(btnEditarTest, "Editar", "test");
                        cargarTest(newValue);
                        break;
                    }
                    case "pregunta": {
                        cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
                        cargarPregunta(newValue);
                        break;
                    }
                    case "alumno": {
                        cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
                        cargarAlumno(newValue);
                        break;
                    }
                    case "profesor": {
                        cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
                        cargarProfesor(newValue);
                        break;
                    }
                    case "area": {
                        cambiarBotonEditar(btnEditarArea, "Editar", "area");
                        cargarArea(newValue);
                        break;
                    }
                    case "pxa": {
                        cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
                        cargarPxA(newValue);
                        break;
                    }
                    case "intentos": {
                        cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
                        cargarIntento(newValue);
                        break;
                    }
                    default: {
                    }
                }
            }
            listViewDesplegableQuest.setVisible(false);
            listViewDesplegableTry.setVisible(false);
        }));


        btnEditarTest.setOnAction(e -> {
            if (btnEditarTest.getText().equals("Editar") || btnEditarTest.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarTest, "Cancelar", "test");
            } else {
                cambiarBotonEditar(btnEditarTest, "Editar", "test");
                if(listViewTest.getSelectionModel().getSelectedItem() != null) {
                    cargarTest(listViewTest.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarPreguntas.setOnAction(e -> {
            if (btnEditarPreguntas.getText().equals("Editar") || btnEditarPreguntas.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarPreguntas, "Cancelar", "pregunta");
            } else {
                cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
                if(listViewPreguntas.getSelectionModel().getSelectedItem() != null) {
                    cargarPregunta(listViewPreguntas.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarAlumnos.setOnAction(e -> {
            if (btnEditarAlumnos.getText().equals("Editar") || btnEditarAlumnos.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarAlumnos, "Cancelar", "alumno");
            } else {
                cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
                if(listViewAlumnos.getSelectionModel().getSelectedItem() != null) {
                    cargarAlumno(listViewAlumnos.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarProfesores.setOnAction(e -> {
            if (btnEditarProfesores.getText().equals("Editar") || btnEditarProfesores.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarProfesores, "Cancelar", "profesor");
            } else {
                cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
                if(listViewProfesores.getSelectionModel().getSelectedItem() != null){
                    cargarProfesor(listViewProfesores.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarArea.setOnAction(e -> {
            if (btnEditarArea.getText().equals("Editar") || btnEditarArea.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarArea, "Cancelar", "area");
            } else {
                cambiarBotonEditar(btnEditarArea, "Editar", "area");
                if(listViewArea.getSelectionModel().getSelectedItem() != null) {
                    cargarArea(listViewArea.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarPxA.setOnAction(e -> {
            if (btnEditarPxA.getText().equals("Editar") || btnEditarPxA.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarPxA, "Cancelar", "pxa");
            } else {
                cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
                if(listViewPxa.getSelectionModel().getSelectedItem() != null){
                    cargarPxA(listViewPxa.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarIntentos.setOnAction(e -> {
            if (btnEditarIntentos.getText().equals("Editar") || btnEditarIntentos.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarIntentos, "Cancelar", "intentos");
            } else {
                cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
                if(listViewIntentos.getSelectionModel().getSelectedItem() != null) {
                    cargarIntento(listViewIntentos.getSelectionModel().getSelectedItem());
                }
            }
        });


        // LIMPIAR

        btnClearTest.setOnAction(e -> {
            limpiar(tabTest);
            cambiarBotonEditar(btnEditarTest, "Editar", "test");
            txtTestName.setEditable(true);
        });

        btnClearQuest.setOnAction(e -> {
            limpiar(tabQuest);
            cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
            txtTestQuest.setEditable(true);
            txtEnunQuest.setEditable(true);
        });

        btnClearAlum.setOnAction(e -> {
            limpiar(tabAlum);
            cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
            txtNameAlm.setEditable(true);
            txtSurnameAlm.setEditable(true);
            txtDNIAlm.setEditable(true);
            cbDNIProfAlm.setMouseTransparent(false);
            txtPasswordAlm.setEditable(true);
            habilitarArrastrarYSoltar();
            agregarEventListenersParaSeleccionarImagen();
        });

        btnClearProf.setOnAction(e -> {
            limpiar(tabProf);
            cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
            txtNameProf.setEditable(true);
            txtSurnameProf.setEditable(true);
            txtDNIProf.setEditable(true);
            txtPasswordProf.setEditable(true);
            cbAreaProf.setMouseTransparent(false);
        });

        btnClearAr.setOnAction(e -> {
            limpiar(tabAr);
            cambiarBotonEditar(btnEditarArea, "Editar", "area");
            txtNameArea.setEditable(true);
            txtDescripArea.setEditable(true);
        });

        btnClearPxA.setOnAction(e -> {
            limpiar(tabPxA);
            cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
            cbAreaPxA.setMouseTransparent(false);
            cbIdQuestPxA.setMouseTransparent(false);
        });

        btnClearTry.setOnAction(e -> {
            limpiar(tabTr);
            cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
            txtTestTry.setEditable(true);
            cbDNITry.setMouseTransparent(false);
            txtDateTry.setEditable(true);
            txtTimeTry.setEditable(true);
            txtResTry.setEditable(true);
        });

        // ELIMINAR

        btnDelTest.setOnAction(e -> {
            if (!txtIdTest.getText().isEmpty()) borrar("test", txtIdTest.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un test.");

        });

        btnDelQuest.setOnAction(e -> {
            if (!txtIdQuest.getText().isEmpty()) borrar("pregunta", txtIdQuest.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar una pregunta.");
        });

        btnDelAlum.setOnAction(e -> {
            if (!txtIdAlm.getText().isEmpty()) borrar("alumno", txtIdAlm.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un alumno.");
        });

        btnDelProf.setOnAction(e -> {
            if (!txtIdProf.getText().isEmpty()) borrar("profesor", txtIdProf.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un profesor.");
        });

        btnDelAr.setOnAction(e -> {
            if (!txtIdArea.getText().isEmpty()) borrar("area", txtIdArea.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un area.");
        });

        btnDelPxA.setOnAction(e -> {
            if (!txtIdPxA.getText().isEmpty()) borrar("pxa", txtIdPxA.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un PxA.");
        });

        btnDelTry.setOnAction(e -> {
            if (!txtIdTry.getText().isEmpty()) borrar("intentos", txtIdTry.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar", "Debe seleccionar un intento.");
        });


        ValidationSupport vSnombreTest = new ValidationSupport();
        vSnombreTest.registerValidator(txtTestName, false, Validator.createEmptyValidator("El nombre no puede estar vacío"));
        //vSnombreTest.redecorate();

        ValidationSupport vSPregunta = new ValidationSupport();
        vSPregunta.registerValidator(txtEnunQuest,false,  Validator.createEmptyValidator("El enunciado no puede estar vacío"));
        vSPregunta.registerValidator(txtTestQuest, false, Validator.createEmptyValidator("Debe seleccionar un test"));

        ValidationSupport vSAlumno = new ValidationSupport();
        vSAlumno.registerValidator(txtNameAlm,false,  Validator.createEmptyValidator("El nombre no puede estar vacío")); // Jueves 28
        vSAlumno.registerValidator(txtSurnameAlm, false, Validator.createEmptyValidator("El apellido no puede estar vacío"));
        vSAlumno.registerValidator(cbDNIProfAlm, false, Validator.createEmptyValidator("Debe seleccionar un DNI"));
        vSAlumno.registerValidator(txtDNIAlm, false, Validator.createRegexValidator("El formato del DNI es inválido", "^\\d{8}[A-Za-z]$", Severity.ERROR));
        vSAlumno.registerValidator(txtPasswordAlm,false,  Validator.createRegexValidator("""
                La contraseña debe tener al menos 8 caracteres,
                incluyendo una mayúscula, una minúscula,
                un número y un carácter especial
                (@ $ ! % * ? & . - _ #).
                """, "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&\\.\\-\\_\\#])[A-Za-z\\d@$!%*?&\\.\\-\\_\\#]{8,}$", Severity.ERROR));
        //vSAlumno.redecorate();

        ValidationSupport vSProfesor = new ValidationSupport();
        vSProfesor.registerValidator(txtNameProf,false,  Validator.createEmptyValidator("El nombre no puede estar vacío"));
        vSProfesor.registerValidator(txtSurnameProf,false,  Validator.createEmptyValidator("El apellido no puede estar vacío"));
        vSProfesor.registerValidator(cbAreaProf,false,  Validator.createEmptyValidator("Debe seleccionar un Area"));
        vSProfesor.registerValidator(txtDNIProf, false, Validator.createRegexValidator("El formato del DNI es inválido", "^\\d{8}[A-Za-z]$", Severity.ERROR));
        vSProfesor.registerValidator(txtPasswordProf, false, Validator.createRegexValidator("""
                	La contraseña debe tener al menos 8 caracteres,
                	incluyendo una mayúscula, una minúscula,
                un número y un carácter especial
                (@ $ ! % * ? & . - _ #).
                """, "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&\\.\\-\\_\\#])[A-Za-z\\d@$!%*?&\\.\\-\\_\\#]{8,}$", Severity.ERROR));

        ValidationSupport vSArea = new ValidationSupport();
        vSArea.registerValidator(txtNameArea,false,  Validator.createEmptyValidator("El nombre no puede estar vacío"));
        vSArea.registerValidator(txtDescripArea,false,  Validator.createEmptyValidator("La descripción no puede estar vacía"));

        ValidationSupport vSPxA = new ValidationSupport();
        vSPxA.registerValidator(cbAreaPxA, false, Validator.createEmptyValidator("Debe seleccionar un area"));
        vSPxA.registerValidator(cbIdQuestPxA,false,  Validator.createEmptyValidator("Debe seleccionar una pregunta"));

        ValidationSupport vSIntentos = new ValidationSupport();
        vSIntentos.registerValidator(txtTestTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));
        vSIntentos.registerValidator(cbDNITry,false,  Validator.createEmptyValidator("Debe seleccionar un alumno"));
        vSIntentos.registerValidator(txtDateTry, false, Validator.createEmptyValidator("El formato de la fecha es incorrect"));
		/*
		vSProfesor.registerValidator(txtDateTry, Validator.createRegexValidator("""
			Formato de fecha inválido. Use yyyy-MM-dd
		""", "^(\\d{4})-(\\d{2})-(\\d{2})$", Severity.ERROR));
		 */
        vSIntentos.registerValidator(txtTimeTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));
        vSIntentos.registerValidator(txtResTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));

        // INSERTAR
        btnSaveTest.setOnAction(e -> {
            ValidationResult resultado = vSnombreTest.getValidationResult();
            Collection<ValidationMessage> errores = resultado.getErrors();

            if (!errores.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR,"Error", "Validación Fallida", "Por favor, corrige los errores antes de guardar.");

                ValidationMessage primerError = errores.iterator().next();
                if (primerError.getTarget() instanceof Control control) {
                    control.requestFocus();
                }
            } else {
                insertarYActualizar("test");
                btnClearTest.fire();
            }
        });


        btnSaveQuest.setOnAction(e -> {
            ValidationResult resultado = vSPregunta.getValidationResult();
            Collection<ValidationMessage> errores = resultado.getErrors();

            if (!errores.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText("Validación Fallida");
                alerta.setContentText("Por favor, corrige los errores antes de guardar.");
                alerta.showAndWait();

                ValidationMessage primerError = errores.iterator().next();
                if (primerError.getTarget() instanceof Control control) {
                    control.requestFocus();
                }
            } else {
                insertarYActualizar("pregunta");
                btnClearQuest.fire();
            }
        });

        btnSaveAlum.setOnAction(e -> {
            insertarYActualizar("alumno");
            btnClearAlum.fire();
        });

        btnSaveProf.setOnAction(e -> {
            insertarYActualizar("profesor");
            btnClearProf.fire();
        });

        btnSaveAr.setOnAction(e -> {
            insertarYActualizar("area");
            btnClearAr.fire();
        });

        btnSavePxA.setOnAction(e -> {
            insertarYActualizar("pxa");
            btnClearPxA.fire();
        });

        btnSaveTry.setOnAction(e -> {
            insertarYActualizar("intentos");
            btnClearTry.fire();
        });

        ajustarImagenes();
        habilitarArrastrarYSoltar();
        agregarEventListenersParaSeleccionarImagen();


        Platform.runLater(() -> {
            vSnombreTest.initInitialDecoration();
            vSPregunta.initInitialDecoration();
            //vSAlumno.initInitialDecoration();
            //vSProfesor.initInitialDecoration();
            vSArea.initInitialDecoration();
            //vSPxA.initInitialDecoration();
            //vSIntentos.initInitialDecoration();
        });
    }


    public void cambiarBotonEditar(Button boton, String textobtn, String tab) {
        boton.setText(textobtn);
        comprobarEditar(tab);
    }

    private void cargarTest(String newValue) {
        Test test = buscarTestPorNombre(newValue);
        txtIdTest.setText("" + test.getId());
        txtTestName.setText(test.getNombre());
    }

    private void cargarPregunta(String newValue) {
        Pregunta pregunta = buscarPreguntaPorId(Integer.parseInt(newValue));
        txtIdQuest.setText("" + pregunta.getId());
        txtTestQuest.setText(pregunta.getTest().getNombre());
        txtEnunQuest.setText(pregunta.getEnunciado());
    }

    private void cargarAlumno(String newValue) {
        Alumno alumno = buscarAlumnoPorDni(newValue);
        txtIdAlm.setText("" + alumno.getId());
        txtNameAlm.setText(alumno.getNombre());
        txtSurnameAlm.setText(alumno.getApellidos());
        txtDNIAlm.setText(alumno.getDni());
        cbDNIProfAlm.setValue(alumno.getProfesor().getDni());
        txtPasswordAlm.setText(alumno.getClaveaccesoalumno());
        if (!Objects.equals(alumno.getFoto(), "")) {
            imgViewPicAlum.setImage(base64ToImage(alumno.getFoto()));
        } else {
            imgViewPicAlum.setImage(null);
        }
    }

    private void cargarProfesor(String newValue) {
        Profesor profesor = buscarProfesorPorDni(newValue);
        txtIdProf.setText("" + profesor.getId());
        txtNameProf.setText(profesor.getNombre());
        txtSurnameProf.setText(profesor.getApellidos());
        txtDNIProf.setText(profesor.getDni());
        txtPasswordProf.setText(profesor.getClaveaccesoprof());
        cbAreaProf.setValue(profesor.getArea().getNombre());
        if (!Objects.equals(profesor.getFoto(), "")) {
            imgViewFoto1.setImage(base64ToImage(profesor.getFoto()));
        } else {
            imgViewFoto1.setImage(null);
        }
    }

    private void cargarArea(String newValue) {
        Area area = buscarAreaPorNombre(newValue);
        txtNameArea.setText(area.getNombre());
        txtDescripArea.setText(area.getDescripcion());
        txtIdArea.setText("" + area.getId());
        if (!Objects.equals(area.getLogo(), "")) {
            imgViewPicArea.setImage(base64ToImage(area.getLogo()));
        } else {
            imgViewPicArea.setImage(null);
        }
    }

    private void cargarPxA(String newValue) {
        PxA pxa = buscarPxAPorId(Integer.parseInt(newValue));
        txtIdPxA.setText("" + pxa.getId());
        cbAreaPxA.setValue(pxa.getArea().getNombre());
        cbIdQuestPxA.setValue("" + pxa.getPregunta().getId());
        txtEnumPxA.setText(pxa.getPregunta().getEnunciado());
    }

    private void cargarIntento(String newValue) {
        Intentos intento = buscarIntentoPorId(Integer.parseInt(newValue));
        txtIdTry.setText("" + intento.getId());
        txtTestTry.setText(intento.getTest().getNombre());
        cbDNITry.setValue(intento.getAlumno().getDni());
        txtDateTry.setValue(intento.getFecha());
        txtTimeTry.setText(intento.getHora());
        txtResTry.setText(intento.getResultados());
    }

    private void borrar(String tipo, String id) {
        GenericDAO dao = getDAOForType(tipo);
        Tab tab = getTabByTipo(tipo);

        if (dao != null && tab != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar este elemento?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                if (dao.delete(Integer.parseInt(id))) {
                    limpiar(tab);
                    cargar(tab);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al borrar",
                            "No se ha podido borrar el elemento seleccionado. Compruebe que no está siendo usado en otro registro.");
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error","Error", "Tipo desconocido: " + tipo);
        }
    }


    private GenericDAO getDAOForType(String tipo) {
        return switch (tipo) {
            case "test" -> testDAO;
            case "pregunta" -> preguntaDAO;
            case "alumno" -> alumnoDAO;
            case "profesor" -> profesorDAO;
            case "area" -> areaDAO;
            case "pxa" -> pxaDAO;
            case "intentos" -> intentosDAO;
            default -> null;
        };
    }


    private void insertarYActualizar(String tipo) {
        boolean isUpdate = false;
        boolean resultado;

        switch (tipo) {
            case "test": {
                Test t1 = new Test();
                t1.setNombre(txtTestName.getText());

                if (!txtIdTest.getText().isEmpty()) {
                    t1.setId(Integer.parseInt(txtIdTest.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? testDAO.update(t1) : testDAO.insert(t1);
                break;
            }
            case "pregunta": {
                Pregunta p1 = new Pregunta();
                p1.setTest(buscarTestPorNombre(txtTestQuest.getText()));
                p1.setEnunciado(txtEnunQuest.getText());

                if (!txtIdQuest.getText().isEmpty()) {
                    p1.setId(Integer.parseInt(txtIdQuest.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? preguntaDAO.update(p1) : preguntaDAO.insert(p1);
                break;
            }
            case "alumno": {
                Alumno a1 = new Alumno();
                a1.setNombre(txtNameAlm.getText());
                a1.setApellidos(txtSurnameAlm.getText());
                a1.setDni(txtDNIAlm.getText());
                a1.setProfesor(buscarProfesorPorDni(cbDNIProfAlm.getSelectionModel().getSelectedItem()));
                a1.setClaveaccesoalumno(txtPasswordAlm.getText());
                if (imgViewPicAlum.getImage() != null) {
                    String base64Image = imageToBase64(imgViewPicAlum.getImage());
                    a1.setFoto(base64Image);
                }
                if (!txtIdAlm.getText().isEmpty()) {
                    a1.setId(Integer.parseInt(txtIdAlm.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? alumnoDAO.update(a1) : alumnoDAO.insert(a1);
                break;
            }
            case "profesor": {
                Profesor p1 = new Profesor();
                p1.setNombre(txtNameProf.getText());
                p1.setApellidos(txtSurnameProf.getText());
                p1.setDni(txtDNIProf.getText());
                p1.setArea(buscarAreaPorNombre(cbAreaProf.getSelectionModel().getSelectedItem()));
                p1.setClaveaccesoprof(txtPasswordProf.getText());
                if (imgViewFoto1.getImage() != null) {
                    String base64Image = imageToBase64(imgViewFoto1.getImage());
                    p1.setFoto(base64Image);
                }

                if (!txtIdProf.getText().isEmpty()) {
                    p1.setId(Integer.parseInt(txtIdProf.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? profesorDAO.update(p1) : profesorDAO.insert(p1);
                break;
            }
            case "area": {
                Area a1 = new Area();
                a1.setNombre(txtNameArea.getText());
                a1.setDescripcion(txtDescripArea.getText());
                if (imgViewPicArea.getImage() != null) {
                    String base64Image = imageToBase64(imgViewPicArea.getImage());
                    a1.setLogo(base64Image);
                }
                if (!txtIdArea.getText().isEmpty()) {
                    a1.setId(Integer.parseInt(txtIdArea.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? areaDAO.update(a1) : areaDAO.insert(a1);
                break;
            }
            case "pxa": {
                PxA p1 = new PxA();
                p1.setArea(buscarAreaPorNombre(cbAreaPxA.getSelectionModel().getSelectedItem()));
                p1.setPregunta(buscarPreguntaPorId(Integer.parseInt(cbIdQuestPxA.getSelectionModel().getSelectedItem())));

                if (!txtIdPxA.getText().isEmpty()) {
                    p1.setId(Integer.parseInt(txtIdPxA.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? pxaDAO.update(p1) : pxaDAO.insert(p1);
                break;
            }
            case "intentos": {
                Intentos i1 = new Intentos();
                i1.setAlumno(buscarAlumnoPorDni(cbDNITry.getSelectionModel().getSelectedItem()));
                i1.setTest(buscarTestPorNombre(txtTestTry.getText()));
                i1.setFecha(String.valueOf(txtDateTry.getValue()));
                i1.setHora(txtTimeTry.getText());
                i1.setResultados(txtResTry.getText());

                if (!txtIdTry.getText().isEmpty()) {
                    i1.setId(Integer.parseInt(txtIdTry.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? intentosDAO.update(i1) : intentosDAO.insert(i1);
                break;
            }
            default: {
                mostrarAlerta(Alert.AlertType.WARNING, "Warning","Operación no válida", "El tipo especificado no es válido.");
                return;
            }
        }

        if (resultado) {
            limpiar(getTabByTipo(tipo));
            cargar(getTabByTipo(tipo));
        } else {
            String operacion = isUpdate ? "actualizar" : "insertar";
            mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al " + operacion, "No se ha podido " + operacion + " el elemento seleccionado.");
        }
    }

    private Tab getTabByTipo(String tipo) {
        return switch (tipo) {
            case "test" -> tabTest;
            case "pregunta" -> tabQuest;
            case "alumno" -> tabAlum;
            case "profesor" -> tabProf;
            case "area" -> tabAr;
            case "pxa" -> tabPxA;
            case "intentos" -> tabTr;
            default -> null;
        };
    }

    private void comprobarEditar(String tipo) {
        switch (tipo) {
            case "test": {
                txtTestName.setEditable(btnEditarTest.getText().equals("Cancelar"));
                break;
            }
            case "pregunta": {
                boolean editable = btnEditarPreguntas.getText().equals("Cancelar");
                txtTestQuest.setEditable(editable);
                txtEnunQuest.setEditable(editable);
                break;
            }
            case "alumno": {
                boolean editable = btnEditarAlumnos.getText().equals("Cancelar");
                txtNameAlm.setEditable(editable);
                txtSurnameAlm.setEditable(editable);
                txtDNIAlm.setEditable(editable);
                cbDNIProfAlm.setMouseTransparent(!editable);
                txtPasswordAlm.setEditable(editable);
                if (editable) {
                    habilitarArrastrarYSoltar();
                    agregarEventListenersParaSeleccionarImagen();
                } else {
                    deshabilitarArrastrarYSoltar();
                    quitarEventListenersParaSeleccionarImagen();
                }
                break;
            }
            case "profesor": {
                boolean editable = btnEditarProfesores.getText().equals("Cancelar");
                txtNameProf.setEditable(editable);
                txtSurnameProf.setEditable(editable);
                txtDNIProf.setEditable(editable);
                txtPasswordProf.setEditable(editable);
                cbAreaProf.setMouseTransparent(!editable);
                if (editable) {
                    habilitarArrastrarYSoltar();
                    agregarEventListenersParaSeleccionarImagen();
                } else {
                    deshabilitarArrastrarYSoltar();
                    quitarEventListenersParaSeleccionarImagen();
                }
                break;
            }
            case "area": {
                boolean editable = btnEditarArea.getText().equals("Cancelar");
                txtNameArea.setEditable(editable);
                txtDescripArea.setEditable(editable);
                if (editable) {
                    habilitarArrastrarYSoltar();
                    agregarEventListenersParaSeleccionarImagen();
                } else {
                    deshabilitarArrastrarYSoltar();
                    quitarEventListenersParaSeleccionarImagen();
                }
                break;
            }
            case "pxa": {
                boolean editable = btnEditarPxA.getText().equals("Cancelar");
                cbAreaPxA.setMouseTransparent(!editable);
                cbIdQuestPxA.setMouseTransparent(!editable);
                break;
            }
            case "intentos": {
                boolean editable = btnEditarIntentos.getText().equals("Cancelar");
                txtTestTry.setEditable(editable);
                cbDNITry.setMouseTransparent(!editable);
                txtDateTry.setEditable(editable);
                txtTimeTry.setEditable(editable);
                txtResTry.setEditable(editable);
                break;
            }
            default: {
            }
        }
    }

    private void deshabilitarArrastrarYSoltar() {
        deshabilitarArrastreImagen(imgViewPicAlum);
        deshabilitarArrastreImagen(imgViewPicArea);
        deshabilitarArrastreImagen(imgViewFoto1);
    }

    private void deshabilitarArrastreImagen(ImageView imageView) {
        imageView.setOnDragOver(null);
        imageView.setOnDragDropped(null);
    }

    private void quitarEventListenersParaSeleccionarImagen() {
        imgViewPicAlum.setOnMouseClicked(null);
        imgViewPicArea.setOnMouseClicked(null);
        imgViewFoto1.setOnMouseClicked(null);
    }


    private void ajustarImagenes() {
        imgViewPicAlum.setFitHeight(67);
        imgViewPicAlum.setFitWidth(74);
        imgViewPicArea.setFitHeight(67);
        imgViewPicArea.setFitWidth(74);
        imgViewFoto1.setFitHeight(67);
        imgViewFoto1.setFitWidth(74);

        imgViewPicAlum.setPreserveRatio(true);
        imgViewPicArea.setPreserveRatio(true);
        imgViewFoto1.setPreserveRatio(true);
    }


    private void agregarEventListenersParaSeleccionarImagen() {
        imgViewPicAlum.setOnMouseClicked(e -> abrirFileChooser(imgViewPicAlum));
        imgViewPicArea.setOnMouseClicked(e -> abrirFileChooser(imgViewPicArea));
        imgViewFoto1.setOnMouseClicked(e -> abrirFileChooser(imgViewFoto1));
    }

    private void abrirFileChooser(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    private void habilitarArrastrarYSoltar() {
        habilitarArrastreImagen(imgViewPicAlum);
        habilitarArrastreImagen(imgViewPicArea);
        habilitarArrastreImagen(imgViewFoto1);
    }

    private void habilitarArrastreImagen(ImageView imageView) {
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        imageView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().getFirst();
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private String imageToBase64(Image image) {
        if (image == null) {
            return null;
        }

        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            int maxWidth = 300;
            int maxHeight = 300;
            BufferedImage resizedImage = resizeImage(bufferedImage, maxWidth, maxHeight);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            float compressionQuality = 0.9f;
            String base64Image;

            do {
                baos.reset();
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();

                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(compressionQuality);
                }

                ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                writer.write(null, new IIOImage(resizedImage, null, null), param);
                writer.dispose();

                base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
                compressionQuality -= 0.1f;
            } while (base64Image.length() > 20000 && compressionQuality > 0.1f);

            if (base64Image.length() > 20000) {
                throw new IllegalArgumentException("La imagen no puede comprimirse lo suficiente para cumplir el límite.");
            }

            return base64Image;

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    private Image base64ToImage(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

            BufferedImage bufferedImage = ImageIO.read(bis);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            return image;
        } catch (IOException e) {
            return null;
        }
    }

    private void cargarComboBox(Tab newTab) {
        if (newTab == tabAlum) {
            cargarDatosComboBoxProfesor();
        } else if (newTab == tabProf) {
            cargarDatosComboBoxAreas();
        } else if (newTab == tabTr) {
            cargarDatosComboBoxAlumnos();
            cargarDatosComboBoxTests();
        } else if (newTab == tabQuest) {
            cargarDatosComboBoxTests();
        } else if (newTab == tabPxA) {
            cargarDatosComboBoxAreas();
            cargarDatosComboBoxPreguntas();
        }
    }

    private void cargarDatosComboBoxPreguntas() {
        ObservableList<String> idList = FXCollections.observableArrayList();

        String sql = "SELECT id FROM pregunta";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                idList.add(id);
            }

            FilteredList<String> filteredList = new FilteredList<>(idList, s -> true);
            cbIdQuestPxA.setItems(filteredList);

            TextField editor = cbIdQuestPxA.getEditor();
            cbIdQuestPxA.setEditable(true);

            editor.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));

                if (!filteredList.isEmpty()) {
                    cbIdQuestPxA.show();
                }
            });

        } catch (SQLException e) {
            System.out.println("No se pueden cargar los datos del ComboBox Preguntas");
        }
    }

    private void cargarDatosComboBoxAlumnos() {
        ObservableList<String> dniList = FXCollections.observableArrayList();

        String sql = "SELECT dni FROM alumno";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String dni = resultSet.getString("dni");
                dniList.add(dni);
            }

            FilteredList<String> filteredList = new FilteredList<>(dniList, s -> true);
            cbDNITry.setItems(filteredList);

            TextField editor = cbDNITry.getEditor();
            cbDNITry.setEditable(true);
            editor.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));
                if (!filteredList.isEmpty()) {
                    cbDNITry.show();
                }
            });

        } catch (SQLException e) {
            System.out.println("No se pueden cargar los datos del ComboBox Alumnos");
        }
    }

    private final AnchorPane anchorPaneQuest = new AnchorPane();
    private final AnchorPane anchorPaneTry = new AnchorPane();

    private void cargarDatosComboBoxTests() {
        txtTestQuest.setMaxWidth(165.6);
        txtTestTry.setMaxWidth(165.6);
        ObservableList<String> originalList = FXCollections.observableArrayList();

        double rowHeight = 24.0;
        double maxHeight = 50.0;
        listViewDesplegableQuest.setMaxHeight(maxHeight);
        listViewDesplegableTry.setMaxHeight(maxHeight);
        listViewDesplegableQuest.setMaxWidth(165.6);
        listViewDesplegableTry.setMaxWidth(165.6);
        listViewDesplegableQuest.setVisible(false);
        listViewDesplegableTry.setVisible(false);

        // Filtrado para txtTestQuest
        txtTestQuest.textProperty().addListener((observable, oldValue, newValue) -> filterListViewQuest(newValue, originalList));

        // Filtrado para txtTestTry
        txtTestTry.textProperty().addListener((observable, oldValue, newValue) -> filterListViewTry(newValue, originalList));

        // Acción al seleccionar un item de listViewDesplegableQuest
        listViewDesplegableQuest.setOnMouseClicked(event -> {
            String selectedItem = listViewDesplegableQuest.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                txtTestQuest.setText(selectedItem);
                listViewDesplegableQuest.setVisible(false);
            }
        });

        // Acción al seleccionar un item de listViewDesplegableTry
        listViewDesplegableTry.setOnMouseClicked(event -> {
            String selectedItem = listViewDesplegableTry.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                txtTestTry.setText(selectedItem);
                listViewDesplegableTry.setVisible(false);
            }
        });

        // Agregar los controles al AnchorPane en TabQuest solo si está vacío
        if (anchorPaneQuest.getChildren().isEmpty()) {
            anchorPaneQuest.setPrefWidth(165.6);
            anchorPaneQuest.setMaxWidth(165.6);
            anchorPaneQuest.setStyle("-fx-border-color: lightgray;");
            anchorPaneQuest.getChildren().addAll(txtTestQuest, listViewDesplegableQuest);

            AnchorPane.setTopAnchor(txtTestQuest, 0.0);
            AnchorPane.setLeftAnchor(txtTestQuest, 0.0);

            AnchorPane.setTopAnchor(listViewDesplegableQuest, 30.0);
            AnchorPane.setLeftAnchor(listViewDesplegableQuest, 0.0);

            listViewDesplegableQuest.managedProperty().bind(listViewDesplegableQuest.visibleProperty());
        }

        // Agregar los controles al AnchorPane en TabTr solo si está vacío
        if (anchorPaneTry.getChildren().isEmpty()) {
            anchorPaneTry.setPrefWidth(165.6);
            anchorPaneTry.setMaxWidth(165.6);
            anchorPaneTry.setStyle("-fx-border-color: lightgray;");
            anchorPaneTry.getChildren().addAll(txtTestTry, listViewDesplegableTry);

            AnchorPane.setTopAnchor(txtTestTry, 0.0);
            AnchorPane.setLeftAnchor(txtTestTry, 0.0);

            AnchorPane.setTopAnchor(listViewDesplegableTry, 30.0);
            AnchorPane.setLeftAnchor(listViewDesplegableTry, 0.0);

            listViewDesplegableTry.managedProperty().bind(listViewDesplegableTry.visibleProperty());
        }

        // Agregar los AnchorPane a los VBox correspondientes si no están ya agregados
        if (vboxQuest != null) {
            if (!vboxQuest.getChildren().contains(anchorPaneQuest)) {
                vboxQuest.getChildren().add(1, anchorPaneQuest); // Agregar a la posición 1 (segundo lugar)
            }
        }

        if (vboxTry != null) {
            if (!vboxTry.getChildren().contains(anchorPaneTry)) {
                vboxTry.getChildren().add(1, anchorPaneTry); // Agregar a la posición 1 (segundo lugar)
            }
        }

        // Consulta SQL para cargar los datos en originalList
        String sql = "SELECT nombretest FROM test";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                originalList.add(resultSet.getString("nombretest"));
            }

            // Asignar la lista de elementos a ambos ListView
            listViewDesplegableQuest.setItems(originalList);
            listViewDesplegableTry.setItems(originalList);

            // Ajustar la altura de los ListView
            int itemCount = listViewDesplegableQuest.getItems().size();
            listViewDesplegableQuest.setPrefHeight(Math.min(itemCount * rowHeight, maxHeight));
            listViewDesplegableTry.setPrefHeight(Math.min(itemCount * rowHeight, maxHeight));

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void filterListViewQuest(String filterText, ObservableList<String> originalList) {
        if (filterText != null && !filterText.isEmpty()) {
            ObservableList<String> filtered = FXCollections.observableArrayList();
            for (String item : originalList) {
                if (item.toLowerCase().contains(filterText.toLowerCase())) {
                    filtered.add(item);
                }
            }
            listViewDesplegableQuest.setItems(filtered);
            listViewDesplegableQuest.setVisible(!filtered.isEmpty());
        } else {
            listViewDesplegableQuest.setItems(originalList);
            listViewDesplegableQuest.setVisible(false);
        }
    }

    private void filterListViewTry(String filterText, ObservableList<String> originalList) {
        if (filterText != null && !filterText.isEmpty()) {
            ObservableList<String> filtered = FXCollections.observableArrayList();
            for (String item : originalList) {
                if (item.toLowerCase().contains(filterText.toLowerCase())) {
                    filtered.add(item);
                }
            }
            listViewDesplegableTry.setItems(filtered);
            listViewDesplegableTry.setVisible(!filtered.isEmpty());
        } else {
            listViewDesplegableTry.setItems(originalList);
            listViewDesplegableTry.setVisible(false);
        }
    }



    private void showErrorBD() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al cargar los datos");
        alert.setHeaderText(null);
        alert.setContentText("Hubo un problema con la conexión a la base de datos.");
        alert.showAndWait();
    }


    private void cargarDatosComboBoxAreas() {
		ObservableList<String> area = FXCollections.observableArrayList();

		String sql = "SELECT nombre FROM area";

		try (PreparedStatement statement = conexion.prepareStatement(sql);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String nombre = resultSet.getString("nombre");
				area.add(nombre);
			}

			FilteredList<String> filteredList = new FilteredList<>(area, s -> true);
			cbAreaProf.setItems(filteredList);
			cbAreaPxA.setItems(filteredList);

			TextField editorProf = cbAreaProf.getEditor();
			cbAreaProf.setEditable(true);
			editorProf.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredList.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));
				if (!filteredList.isEmpty()) {
					cbAreaProf.show();
				}
			});

			TextField editorPxA = cbAreaPxA.getEditor();
			cbAreaPxA.setEditable(true);
			editorPxA.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredList.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));
				if (!filteredList.isEmpty()) {
					cbAreaPxA.show();
				}
			});

		} catch (SQLException e) {
            System.out.println("No se pueden cargar los datos del ComboBox Areas");
		}
	}

	private void cargarDatosComboBoxProfesor() {
		ObservableList<String> dniList = FXCollections.observableArrayList();

		String sql = "SELECT dni FROM profesor";

		try (PreparedStatement statement = conexion.prepareStatement(sql);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String dni = resultSet.getString("dni");
				dniList.add(dni);
			}

			FilteredList<String> filteredList = new FilteredList<>(dniList, s -> true);
			cbDNIProfAlm.setItems(filteredList);

			TextField editor = cbDNIProfAlm.getEditor();
			cbDNIProfAlm.setEditable(true);

			editor.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredList.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));

				if (!filteredList.isEmpty()) {
					cbDNIProfAlm.show();
				}
			});

		} catch (SQLException e) {
            System.out.println("No se pueden cargar los datos del ComboBox Profesor");
		}
	}


	private void cargar(Tab newTab){
		if (newTab == tabAlum) {
			cargarDatos( "alumno",listViewAlumnos);
		} else if (newTab == tabProf) {
			cargarDatos("profesor",listViewProfesores);
		} else if (newTab == tabAr) {
			cargarDatos( "area",listViewArea);
		} else if (newTab == tabTr) {
			cargarDatos( "intentos", listViewIntentos);
		}else if (newTab == tabQuest) {
			cargarDatos("pregunta", listViewPreguntas);
		}else if (newTab == tabPxA) {
			cargarDatos( "pxa", listViewPxa);
		}else if (newTab == tabTest) {
			cargarDatos( "test", listViewTest);
		}
	}

	private void limpiar(Tab newTab){
		switch (newTab.getText()) {
			case "Alumno" -> {
				txtIdAlm.clear();
				txtNameAlm.clear();
				txtSurnameAlm.clear();
				txtPasswordAlm.clear();
				cbDNIProfAlm.setValue("");
				txtDNIAlm.clear();
				imgViewPicAlum.setImage(null);
				listViewAlumnos.getSelectionModel().clearSelection();
			}
			case "Profesor" -> {
				txtIdProf.clear();
				txtNameProf.clear();
				txtSurnameProf.clear();
				txtPasswordProf.clear();
				cbAreaProf.setValue("");
				txtDNIProf.clear();
				imgViewFoto1.setImage(null);
				listViewProfesores.getSelectionModel().clearSelection();
			}
			case "Test" -> {
				txtIdTest.clear();
				txtTestName.clear();
				listViewTest.getSelectionModel().clearSelection();
			}
			case "Preguntas" -> {
				txtIdQuest.clear();
				txtTestQuest.clear();
				txtEnunQuest.clear();
				listViewPreguntas.getSelectionModel().clearSelection();
			}
			case "PxA" ->{
				txtIdPxA.clear();
				cbAreaPxA.setValue("");
				cbIdQuestPxA.setValue("");
				txtEnumPxA.clear();
				listViewPxa.getSelectionModel().clearSelection();
			}
			case "Intentos" -> {
				txtIdTry.clear();
				txtTestTry.clear();
				cbDNITry.setValue("");
				txtDateTry.setValue(null);
				txtTimeTry.clear();
				txtResTry.clear();
				listViewIntentos.getSelectionModel().clearSelection();
			}
			case "Area" -> {
				txtIdArea.clear();
				txtNameArea.clear();
				txtDescripArea.clear();
				imgViewPicArea.setImage(null);
				listViewArea.getSelectionModel().clearSelection();
			}
		}
	}

	public void login() {
		String user = txtUser.getText();
		String password = txtPassword.getText();
		String tipo = chkProf.isSelected() ? "profesor" : "alumno";
		String query;
		if(tipo.equals("profesor")) {
			query = "SELECT * FROM " + tipo + " WHERE dni = ? AND claveaccesoprof = ?";
		}else{
			query = "SELECT * FROM " + tipo + " WHERE dni = ? AND claveaccesoalum = ?";
		}

		try (PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, user);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				boolean isOrientador = false;
				if (tipo.equals("profesor")) {
					isOrientador = rs.getInt("isOrientador") == 1;
				}

				if (tipo.equals("alumno")) {
					rolAlumno();
				} else {
					if (isOrientador) {
						rolAdmin();
					} else {
						rolProfesor();
					}
				}
			} else {
				mostrarAlerta(Alert.AlertType.ERROR,"Error","Login fallido","Usuario o contraseña incorrectos. Intentelo de nuevo");
			}
		} catch (SQLException e) {
            System.out.println("No se puede realizar el login");
		}
	}

	private void cargarDatos(String tabla, ListView<String> listView) {
		String query = "SELECT * FROM " + tabla;

		try (PreparedStatement stmt = conexion.prepareStatement(query);
			 ResultSet rs = stmt.executeQuery()) {

			ObservableList<String> items = FXCollections.observableArrayList();
			while (rs.next()) {
				String nombre = switch (tabla) {
					case "alumno", "profesor" -> rs.getString("dni");
					case "area" -> rs.getString("nombre");
					case "test" -> rs.getString("nombretest");
					default -> rs.getString("id");
				};
				items.add(nombre);
			}
			listView.setItems(items);
		} catch (SQLException e) {
            System.out.println("No se pueden cargar los datos");
			mostrarAlerta(Alert.AlertType.ERROR, "Error","Error al cargar datos", "No se pudo cargar los datos de la base de datos.");
		}
	}

	public Connection getConnection() throws IOException {
		String IP, PORT, BBDD, USER, PWD;
		IP = configuracion.getIP();
		PORT = configuracion.getPort();
		BBDD = configuracion.getBbdd();
		USER = configuracion.getUser();
		PWD = configuracion.getPwd();

		Connection conn;
		try {
			String cadconex = "jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
			System.out.println(cadconex);
			//Si usamos LAMP Funciona con ambos conectores
			conn = DriverManager.getConnection("jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
			return conn;
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ha ocurrido un error de conexión");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			exit(0);
			return null;
		}
	}

	// Nombre del test
	public Test buscarTestPorNombre(String nombre) {
		return testDAO.findByColumn("nombretest", nombre);
	}


	// Id de la pregunta
	public Pregunta buscarPreguntaPorId(int id) {
		return preguntaDAO.findById(id);
	}

	// Dni del alumno
	public Alumno buscarAlumnoPorDni(String dni) {
		return alumnoDAO.findByColumn("dni", dni);
	}

	// Dni del profesor
	public Profesor buscarProfesorPorDni(String dni) {
		return profesorDAO.findByColumn("dni", dni);
	}

	// Nombre del área
	public Area buscarAreaPorNombre(String nombre) {
		return areaDAO.findByColumn("nombre", nombre);
	}

	// Id del PxA
	public PxA buscarPxAPorId(int id) {
		return pxaDAO.findById(id);
	}
	// Id del intento
	public Intentos buscarIntentoPorId(int id) {
		return intentosDAO.findById(id);
	}
	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String mensaje) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(header);
		alerta.setContentText(mensaje);
		alerta.showAndWait();
	}

	public void mostrarMenu(ActionEvent event) {
		Button sourceButton = (Button) event.getSource();

		ContextMenu contextMenu = new ContextMenu();

		MenuItem creditos = new MenuItem("Créditos");
		creditos.setOnAction(e -> {
			try {
				mostrarCreditos();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		MenuItem salir = new MenuItem("Salir");
		salir.setOnAction(e -> salirAplicacion());

		contextMenu.getItems().addAll(creditos, salir);

		contextMenu.show(sourceButton, Side.BOTTOM, 0, 0);
	}

	public void mostrarCreditos() throws IOException {
		System.out.println("Mostrando créditos...");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/creditos.fxml"));
		Parent creditosView = loader.load();

		Stage creditosStage = new Stage();
		creditosStage.setTitle("Créditos");

		Scene creditosScene = new Scene(creditosView);
		creditosStage.setScene(creditosScene);
		creditosStage.getIcons().add(new Image("escolavision.png"));
		creditosStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

		creditosStage.showAndWait();
	}


    public void salirAplicacion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de salida");
        confirmacion.setHeaderText("¿Está seguro de que desea salir?");
        confirmacion.setContentText("Se perderán todos los cambios no guardados.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}
