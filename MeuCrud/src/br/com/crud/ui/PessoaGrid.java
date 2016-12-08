package br.com.crud.ui;

import java.sql.SQLException;

import br.com.crud.business.Constantes;
import br.com.crud.dao.PessoaDAO;
import br.com.crud.model.Pessoa;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

//Tela de listagem de pessoas
public class PessoaGrid extends Container{
	private static final int NOVO_BTN_ID = 100;
	private static final int EDITAR_BTN_ID = 101;
	private static final int EXCLUIR_BTN_ID = 102;
	private static PessoaDAO dao = null; 
	private Grid pessoaGrid;
	
	public PessoaGrid() throws SQLException{
		dao = new PessoaDAO();
	}
	
	//Método que inicializa controles de interfaces de usuario
	public void initUI(){
		Button novoButton = null;
		Button editarButton = null;
		Button excluirButton = null;
		
		try {
			novoButton = new Button("Novo", new Image("novo.png"), BOTTOM, 20);
		} catch (ImageException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			editarButton = new Button("Editar", new Image("editar.png"), BOTTOM, 20);
		} catch (ImageException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			excluirButton = new Button("Excluir", new Image("deletar.png"), BOTTOM, 20);
		} catch (ImageException | IOException e1) {
			e1.printStackTrace();
		}
		
		novoButton.appId = NOVO_BTN_ID;
		editarButton.appId = EDITAR_BTN_ID;
		excluirButton.appId	= EXCLUIR_BTN_ID;
		
		add(excluirButton, RIGHT - 2, TOP + 2, PREFERRED + 5, PREFERRED + 5 );
		add(editarButton, BEFORE - 2, SAME, SAME, SAME);
		add(novoButton, BEFORE - 2, SAME, SAME, SAME);
		
		pessoaGrid = new Grid(new String[]{"Id", "Nome", "Cpf"}, new int[]{-10, -30, -30}, new int[]{RIGHT, LEFT, CENTER}, false);
		
		add(pessoaGrid, LEFT, AFTER + 2, FILL - 2, FILL - 2);
		try {
			pessoaGrid.setItems(dao.listaTodos());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Método que controla eventos dos botões
	public void onEvent(Event event){
		switch (event.type) {
		case ControlEvent.PRESSED:
			switch (((Control) event.target).appId) {
				case NOVO_BTN_ID:
					new PessoaForm(dao, new Pessoa(), Constantes.INSERT).popupNonBlocking();				
					break;
				case EDITAR_BTN_ID:{
					if(verificaSelecao()){
						new PessoaForm(dao, registroSelecionado(), Constantes.UPDATE).popupNonBlocking();
					}
					break;
				}
				case EXCLUIR_BTN_ID:{
					if(verificaSelecao()){
						try {
							dao.delete(registroSelecionado());
						} catch (SQLException e) {
							e.printStackTrace();
						}
						try {
							pessoaGrid.setItems(dao.listaTodos());
						} catch (SQLException e) {
							e.printStackTrace();
						}
						pessoaGrid.setSelectedIndex(-1);
					}
					break;
				}
			}			
			break;
		case ControlEvent.WINDOW_CLOSED:{
			if(event.target instanceof PessoaForm){
				try {
					pessoaGrid.setItems(dao.listaTodos());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				pessoaGrid.setSelectedIndex(-1);
			}
		}
		break;
		}		
	}
	
	private boolean verificaSelecao(){
		
		boolean selecionouRegistro = pessoaGrid.getSelectedIndex() >= 0;
		
		if(!selecionouRegistro){
			new MessageBox("Atenção", "Selecione um registro para continuar").popup();;
		}		
		return selecionouRegistro;
	}
	
	private Pessoa registroSelecionado(){
		
		Pessoa p = new Pessoa();
		String[] colunas = pessoaGrid.getSelectedItem();
		
		try{
			p.setId(Convert.toInt(colunas[0]));
		}catch(InvalidNumberException e){
			e.printStackTrace();
		}
		p.setNome(colunas[1]);
		p.setCpf(colunas[2]);
		p.setSexo(colunas[3]);
		p.setJavaSkill(colunas[4]);
		p.setTotalCrossSkill(colunas[5]);
		p.setEstadoCivil(colunas[6]);
		p.setNascimento(colunas[7]);
		return p;
	}
	
}
