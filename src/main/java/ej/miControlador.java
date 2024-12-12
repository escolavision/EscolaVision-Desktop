package ej;

import ej.DAO.GenericDAO;
import ej.DAO.TablasDAO.*;
import ej.Tablas.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
import java.util.List;

import static java.lang.System.exit;

public class miControlador implements Initializable {

    @FXML
    private Button btnClearTest, btnClearQuest, btnClearAlum, btnClearProf, btnClearTry, btnLogo,
            btnDelTest, btnDelQuest, btnDelAlum, btnDelProf, btnDelTry, btnLogin, btnSaveTest,
            btnSaveQuest, btnSaveAlum, btnSaveProf, btnSaveTry, btnClearPxA, btnDelPxA, btnSavePxA,
            btnEditarTest, btnEditarPreguntas, btnEditarAlumnos, btnEditarProfesores, btnEditarArea,
            btnEditarPxA, btnEditarIntentos, btnHelp;

    @FXML
    private ListView<String> listViewAlumnos, listViewArea, listViewIntentos, listViewPreguntas,
            listViewProfesores, listViewPxa, listViewTest;

    @FXML
    private CheckBox chkProf;

    @FXML
    private ImageView imglogo, imgViewFoto1, imgViewPicAlum, imgViewPicArea, imgDelTest, imgDelQuest,
            imgDelAlum, imgDelProf, imgDelAr, imgDelPxA, imgDelTry, imgViewlogIn, imgClearTest, imgClearQuest,
            imgClearAlum, imgClearProf, imgClearPxA, imgClearAr, imgClearTry, imgSaveTest, imgSaveQuest,
            imgSaveAlum, imgSaveProf, imgSavePxA, imgSaveAr, imgSaveTry;

    @FXML
    private Tab tabAlum, tabAr, tabHome, tabProf, tabPxA, tabQuest, tabTest, tabTr;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField txtDNIAlm, txtDNIProf, txtIdAlm, txtIdArea, txtTestTry, txtAreaPxA, txtQuestPxA,
            txtIdProf, txtIdQuest, txtIdTest, txtIdTry, txtNameAlm, txtNameArea, txtNameProf, txtResTry,
            txtSurnameAlm, txtSurnameProf,  txtTimeTry, txtUser, txtIdPxA, txtDNITry, txtTestQuest, txtAreaProf,
            txtDNIProfAlm, txtTituloQuest;

    @FXML
    private TextArea txtDescripArea, txtEnunQuest,txtTestName, txtEnumPxA;

    @FXML
    private PasswordField txtPassword, txtPasswordAlm, txtPasswordProf;

    @FXML
    private DatePicker txtDateTry;
    
    @FXML
    private HBox hbHeader, hboxTry;

    @FXML
    private VBox vboxGraficTest;

    @FXML
    private Label lblEscolavisionDesktop;
    
    @FXML
    private StackPane miStackPane;


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
    private final Map<Tab, Button> tabToButtonMap = new HashMap<>();

    public miControlador() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarMap();
        inicializarConexion();
        inicializarLogin();
        inicializarDAO();
        inicializarImagenes();
        inicializarTabToButtonMap();
    }
    
    private void inicializarImagenes() {
        double imageSize = 20.0;
        
        setImageWithSize(imgDelTest, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelQuest, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelAlum, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelProf, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelAr, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelPxA, "delete.png", imageSize, imageSize);
        setImageWithSize(imgDelTry, "delete.png", imageSize, imageSize);
        setImageWithSize(imgViewlogIn, "logIn.png", imageSize, imageSize);
        setImageWithSize(imgClearTest, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearQuest, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearAlum, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearProf, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearAr, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearPxA, "clear.png", imageSize, imageSize);
        setImageWithSize(imgClearTry, "clear.png", imageSize, imageSize);
        setImageWithSize(imgSaveTest, "save.png", imageSize, imageSize);
        setImageWithSize(imgSaveQuest, "save.png", imageSize, imageSize);
        setImageWithSize(imgSaveAlum, "save.png", imageSize, imageSize);
        setImageWithSize(imgSaveProf, "save.png", imageSize, imageSize);
        setImageWithSize(imgSaveAr, "save.png", imageSize, imageSize);
        setImageWithSize(imgSavePxA, "save.png", imageSize, imageSize);
        setImageWithSize(imgSaveTry, "save.png", imageSize, imageSize);
    }
    
    private void setImageWithSize(ImageView imageView, String imagePath, double width, double height) {
        Image image = new Image(imagePath);
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
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
            inicializarEventosYValidadores();
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
        btnHelp.setVisible(false);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
    }

    private void rolAdmin() {
        tabPane.getTabs().remove(tabHome);
        tabPane.getTabs().add(tabTest);
        tabPane.getTabs().add(tabQuest);
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabProf);
        tabPane.getTabs().add(tabAr);
        tabPane.getTabs().add(tabPxA);
        tabPane.getTabs().add(tabTr);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
        btnHelp.setVisible(true);
    }

    private void rolAlumno() {
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabTr);
        tabPane.getTabs().remove(tabHome);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
        btnHelp.setVisible(true);
    }

    private void rolProfesor() {
        tabPane.getTabs().add(tabAlum);
        tabPane.getTabs().add(tabProf);
        tabPane.getTabs().add(tabTr);
        tabPane.getTabs().remove(tabHome);
        btnLogo.setVisible(true);
        btnLogo.setManaged(true);
        btnHelp.setVisible(true);
    }


        private void inicializarEventosYValidadores() {

        btnLogin.setOnAction(e -> login());

        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnLogin.fire();
            }
        });

        //ANIMACION
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                ProgressIndicator progressIndicator = new ProgressIndicator();
                progressIndicator.setPrefSize(70, 70);
                
                Region background = new Region();
                background.setStyle("-fx-background-color: rgba(174,214,241,0.8);");
                background.setPrefSize(miStackPane.getWidth(), miStackPane.getHeight());
                
                StackPane loadingPane = new StackPane();
                loadingPane.getChildren().addAll(background, progressIndicator);
                StackPane.setAlignment(progressIndicator, Pos.CENTER);
                
                javafx.application.Platform.runLater(() -> miStackPane.getChildren().add(loadingPane));
                
                long startTime = System.currentTimeMillis();
                
                new Thread(() -> {
                    try {
                        limpiar(newTab);
                        cargar(newTab);
                        cargarDatos(newTab);

                        long elapsedTime = System.currentTimeMillis() - startTime;
                        if (elapsedTime < 800) {
                            Thread.sleep(800 - elapsedTime);
                        }
                        
                        javafx.application.Platform.runLater(() -> miStackPane.getChildren().remove(loadingPane));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        txtQuestPxA.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    String[] parts = newValue.trim().split("\\s+");
                    if (parts.length > 1) {
                        int id = Integer.parseInt(parts[1]);
                        Pregunta pregunta = buscarPreguntaPorId(id);
                        if (pregunta != null) {
                            txtEnumPxA.setText(pregunta.getEnunciado());
                        } else {
                            txtEnumPxA.setText("");
                        }
                    } else {
                        txtEnumPxA.setText("");
                    }
                } catch (NumberFormatException e) {
                    txtEnumPxA.setText("");
                }
            } else {
                txtEnumPxA.setText("");
            }
        });


        listViewMap.forEach((tipo, listView) -> listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                comprobarEditar(tipo);
                String[] parts = newValue.trim().split("\\s+", 2);
                switch (tipo) {
                    case "test": {
                        cambiarBotonEditar(btnSaveTest, "Actualizar", "test");
                        if (parts.length > 1) {
                            String[] parts2 = newValue.trim().split("\\s+");
                            cambiarBotonEditar(btnEditarTest, "Editar", "test");
                            cargarTestPartido(parts2[1]);
                        }
                        break;
                    }
                    case "pregunta": {
                        cambiarBotonEditar(btnSaveQuest, "Actualizar", "pregunta");
                        cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
                        cargarPregunta(parts[1]);
                        break;
                    }
                    case "alumno": {
                        cambiarBotonEditar(btnSaveAlum, "Actualizar", "alumno");
                        cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
                        cargarAlumno(parts[0],parts[1]);
                        break;
                    }
                    case "profesor": {
                        cambiarBotonEditar(btnSaveProf, "Actualizar", "profesor");
                        cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
                        cargarProfesor(parts[0],parts[1]);
                        break;
                    }
                    case "area": {
                        cargarArea(newValue);
                        break;
                    }
                    case "pxa": {
                        cambiarBotonEditar(btnSavePxA, "Actualizar", "pxa");
                        String[] parts2 = newValue.trim().split("\\s+");
                        cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
                        cargarPxA(parts2[3]);
                        break;
                    }
                    case "intentos": {
                        cambiarBotonEditar(btnSaveTry, "Actualizar", "intentos");
                        cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
                        cargarIntento(parts[1]);
                        break;
                    }
                    default: {
                    }
                }
            }
            listViewDesplegableQuest.setVisible(false);
            listViewDesplegableTry.setVisible(false);
        }));

        //AYUDA
        btnHelp.setOnAction(e -> {
            String helpMessage = "";
            switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
                case "Test" -> {
                    helpMessage = "En esta sección puedes crear y modificar tests, asignándoles un nombre. También puedes ver y editar los tests guardados en el sistema.";
                }
                case "Preguntas" -> {
                    helpMessage = "En esta sección puedes añadir y modificar preguntas, asignándoles un título, un enunciado, y asociándolas a un test específico. Además, puedes ver y editar las preguntas guardadas en el sistema.";
                }
                case "Alumnado" -> {
                    helpMessage = "En esta sección puedes introducir y modificar los datos del alumno, incluyendo nombre, apellidos, DNI, clave de acceso, foto y el profesor asignado. También puedes ver y editar los alumnos guardados en el sistema.";
                }
                case "Profesorado" -> {
                    helpMessage = "En esta sección puedes introducir y modificar los datos del profesor, incluyendo nombre, apellidos, DNI, área de especialización, clave de acceso y foto. También puedes ver y editar los profesores guardados en el sistema.";
                }
                case "Área" -> {
                    helpMessage = "En esta sección puedes consultar las áreas de especialización disponibles. No se permite la modificación de las áreas guardadas en el sistema.";
                }
                case "Pregunta x Área" -> {
                    helpMessage = "En esta sección puedes asociar y modificar preguntas al área correspondiente, facilitando su organización y asignación. También puedes ver y editar las asociaciones guardadas en el sistema.";
                }
                case "Intentos" -> {
                    helpMessage = "En esta sección se muestran los intentos realizados por el alumno en cada test, incluyendo el nombre y apellidos del alumno, la fecha y hora de realización, y el resultado obtenido. Puedes ver y modificar los intentos guardados en el sistema.";
                }
                default -> {
                }
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Ayuda | EscolaVision Desktop");
            dialog.setHeaderText("Información de la pestaña seleccionada");

            Label helpLabel = new Label(helpMessage);
            helpLabel.setStyle("-fx-text-alignment: justify;");
            helpLabel.setWrapText(true);

            VBox content = new VBox(10, helpLabel);
            content.setPrefWidth(Region.USE_PREF_SIZE);
            content.setMinHeight(Region.USE_PREF_SIZE);
            content.setAlignment(javafx.geometry.Pos.CENTER);

            dialog.getDialogPane().setContent(content);
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image("escolavision.png"));
            dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.CLOSE);

            dialog.showAndWait();
        });

        //EDITAR

        btnEditarTest.setOnAction(e -> {
            if (btnEditarTest.getText().equals("Editar") || btnEditarTest.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarTest, "Cancelar", "test");
            } else {
                cambiarBotonEditar(btnEditarTest, "Editar", "test");
                if (listViewTest.getSelectionModel().getSelectedItem() != null) {
                    cargarTest(listViewTest.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarPreguntas.setOnAction(e -> {
            if (btnEditarPreguntas.getText().equals("Editar") || btnEditarPreguntas.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarPreguntas, "Cancelar", "pregunta");
            } else {
                cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
                if (listViewPreguntas.getSelectionModel().getSelectedItem() != null) {
                    cargarPregunta(listViewPreguntas.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarAlumnos.setOnAction(e -> {
            if (btnEditarAlumnos.getText().equals("Editar") || btnEditarAlumnos.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarAlumnos, "Cancelar", "alumno");
            } else {
                cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
                if (listViewAlumnos.getSelectionModel().getSelectedItem() != null) {
                    String na = listViewAlumnos.getSelectionModel().getSelectedItem();
                    String[] parts = na.trim().split(" ", 2);
                    cargarAlumno(parts[0],parts[1]);
                }
            }
        });

        btnEditarProfesores.setOnAction(e -> {
            if (btnEditarProfesores.getText().equals("Editar") || btnEditarProfesores.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarProfesores, "Cancelar", "profesor");
            } else {
                cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
                if (listViewProfesores.getSelectionModel().getSelectedItem() != null) {
                    String na = listViewProfesores.getSelectionModel().getSelectedItem();
                    String[] parts = na.trim().split(" ", 2);
                    cargarProfesor(parts[0],parts[1]);
                }
            }
        });

        btnEditarArea.setOnAction(e -> {
            if (btnEditarArea.getText().equals("Editar") || btnEditarArea.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarArea, "Cancelar", "area");
            } else {
                cambiarBotonEditar(btnEditarArea, "Editar", "area");
                if (listViewArea.getSelectionModel().getSelectedItem() != null) {
                    cargarArea(listViewArea.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarPxA.setOnAction(e -> {
            if (btnEditarPxA.getText().equals("Editar") || btnEditarPxA.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarPxA, "Cancelar", "pxa");
            } else {
                cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
                if (listViewPxa.getSelectionModel().getSelectedItem() != null) {
                    cargarPxA(listViewPxa.getSelectionModel().getSelectedItem());
                }
            }
        });

        btnEditarIntentos.setOnAction(e -> {
            if (btnEditarIntentos.getText().equals("Editar") || btnEditarIntentos.getText().equals("_Editar")) {
                cambiarBotonEditar(btnEditarIntentos, "Cancelar", "intentos");
            } else {
                cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
                if (listViewIntentos.getSelectionModel().getSelectedItem() != null) {
                    cargarIntento(listViewIntentos.getSelectionModel().getSelectedItem());
                }
            }
        });


        // LIMPIAR

        btnClearTest.setOnAction(e -> {
            limpiar(tabTest);
            txtTestName.setEditable(true);
        });

        btnClearQuest.setOnAction(e -> {
            limpiar(tabQuest);
            txtTestQuest.setEditable(true);
            txtEnunQuest.setEditable(true);
        });

        btnClearAlum.setOnAction(e -> {
            limpiar(tabAlum);
            txtNameAlm.setEditable(true);
            txtSurnameAlm.setEditable(true);
            txtDNIAlm.setEditable(true);
            txtDNIProfAlm.setMouseTransparent(false);
            txtPasswordAlm.setEditable(true);
            habilitarArrastrarYSoltar();
            agregarEventListenersParaSeleccionarImagen();
        });

        btnClearProf.setOnAction(e -> {
            limpiar(tabProf);
            txtNameProf.setEditable(true);
            txtSurnameProf.setEditable(true);
            txtDNIProf.setEditable(true);
            txtPasswordProf.setEditable(true);
            txtAreaProf.setMouseTransparent(false);
        });

        btnClearPxA.setOnAction(e -> {
            limpiar(tabPxA);
            txtAreaPxA.setMouseTransparent(false);
            txtQuestPxA.setMouseTransparent(false);
        });

        btnClearTry.setOnAction(e -> {
            limpiar(tabTr);
            txtTestTry.setEditable(true);
            txtDNITry.setMouseTransparent(false);
            txtDateTry.setEditable(true);
            txtTimeTry.setEditable(true);
            txtResTry.setEditable(true);
        });

        // ELIMINAR

        btnDelTest.setOnAction(e -> {
            if (!txtIdTest.getText().isEmpty()) borrar("test", txtIdTest.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar un test.");

        });

        btnDelQuest.setOnAction(e -> {
            if (!txtIdQuest.getText().isEmpty()) borrar("pregunta", txtIdQuest.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar una pregunta.");
        });

        btnDelAlum.setOnAction(e -> {
            if (!txtIdAlm.getText().isEmpty()) borrar("alumno", txtIdAlm.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar un alumno.");
        });

        btnDelProf.setOnAction(e -> {
            if (!txtIdProf.getText().isEmpty()) borrar("profesor", txtIdProf.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar un profesor.");
        });

        btnDelPxA.setOnAction(e -> {
            if (!txtIdPxA.getText().isEmpty()) borrar("pxa", txtIdPxA.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar un PxA.");
        });

        btnDelTry.setOnAction(e -> {
            if (!txtIdTry.getText().isEmpty()) borrar("intentos", txtIdTry.getText());
            else mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar", "Debe seleccionar un intento.");
        });

        //VALIDACIÓN
        ValidationSupport vSnombreTest = new ValidationSupport();
        vSnombreTest.registerValidator(txtTestName, false, Validator.createEmptyValidator("El nombre no puede estar vacío"));

        ValidationSupport vSPregunta = new ValidationSupport();
        vSPregunta.registerValidator(txtEnunQuest, false, Validator.createEmptyValidator("El enunciado no puede estar vacío"));
        vSPregunta.registerValidator(txtTituloQuest, false, Validator.createEmptyValidator("El titulo no puede estar vacío"));
        vSPregunta.registerValidator(txtTestQuest, false, Validator.createEmptyValidator("Debe seleccionar un test"));

        ValidationSupport vSAlumno = new ValidationSupport();
        vSAlumno.registerValidator(txtNameAlm, false, Validator.createEmptyValidator("El nombre no puede estar vacío"));         vSAlumno.registerValidator(txtSurnameAlm, false, Validator.createEmptyValidator("El apellido no puede estar vacío"));
        vSAlumno.registerValidator(txtDNIProfAlm, false, Validator.createEmptyValidator("Debe seleccionar un Profesora"));
        vSAlumno.registerValidator(txtDNIAlm, false, Validator.createRegexValidator("El formato del DNI es inválido", "^\\d{8}[A-Za-z]$", Severity.ERROR));
        vSAlumno.registerValidator(txtPasswordAlm, false, Validator.createRegexValidator("""
                La contraseña debe tener al menos 8 caracteres,
                incluyendo una mayúscula, una minúscula,
                un número y un carácter especial
                (@ $ ! % * ? & . - _ #).
                """, "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&\\.\\-\\_\\#])[A-Za-z\\d@$!%*?&\\.\\-\\_\\#]{8,}$", Severity.ERROR));

        ValidationSupport vSProfesor = new ValidationSupport();
        vSProfesor.registerValidator(txtNameProf, false, Validator.createEmptyValidator("El nombre no puede estar vacío"));
        vSProfesor.registerValidator(txtSurnameProf, false, Validator.createEmptyValidator("El apellido no puede estar vacío"));
        vSProfesor.registerValidator(txtAreaProf, false, Validator.createEmptyValidator("Debe seleccionar un Area"));
        vSProfesor.registerValidator(txtDNIProf, false, Validator.createRegexValidator("El formato del DNI es inválido", "^\\d{8}[A-Za-z]$", Severity.ERROR));
        vSProfesor.registerValidator(txtPasswordProf, false, Validator.createRegexValidator("""
                	La contraseña debe tener al menos 8 caracteres,
                	incluyendo una mayúscula, una minúscula,
                un número y un carácter especial
                (@ $ ! % * ? & . - _ #).
                """, "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&\\.\\-\\_\\#])[A-Za-z\\d@$!%*?&\\.\\-\\_\\#]{8,}$", Severity.ERROR));

        ValidationSupport vSPxA = new ValidationSupport();
        vSPxA.registerValidator(txtAreaPxA, false, Validator.createEmptyValidator("Debe seleccionar un area"));
        vSPxA.registerValidator(txtQuestPxA, false, Validator.createEmptyValidator("Debe seleccionar una pregunta"));

        ValidationSupport vSIntentos = new ValidationSupport();
        vSIntentos.registerValidator(txtTestTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));
        vSIntentos.registerValidator(txtDNITry, false, Validator.createEmptyValidator("Debe seleccionar un alumno"));
        vSIntentos.registerValidator(txtDateTry, false, Validator.createEmptyValidator("El formato de la fecha es incorrect"));
        vSIntentos.registerValidator(txtTimeTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));
        vSIntentos.registerValidator(txtResTry, false, Validator.createEmptyValidator("Debe seleccionar un test"));

        // INSERTAR
        btnSaveTest.setOnAction(e -> {
            ValidationResult resultado = vSnombreTest.getValidationResult();
            Collection<ValidationMessage> errores = resultado.getErrors();

            if (!errores.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Datos incompletos o incorrectos", "Por favor, introduce un nombre para el test.");

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
                alerta.setTitle("Error | EscolaVision Desktop");
                alerta.setHeaderText("Datos incompletos o incorrectos");
                alerta.setContentText("Por favor, corrige los errores antes de guardar.");
                Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image("escolavision.png"));
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
            vSAlumno.initInitialDecoration();
            vSProfesor.initInitialDecoration();
            vSPxA.initInitialDecoration();
            vSIntentos.initInitialDecoration();
        });
    }

    public void cambiarBotonEditar(Button boton, String textobtn, String tab) {
        boton.setText(textobtn);
        comprobarEditar(tab);
    }

    public void cambiarBotonSave(Button boton, String textobtn, String tab) {
        boton.setText(textobtn);
        comprobarEditar(tab);
    }

    private void cargarTest(String newValue) {
        String[] parts2 = newValue.trim().split("\\s+");
        Test test = buscarTestPorId(parts2[1]);
        txtIdTest.setText("" + test.getId());
        txtTestName.setText(test.getNombre());
    }

    private void cargarTestPartido(String newValue) {
        Test test = buscarTestPorId(newValue);
        txtIdTest.setText("" + test.getId());
        txtTestName.setText(test.getNombre());
    }

    private void cargarPregunta(String newValue) {
        Pregunta pregunta;
        if(newValue.contains("Pregunta")){
            String[] parts = newValue.trim().split(" ",2);
            pregunta = buscarPreguntaPorId(Integer.parseInt(parts[1]));
        }else{
            pregunta = buscarPreguntaPorId(Integer.parseInt(newValue));
        }
        txtIdQuest.setText("" + pregunta.getId());
        txtTestQuest.setText(pregunta.getTest().getNombre());
        txtTituloQuest.setText(pregunta.getTitulo());
        txtEnunQuest.setText(pregunta.getEnunciado());
    }

    private void cargarAlumno(String nombre, String apellidos) {
        Alumno alumno = buscarAlumnoPorNombreYApellidos(nombre, apellidos);
        txtIdAlm.setText("" + alumno.getId());
        txtNameAlm.setText(alumno.getNombre());
        txtSurnameAlm.setText(alumno.getApellidos());
        txtDNIAlm.setText(alumno.getDni());
        txtDNIProfAlm.setText(alumno.getProfesor().getNombre() + " " + alumno.getProfesor().getApellidos());
        txtPasswordAlm.setText(alumno.getClaveaccesoalumno());
        if (!Objects.equals(alumno.getFoto(), "")) {
            imgViewPicAlum.setImage(base64ToImage(alumno.getFoto()));
        } else {
            imgViewPicAlum.setImage(null);
        }
    }

    private void cargarProfesor(String nombre, String apellidos) {
        Profesor profesor = buscarProfesorPorNombreYApellidos(nombre,apellidos);
        txtIdProf.setText("" + profesor.getId());
        txtNameProf.setText(profesor.getNombre());
        txtSurnameProf.setText(profesor.getApellidos());
        txtDNIProf.setText(profesor.getDni());
        txtPasswordProf.setText(profesor.getClaveaccesoprof());
        txtAreaProf.setText(profesor.getArea().getNombre());
        if (!Objects.equals(profesor.getFoto(), "")) {
            imgViewFoto1.setImage(base64ToImage(profesor.getFoto()));
        } else {
            imgViewFoto1.setImage(null);
        }
    }

    private void cargarArea(String newValue) {
        String[] parts = newValue.trim().split("\\s+", 4);
        Area area = buscarAreaPorNombre(parts[3]);
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
        PxA pxa;
        if(newValue.contains("Pregunta")){
            String[] parts = newValue.trim().split("\\s+",4);
            pxa = buscarPxAPorId(Integer.parseInt(parts[3]));
        }else{
            pxa = buscarPxAPorId(Integer.parseInt(newValue));
        }

        txtIdPxA.setText("" + pxa.getId());
        txtAreaPxA.setText(pxa.getArea().getNombre());
        txtQuestPxA.setText("Pregunta " + pxa.getPregunta().getId());
        txtEnumPxA.setText(pxa.getPregunta().getEnunciado());
    }

    private void cargarIntento(String newValue) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(70, 70);

        Region background = new Region();
        background.setStyle("-fx-background-color: rgba(174,214,241,0.8);");
        background.setPrefSize(miStackPane.getWidth(), miStackPane.getHeight());

        StackPane loadingPane = new StackPane();
        loadingPane.getChildren().addAll(background, progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        javafx.application.Platform.runLater(() -> miStackPane.getChildren().add(loadingPane));

        long startTime = System.currentTimeMillis();

        new Thread(() -> {
            try {
                Intentos intento;
                if(newValue.contains("Intento")){
                    String[] parts = newValue.trim().split("\\s+",2);
                    intento = buscarIntentoPorId(Integer.parseInt(parts[1]));
                }else{
                    intento = buscarIntentoPorId(Integer.parseInt(newValue));
                }

                javafx.application.Platform.runLater(() -> {
                    txtIdTry.setText(String.valueOf(intento.getId()));
                    txtTestTry.setText(intento.getTest().getNombre());
                    txtDNITry.setText(intento.getAlumno().getNombre() + " " + intento.getAlumno().getApellidos());
                    txtDateTry.setValue(intento.getFecha());
                    txtTimeTry.setText(intento.getHora());
                    txtResTry.setText(intento.getResultados());

                    String[] valores = intento.getResultados().split(";");
                    String[] areas = {"AREA 1", "AREA 2", "AREA 3", "AREA 4", "AREA 5"};

                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    for (int i = 0; i < valores.length; i++) {
                        dataset.addValue(Double.parseDouble(valores[i]), "Resultados", areas[i]);
                    }

                    JFreeChart chart = ChartFactory.createBarChart(
                            "Resultados por Área",
                            "Áreas",
                            "Resultados",
                            dataset,
                            PlotOrientation.VERTICAL,
                            true,
                            true,
                            false
                    );

                    BufferedImage bufferedImage = chart.createBufferedImage(800, 600);

                    Image fxImage = convertToFXImage(bufferedImage);

                    ImageView imageView = new ImageView(fxImage);

                    imageView.fitWidthProperty().bind(hboxTry.widthProperty());
                    imageView.fitHeightProperty().bind(hboxTry.heightProperty());
                    imageView.setPreserveRatio(true);

                    hboxTry.getChildren().clear();
                    hboxTry.setAlignment(Pos.CENTER);
                    hboxTry.getChildren().add(imageView);

                    imageView.setOnMouseEntered(event -> {
                        imageView.setCursor(javafx.scene.Cursor.HAND);
                        imageView.setEffect(new DropShadow(20, Color.GRAY));
                    });

                    imageView.setOnMouseExited(event -> {
                        imageView.setCursor(javafx.scene.Cursor.DEFAULT);
                        imageView.setEffect(null);
                    });

                    imageView.setOnMouseClicked(event -> {
                        Stage imageStage = new Stage();
                        imageStage.setTitle("Gráfico Ampliado | EscolaVision Desktop");
                        imageStage.getIcons().add(new Image("escolavision.png"));

                        ImageView expandedImageView = new ImageView(fxImage);
                        expandedImageView.setPreserveRatio(true);
                        expandedImageView.setFitWidth(800);
                        expandedImageView.setFitHeight(600);

                        Scene scene = new Scene(new StackPane(expandedImageView), 800, 600);

                        imageStage.setScene(scene);
                        imageStage.show();
                    });
                });

                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < 800) {
                    Thread.sleep(800 - elapsedTime);
                }

                javafx.application.Platform.runLater(() -> miStackPane.getChildren().remove(loadingPane));

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> miStackPane.getChildren().remove(loadingPane));
            }
        }).start();
    }

    private Image convertToFXImage(BufferedImage bufferedImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new Image(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void borrar(String tipo, String id) {
        GenericDAO dao = getDAOForType(tipo);
        Tab tab = getTabByTipo(tipo);

        if (dao != null && tab != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación | EscolaVision Desktop");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar este elemento?");
            Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("escolavision.png"));

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                if (dao.delete(Integer.parseInt(id))) {
                    limpiar(tab);
                    cargar(tab);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al borrar",
                            "No se ha podido borrar el elemento seleccionado. Compruebe que no está siendo usado en otro registro.");
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error", "Tipo desconocido: " + tipo);
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
                p1.setTitulo(txtTituloQuest.getText());

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
                String[] parts = txtDNIProfAlm.getText().trim().split("\\s+", 2);
                a1.setProfesor(buscarProfesorPorNombreYApellidos(parts[0], parts[1]));
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
                p1.setArea(buscarAreaPorNombre(txtAreaProf.getText()));
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
                p1.setArea(buscarAreaPorNombre(txtAreaPxA.getText()));
                if(txtQuestPxA.getText().contains("Pregunta")){
                    String[] parts = txtQuestPxA.getText().trim().split("\\s+",2);
                    p1.setPregunta(buscarPreguntaPorId(Integer.parseInt(parts[1])));
                }else{
                    p1.setPregunta(buscarPreguntaPorId(Integer.parseInt(txtQuestPxA.getText())));
                }

                if (!txtIdPxA.getText().isEmpty()) {
                    p1.setId(Integer.parseInt(txtIdPxA.getText()));
                    isUpdate = true;
                }
                resultado = isUpdate ? pxaDAO.update(p1) : pxaDAO.insert(p1);
                break;
            }
            case "intentos": {
                Intentos i1 = new Intentos();
                String[] parts = txtDNITry.getText().trim().split("\\s+", 2);
                i1.setAlumno(buscarAlumnoPorNombreYApellidos(parts[0], parts[1]));
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
                mostrarAlerta(Alert.AlertType.WARNING, "Warning", "Operación no válida", "El tipo especificado no es válido.");
                return;
            }
        }

        if (resultado) {
            limpiar(getTabByTipo(tipo));
            cargar(getTabByTipo(tipo));
        } else {
            String operacion = isUpdate ? "actualizar" : "insertar";
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al " + operacion, "No se ha podido " + operacion + " el elemento seleccionado.");
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
                txtDNIProfAlm.setMouseTransparent(!editable);
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
                txtAreaProf.setMouseTransparent(!editable);
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
                txtAreaPxA.setMouseTransparent(!editable);
                txtQuestPxA.setMouseTransparent(!editable);
                break;
            }
            case "intentos": {
                boolean editable = btnEditarIntentos.getText().equals("Cancelar");
                txtTestTry.setEditable(editable);
                txtDNITry.setMouseTransparent(!editable);
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
			
			return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            return null;
        }
    }

    private void cargarDatos(Tab newTab) {
        if (newTab == tabAlum) {
            cargarDatosProfesor();
        } else if (newTab == tabProf) {
            cargarDatosAreas();
        } else if (newTab == tabTr) {
            cargarDatosAlumnos();
            cargarDatosTests();
        } else if (newTab == tabQuest) {
            cargarDatosTests();
        } else if (newTab == tabPxA) {
            cargarDatosAreas();
            cargarDatosPreguntas();
        }
    }

    private void cargarDatosPreguntas() {
        ObservableList<String> idList = FXCollections.observableArrayList();

        String sql = "SELECT id FROM pregunta";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                idList.add("Pregunta " + id);
            }

            configureAutocomplete(txtQuestPxA, idList);

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void cargarDatosAlumnos() {
        ObservableList<String> nombreApellidoList = FXCollections.observableArrayList();

        String sql = "SELECT nombre, apellidos FROM alumno";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellidos");
                String nombreApellido = nombre + " " + apellido;
                nombreApellidoList.add(nombreApellido);
            }

            TextFields.bindAutoCompletion(txtDNITry, nombreApellidoList);

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void cargarDatosTests() {
        ObservableList<String> originalList = FXCollections.observableArrayList();

        String sql = "SELECT nombretest FROM test";
        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                originalList.add(resultSet.getString("nombretest"));
            }

            configureAutocomplete(txtTestQuest, originalList);
            configureAutocomplete(txtTestTry, originalList);

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void cargarDatosAreas() {
        ObservableList<String> areaList = FXCollections.observableArrayList();

        String sql = "SELECT nombre FROM area";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                areaList.add(nombre);
            }

            configureAutocomplete(txtAreaProf, areaList);
            configureAutocomplete(txtAreaPxA, areaList);

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void cargarDatosProfesor() {
        ObservableList<String> nombreApellidoList = FXCollections.observableArrayList();

        String sql = "SELECT nombre, apellidos FROM profesor";

        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellidos");
                String nombreApellido = nombre + " " + apellido;
                nombreApellidoList.add(nombreApellido);
            }

            configureAutocomplete(txtDNIProfAlm, nombreApellidoList);

        } catch (SQLException e) {
            showErrorBD();
        }
    }

    private void configureAutocomplete(TextField textField, ObservableList<String> data) {
        TextFields.bindAutoCompletion(textField, data);
        textField.setEditable(true);
    }

    private void showErrorBD() {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Base de Datos | EscolaVision Desktop");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("escolavision.png"));
            alert.setHeaderText(null);
            alert.setContentText("Hubo un error al acceder a la base de datos.");
            alert.showAndWait();
        });
    }

    private void cargar(Tab newTab) {
        if (newTab == tabAlum) {
            cargarDatos("alumno", listViewAlumnos);
        } else if (newTab == tabProf) {
            cargarDatos("profesor", listViewProfesores);
        } else if (newTab == tabAr) {
            cargarDatos("area", listViewArea);
        } else if (newTab == tabTr) {
            cargarDatos("intentos", listViewIntentos);
        } else if (newTab == tabQuest) {
            cargarDatos("pregunta", listViewPreguntas);
        } else if (newTab == tabPxA) {
            cargarDatos("pxa", listViewPxa);
        } else if (newTab == tabTest) {
            cargarDatos("test", listViewTest);
        }
    }

    private void limpiar(Tab newTab) {
        Platform.runLater(() -> {
            switch (newTab.getText()) {
                case "EscolaVision" -> {
                    txtUser.clear();
                    txtPassword.clear();
                    chkProf.setSelected(false);
                }
                case "Test" -> {
                    txtIdTest.clear();
                    txtTestName.clear();
                    listViewTest.getSelectionModel().clearSelection();
                    cambiarBotonEditar(btnEditarTest, "Editar", "test");
                    cambiarBotonSave(btnSaveTest, "Guardar", "test");
                }
                case "Preguntas" -> {
                    txtIdQuest.clear();
                    txtTestQuest.clear();
                    txtEnunQuest.clear();
                    txtTituloQuest.clear();
                    listViewPreguntas.getSelectionModel().clearSelection();
                    cambiarBotonEditar(btnEditarPreguntas, "Editar", "pregunta");
                    cambiarBotonSave(btnSaveQuest, "Guardar", "pregunta");
                }
                case "Alumnado" -> {
                    txtIdAlm.clear();
                    txtNameAlm.clear();
                    txtSurnameAlm.clear();
                    txtPasswordAlm.clear();
                    txtDNIProfAlm.clear();
                    txtDNIAlm.clear();
                    imgViewPicAlum.setImage(null);
                    listViewAlumnos.getSelectionModel().clearSelection();
                    cambiarBotonEditar(btnEditarAlumnos, "Editar", "alumno");
                    cambiarBotonSave(btnSaveAlum, "Guardar", "alumno");
                }
                case "Profesorado" -> {
                    txtIdProf.clear();
                    txtNameProf.clear();
                    txtSurnameProf.clear();
                    txtPasswordProf.clear();
                    txtAreaProf.clear();
                    txtDNIProf.clear();
                    imgViewFoto1.setImage(null);
                    listViewProfesores.getSelectionModel().clearSelection();
                    cambiarBotonEditar(btnEditarProfesores, "Editar", "profesor");
                    cambiarBotonSave(btnSaveProf, "Guardar", "profesor");
                }
                case "Area" -> {
                    txtIdArea.clear();
                    txtNameArea.clear();
                    txtDescripArea.clear();
                    imgViewPicArea.setImage(null);
                    listViewArea.getSelectionModel().clearSelection();
                }
                case "Pregunta x Área" -> {
                    txtIdPxA.clear();
                    txtAreaPxA.clear();
                    txtQuestPxA.clear();
                    txtEnumPxA.clear();
                    listViewPxa.getSelectionModel().clearSelection();
                    cambiarBotonEditar(btnEditarPxA, "Editar", "pxa");
                    cambiarBotonSave(btnSavePxA, "Guardar", "pxa");
                }
                case "Intentos" -> {
                    txtIdTry.clear();
                    txtTestTry.clear();
                    txtDNITry.clear();
                    txtDateTry.setValue(null);
                    txtTimeTry.clear();
                    txtResTry.clear();
                    listViewIntentos.getSelectionModel().clearSelection();
                    hboxTry.getChildren().clear();
                    cambiarBotonEditar(btnEditarIntentos, "Editar", "intentos");
                    cambiarBotonSave(btnSaveTry, "Guardar", "intentos");
                }
            }
        });
    }

    private String quiensoy = "";
    
    public void login() {
        String user = txtUser.getText();
        String password = txtPassword.getText();
        String tipo = chkProf.isSelected() ? "profesor" : "alumno";
        String query = tipo.equals("profesor")
                ? "SELECT * FROM profesor WHERE dni = ? AND claveaccesoprof = ?"
                : "SELECT * FROM alumno WHERE dni = ? AND claveaccesoalum = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, user);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellidos");
                configurarHeader(nombreCompleto);

                if (tipo.equals("profesor") && rs.getInt("isOrientador") == 1) {
                    rolAdmin();
                    quiensoy = "Admin";
                } else if (tipo.equals("profesor")) {
                    rolProfesor();
                    quiensoy = "Profesor";
                } else {
                    rolAlumno();
                    quiensoy = "Alumno";
                }

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Login fallido", "Usuario o contraseña incorrectos. Intentelo de nuevo");
            }
        } catch (SQLException e) {
            System.out.println("No se puede realizar el login: " + e.getMessage());
        }
    }
    private Label lblNombre = new Label();
    
    private void configurarHeader(String nombreCompleto) {
        hbHeader.getChildren().clear();
        
        hbHeader.getChildren().addAll(btnLogo, lblEscolavisionDesktop);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        lblNombre = new Label(nombreCompleto);
        lblNombre.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        HBox.setMargin(lblNombre, new Insets(0, 10, 0, 0));
        
        Button btnCerrarSesion = new Button("Cerrar sesión");
        btnCerrarSesion.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-start-margin: 15px;");
        ImageView imageView = new ImageView(new Image("logOut.png"));
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        btnCerrarSesion.setGraphic(imageView);
        btnCerrarSesion.setOnAction(event -> cerrarSesion(spacer, lblNombre, btnCerrarSesion));
        
        
        hbHeader.getChildren().addAll(spacer, lblNombre, btnCerrarSesion);
    }

    private void cerrarSesion(Region spacer, Label lblNombre, Button btnCerrarSesion) {
        Tab seleccionado = tabPane.getSelectionModel().getSelectedItem();
        Button editarSeleccionado = tabToButtonMap.get(seleccionado);
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("escolavision.png"));
        confirmacion.setTitle("Confirmación de cierre de sesión | EscolaVision Desktop");
        confirmacion.setHeaderText("¿Está seguro de que desea cerrar sesión?");
        if (editarSeleccionado.getText().equals("Cancelar")) {
            confirmacion.setContentText("Se perderán todos los cambios no guardados.");
        }
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            hbHeader.getChildren().removeAll(spacer, lblNombre, btnCerrarSesion);
            tabPane.getTabs().removeAll(tabTest,tabQuest,tabAlum,tabProf,tabAr,tabPxA,tabTr);
            tabPane.getTabs().add(tabHome);
        }
    }

    private void cargarDatos(String tabla, ListView<String> listView) {
        List<String> tablasPermitidas = Arrays.asList("alumno", "profesor", "area", "test", "pregunta", "pxa", "intentos");
        if (!tablasPermitidas.contains(tabla.toLowerCase())) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Tabla no válida",
                    "El nombre de la tabla especificada no es válido."
            );
            return;
        }

        String query = "SELECT * FROM " + tabla;

        try (PreparedStatement stmt = conexion.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            ObservableList<String> items = FXCollections.observableArrayList();

            String[] partes = lblNombre.getText().trim().split("\\s+");
            String nombre = partes.length > 0 ? partes[0] : null;
            String apellidos = partes.length > 2 ? partes[1] + " " + partes[2] : null;

            if (quiensoy.equals("Profesor")) {
                if ("profesor".equalsIgnoreCase(tabla) && nombre != null && apellidos != null) {
                    Profesor profesor = buscarProfesorPorNombreYApellidos(nombre, apellidos);
                    if (profesor != null) {
                        items.add(profesor.getNombre() + " " + profesor.getApellidos());
                    } else {
                        items.add("No se encontró al profesor con el nombre y apellidos especificados.");
                    }
                    Platform.runLater(() -> actualizarListView(listView, items));
                    return;
                } else if ("alumno".equalsIgnoreCase(tabla)) {
                    int idProfesor = buscarProfesorPorNombreYApellidos(nombre, apellidos).getId();
                    query = "SELECT * FROM alumno WHERE idprofesor = ?";
                    try (PreparedStatement stmtAlumnos = conexion.prepareStatement(query)) {
                        stmtAlumnos.setInt(1, idProfesor);
                        ResultSet rsAlumnos = stmtAlumnos.executeQuery();

                        while (rsAlumnos.next()) {
                            String dato = rsAlumnos.getString("nombre") + " " + rsAlumnos.getString("apellidos");
                            items.add(dato);
                        }

                        Platform.runLater(() -> actualizarListView(listView, items));
                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> mostrarAlerta(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Error al cargar alumnos",
                                "No se pudieron cargar los alumnos asociados a este profesor."
                        ));
                    }
                }else if("intentos".equalsIgnoreCase(tabla)){
                    int idProfesor = buscarProfesorPorNombreYApellidos(nombre, apellidos).getId();
                    query = "SELECT * FROM alumno WHERE idprofesor = ?";
                    try (PreparedStatement stmtAlumnos = conexion.prepareStatement(query)) {
                        stmtAlumnos.setInt(1, idProfesor);
                        ResultSet rsAlumnos = stmtAlumnos.executeQuery();

                        while (rsAlumnos.next()) {
                            int idIntento = buscarAlumnoPorDni(rsAlumnos.getString("dni")).getId();
                            query = "SELECT * FROM intentos WHERE idAlumno = ?";
                            try (PreparedStatement stmtAlumnos2 = conexion.prepareStatement(query)) {
                                stmtAlumnos2.setInt(1, idIntento);
                                ResultSet rsIntentos = stmtAlumnos2.executeQuery();

                                while (rsIntentos.next()) {
                                    String dato = "Intento " + rsIntentos.getString("id");
                                    items.add(dato);
                                }

                                Platform.runLater(() -> actualizarListView(listView, items));
                            }catch (SQLException e) {
                                e.printStackTrace();
                                Platform.runLater(() -> mostrarAlerta(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "Error al cargar intentos",
                                        "No se pudieron cargar los intentos asociados a este alumno."
                                ));
                            }
                        }

                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> mostrarAlerta(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Error al cargar alumnos",
                                "No se pudieron cargar los alumnos asociados a este profesor."
                        ));
                    }
                }
            } else if (quiensoy.equals("Alumno")) {
                if ("alumno".equalsIgnoreCase(tabla) && nombre != null && apellidos != null) {
                    Alumno alumno = buscarAlumnoPorNombreYApellidos(nombre, apellidos);
                    if (alumno != null) {
                        items.add(alumno.getNombre() + " " + alumno.getApellidos());
                    } else {
                        items.add("No se encontró al alumno con el nombre y apellidos especificados.");
                    }
                    Platform.runLater(() -> actualizarListView(listView, items));
                    return;
                }else if ("intentos".equalsIgnoreCase(tabla)) {
                    int idIntento = buscarAlumnoPorNombreYApellidos(nombre, apellidos).getId();
                    query = "SELECT * FROM intentos WHERE idAlumno = ?";
                    try (PreparedStatement stmtAlumnos = conexion.prepareStatement(query)) {
                        stmtAlumnos.setInt(1, idIntento);
                        ResultSet rsIntentos = stmtAlumnos.executeQuery();

                        while (rsIntentos.next()) {
                            String dato = "Intento " + rsIntentos.getString("id");
                            items.add(dato);
                        }

                        Platform.runLater(() -> actualizarListView(listView, items));
                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> mostrarAlerta(
                                Alert.AlertType.ERROR,
                                "Error",
                                "Error al cargar intentos",
                                "No se pudieron cargar los intentos asociados a este alumno."
                        ));
                    }
                }
            }

            while (rs.next()) {
                String dato = obtenerDatoPorTabla(tabla, rs);
                items.add(dato);
            }

            Platform.runLater(() -> actualizarListView(listView, items));

        } catch (SQLException e) {
            e.printStackTrace();
            Platform.runLater(() -> mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error al cargar datos",
                    "No se pudo cargar los datos de la base de datos."
            ));
        }
    }


    private String obtenerDatoPorTabla(String tabla, ResultSet rs) throws SQLException {
        return switch (tabla.toLowerCase()) {
            case "test" -> {
                String testId = rs.getString("id");
                String nombreTest = rs.getString("nombretest");
                yield "Test " + String.format("%03d", Integer.parseInt(testId)) + " - " + nombreTest;
            }
            case "pregunta" -> "Pregunta " + rs.getString("id");
            case "alumno", "profesor" -> rs.getString("nombre") + " " + rs.getString("apellidos");
            case "pxa" -> "Pregunta por Área " + rs.getString("id");
            case "intentos" -> "Intento " + rs.getString("id");
            case "area" -> "AREA " + rs.getString("id") + " - " + rs.getString("nombre");
            default -> rs.getString("id");
        };
    }

    private void actualizarListView(ListView<String> listView, ObservableList<String> items) {
        listView.setItems(items);
        if (!items.isEmpty() && !quiensoy.equals("Admin")) {
            if(quiensoy.equals("Profesor")){
                listViewProfesores.getSelectionModel().select(0);
            }else if(quiensoy.equals("Alumno")){
                listViewAlumnos.getSelectionModel().select(0);
            }
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
            alert.setTitle("Error | EscolaVision Desktop");
            alert.setHeaderText("Ha ocurrido un error de conexión");
            alert.setContentText(e.getMessage());
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image("escolavision.png"));
            alert.showAndWait();
            exit(0);
            return null;
        }
    }

    public Test buscarTestPorNombre(String nombre) {
        return testDAO.findByColumn("nombretest", nombre);
    }

    public Test buscarTestPorId(String id) {
        return testDAO.findByColumn("id", id);
    }
    public Pregunta buscarPreguntaPorId(int id) {
        return preguntaDAO.findById(id);
    }

    public Alumno buscarAlumnoPorDni(String dni) {
        return alumnoDAO.findByColumn("dni", dni);
    }

    public Alumno buscarAlumnoPorNombreYApellidos(String nombre, String apellidos) {
        return alumnoDAO.buscarAlumnoPorNombreYApellidos(nombre,apellidos);
    }

    public Profesor buscarProfesorPorNombreYApellidos(String nombre, String apellidos) {
        return profesorDAO.buscarProfesorPorNombreYApellidos(nombre,apellidos);
    }

    public Area buscarAreaPorNombre(String nombre) {
        return areaDAO.findByColumn("nombre", nombre);
    }

    public PxA buscarPxAPorId(int id) {
        return pxaDAO.findById(id);
    }

    public Intentos buscarIntentoPorId(int id) {
        return intentosDAO.findById(id);
    }


    private void    mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo +" | EscolaVision Desktop");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("escolavision.png"));
        alerta.showAndWait();
    }

    //MENU CONTEXTUAL
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/creditos.fxml"));
        Parent creditosView = loader.load();

        Stage creditosStage = new Stage();
        creditosStage.setTitle("Créditos | EscolaVision Desktop");

        Scene creditosScene = new Scene(creditosView);
        creditosStage.setScene(creditosScene);
        creditosStage.getIcons().add(new Image("escolavision.png"));
        creditosStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        creditosStage.showAndWait();
    }

    public void salirAplicacion() {
        if(tabHome.isSelected()){
            System.exit(0);
        }

        Tab seleccionado = tabPane.getSelectionModel().getSelectedItem();
        Button editarSeleccionado = tabToButtonMap.get(seleccionado);
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de salida | EscolaVision Desktop");
        confirmacion.setHeaderText("¿Está seguro de que desea salir?");
        Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image("escolavision.png"));
        if(editarSeleccionado.getText().equals("Cancelar")){
            confirmacion.setContentText("Se perderán todos los cambios no guardados.");
        }

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
