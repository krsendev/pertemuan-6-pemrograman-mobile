package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Badge // baru ditambahkan
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


/**
 * Activity utama aplikasi yang menjadi titik masuk (entry point).
 * Mengatur tema dasar dan menampilkan layar pendaftaran [RegistrationScreen].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Bagian ini saya ubah sedikit
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RegistrationScreen()
                }
            }
        }
    }
}

/**
 * Screen-level state owner untuk contoh state hoisting dasar.
 * Memperagakan penerapan State, Event, Anatomi Field (OutlinedTextField), dan Form Validation di Jetpack Compose.
 */
@Composable
fun RegistrationScreen() {
    // State input formulir
    // - rememberSaveable: Menjaga state tetap bertahan saat terjadi perubahan konfigurasi (misal: rotasi layar).
    var nama by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var nim by rememberSaveable { mutableStateOf("") } // field tambahan

    // - remember: Menjaga state selama rekomposisi UI, namun nilai akan di-reset jika Activity dihancurkan.
    //   Password bersifat sementara (transient) dan tidak disimpan dalam saved UI state demi alasan keamanan.
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }
    var setuju by rememberSaveable { mutableStateOf(false) }
    var passwordTerlihat by rememberSaveable { mutableStateOf(false) }

    // State status pengiriman dan hasil validasi
    var sudahSubmit by remember { mutableStateOf(false) }
    var berhasil by remember { mutableStateOf(false) }

    // Evaluasi pesan error untuk setiap field (hanya ditampilkan setelah tombol submit ditekan)
    val errorNama = sudahSubmit && nama.trim().isEmpty()
    val errorEmail = sudahSubmit && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val errorNim = sudahSubmit && (nim.trim().isEmpty() || !nim.all { it.isDigit() }) // Tambahkan validasi untuk NIM
    val errorPassword = sudahSubmit && password.length < 8
    val errorKonfirmasi = sudahSubmit && konfirmasiPassword != password
    val errorPersetujuan = sudahSubmit && !setuju

    val formValid = nama.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
            nim.trim().isNotEmpty() && nim.all { it.isDigit() } && password.length >= 8 && konfirmasiPassword == password && setuju

    // Layout utama layar dengan dukungan scroll vertikal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header aplikasi
        Text("Kiyuris Hub", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Bergabung Bersama Kami", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("Daftar sebagai anggota baru untuk mengikuti workshop dan proyek kolaborasi.", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)

        // Kartu kontainer formulir
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Anatomi Field Input Nama Lengkap
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nama lengkap") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    singleLine = true,
                    isError = errorNama,
                    supportingText = { if (errorNama) Text("Nama wajib diisi") }
                )

                // Anatomi Field Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = errorEmail,
                    supportingText = { if (errorEmail) Text("Masukkan alamat email yang valid") }
                )

                // State Hoisting untuk pemanggilan composeable NimField
                NimField(
                    value = nim,
                    onValueChange = {
                        nim = it
                        berhasil = false
                    },
                    isError = errorNim,
                    errorMessage = if (nim.isBlank()) "NIM wajib diisi" else "NIM hanya boleh berisi angka"
                )

                // Anatomi Field Input Kata Sandi
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Kata sandi") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordTerlihat = !passwordTerlihat
                        }) {
                            Icon(
                                imageVector = if (passwordTerlihat) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordTerlihat) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                            )
                        }
                    },
                    visualTransformation = if (passwordTerlihat) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    isError = errorPassword,
                    supportingText = { if (errorPassword) Text("Kata sandi minimal 8 karakter") }
                )

                // Anatomi Field Input Konfirmasi Kata Sandi
                OutlinedTextField(
                    value = konfirmasiPassword,
                    onValueChange = {
                        konfirmasiPassword = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Konfirmasi kata sandi") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    isError = errorKonfirmasi,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // baru ditambahkan
                    supportingText = { if (errorKonfirmasi) Text("Konfirmasi harus sama dengan kata sandi") }
                )

                // Checkbox Persetujuan Ketentuan (Event & Validasi)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = setuju, // [State] Menunjukkan status centang
                        onCheckedChange = { // [EVENT] Dipanggil saat Checkbox diklik
                            setuju = it
                            berhasil = false
                        }
                    )
                    Text("Saya setuju dengan ketentuan keanggotaan Kiyuris Hub", fontSize = 13.sp)
                }
                if (errorPersetujuan) {
                    Text("Persetujuan wajib dicentang", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Mengatur alur saat pengguna menekan tombol pendaftaran.
                Button(
                    onClick = {
                        sudahSubmit = true // Memicu pembaruan state bahwa formulir telah dikirim
                        berhasil = formValid // Menyimpan hasil validasi formulir
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Daftar sekarang", color = Color.White, modifier = Modifier.padding(vertical = 3.dp), fontWeight = FontWeight.SemiBold)
                }

                // Tampilan Pesan Sukses jika validasi berhasil
                if (berhasil) {
                    HorizontalDivider(color = Color(0xFFD9EAD9))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF27834A))
                        Column {
                            Text("Selamat bergabung di Kiyuris Hub!", color = Color(0xFF237A42), fontWeight = FontWeight.Bold)
                            Text("Halo, ${nama.trim()}! Pendaftaran anggota kamu berhasil diverifikasi.", color = Color(0xFF44534A), fontSize = 13.sp)
                        }
                    }
                }
            }
        }
        Text("Isi seluruh kolom identitas, pastikan NIM valid berupa angka, kata sandi minimal 8 karakter, lalu centang persetujuan.", fontSize = 12.sp, color = Color(0xFF666779))
    }
}

// Kode tambahan untuk implementasi State Hoisting
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

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    MyApplicationTheme {
        RegistrationScreen()
    }
}
