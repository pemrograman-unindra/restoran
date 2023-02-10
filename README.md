# **Aplikasi Restoran**
Ini adalah aplikasi restoran sederhana menggunakan Java dan SQLite.

## Anggota
1. 202143500474 - [Aditya Bwonoharto](https://github.com/ditsfy)
2. 202143579061 - [Ahmad Ramadhani](https://github.com/ramadhani-22)
3. 202143579049 - [Gunawan Heri Saputra](https://github.com/Gunawan-Heri)
4. 202243570024 - [Jeffry Luqman](https://github.com/jeffry-luqman)
5. 202143500549 - [Moh Naofal Nurul Huda](https://github.com/mohnaofal)
6. 202143500411 - Muhammad Aftar Roziq

## Memulai
1. Pastikan komputer kamu sudah terinstall [Git](https://git-scm.com/) dan [Java Development Kit](http://jdk.java.net/).
2. Kloning repositori ini ke komputer kamu dan masuk ke folder restoran dari terminal atau cmd
	```bash
	git clone https://github.com/unindra-p3-k2-y3e/restoran.git && cd restoran
	```
3. Lakukan kompilasi aplikasi
	```bash
	javac restoran.java
	```
4. Jalankan aplikasi
  * Windows
	```bash
	java -cp ".;sqlite-jdbc-3.40.0.0.jar" restoran
	```
  * Linux atau Mac
	```bash
	java -cp .:sqlite-jdbc-3.40.0.0.jar restoran
	```

## Fitur
- [x] Data Pelanggan
  - [x] Melihat Data Pelanggan
  - [x] Menambah Data Pelanggan
  - [x] Merubah Data Pelanggan
  - [x] Menghapus Data Pelanggan
- [x] Data Menu
  - [x] Melihat Data Menu
  - [x] Menambah Data Menu
  - [x] Merubah Data Menu
  - [x] Menghapus Data Menu
- [x] Data Kelompok Menu
  - [x] Melihat Data Kelompok Menu
  - [x] Menambah Data Kelompok Menu
  - [x] Merubah Data Kelompok Menu
  - [x] Menghapus Data Kelompok Menu
- [x] Pesanan
  - [x] Melihat Daftar Pesanan
  - [x] Melihat Rincian Pesanan
  - [x] Menambah Pesanan
  - [x] Merubah Pesanan
  - [x] Menghapus Pesanan

## Video Penggunaan
https://user-images.githubusercontent.com/11884257/217404089-dbada4d4-9844-4123-8bee-b2f5894384da.mp4


## Catatan
Aplikasi ini menggunakan [Driver SQLite JDBC](https://github.com/xerial/sqlite-jdbc), seharusnya driver tersebut diunduh langsung dari sana, namun untuk memudahkan proses belajar terutama bagi pemula driver tersebut juga disertakan pada repositori ini.
