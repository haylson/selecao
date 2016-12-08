package br.com.crud.main;

import java.sql.SQLException;

import br.com.crud.ui.PessoaGrid;
import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.gfx.Color;

//Classe que estende MainWindow chamada para inicializar a aplicação
public class Main extends MainWindow {

	public Main() throws SQLException {
		super("Cadastro de Pessoas", VERTICAL_GRADIENT);
		gradientTitleStartColor = 0;
		gradientTitleEndColor = 0xAAA;

		setUIStyle(Settings.Android);

		Settings.uiAdjustmentsBasedOnFontHeight = true;
		setBackColor(Color.brighter(Color.WHITE, Color.HALF_STEP));
		swap(new PessoaGrid());
	}
}
