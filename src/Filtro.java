public class Filtro {

	// Da replace nas letras da String input e retorna a String sem as letras.
	private String deletarLetras(String texto) {
		for (int i = 0; i < texto.length(); i++) {
			if (((int) texto.charAt(i) < 48 || (int) texto.charAt(i) > 57) && ((int) texto.charAt(i) != 46)) {
				texto = texto.replaceAll("" + texto.charAt(i), "");
			}
		}
		return texto;
	}

	// Da replace nos caracteres especiais da String input e retorna a String sem os caracteres especiais.
	private String deletarEspeciais(String texto) {
		for (int i = 0; i < texto.length(); i++) {
			if (((int) texto.charAt(i) < 48 || (int) texto.charAt(i) > 57) && ((int) texto.charAt(i) != 46)) {
				texto = texto.replaceAll("\\" + texto.charAt(i), "");
			}
		}
		return texto;
	}
	// Adiciona o ZERO na frente se não tiver um número e for adicionado um ponto.
	private String adicionarZero(String texto) {
		if (texto.equals(".")) {
			texto = "0.";
		}
		return texto;
	}

	// Limita o número de ponto na String.
	private String verificaSegundoPonto(String texto) {

		int numPontos = texto.length() - texto.replaceAll("\\.", "").length();

		if (texto.length() > 0 && numPontos > 1) {
			texto = texto.substring(0, texto.lastIndexOf(".")) + texto.substring(texto.lastIndexOf(".") + 1);
		}
		return texto;
	}

	// Limita o número de casas decimais, para não conter mais que 2.
	private String limitaCasasDecimais(String texto) {

		if (texto.contains(".") && texto.lastIndexOf(".") + 3 < texto.length()) {
			texto = texto.substring(0, texto.lastIndexOf(".") + 3);
		}
		return texto;
	}

	// Limita o tamanho da String, no momento o número limite são 5 casas + 1 do ponto e + 2 das casas decimais.
	private String limitaTamanhoTexto(String texto) {

		if (texto.contains(".") && texto.length() > 8) {
			texto = texto.substring(0, 8);
		} else if (texto.contains(".") == false && texto.length() > 5) {
			texto = texto.substring(0, 5);
		}
		return texto;
	}
	
	public String filtro(String texto) {		

		texto = adicionarZero(texto);

		texto = verificaSegundoPonto(texto);

		texto = deletarLetras(texto);

		texto = deletarEspeciais(texto);

		texto = limitaCasasDecimais(texto);

		texto = limitaTamanhoTexto(texto);

		return texto;
	}

}
