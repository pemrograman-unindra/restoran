public class restoran {

	public static void main(String[] args) {
		System.out.println("Menyiapkan data...");

		// migration
		pelanggan.buatTabel();
		produk.buatTabel();
		pesanan.buatTabel();

		// seeder
		pelanggan.buatDataAwal();
		produk.buatDataAwal();
		pesanan.buatDataAwal();

		// mulai
		System.out.println();
		System.out.println("+---------------------------------------+");
		System.out.println("|           Aplikasi Restoran           |");
		System.out.println("+---------------------------------------+");
		System.out.println("|          Tugas Pemrograman 3          |");
		System.out.println("|               Kelompok 2              |");
		System.out.println("|                                       |");
		System.out.println("| 202143500474 - Aditya Bwonoharto      |");
		System.out.println("| 202143579061 - Ahmad Ramadhani        |");
		System.out.println("| 202143579049 - Gunawan Heri Saputra   |");
		System.out.println("| 202243570024 - Jeffry Luqman          |");
		System.out.println("| 202143500549 - Moh Naofal Nurul Huda  |");
		System.out.println("| 202143500411 - Muhammad Aftar Roziq   |");
		System.out.println("+---------------------------------------+");

		// mulai navigasi
		menuUtama();
	}

	public static void menuUtama() {
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println("🏠 Menu Utama");
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("1. Pesanan 💰");
		System.out.println("2. Data Pelanggan 👤");
		System.out.println("3. Data Menu 🍝");
		System.out.println();
		System.out.println("0. Keluar ⬅️");
		System.out.println();
		System.out.print("Silakan pilih menu (0-3) : ");

		switch (util.bacaInput()) {
			case "1": pesanan.menu(); break;
			case "2": pelanggan.menu(); break;
			case "3": produk.menu(); break;
			case "0": 
				System.out.println();
				System.out.println("Terima Kasih..."); 
				System.exit(0); break;
			default: 
				System.out.println();
				System.out.println("Nomor menu tidak valid! silakan pilih menu dengan angka 0 sampai 3"); 
				menuUtama(); break;
		}
	}
}