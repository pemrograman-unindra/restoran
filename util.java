import java.sql.Connection;
import java.sql.DriverManager;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Scanner;

public class util {

	static Connection dbConn;

	static NumberFormat nf;

	static Scanner scanner;

	public static Connection koneksiDB() {
		if (dbConn == null) {
			try {
				dbConn = DriverManager.getConnection("jdbc:sqlite:restoran.db");
				System.out.println("Berhasil terhubung ke database restoran.db...");
			} catch (Exception e) {
				System.out.println("Gagal terhubung ke database restoran.db : " + e.getMessage());
				System.exit(1);
			}
		}
		return dbConn;
	}

	public static String bacaInput() {
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		return scanner.nextLine();
	}

	public static Boolean isValidNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Integer toInteger(String str) {
		Integer res = 0;
		try {
			res = Integer.parseInt(str);
		} catch (Exception e) {
			
		}
		return res;
	}

	public static String formatAngka(Integer angka) {
		if (nf == null) {
			nf = NumberFormat.getInstance(new Locale("id", "ID"));
		}
		return nf.format(angka);
	}

	public static LinkedHashMap<String, Integer> getTableCharLength(ArrayList<LinkedHashMap<String, String>> list) {
		LinkedHashMap<String, String> judulKolom = list.get(0);
		LinkedHashMap<String, Integer> charLength = new LinkedHashMap<String, Integer>();
		for (String key : judulKolom.keySet()) {
			charLength.put(key, key.length());
		}
		for (LinkedHashMap<String, String> map : list) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key).length() > charLength.get(key)) {
					charLength.put(key, map.get(key).length());
				}
			}
		}
		return charLength;
	}

	public static void tampilkanData(ArrayList<LinkedHashMap<String, String>> list) {
		if (list.size() > 0) {
			LinkedHashMap<String, Integer> charLength = getTableCharLength(list);

			cetakGaris(charLength);

			LinkedHashMap<String, String> judulKolom = list.get(0);
			for (String key : judulKolom.keySet()) {
				System.out.print("|");
				cetakCell(charLength.get(key), key);
			}
			System.out.println("|");

			cetakGaris(charLength);

			for (LinkedHashMap<String, String> map : list) {
				for (String key : map.keySet()) {
					System.out.print("|");
					cetakCell(charLength.get(key), (map.get(key)==null || map.get(key).length()==0) ? " " : map.get(key));
				}
				System.out.println("|");
			}

			cetakGaris(charLength);
		}
	}

	static void cetakGaris(LinkedHashMap<String, Integer> charLength) {
		for (String key : charLength.keySet()) {
			System.out.print("+");
			for (Integer i = 0; i < (charLength.get(key) + 2); i++) {
				System.out.print("-");
			}
		}		
		System.out.println("+");
	}

	static void cetakCell(Integer length, String str) {
		Integer maxLength = (length - str.length() + 1);
		if (str.charAt(0) >= '1' && str.charAt(0) <= '9') {
			for (Integer i = 0; i < maxLength; i++) {
				System.out.print(" ");
			}
			System.out.print(str);
			System.out.print(" ");
		} else {
			System.out.print(" ");
			System.out.print(str);
			for (Integer i = 0; i < maxLength; i++) {
				System.out.print(" ");
			}
		}
	}
}