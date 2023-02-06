import java.sql.Statement;

public class pesanan {

	// migration
	public static void buatTabel() {
		rincianPesanan.buatTabel();
		String sql = ""
			+ "  CREATE TABLE IF NOT EXISTS pesanan ("
			+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "    id_pelanggan INTEGER,"
			+ "    nomor VARCHAR(25) UNIQUE,"
			+ "    status VARCHAR(255),"
			+ "    nilai_pesanan INTEGER,"
			+ "    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan (id)"
			+ " )";
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
			case "1": restoran.menuUtama(); break;
			case "0": restoran.menuUtama(); break;
			default: 
				System.out.println();
				System.out.println("Nomor menu tidak valid! silakan pilih menu dengan angka 0 sampai 5"); 
				menu(); break;
		}
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