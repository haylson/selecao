package br.com.crud.ui;



import java.sql.SQLException;

import br.com.crud.business.Constantes;
import br.com.crud.dao.PessoaDAO;
import br.com.crud.model.Pessoa;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;
import totalcross.ui.Check;
import totalcross.ui.ComboBox;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.Radio;
import totalcross.ui.RadioGroupController;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;
import totalcross.util.Date;

//tela de formulario de pessoas
public class PessoaForm extends Window{
	
	private static final int SALVAR_BTN_ID = 100;
	private static final int RETORNAR_BTN_ID = 101;
	private final PessoaDAO dao;
	
	private Edit nomeEdit;
	private Edit cpfEdit;
	private Edit nascimentoEdit;
	private RadioGroupController controller;
	private Radio masculino;
	private Radio feminino;
	private Check javaCheck;
	private Check totalCrossCheck;
	private ComboBox estadoCivilComboBox;
	
	public PessoaForm(PessoaDAO dao, Pessoa pessoa, int editMode){
		this.dao = dao;
		this.appObj = pessoa;
		this.appId = editMode;
	}
	
	protected void postPopup(){
		super.postPopup();
		initUI();
	}
	
	//Método que inicializa controles de interfaces de usuario
	public void initUI(){
		super.initUI();
		
		Button salvarButton = null;
		Button retornarButton = null;
		
		try {
			salvarButton = new Button("Salvar", new Image("salvar.png"), BOTTOM, 20);
		} catch (ImageException | IOException e) {
			e.printStackTrace();
		}
		
		try {
			retornarButton = new Button("Retornar", new Image("retornar.png"), BOTTOM, 20);
		} catch (ImageException | IOException e) {
			e.printStackTrace();
		}
		
		Edit idEdit = new Edit("9999");
		
		//Componentes Edits
		nomeEdit = new Edit();
		
		//Edit com mascara
		cpfEdit = new Edit("999.999.999-99");
		cpfEdit.setMode(Edit.NORMAL, true);
		cpfEdit.setValidChars("0123456789");
		
		//Edit Data
		nascimentoEdit = new Edit("99/99/9999");
		nascimentoEdit.setMode(Edit.DATE, true);
		nascimentoEdit.setValidChars("0123456789");
		
		//Componente Combobox
		estadoCivilComboBox = new ComboBox(new String[]{"Casado","Solteiro","Divorciado"});
		
		//componentes RadioGroup e Radio
		controller = new RadioGroupController();
		masculino = new Radio("masculino", controller);
		feminino = new Radio("feminino", controller);

		//componente Check
		javaCheck = new Check("Java");
		totalCrossCheck = new Check("TotalCross");
		
		//Id dos botoes
		salvarButton.appId = SALVAR_BTN_ID;
		retornarButton.appId = RETORNAR_BTN_ID;
		
		//recebe os valores do objeto e faz o binding dos valores dos componentes na tela
		idEdit.setEditable(false);
		idEdit.setText(Convert.toString(((Pessoa) appObj).getId()));
		
		nomeEdit.setText(((Pessoa) appObj).getNome());
		
		cpfEdit.setText(((Pessoa) appObj).getCpf());
		
		if(((Pessoa) appObj).getNascimento() != null){
			nascimentoEdit.setText(((Pessoa) appObj).getNascimento().toString());
		}
		
		controller.setSelectedItem(((Pessoa) appObj).getSexo());
		
		String valorJava = ((Pessoa) appObj).getJavaSkill();
		String valorTC = ((Pessoa) appObj).getTotalCrossSkill();
		
		if(valorJava.equalsIgnoreCase("0")){
			javaCheck.setChecked(false);
		} else {
			javaCheck.setChecked(true);
		}
		
		if(valorTC.equalsIgnoreCase("0")){
			totalCrossCheck.setChecked(false);
		} else {
			totalCrossCheck.setChecked(true);
		}
		
		String estadoCivil = ((Pessoa) appObj).getEstadoCivil();
		estadoCivilComboBox.setSelectedItem(estadoCivil);
		
		//adiciona os componentes na tela
		add(retornarButton, RIGHT - 2, TOP + 2, PREFERRED + 2, PREFERRED);
		add(salvarButton, BEFORE - 2, SAME, SAME, SAME);
		
		add(new Label("Nome:"), LEFT, AFTER);
		add(nomeEdit, SAME, AFTER);
		
		add(new Label("Cpf:"), LEFT, AFTER);
		add(cpfEdit, SAME, AFTER);
		
		add(new Label("Nascimento:"), LEFT, AFTER);
		add(nascimentoEdit, SAME, AFTER);
		
		add(new Label("Estado Civil:"), LEFT, AFTER);
		add(estadoCivilComboBox, SAME, AFTER);
		
		add(new Label("Sexo:"), LEFT, AFTER);
		add(masculino, AFTER, SAME);
		add(feminino, AFTER, SAME);
		
		add(new Label("Skills:"), LEFT, AFTER);
		add(javaCheck, LEFT, AFTER);
		add(totalCrossCheck, AFTER, SAME);
	}
	
	//Método que controla eventos dos botões
	public void onEvent(Event event){
		switch (event.type) {
			case ControlEvent.PRESSED:{
				switch (((Control) event.target).appId) {
					case SALVAR_BTN_ID:{
						if(validacaoFormulario()){
							switch (appId) {
								case Constantes.INSERT:{
									try {
										dao.insert((Pessoa) appObj);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									break;
								}
								case Constantes.UPDATE:{
									try {
										dao.update((Pessoa) appObj);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									break;
								}
							}
							unpop();
						}
						break;
					}
					case RETORNAR_BTN_ID:{
						unpop();
						break;
					}
				}
			}						
		}
	}
	
	public boolean validacaoFormulario(){
		
		//validando nome
		if(!(this.nomeEdit.getLength() >= 3)){
			new MessageBox("Erro", "Preencha um nome válido").popup();
			return false;
		}
		
		((Pessoa) appObj).setNome(this.nomeEdit.getText());
		
		//validando cpf
		if(!(this.cpfEdit.getLength() == 11)){
			new MessageBox("Erro", "Preencha o CPF completo").popup();
			return false;
		}
		
		((Pessoa) appObj).setCpf(this.cpfEdit.getText());
		
		//validando data de nascimento
		String dataNascimento = this.nascimentoEdit.getText();
		
		if(dataNascimento.equalsIgnoreCase("")){
			new MessageBox("Erro", "Informe a data de Nascimento").popup();
			return false;
		}
		
		boolean validado = validarDataNascimento(dataNascimento);
		
		if(validado){
			((Pessoa) appObj).setNascimento(dataNascimento);	
		} else {
			return false;
		}
		
		//validando estado civil
		String estadoCivil = (String) this.estadoCivilComboBox.getSelectedItem();
		
		if(estadoCivil.equalsIgnoreCase("")){
			new MessageBox("Erro", "Informe o Estado Civil").popup();
			return false;
		} else {
			((Pessoa) appObj).setEstadoCivil(estadoCivil);
		}
		
		//validando sexo
		if(this.controller.getSelectedItem() == null){
			new MessageBox("Erro", "Marque a opção sexo").popup();
			return false;
		} else {
			String sexo = this.controller.getSelectedItem().getText();
			((Pessoa) appObj).setSexo(sexo);	 
		}
		
		//checkers podem ou nao estar marcados
		if(this.javaCheck.isChecked()){
			((Pessoa) appObj).setJavaSkill("1");
		} else {
			((Pessoa) appObj).setJavaSkill("0");
		}
		
		if(this.totalCrossCheck.isChecked()){
			((Pessoa) appObj).setTotalCrossSkill("1");
		} else {
			((Pessoa) appObj).setTotalCrossSkill("0");
		}
		
		return true;
	}
	
	private boolean validarDataNascimento(String dataNascimento){
		
		int data = 0;
		int mes = 0;
		int ano = 0;
		
		data = retornarData(dataNascimento);
		
		if(data >= 32){
			new MessageBox("Erro", "Preencha uma data válida").popup();
			return false;
		}

		mes = retornarMes(dataNascimento);
		
		if(mes >= 13){
			new MessageBox("Erro", "Preencha um mês válido").popup();
			return false;
		}
			
		ano = retornarAno(dataNascimento);
			
		if(ano > new Date().getYear()){
			new MessageBox("Erro", "Preencha um ano válido").popup();
			return false;
		}

		return true;
	}
	
	private int retornarData(String dataNascimento){
		int data = 0;
		
		try {
			data = Convert.toInt(dataNascimento.substring(0, 2));
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	private int retornarMes(String dataNascimento){
		
		int mes = 0;
		
		try {
			mes = Convert.toInt(dataNascimento.substring(3, 5));
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		
		return mes;
	}
	
	private int retornarAno(String dataNascimento){
		
		int ano = 0;
		
		try {
			ano = Convert.toInt(dataNascimento.substring(6, 10));
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}

		return ano;
	}
	
	/*private Date converterStringEmData(String dataNascimento){
		
		int data = 0;
		int mes = 0;
		int ano = 0;
		
		data = retornarData(dataNascimento);
		mes = retornarMes(dataNascimento);
		ano = retornarAno(dataNascimento);
		
		Date dataConvertida = null;
		
		try {
			dataConvertida = new Date(data, mes, ano);
		} catch (InvalidDateException e) {
			e.printStackTrace();
		}
		
		return dataConvertida;
	}*/

}
