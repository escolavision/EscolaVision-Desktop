package ej;

import ej.DAO.TablasDAO.*;
import ej.Tablas.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

import static java.lang.System.exit;

public class miControlador implements Initializable {

	@FXML private Button btnClearTest, btnClearQuest, btnClearAlum, btnClearProf, btnClearTry, btnLogo,
			btnClearAr, btnDelTest, btnDelQuest, btnDelAlum, btnDelProf, btnDelTry, btnDelAr,
			btnLogin, btnSaveTest, btnSaveQuest, btnSaveAlum, btnSaveProf, btnSaveTry, btnSaveAr,
			btnClearPxA, btnDelPxA, btnSavePxA, btnEditarTest, btnEditarPreguntas, btnEditarAlumnos,
			btnEditarProfesores, btnEditarArea,btnEditarPxA , btnEditarIntentos;

	@FXML private ListView<String> listViewAlumnos, listViewArea, listViewIntentos, listViewPreguntas,
			listViewProfesores, listViewPxa, listViewTest;

	@FXML private CheckBox chkProf;

	@FXML private ImageView imglogo, imgViewFoto1, imgViewPicAlum, imgViewPicArea, imageViewLimpiar;

	@FXML private Tab tabAlum, tabAr, tabHome, tabProf, tabPxA, tabQuest, tabTest, tabTr;

	@FXML private TabPane tabPane;

	@FXML private TextField txtDNIAlm, txtDNIProf, txtIdAlm, txtIdArea,
			txtIdProf, txtIdQuest, txtIdTest, txtIdTry, txtNameAlm, txtNameArea, txtNameProf, txtResTry,
			txtSurnameAlm, txtSurnameProf, txtTestName, txtTimeTry, txtUser, txtIdPxA;

	@FXML private TextArea txtDescripArea, txtEnunQuest, txtEnumPxA;

	@FXML private PasswordField txtPassword, txtPasswordAlm, txtPasswordProf;

	@FXML private ComboBox<String> cbTestQuest, cbAreaProf, cbAreaPxA, cbIdTestTry, cbDNITry, cbDNIProfAlm, cbIdQuestPxA ;

	@FXML private DatePicker txtDateTry;


	private AlumnoDAO alumnoDAO;
	private TestDAO testDAO;
	private PreguntaDAO preguntaDAO;
	private ProfesorDAO profesorDAO;
	private AreaDAO areaDAO;
	private PxADAO pxaDAO;
	private IntentosDAO intentosDAO;
	private Configuracion configuracion = new Configuracion();
	private Connection conexion;
	private final Map<String, ListView<String>> listViewMap = new HashMap<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarMap();
		inicializarConexion();
		inicializarLogin();
		inicializarDAO();
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

	private void inicializarLogin(){
		if (conexion != null) {
			imglogo.setImage(new Image("escolavision.png"));
			pantallaPrincipal();
			inicializarEventos();
		}
	}

	private void inicializarDAO(){
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


		btnLogin.setOnAction(e -> {
			login();
		});

		txtPassword.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				btnLogin.fire();
			}
		});

		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
			limpiar(newTab);
			cargar(newTab);
			cargarComboBox(newTab);
		});

		cbIdQuestPxA.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null ) {
				if(!newValue.trim().isEmpty()){
					Pregunta pregunta = buscarPreguntaPorId(Integer.parseInt(newValue));
					if(pregunta != null) {
						txtEnumPxA.setText(pregunta.getEnunciado());
					}
				}
			}
		});

		listViewMap.forEach((tipo, listView) -> listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				comprobarEditar(tipo);
				switch (tipo) {
					case "test" : {
						Test test = buscarTestPorNombre(newValue);
						txtIdTest.setText(""+test.getId());
						txtTestName.setText(test.getNombre());
						break;
					}
					case "pregunta" : {
						Pregunta pregunta = buscarPreguntaPorId(Integer.parseInt(newValue));
						txtIdQuest.setText(""+pregunta.getId());
						cbTestQuest.setValue(""+pregunta.getTest().getNombre());
						txtEnunQuest.setText(pregunta.getEnunciado());
						break;
					}
					case "alumno": {
						Alumno alumno = buscarAlumnoPorDni(newValue);
						txtIdAlm.setText(""+alumno.getId());
						txtNameAlm.setText(alumno.getNombre());
						txtSurnameAlm.setText(alumno.getApellidos());
						txtDNIAlm.setText(alumno.getDni());
						cbDNIProfAlm.setValue(alumno.getProfesor().getDni());
						txtPasswordAlm.setText(alumno.getClaveaccesoalumno());
						if(!Objects.equals(alumno.getFoto(), "")) {
							imgViewPicAlum.setImage(base64ToImage(alumno.getFoto()));
						}else{
							imgViewPicAlum.setImage(null);
						}
						break;
					}
					case "profesor": {
						Profesor profesor = buscarProfesorPorDni(newValue);
						txtIdProf.setText(""+profesor.getId());
						txtNameProf.setText(profesor.getNombre());
						txtSurnameProf.setText(profesor.getApellidos());
						txtDNIProf.setText(profesor.getDni());
						txtPasswordProf.setText(profesor.getClaveaccesoprof());
						cbAreaProf.setValue(profesor.getArea().getNombre());
						if(!Objects.equals(profesor.getFoto(), "")) {
							imgViewFoto1.setImage(base64ToImage(profesor.getFoto()));
						}else{
							imgViewFoto1.setImage(null);
						}
						break;
					}
					case "area" : {
						Area area = buscarAreaPorNombre(newValue);
						txtNameArea.setText(area.getNombre());
						txtDescripArea.setText(area.getDescripcion());
						txtIdArea.setText(""+area.getId());
						if(!Objects.equals(area.getLogo(), "")) {
							imgViewPicArea.setImage(base64ToImage(area.getLogo()));
						}else{
							imgViewPicArea.setImage(null);
						}
						break;
					}
					case "pxa" : {
						PxA pxa = buscarPxAPorId(Integer.parseInt(newValue));
						txtIdPxA.setText(""+pxa.getId());
						cbAreaPxA.setValue(pxa.getArea().getNombre());
						cbIdQuestPxA.setValue(""+pxa.getPregunta().getId());
						txtEnumPxA.setText(pxa.getPregunta().getEnunciado());
						break;
					}
					case "intentos" : {
						Intentos intento = buscarIntentoPorId(Integer.parseInt(newValue));
						txtIdTry.setText(""+intento.getId());
						cbIdTestTry.setValue(intento.getTest().getNombre());
						cbDNITry.setValue(intento.getAlumno().getDni());
						txtDateTry.setValue(intento.getFecha());
						txtTimeTry.setText(intento.getHora());
						txtResTry.setText(intento.getResultados());
						break;
					}
					default: {}
				}
			}
		}));

		btnEditarTest.setOnAction(e -> {
			if (btnEditarTest.getText().equals("Editar") || btnEditarTest.getText().equals("_Editar")) {
				btnEditarTest.setText("Cancelar");
			} else {
				btnEditarTest.setText("Editar");
			}
			comprobarEditar("test");
		});

		btnEditarPreguntas.setOnAction(e -> {
			if (btnEditarPreguntas.getText().equals("Editar") || btnEditarPreguntas.getText().equals("_Editar")) {
				btnEditarPreguntas.setText("Cancelar");
			} else {
				btnEditarPreguntas.setText("Editar");
			}
			comprobarEditar("pregunta");
		});

		btnEditarAlumnos.setOnAction(e -> {
			if (btnEditarAlumnos.getText().equals("Editar") || btnEditarAlumnos.getText().equals("_Editar")) {
				btnEditarAlumnos.setText("Cancelar");
			} else {
				btnEditarAlumnos.setText("Editar");
			}
			comprobarEditar("alumno");
		});

		btnEditarProfesores.setOnAction(e -> {
			if (btnEditarProfesores.getText().equals("Editar") || btnEditarProfesores.getText().equals("_Editar")) {
				btnEditarProfesores.setText("Cancelar");
			} else {
				btnEditarProfesores.setText("Editar");
			}
			comprobarEditar("profesor");
		});

		btnEditarArea.setOnAction(e -> {
			if (btnEditarArea.getText().equals("Editar") || btnEditarArea.getText().equals("_Editar")) {
				btnEditarArea.setText("Cancelar");
			} else {
				btnEditarArea.setText("Editar");
			}
			comprobarEditar("area");
		});

		btnEditarIntentos.setOnAction(e -> {
			if (btnEditarIntentos.getText().equals("Editar") || btnEditarIntentos.getText().equals("_Editar")) {
				btnEditarIntentos.setText("Cancelar");
			} else {
				btnEditarIntentos.setText("Editar");
			}
			comprobarEditar("intentos");
		});


		ajustarImagenes();
	}

	private void comprobarEditar(String tipo) {
		switch (tipo) {
			case "test": {
				txtTestName.setEditable(btnEditarTest.getText().equals("Cancelar"));
				break;
			}
			case "pregunta": {
				boolean editable = btnEditarPreguntas.getText().equals("Cancelar");
				cbTestQuest.setMouseTransparent(!editable);
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
				boolean editable = btnEditarArea.getText().equals("Cancelar");
				cbAreaPxA.setMouseTransparent(!editable);
				cbIdQuestPxA.setMouseTransparent(!editable);
				txtEnumPxA.setEditable(editable);
				break;
			}
			case "intentos": {
				boolean editable = btnEditarIntentos.getText().equals("Cancelar");
				cbIdTestTry.setMouseTransparent(!editable);
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
				File file = db.getFiles().get(0);
				Image image = new Image(file.toURI().toString());
				imageView.setImage(image);
				success = true;
			}

			event.setDropCompleted(success);
			event.consume();
		});
	}
	private Image base64ToImage(String base64Image) {
		try {
			// Decodificar la cadena Base64
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);

			// Convertir los bytes a un InputStream
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

			// Convertir el InputStream a BufferedImage
			BufferedImage bufferedImage = ImageIO.read(bis);

			// Convertir BufferedImage a Image de JavaFX
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);

			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void cargarComboBox(Tab newTab){
		if (newTab == tabAlum) {
			cargarDatosComboBoxProfesor();
		} else if (newTab == tabProf) {
			cargarDatosComboBoxAreas();
		} else if (newTab == tabTr) {
			cargarDatosComboBoxTests();
			cargarDatosComboBoxAlumnos();
		}else if (newTab == tabQuest) {
			cargarDatosComboBoxTests();
		}else if (newTab == tabPxA) {
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

			cbIdQuestPxA.setItems(idList);

		} catch (SQLException e) {
			e.printStackTrace();
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

			cbDNITry.setItems(dniList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void cargarDatosComboBoxTests() {
		ObservableList<String> idList = FXCollections.observableArrayList();

		String sql = "SELECT nombretest FROM test";

		try (PreparedStatement statement = conexion.prepareStatement(sql);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String nombre = resultSet.getString("nombretest");
				idList.add(nombre);
			}

			cbTestQuest.setItems(idList);
			cbIdTestTry.setItems(idList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
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

			cbAreaProf.setItems(area);
			cbAreaPxA.setItems(area);

		} catch (SQLException e) {
			e.printStackTrace();
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

			cbDNIProfAlm.setItems(dniList);

		} catch (SQLException e) {
			e.printStackTrace();
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
				cbTestQuest.setValue("");
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
				cbIdTestTry.setValue("");
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
		String contraseña = txtPassword.getText();
		String tipo = chkProf.isSelected() ? "profesor" : "alumno";
		String query = "";
		if(tipo.equals("profesor")) {
			query = "SELECT * FROM " + tipo + " WHERE dni = ? AND claveaccesoprof = ?";
		}else{
			query = "SELECT * FROM " + tipo + " WHERE dni = ? AND claveaccesoalum = ?";
		}

		try (PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, user);
			stmt.setString(2, contraseña);

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
				mostrarAlerta(Alert.AlertType.ERROR,"Login fallido","Usuario o contraseña incorrectos. Intentelo de nuevo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudo cargar los datos de la base de datos.");
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
		} catch (SQLException e) {;
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
	// Id del test
	public Test buscarTestPorId(int id) {
		return testDAO.findById(id);
	}

	// Id de la pregunta
	public Pregunta buscarPreguntaPorId(int id) {
		return preguntaDAO.findById(id);
	}

	// Dni del alumno
	public Alumno buscarAlumnoPorDni(String dni) {
		return alumnoDAO.findByColumn("dni", dni);
	}
	//Id del alumno
	public Alumno buscarAlumnoPorId(int id) {
		return alumnoDAO.findById(id);
	}

	// Dni del profesor
	public Profesor buscarProfesorPorDni(String dni) {
		return profesorDAO.findByColumn("dni", dni);
	}
	// Id del profesor
	public Profesor buscarProfesorPorId(int id) {
		return profesorDAO.findById(id);
	}
	// Nombre del área
	public Area buscarAreaPorNombre(String nombre) {
		return areaDAO.findByColumn("nombre", nombre);
	}	// Id del area
	public Area buscarAreaPorId(int id) {
		return areaDAO.findById(id);
	}
	// Id del PxA
	public PxA buscarPxAPorId(int id) {
		return pxaDAO.findById(id);
	}
	// Id del intento
	public Intentos buscarIntentoPorId(int id) {
		return intentosDAO.findById(id);
	}
	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
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
		System.exit(0);
	}
}