# Guru Penjas Android App

Project ini adalah aplikasi Android sederhana untuk guru penjas dengan fitur:

- tambah kelas
- tambah siswa
- simpan foto siswa
- biodata siswa
- absensi harian
- penilaian harian
- catatan per siswa per hari
- timer untuk kegiatan penjas

## Cara buka project

1. Upload folder ini ke GitHub.
2. Buka di Android Studio.
3. Jika Android Studio menanyakan Gradle, pilih impor project Gradle ini lalu tunggu proses sync dan download selesai.
4. Jalankan ke HP Android atau emulator.

## Catatan

- Data disimpan lokal di HP menggunakan Room Database.
- Format tanggal saat ini memakai `yyyy-mm-dd`.
- Foto siswa dipilih dari penyimpanan HP dan disimpan sebagai URI lokal.
- File `gradle-wrapper.jar` belum bisa saya hasilkan di mesin ini karena Gradle tidak tersedia di environment kerja sekarang. Biasanya Android Studio di perangkat Anda tetap bisa membantu menyelesaikan setup saat project dibuka.
- Jika ingin, tahap berikutnya saya bisa tambahkan export PDF, backup data, rekap nilai per kelas, dan filter tanggal absensi.
