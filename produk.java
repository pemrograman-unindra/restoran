import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class produk {

	// migration
	public static void buatTabel() {
		kategoriProduk.buatTabel();
		String sql = ""
			+ "CREATE TABLE IF NOT EXISTS produk ("
			+ "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "  id_kategori INTEGER,"
			+ "  nomor VARCHAR(25) UNIQUE,"
			+ "  nama VARCHAR(255),"
			+ "  harga INTEGER,"
			+ "  FOREIGN KEY (id_kategori) REFERENCES kategori_produk (id)"
			+ ")";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Tabel produk berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat tabel produk : " + e.getMessage());
			System.exit(1);
		}
	}

	// seeder
	public static void buatDataAwal() {
		kategoriProduk.buatDataAwal();
		String sql = ""
			+ "INSERT OR IGNORE INTO produk (id_kategori, nomor, nama, harga) VALUES"
			+ "(1, 1, 'Cumi Saus Padang', 26000),"
			+ "(1, 2, 'Udang Saus Tiram', 25000),"
			+ "(3, 3, 'Ayam Rica-Rica', 23000),"
			+ "(4, 4, 'Cah Brokoli Seafood', 18000),"
			+ "(5, 5, 'Lemon Tea', 12000)";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Data awal kategori_produk berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat data awal kategori_produk : " + e.getMessage());
			System.exit(1);
		}
	}

	public static void menu() {
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println("🍝 Data Menu");
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("1. Tampilkan Daftar Menu 📋");
		System.out.println("2. Tambah Menu ➕");
		System.out.println("3. Ubah Menu 📝");
		System.out.println("4. Hapus Menu ❌");
		System.out.println();
		System.out.println("5. Tampilkan Daftar Kategori 📋");
		System.out.println("6. Tambah Kategori ➕");
		System.out.println("7. Ubah Kategori 📝");
		System.out.println("8. Hapus Kategori ❌");
		System.out.println();
		System.out.println("0. Kembali Ke Menu Utama ⬅️");
		System.out.println();
		System.out.print("Silakan pilih menu (0-8) : ");
		switch (util.bacaInput()) {
			case "1": tampilkan(true); break;
			case "2": tambah(); break;
			case "3": ubah(); break;
			case "4": hapus(); break;
			case "5": kategoriProduk.tampilkan(true); break;
			case "6": kategoriProduk.tambah(); break;
			case "7": kategoriProduk.ubah(); break;
			case "8": kategoriProduk.hapus(); break;
			case "0": restoran.menuUtama(); break;
			default: 
				System.out.println();
				System.out.println("Nomor menu tidak valid! silakan pilih menu dengan angka 0 sampai 8"); 
				menu(); break;
		}
	}

	public static void tampilkan(Boolean navigasi) {
		if (navigasi) {		
			System.out.println();
			System.out.println("📋 Daftar Menu");
		}
		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
		String sql = ""
			+ "SELECT "
			+ "  kp.nama kategori, "
			+ "  p.nomor nomor, "
			+ "  p.nama nama, "
			+ "  p.harga harga "
			+ "FROM "
			+ "  produk p "
			+ "  JOIN kategori_produk kp ON kp.id = p.id_kategori "
			+ "ORDER BY "
			+ "  p.nomor ASC";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
				data.put("No.", rs.getString("nomor"));
				data.put("Kategori", rs.getString("kategori"));
				data.put("Nama Menu", rs.getString("nama"));
				data.put("Harga", util.formatAngka(rs.getInt("harga")));
				list.add(data);
			}
			util.tampilkanData(list);
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menampilkan daftar menu : " + e.getMessage());
		}
		if (navigasi) {
			menu();
		}
	}

	public static void tambah() {
		System.out.println();
		System.out.println("➕ Tambah Data Menu");
		String nomor = nomorBaru();
		Integer idKategori = kategoriProduk.pilih();
		if (idKategori==0) {
			menu();
		}
		String nama = inputNama();
		Integer harga = inputHarga();
		String sql = "INSERT INTO produk (id_kategori, nomor, nama, harga) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, idKategori);
			ps.setString(2, nomor);
			ps.setString(3, nama);
			ps.setInt(4, harga);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data menu berhasil ditambahkan!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menambah data menu : " + e.getMessage());
		}
		menu();
	}

	public static void ubah() {
		System.out.println();
		System.out.println("📝 Ubah Data Menu");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String nomor = inputNomorBaru(id);
		Integer idKategori = kategoriProduk.pilih();
		if (idKategori==0) {
			menu();
		}
		String nama = inputNama();
		Integer harga = inputHarga();
		String sql = "UPDATE produk SET id_kategori = ?, nomor = ?, nama = ?, harga = ? WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, idKategori);
			ps.setString(2, nomor);
			ps.setString(3, nama);
			ps.setInt(4, harga);
			ps.setInt(5, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data menu berhasil diubah!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal merubah data menu : " + e.getMessage());
		}
		menu();
	}

	public static void hapus() {
		System.out.println();
		System.out.println("❌ Hapus Data Menu");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String sql = "DELETE FROM produk WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data menu berhasil dihapus!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menghapus data menu : " + e.getMessage());
		}
		menu();
	}

	public static Integer pilih() {
		tampilkan(false);
		System.out.print("Masukan Nomor Menu : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			return 0;
		}

		Integer id = getIdByNomor(nomor);
		if (id > 0) {
			return id;
		}
		System.out.println("Nomor menu tidak ditemukan! silakan isi sesuai nomor menu yang valid atau isi 0 untuk membatalkan"); 
		return pilih();
	}

	private static String inputNomorBaru(Integer idLama) {
		System.out.print("Masukan Nomor Menu Baru : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			menu();
		}
		if (!util.isValidNumber(nomor)) {
			System.out.println("Nomor harus berupa angka! silakan isi dengan angka yang valid");
			return inputNomorBaru(idLama);
		}

		Integer id = getIdByNomor(nomor);
		if (id>0 && id!=idLama) {
			System.out.println("Nomor sudah digunakan! silakan isi dengan angka yang lain atau isi 0 untuk membatalkan");
			return inputNomorBaru(idLama);
		}
		return nomor;
	}

	private static String inputNama() {
		System.out.print("Masukan Nama : "); String nama = util.bacaInput();
		return nama;
	}

	private static Integer inputHarga() {
		System.out.print("Masukan Harga : "); String hargaStr = util.bacaInput();
		if (!util.isValidNumber(hargaStr)) {
			System.out.println("Harga harus berupa angka! silakan isi dengan angka yang valid");
			return inputHarga();
		}
		return util.toInteger(hargaStr);
	}

	private static String nomorBaru() {
		Integer nomor = 0;
		String sql = "SELECT MAX(nomor) nomor FROM produk";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				nomor = rs.getInt("nomor");
			}
		} catch (Exception e) {}
		Integer baru = nomor + 1;
		return baru.toString();
	}

	private static Integer getIdByNomor(String nomor) {
		Integer id = 0;
		String sql = "SELECT id FROM produk WHERE nomor = ? LIMIT 1";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {}

		return id;
	}

	public static String getNamaById(Integer id) {
		String nama = "";
		String sql = "SELECT nama FROM produk WHERE id = ? LIMIT 1";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nama = rs.getString("nama");
			}
		} catch (Exception e) {}

		return nama;
	}

	public static Integer getHargaById(Integer id) {
		Integer harga = 0;
		String sql = "SELECT harga FROM produk WHERE id = ? LIMIT 1";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				harga = rs.getInt("harga");
			}
		} catch (Exception e) {}

		return harga;
	}
}

class kategoriProduk {

	// migration
	public static void buatTabel() {
		String sql = ""
			+ "CREATE TABLE IF NOT EXISTS kategori_produk ("
			+ "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "  nomor VARCHAR(25) UNIQUE,"
			+ "  nama VARCHAR(255)"
			+ ")";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Tabel kategori_produk berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat tabel kategori_produk : " + e.getMessage());
			System.exit(1);
		}
	}

	// seeder
	public static void buatDataAwal() {
		String sql = ""
			+ "INSERT OR IGNORE INTO kategori_produk (nomor, nama) VALUES"
			+ "(1, 'Seafood'),"
			+ "(2, 'Sapi'),"
			+ "(3, 'Ayam '),"
			+ "(4, 'Sayuran'),"
			+ "(5, 'Minuman')";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Data awal kategori_produk berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat data awal kategori_produk : " + e.getMessage());
			System.exit(1);
		}
	}

	public static void tampilkan(Boolean navigasi) {
		if (navigasi) {		
			System.out.println();
			System.out.println("📋 Daftar Kategori");
		}
		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
		String sql = "SELECT nomor, nama FROM kategori_produk ORDER BY nomor";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
				data.put("No.", rs.getString("nomor"));
				data.put("Nama", rs.getString("nama"));
				list.add(data);
			}
			util.tampilkanData(list);
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menampilkan daftar kategori : " + e.getMessage());
		}
		if (navigasi) {
			produk.menu();
		}
	}

	public static void tambah() {
		System.out.println();
		System.out.println("➕ Tambah Data Kategori");
		System.out.println("-----------------------------------------");
		String nomor = nomorBaru();
		String nama = inputNama();
		String sql = "INSERT INTO kategori_produk (nomor, nama) VALUES (?, ?)";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ps.setString(2, nama);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data kategori berhasil ditambahkan!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menambah data kategori : " + e.getMessage());
		}
		produk.menu();
	}

	public static void ubah() {
		System.out.println();
		System.out.println("📝 Ubah Data Kategori");
		Integer id = pilih();
		if (id==0) {
			produk.menu();
		}
		String nomor = inputNomorBaru(id);
		String nama = inputNama();
		String sql = "UPDATE kategori_produk SET nomor = ?, nama = ? WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ps.setString(2, nama);
			ps.setInt(3, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data kategori berhasil diubah!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal merubah data kategori : " + e.getMessage());
		}
		produk.menu();
	}

	public static void hapus() {
		System.out.println();
		System.out.println("❌ Hapus Data Kategori");
		Integer id = pilih();
		if (id==0) {
			produk.menu();
		}
		String sql = "DELETE FROM kategori_produk WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data kategori berhasil dihapus!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menghapus data kategori : " + e.getMessage());
		}
		produk.menu();
	}

	public static Integer pilih() {
		tampilkan(false);
		System.out.print("Masukan Nomor Kategori : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			return 0;
		}

		Integer id = getIdByNomor(nomor);
		if (id > 0) {
			return id;
		}
		System.out.println("Nomor kategori tidak ditemukan! silakan isi sesuai nomor kategori yang valid atau isi 0 untuk membatalkan"); 
		return pilih();
	}

	private static String inputNomorBaru(Integer idLama) {
		System.out.print("Masukan Nomor Kategori Baru : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			produk.menu();
		}
		if (!util.isValidNumber(nomor)) {
			System.out.println("Nomor harus berupa angka! silakan isi dengan angka yang valid");
			return inputNomorBaru(idLama);
		}

		Integer id = getIdByNomor(nomor);
		if (id > 0 && id!=idLama) {
			System.out.println("Nomor sudah digunakan! silakan isi dengan angka yang lain atau isi 0 untuk membatalkan");
			return inputNomorBaru(idLama);
		}
		return nomor;
	}

	private static String inputNama() {
		System.out.print("Masukan Nama : "); String nama = util.bacaInput();
		return nama;
	}

	private static String nomorBaru() {
		Integer nomor = 0;
		String sql = "SELECT MAX(nomor) nomor FROM kategori_produk";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				nomor = rs.getInt("nomor");
			}
		} catch (Exception e) {}
		Integer baru = nomor + 1;
		return baru.toString();
	}

	private static Integer getIdByNomor(String nomor) {
		Integer id = 0;
		String sql = "SELECT id FROM kategori_produk WHERE nomor = ? LIMIT 1";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {}

		return id;
	}
}