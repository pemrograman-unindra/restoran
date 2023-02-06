import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class pesanan {

	// migration
	public static void buatTabel() {
		rincianPesanan.buatTabel();
		String sql = ""
			+ "CREATE TABLE IF NOT EXISTS pesanan ("
			+ "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "  id_pelanggan INTEGER,"
			+ "  nomor VARCHAR(25) UNIQUE,"
			+ "  status VARCHAR(255),"
			+ "  nilai_pesanan INTEGER,"
			+ "  FOREIGN KEY (id_pelanggan) REFERENCES pelanggan (id)"
			+ ")";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Tabel pesanan berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat tabel pesanan : " + e.getMessage());
			System.exit(1);
		}
	}

	// seeder
	public static void buatDataAwal() {
		rincianPesanan.buatDataAwal();
		String sql = ""
			+ "INSERT OR IGNORE INTO pesanan (id_pelanggan, nomor, status, nilai_pesanan) VALUES"
			+ "(1, 1, 'Dibayar', 38000),"
			+ "(2, 2, 'Dibayar', 25000),"
			+ "(3, 3, 'Dibayar', 23000),"
			+ "(4, 4, 'Disajikan', 18000),"
			+ "(5, 5, 'Dipesan', 23000)";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Data awal pesanan berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat data awal pesanan : " + e.getMessage());
			System.exit(1);
		}
	}

	public static void menu() {
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println("💰 Pesanan");
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("1. Tampilkan Daftar Pesanan 📋");
		System.out.println("2. Lihat Rincian Pesanan 🗂️");
		System.out.println("3. Tambah Pesanan ➕");
		System.out.println("4. Ubah Pesanan 📝");
		System.out.println("5. Hapus Pesanan ❌");
		System.out.println();
		System.out.println("0. Kembali Ke Menu Utama ⬅️");
		System.out.println();
		System.out.print("Silakan pilih menu (0-5) : ");
		switch (util.bacaInput()) {
			case "1": tampilkan(true); break;
			case "2": tampilkanRincian(0); break;
			case "3": tambah(); break;
			case "4": ubah(); break;
			case "5": hapus(); break;
			case "0": restoran.menuUtama(); break;
			default: 
				System.out.println();
				System.out.println("Nomor menu tidak valid! silakan pilih menu dengan angka 0 sampai 5"); 
				menu(); break;
		}
	}

	public static void tampilkan(Boolean navigasi) {
		if (navigasi) {
			System.out.println();
			System.out.println("📋 Daftar Pesanan");
		}
		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
		String sql = ""
			+ "SELECT "
			+ "  p.nomor nomor, "
			+ "  pel.nama pelanggan, "
			+ "  p.nilai_pesanan nilai, "
			+ "  p.status status "
			+ "FROM "
			+ "  pesanan p "
			+ "  JOIN pelanggan pel ON pel.id = p.id_pelanggan "
			+ "ORDER BY "
			+ "  p.nomor ASC";
		try (ResultSet rs = util.koneksiDB().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
				data.put("No.", rs.getString("nomor"));
				data.put("Pelanggan", rs.getString("pelanggan"));
				data.put("Nilai Pesanan", util.formatAngka(rs.getInt("nilai")));
				data.put("Status", rs.getString("status"));
				list.add(data);
			}
			util.tampilkanData(list);
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menampilkan daftar pesanan : " + e.getMessage());
		}
		if (navigasi) {
			menu();
		}
	}

	public static void tampilkanRincian(Integer idPesanan) {
		Integer id = idPesanan;
		if (idPesanan == 0) {
			System.out.println();
			System.out.println("🗂️  Lihat Rincian Pesanan");
			id = pilih();
			if (id==0) {
				menu();
			}
		}
		// todo
		if (idPesanan == 0) {
			menu();
		}
	}

	public static void tambah() {
		System.out.println();
		System.out.println("➕ Tambah Data Pesanan");
		String nomor = nomorBaru();
		Integer idPelanggan = pelanggan.pilih();
		if (idPelanggan==0) {
			menu();
		}
		ArrayList<LinkedHashMap<String, String>> rincian = new ArrayList<LinkedHashMap<String, String>>();
		rincian = tambahRincian(rincian);
		String status = inputStatus();
		Integer nilaiPesanan = hitungNilai();
		String sql1 = "INSERT INTO pesanan (id_pelanggan, nomor, status, nilai_pesanan) VALUES (?, ?, ?, ?)";
		String sql2 = "INSERT INTO rincian_pesanan (id_pesanan, id_produk, jumlah, catatan) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps1 = util.koneksiDB().prepareStatement(sql1)) {
			ps1.setInt(1, idPelanggan);
			ps1.setString(2, nomor);
			ps1.setString(3, status);
			ps1.setInt(4, nilaiPesanan);
			ps1.executeUpdate();
			ResultSet rs = ps1.getGeneratedKeys();
			if (rs.next()) {
				Integer idPesanan = rs.getInt(1);
				for (LinkedHashMap data : rincian) {
					PreparedStatement ps2 = util.koneksiDB().prepareStatement(sql2);
					ps2.setInt(1, idPesanan);
					ps2.setInt(2, util.toInteger(data.get("idProduk").toString()));
					ps2.setInt(3, util.toInteger(data.get("jumlah").toString()));
					ps2.setString(3, data.get("catatan").toString());
					ps2.executeUpdate();
				}
				tampilkanRincian(idPesanan);
			}
			System.out.println();
			System.out.println("Data pesanan berhasil ditambahkan!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menambah data pesanan : " + e.getMessage());
		}
		menu();
	}

	public static void ubah() {
		System.out.println();
		System.out.println("📝 Ubah Data Pesanan");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String nomor = inputNomorBaru(id);
		Integer idPelanggan = pelanggan.pilih();
		if (idPelanggan==0) {
			menu();
		}
		ArrayList<LinkedHashMap<String, String>> rincian = new ArrayList<LinkedHashMap<String, String>>();
		rincian = tambahRincian(rincian);
		String status = inputStatus();
		Integer nilaiPesanan = hitungNilai();
		String sql1 = "UPDATE pesanan SET id_pelanggan = ?, nomor = ?, status = ?, nilai_pesanan = ? WHERE id = ?";
		String sql2 = "DELETE FROM rincian_pesanan WHERE id_pesanan = ?";
		String sql3 = "INSERT INTO rincian_pesanan (id_pesanan, id_produk, jumlah, catatan) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps1 = util.koneksiDB().prepareStatement(sql1)) {
			ps1.setInt(1, idPelanggan);
			ps1.setString(2, nomor);
			ps1.setString(3, status);
			ps1.setInt(4, nilaiPesanan);
			ps1.setInt(5, id);
			ps1.executeUpdate();

			PreparedStatement ps2 = util.koneksiDB().prepareStatement(sql2);
			ps2.setInt(1, id);
			ps2.executeUpdate();

			for (LinkedHashMap data : rincian) {
				PreparedStatement ps3 = util.koneksiDB().prepareStatement(sql2);
				ps3.setInt(1, id);
				ps3.setInt(2, util.toInteger(data.get("idProduk").toString()));
				ps3.setInt(3, util.toInteger(data.get("jumlah").toString()));
				ps3.setString(3, data.get("catatan").toString());
				ps3.executeUpdate();
			}
			tampilkanRincian(id);

			System.out.println();
			System.out.println("Data pesanan berhasil diubah!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal merubah data pesanan : " + e.getMessage());
		}
		menu();
	}

	public static void hapus() {
		System.out.println();
		System.out.println("❌ Hapus Data Pesanan");
		Integer id = pilih();
		if (id==0) {
			menu();
		}
		String sql1 = "DELETE FROM rincian_pesanan WHERE id_pesanan = ?";
		String sql2 = "DELETE FROM pesanan WHERE id = ?";
		try (
			PreparedStatement ps1 = util.koneksiDB().prepareStatement(sql1);
			PreparedStatement ps2 = util.koneksiDB().prepareStatement(sql1);
		) {
			ps1.setInt(1, id);
			ps1.executeUpdate();
			ps2.setInt(1, id);
			ps2.executeUpdate();
			System.out.println();
			System.out.println("Data pesanan berhasil dihapus!");
		} catch (Exception e) {
			System.out.println();
			System.out.println("Gagal menghapus data pesanan : " + e.getMessage());
		}
		menu();
	}

	public static Integer pilih() {
		tampilkan(false);
		System.out.print("Masukan Nomor Pesanan : "); String nomor = util.bacaInput();
		if (nomor.equals("0")) {
			return 0;
		}

		Integer id = getIdByNomor(nomor);
		if (id > 0) {
			return id;
		}
		System.out.println("Nomor pesanan tidak ditemukan! silakan isi sesuai nomor pesanan yang valid atau isi 0 untuk membatalkan"); 
		return pilih();
	}

	static ArrayList<LinkedHashMap<String, String>> tambahRincian(ArrayList<LinkedHashMap<String, String>> rincian) {
		Integer idProduk = produk.pilih();
		if (idProduk==0) {
			menu();
		}
		// todo
		return rincian;
	}

	static String inputNomorBaru(Integer idLama) {
		System.out.print("Masukan Nomor pesanan Baru : "); String nomor = util.bacaInput();
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

	static String inputStatus() {
		System.out.print("Masukan Status : "); String status = util.bacaInput();
		return status;
	}

	static Integer hitungNilai() {
		System.out.print("Masukan Nilai : "); String nilai_pesananStr = util.bacaInput();
		if (!util.isValidNumber(nilai_pesananStr)) {
			System.out.println("nilai_pesanan harus berupa angka! silakan isi dengan angka yang valid");
			return hitungNilai();
		}
		return util.toInteger(nilai_pesananStr);
	}

	static String nomorBaru() {
		Integer nomor = 0;
		String sql = "SELECT MAX(nomor) nomor FROM pesanan";
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
		String sql = "SELECT id FROM pesanan WHERE nomor = ? LIMIT 1";
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

class rincianPesanan {

	// migration
	public static void buatTabel() {
		String sql = ""
			+ "CREATE TABLE IF NOT EXISTS rincian_pesanan ("
			+ "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "  id_pesanan INTEGER,"
			+ "  id_produk INTEGER,"
			+ "  jumlah INTEGER,"
			+ "  catatan VARCHAR(255),"
			+ "  FOREIGN KEY (id_pesanan) REFERENCES pesanan (id),"
			+ "  FOREIGN KEY (id_produk) REFERENCES menu (id)"
			+ ")";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Tabel kelompok_produk berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat tabel kelompok_produk : " + e.getMessage());
			System.exit(1);
		}
	}

	// seeder
	public static void buatDataAwal() {
		String sql = ""
			+ "INSERT OR IGNORE INTO rincian_pesanan (id_pesanan, id_produk, jumlah, catatan) VALUES"
			+ "(1, 1, 1, 'Pedas'),"
			+ "(1, 5, 1, null),"
			+ "(2, 2, 1, null),"
			+ "(3, 3, 1, null),"
			+ "(4, 4, 1, null),"
			+ "(5, 3, 1, null)";
		try {
			Statement stmt = util.koneksiDB().createStatement();
			stmt.execute(sql);
			System.out.println("Data awal rincian_pesanan berhasil dibuat...");
		} catch (Exception e) {
			System.out.println("Gagal membuat data awal rincian_pesanan : " + e.getMessage());
			System.exit(1);
		}
	}
}