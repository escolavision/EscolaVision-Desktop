package ej.Tablas;

public class Pregunta {
	private int id;
	private Test test;
	private String enunciado;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Test getTest() {
		return test;
	}
	
	public void setTest(Test test) {
		this.test = test;
	}
	
	public String getEnunciado() {
		return enunciado;
	}
	
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
}
