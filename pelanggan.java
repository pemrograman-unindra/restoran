import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class pelanggan {

	// migration
	public static void buatTabel() {
		String sql = ""
			+ "CREATE TABLE IF NOT EXISTS pelanggan ("
			+ "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "  nomor VARCHAR(25) UNIQUE,"
			+ "  nama VARCHAR(255),"
			+ "  jenis_kelamin VARCHAR(10)"
			+ ")";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Tabel pelanggan berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat tabel pelanggan : " + e.getMessage());
			System.exit(1);
		}
	}

	// seeder
	public static void buatDataAwal() {
		String sql = ""
			+ "INSERT OR IGNORE INTO pelanggan (nomor, nama, jenis_kelamin) VALUES"
			+ "(1, 'Umum', null),"
			+ "(2, 'Ana', 'Perempuan'),"
			+ "(3, 'Budi', 'Laki-laki'),"
			+ "(4, 'Chandra', 'Laki-laki'),"
			+ "(5, 'Dian', 'Perempuan')";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Data awal pelanggan berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat data awal pelanggan : " + e.getMessage());
			System.exit(1);
		}
	}

	public static void menu() {
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println("👤 Data Pelanggan");
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("1. Tampilkan Daftar Pelanggan 📋");
		System.out.println("2. Tambah Data Pelanggan ➕");
		System.out.println("3. Ubah Data Pelanggan 📝");
		System.out.println("4. Hapus Data Pelanggan ❌");
		System.out.println();
		System.out.println("0. Kembali Ke Menu Utama ⬅️");
		System.out.println();
		System.out.print("Silakan pilih menu (0-4) : ");
		switch (util.bacaInput()) {
			case "1": tampilkan(true); break;
			case "2": tambah(); break;
			case "3": ubah(); break;
			case "4": hapus(); break;
			case "0": restoran.menuUtama(); break;
			default: 
				System.out.println();
				System.out.println("Nomor menu tidak valid! silakan pilih menu dengan angka 0 sampai 4"); 
				menu(); break;
		}
	}

	public static void tampilkan(Boolean navigasi) {
		if (navigasi) {		
			System.out.println();
			System.out.println("📋 Daftar Pelanggan");
		}
		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
		String sql = "SELECT nomor, nama, jenis_kelamin FROM pelanggan ORDER BY nomor";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
				data.put("No.", rs.getString("nomor"));
				data.put("Nama", rs.getString("nama"));
				data.put("Jenis Kelamin", rs.getString("jenis_kelamin"));
				list.add(data);
			}
			util.tampilkanData(list);
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menampilkan daftar pelanggan : " + e.getMessage());
		}
		if (navigasi) {
			menu();
		}
	}

	public static void tambah() {
		System.out.println();
		System.out.println("➕ Tambah Data Pelanggan");
		System.out.println("-----------------------------------------");
		String nomor = nomorBaru();
		String nama = inputNama();
		String jenisKelamin = inputJenisKelamin();
		String sql = "INSERT INTO pelanggan (nomor, nama, jenis_kelamin) VALUES (?, ?, ?)";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ps.setString(2, nama);
			ps.setString(3, jenisKelamin);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data pelanggan berhasil ditambahkan!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menambah data pelanggan : " + e.getMessage());
		}
		menu();
	}

	public static void ubah() {
		System.out.println();
		System.out.println("📝 Ubah Data Pelanggan");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String nomor = inputNomorBaru(id);
		String nama = inputNama();
		String jenisKelamin = inputJenisKelamin();
		String sql = "UPDATE pelanggan SET nomor = ?, nama = ?, jenis_kelamin = ? WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setString(1, nomor);
			ps.setString(2, nama);
			ps.setString(3, jenisKelamin);
			ps.setInt(4, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data pelanggan berhasil diubah!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal merubah data pelanggan : " + e.getMessage());
		}
		menu();
	}

	public static void hapus() {
		System.out.println();
		System.out.println("❌ Hapus Data Pelanggan");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String sql = "DELETE FROM pelanggan WHERE id = ?";
		try (PreparedStatement ps = util.koneksiDB().prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println();
			System.out.println("Data pelanggan berhasil dihapus!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menghapus data pelanggan : " + e.getMessage());
		}
		menu();
	}

	public static Integer pilih() {
		tampilkan(false);
		System.out.print("Masukan Nomor Pelanggan : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			return 0;
		}

		Integer id = getIdByNomor(nomor);
		if (id > 0) {
			return id;
		}
		System.out.println("Nomor pelanggan tidak ditemukan! silakan isi sesuai nomor pelanggan yang valid atau isi 0 untuk membatalkan"); 
		return pilih();
	}

	static String inputNomorBaru(Integer idLama) {
		System.out.print("Masukan Nomor Pelanggan Baru : "); String nomor = util.bacaInput();
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

	static String inputNama() {
		System.out.print("Masukan Nama : "); String nama = util.bacaInput();
		return nama;
	}

	static String inputJenisKelamin() {
		System.out.print("Masukan Jenis Kelamin (Laki-laki/Perempuan) : "); String jenisKelamin = util.bacaInput();
		if (Arrays.asList("Laki-laki", "Perempuan").contains(jenisKelamin)) {
			return jenisKelamin;
		}
		System.out.println("Jenis kelamin tidak valid! silakan isi jenis kelamin dengan Laki-laki atau Perempuan"); 
		return inputJenisKelamin();
	}

	static String nomorBaru() {
		Integer nomor = 0;
		String sql = "SELECT MAX(nomor) nomor FROM pelanggan";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				nomor = rs.getInt("nomor");
			}
		} catch (Exception e) {}
		Integer baru = nomor + 1;
		return baru.toString();
	}

	static Integer getIdByNomor(String nomor) {
		Integer id = 0;
		String sql = "SELECT id FROM pelanggan WHERE nomor = ? LIMIT 1";
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
