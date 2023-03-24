import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class Conversor {

	double[] moedas;

	Conversor() {
		atualizaMoedas();
	}

	
	public String converte(Filtro filtro, String inputQuantidade, int bandeirasDe, int bandeirasPara) {

		String filtroDoFiltro = "";
		filtroDoFiltro = filtro.filtro(inputQuantidade);

		try {
			if (filtroDoFiltro.length() > 0) {
				String resultado = converteMoedas(bandeirasDe, bandeirasPara, Double.parseDouble(filtroDoFiltro));
				return resultado;
			}
		} catch (Exception e) {
			System.out.println("BUG - Limpando campo texto!");
			return "";
		}
		return "";
	}

	public void atualizaMoedas() {

		moedas = new double[6];
		String valorMoedas = "";

		try {
			valorMoedas = jsonGetRequest("http://www.floatrates.com/daily/usd.json");

		} catch (Exception e) {
			valorMoedas = "";
			e.printStackTrace();
			System.out.println("Site não conectou.");
		}

		salvandoMoedas(valorMoedas);
		carregandoMoedas();

	}

	private void salvandoMoedas(String valorMoedas) {

		if (valorMoedas != null && !valorMoedas.equals("")) {
			try {
				FileWriter arquivoTexto = new FileWriter("valorMoedas.txt");
				arquivoTexto.write(valorMoedas);
				arquivoTexto.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void carregandoMoedas() {

		File arquivoTexto = new File("valorMoedas.txt");
		String json = "";

		try {
			Scanner sc = new Scanner(arquivoTexto);
			while (sc.hasNextLine()) {
				json = sc.nextLine();
			}
			sc.close();
			JSONObject jsonObj = new JSONObject(json);

			moedas[0] = Double.valueOf((String) jsonObj.getJSONObject("brl").get("rate").toString());
			moedas[1] = 1.0;
			moedas[2] = Double.valueOf((String) jsonObj.getJSONObject("eur").get("rate").toString());
			moedas[3] = Double.valueOf((String) jsonObj.getJSONObject("gbp").get("rate").toString());
			moedas[4] = Double.valueOf((String) jsonObj.getJSONObject("ars").get("rate").toString());
			moedas[5] = Double.valueOf((String) jsonObj.getJSONObject("clp").get("rate").toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String converteMoedas(int de, int para, double valor) {

		try {
			BigDecimal paraBd = BigDecimal.valueOf(moedas[para]);
			BigDecimal deBd = BigDecimal.valueOf(moedas[de]);
			BigDecimal valorBd = BigDecimal.valueOf(valor);
			paraBd = paraBd.divide(deBd, MathContext.DECIMAL32);
			paraBd = paraBd.multiply(valorBd);
			String cambio = paraBd.toString();

			if (cambio.contains(".") && cambio.lastIndexOf(".") + 3 < cambio.length()) {
				cambio = cambio.substring(0, cambio.lastIndexOf(".") + 3);
			}

			if (cambio.contains(".") && cambio.indexOf(".") + 3 > cambio.length()) {
				cambio += "0";
			}
			return cambio;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro no valor.");
		}
		return "";
	}

	private static String streamToString(InputStream inputStream) {
		Scanner sc = new Scanner(inputStream, "UTF-8");		
		String text = sc.useDelimiter("\\Z").next();
		sc.close();
		return text;		
	}

	public static String jsonGetRequest(String site) {
		String json = null;
		try {
			URL url = new URL(site);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			InputStream inStream = connection.getInputStream();
			json = streamToString(inStream); // input stream to string
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return json;
	}
}