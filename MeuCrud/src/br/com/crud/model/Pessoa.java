package br.com.crud.model;

public class Pessoa {
	private int	id;
	private String nome;
	private String nascimento;
	private String cpf;
	private String sexo;
	private String javaSkill;
	private String totalCrossSkill;
	private String estadoCivil;
	
	public Pessoa(){
		javaSkill = "0";
		totalCrossSkill = "0";
		estadoCivil = "";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getJavaSkill() {
		return javaSkill;
	}
	public void setJavaSkill(String javaSkill) {
		this.javaSkill = javaSkill;
	}
	public String getTotalCrossSkill() {
		return totalCrossSkill;
	}
	public void setTotalCrossSkill(String totalCrossSkill) {
		this.totalCrossSkill = totalCrossSkill;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getNascimento() {
		return nascimento;
	}
	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}
	
}
