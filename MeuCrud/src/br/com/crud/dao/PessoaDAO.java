package br.com.crud.dao;

import java.sql.SQLException;

import br.com.crud.model.Pessoa;
import totalcross.sql.Connection;
import totalcross.sql.DriverManager;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sys.Convert;
import totalcross.sys.Settings;

public class PessoaDAO {

	private Connection con 			= null;
	PreparedStatement  pSmt	     	= null;
	PreparedStatement  pSmtInsert   = null;
	PreparedStatement  pSmtUpdate   = null;
	PreparedStatement  pSmtDelete   = null;
	PreparedStatement  pSmtSelect   = null;
	PreparedStatement  pSmtId   = null;
	
	
	public PessoaDAO() throws SQLException {
		con = DriverManager.getConnection("jdbc:sqlite:" + Convert.appendPath(Settings.appPath, "database.db"));

		pSmt = con.prepareStatement("create table if not exists pessoa(id int primary key not null, nome varchar, cpf varchar, sexo varchar, javaSkill char(1), totalCrossSkill char(1), estadoCivil varchar, nascimento datetime)");
		pSmt.executeUpdate();
	}

	public void insert(Pessoa p) throws SQLException {	
		pSmtInsert = con.prepareStatement("insert into pessoa values(?, ?, ?, ?, ?, ?, ?, ?)");
		pSmtInsert.clearParameters();
		p.setId(proximoId());
		pSmtInsert.setInt(1, p.getId());
		pSmtInsert.setString(2, p.getNome());
		pSmtInsert.setString(3, p.getCpf());
		pSmtInsert.setString(4, p.getSexo());
		pSmtInsert.setString(5, p.getJavaSkill());
		pSmtInsert.setString(6, p.getTotalCrossSkill());
		pSmtInsert.setString(7, p.getEstadoCivil());
		pSmtInsert.setString(8, p.getNascimento());
		
		pSmtInsert.executeUpdate();
	}

	public void update(Pessoa p) throws SQLException {
		pSmtUpdate = con.prepareStatement("UPDATE pessoa SET nome = ?, cpf = ?, sexo = ?, javaSkill = ?, totalCrossSkill = ?, estadoCivil = ?  WHERE id = ?");
		pSmtUpdate.clearParameters();
		pSmtUpdate.setString(1, p.getNome());
		pSmtUpdate.setString(2, p.getCpf());
		pSmtUpdate.setString(3, p.getSexo());
		pSmtUpdate.setString(4, p.getJavaSkill());
		pSmtUpdate.setString(5, p.getTotalCrossSkill());
		pSmtUpdate.setString(6, p.getEstadoCivil());
		pSmtUpdate.setInt(7, p.getId());
		
		pSmtUpdate.executeUpdate();
	}

	public void delete(Pessoa p) throws SQLException {
		pSmtDelete = con.prepareStatement("DELETE FROM pessoa WHERE id = ?");
		pSmtDelete.clearParameters();
		pSmtDelete.setInt(1, p.getId());
		
		pSmtDelete.executeUpdate();		
	}

	public String[][] listaTodos() throws SQLException {
		
		pSmtSelect = con.prepareStatement("SELECT * FROM pessoa");
		ResultSet rs = pSmtSelect.executeQuery();
		
		int quantColunas = 8;
		
	    int quantRegistros = 0;
		while(rs.next()){
			quantRegistros += 1;
		}
		
		String[][] retorno  = new String[quantRegistros][quantColunas];		
		
		rs	=  pSmtSelect.executeQuery();	
		for (int i = 0; i < quantRegistros; i++) {
			rs.next();
			for (int j = 0; j < quantColunas; j++) {
				retorno[i][j] = rs.getString(j+1);		
			}
			
		}		
		rs.close();				
		return retorno;
	}
	
	public int proximoId() throws SQLException{		
		int retorno = 1;
		
		pSmtId = con.prepareStatement("SELECT max(id) as  vId from pessoa");
		ResultSet rs = pSmtId.executeQuery();
		while(rs.next()){
			retorno = rs.getInt("vId") + 1;
		}
		rs.close();
		return retorno;
	}

}
