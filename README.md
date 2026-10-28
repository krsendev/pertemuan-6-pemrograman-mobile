# Kiyuris Hub - Formulir Registrasi Keanggotaan

Aplikasi registrasi keanggotaan komunitas teknologi dan kreatif **Kiyuris Hub** yang dibangun menggunakan **Kotlin** dan **Jetpack Compose**. Project ini merupakan implementasi tugas praktikum **Pemrograman Mobile - Pertemuan 6** dengan fokus materi pada pengelolaan State, penanganan Event, Recomposition, Anatomi Field (`OutlinedTextField`), Form Validation, dan arsitektur State Hoisting.

---

## 👨‍💻 Identitas Mahasiswa

- **Nama** : Krisna Guntur Pamungkas
- **NIM** : 241080200051
- **Program Studi** : Informatika (5B1)
- **Fakultas** : Sains dan Teknologi
- **Perguruan Tinggi** : Universitas Muhammadiyah Sidoarjo (UMSIDA)
- **Mata Kuliah** : Pemrograman Mobile (Praktikum Pertemuan 6)

---

## 🎯 Tujuan Praktikum

1. Memahami konsep deklarasi dan siklus hidup **State** pada Jetpack Compose.
2. Mengimplementasikan penanganan **Event** melalui lambda callback pengguna (`onValueChange`, `onClick`, `onCheckedChange`).
3. Memahami proses **Recomposition** otomatis saat nilai state mengalami mutasi.
4. Membedakan penggunaan `remember` (transient RAM) dan `rememberSaveable` (bertahan saat rotasi layar/rekreasi Activity).
5. Menerapkan pola arsitektur **State Hoisting** dengan memisahkan stateful container (*parent*) dan stateless visual component (*child*).
6. Membangun sistem **Form Validation** menyeluruh dengan pesan kesalahan real-time dan feedback sukses interaktif.

---

## ✨ Fitur Aplikasi

- **Desain Modern & Konsisten**: Palet warna kustom *Putih & Deep Navy Blue* (`#0F2537`) dengan kontras tajam sesuai pedoman aksesibilitas WCAG.
- **Validasi Multi-Kriteria**:
  - **Nama Lengkap**: Wajib diisi (tidak boleh kosong/hanya spasi).
  - **Email**: Format email valid menggunakan `Patterns.EMAIL_ADDRESS`.
  - **Nomor Induk Mahasiswa (NIM)**: Wajib diisi, hanya boleh berisi angka (`nim.all { it.isDigit() }`), dan keyboard virtual otomatis membuka layout numerik.
  - **Kata Sandi**: Minimal 8 karakter dengan tombol toggle tampilkan/sembunyikan kata sandi (*visibility toggle*).
  - **Konfirmasi Kata Sandi**: Harus identik dengan kata sandi.
  - **Persetujuan Ketentuan**: Checkbox wajib dicentang sebelum form dapat dikirim.
- **State Hoisting (`NimField`)**: Komponen input NIM dibuat modular dan stateless, menerima data dari parent dan memancarkan perubahan ke atas.
- **Keamanan Data Kredensial**: Password dikelola dengan `remember` sederhana (tidak disimpan ke `savedInstanceState` atau bundle disk).
- **Responsive Scrolling**: Menggunakan layout `verticalScroll` sehingga tampilan tidak terpotong pada perangkat berlayar kecil.

---

## 🧠 Konsep Teori Jetpack Compose

### 1. State dan Event (Unidirectional Data Flow)
Jetpack Compose mengadopsi paradigma deklaratif dengan pola **Unidirectional Data Flow (UDF)**:
- **State mengalir ke bawah (*State Down*)**: Data dikirimkan dari pemegang state (*parent*) ke komponen antarmuka (*child*).
- **Event mengalir ke atas (*Event Up*)**: Interaksi pengguna (seperti pengetikan teks atau klik tombol) memancarkan event ke parent untuk mengubah state.

### 2. `remember` vs `rememberSaveable`
- **`remember`**: Menyimpan nilai state selama proses rekomposisi UI berlangsung. Nilai akan hilang jika Activity dihancurkan (misal rotasi layar). Digunakan pada `password` demi alasan keamanan agar data sensitif tidak tersimpan di bundle memori sistem.
- **`rememberSaveable`**: Menyimpan state ke dalam `Bundle` Android sehingga nilai input (seperti `nama`, `email`, `nim`, dan checkbox `setuju`) tetap bertahan saat terjadi perubahan konfigurasi perangkat (rotasi layar atau pergantian tema sistem).

### 3. State Hoisting pada `NimField`
Komponen `NimField` dijadikan komponen **Stateless** yang tidak memiliki variabel `remember` sendiri:
```kotlin
@Composable
fun NimField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Nomor Induk Mahasiswa (NIM)") },
        leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(errorMessage)
            }
        }
    )
}
```
Layar `RegistrationScreen` bertindak sebagai *Single Source of Truth* yang mengontrol logika validasi dan nilai aktual NIM.

---

## 🛠️ Teknologi & Dependency

- **Bahasa**: Kotlin 2.x
- **Toolkit UI**: Jetpack Compose (Compose BOM 2024+)
- **Komponen Material**: Material 3 (`androidx.compose.material3:material3`)
- **Ikon**: Material Icons Extended (`androidx.compose.material:material-icons-extended`) untuk ikon `Badge`, `Visibility`, dan `VisibilityOff`.
- **Target SDK**: Android 34 / 35 (Android 14 / 15)
- **Min SDK**: API 24 (Android 7.0 Nougat)

---

## 🚀 Cara Menjalankan Project

1. **Clone repository ini**:
   ```bash
   git clone https://github.com/krsendev/pertemuan-6-pemrograman-mobile.git
   ```
2. **Buka di Android Studio**:
   - Pilih menu **File ➜ Open...**.
   - Arahkan ke direktori hasil clone (`pertemuan-6-pemrograman-mobile`).
3. **Gradle Sync**:
   - Tunggu hingga Android Studio menyelesaikan sinkronisasi dependencies via Gradle Catalog (`libs.versions.toml`).
4. **Jalankan Aplikasi**:
   - Hubungkan perangkat fisik via USB Debugging (atau jalankan Android Virtual Device / AVD).
   - Klik tombol **Run 'app' (Shift + F10 / ▶️)**.

---

## 🧪 Tabel Skenario Pengujian

| No. | Skenario Pengujian | Input yang Dimasukkan | Hasil yang Diharapkan | Hasil Aktual | Status |
| :-: | :--- | :--- | :--- | :--- | :-: |
| 1 | Seluruh kolom kosong | Kolom dibiarkan kosong, klik "Daftar sekarang" | Pesan error tampil di semua field | Error tampil serentak | ✅ Lolos |
| 2 | Format email tidak valid | Email diisi `krisna.dev` (tanpa `@` dan domain) | Muncul pesan "Masukkan alamat email yang valid" | Pesan error email tampil | ✅ Lolos |
| 3 | Kolom NIM kosong | NIM dikosongkan | Muncul pesan "NIM wajib diisi" | Pesan error NIM kosong tampil | ✅ Lolos |
| 4 | NIM berisi simbol / huruf | NIM diisi `24108abc#@` | Muncul pesan "NIM hanya boleh berisi angka" | Simbol ditolak & pesan tampil | ✅ Lolos |
| 5 | Panjang kata sandi < 8 | Kata sandi diisi `pass123` (7 karakter) | Muncul pesan "Kata sandi minimal 8 karakter" | Pesan batas panjang tampil | ✅ Lolos |
| 6 | Konfirmasi sandi berbeda | Sandi: `password123`, Konfirmasi: `password321` | Muncul pesan "Konfirmasi harus sama dengan kata sandi" | Pesan ketidakcocokan tampil | ✅ Lolos |
| 7 | Persetujuan belum dicentang | Data valid, checkbox tidak dicentang | Muncul pesan "Persetujuan wajib dicentang" | Pesan persetujuan tampil | ✅ Lolos |
| 8 | Seluruh data valid | Seluruh kolom memenuhi syarat validasi | Muncul container sukses hijau dan ucapan personal | Kartu sukses berhasil tampil | ✅ Lolos |
| 9 | Perubahan data pasca sukses | Edit salah satu karakter nama setelah submit | Status sukses ter-reset otomatis | Kartu sukses langsung hilang | ✅ Lolos |
| 10 | Rotasi layar (Rekreasi Activity) | Rotasi perangkat dari portrait ke landscape | State nama, email, NIM bertahan; sandi ter-reset | Sesuai aturan rememberSaveable | ✅ Lolos |

---

## 📸 Dokumentasi Tangkapan Layar

| Tampilan Utama (Awal) | Validasi Gagal (Semua Kosong) | Validasi NIM (Simbol Ditolak) | Validasi Berhasil (Sukses) |
| :---: | :---: | :---: | :---: |
| *(Lampirkan `01-tampilan-utama.png`)* | *(Lampirkan `02-validasi-gagal-kosong.png`)* | *(Lampirkan `03-validasi-nim-simbol.png`)* | *(Lampirkan `04-validasi-berhasil.png`)* |

---

*Laporan dan dokumentasi ini disusun untuk memenuhi tugas praktikum Pemrograman Mobile Pertemuan 6 UMSIDA.*
