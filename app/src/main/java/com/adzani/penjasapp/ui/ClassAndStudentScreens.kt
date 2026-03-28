package com.adzani.penjasapp.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateTopPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adzani.penjasapp.data.AttendanceStatus
import com.adzani.penjasapp.data.PenjasViewModel
import com.adzani.penjasapp.data.Student
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    classId: Long,
    className: String,
    viewModel: PenjasViewModel,
    onBack: () -> Unit,
    onOpenStudent: (Student) -> Unit,
) {
    val context = LocalContext.current
    val students by viewModel.studentsForClass(classId).collectAsState()
    var fullName by rememberSaveable { mutableStateOf("") }
    var nickName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var bio by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
        }
        photoUri = uri?.toString().orEmpty()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(className) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = padding.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                InputCard(title = "Tambah siswa") {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nama lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nickName,
                        onValueChange = { nickName = it },
                        label = { Text("Nama panggilan") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        label = { Text("Tanggal lahir (yyyy-mm-dd)") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = { Text("Tinggi cm") },
                            modifier = Modifier.weight(1f),
                        )
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Berat kg") },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio tambahan") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            picker.launch(arrayOf("image/*"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (photoUri.isBlank()) "Pilih foto siswa" else "Foto terpilih")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.addStudent(
                                classId = classId,
                                fullName = fullName,
                                nickName = nickName,
                                birthDate = birthDate,
                                heightCm = height,
                                weightKg = weight,
                                bio = bio,
                                photoUri = photoUri,
                            )
                            fullName = ""
                            nickName = ""
                            birthDate = ""
                            height = ""
                            weight = ""
                            bio = ""
                            photoUri = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Simpan siswa")
                    }
                }
            }
            item {
                Text(
                    text = "Siswa di kelas ini",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (students.isEmpty()) {
                item { EmptyState("Belum ada siswa pada kelas ini.") }
            } else {
                items(students, key = { it.id }) { student ->
                    StudentCard(
                        student = student,
                        onClick = { onOpenStudent(student) },
                        onAttendance = { status -> viewModel.saveAttendance(student.id, status) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Long,
    viewModel: PenjasViewModel,
    onBack: () -> Unit,
) {
    val bundle by viewModel.studentDetail(studentId).collectAsState()
    var score by rememberSaveable { mutableStateOf("") }
    var scoreNote by rememberSaveable { mutableStateOf("") }
    var dailyNote by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail siswa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        if (bundle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Data siswa tidak ditemukan.")
            }
        } else {
            val student = bundle!!.student
            val today = viewModel.today()

            LaunchedEffect(bundle!!.assessments, bundle!!.notes) {
                bundle!!.assessments.firstOrNull { it.date == today }?.let {
                    score = it.score
                    scoreNote = it.note
                }
                bundle!!.notes.firstOrNull { it.date == today }?.let {
                    dailyNote = it.content
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StudentHeader(student = student)
                InputCard(title = "Absensi hari ini") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AttendanceStatus.entries.forEach { status ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.saveAttendance(student.id, status) },
                                label = { Text(status.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            )
                        }
                    }
                }
                InputCard(title = "Penilaian harian") {
                    OutlinedTextField(
                        value = score,
                        onValueChange = { score = it },
                        label = { Text("Nilai hari ini") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = scoreNote,
                        onValueChange = { scoreNote = it },
                        label = { Text("Catatan nilai") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.saveAssessment(student.id, score, scoreNote) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Simpan nilai")
                    }
                }
                InputCard(title = "Catatan siswa per hari") {
                    OutlinedTextField(
                        value = dailyNote,
                        onValueChange = { dailyNote = it },
                        label = { Text("Catatan hari ini") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.saveDailyNote(student.id, dailyNote) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Simpan catatan")
                    }
                }
                SportTimerCard()
                HistoryCard(
                    title = "Riwayat absensi",
                    emptyText = "Belum ada absensi.",
                    rows = bundle!!.attendance.map { "${it.date} • ${it.status.name}" },
                )
                HistoryCard(
                    title = "Riwayat penilaian",
                    emptyText = "Belum ada penilaian.",
                    rows = bundle!!.assessments.map { "${it.date} • Nilai ${it.score} • ${it.note}" },
                )
                HistoryCard(
                    title = "Riwayat catatan",
                    emptyText = "Belum ada catatan.",
                    rows = bundle!!.notes.map { "${it.date} • ${it.content}" },
                )
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun StudentHeader(student: Student) {
    Card(shape = RoundedCornerShape(28.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (student.photoUri.isNotBlank()) {
                    AsyncImage(
                        model = student.photoUri,
                        contentDescription = student.fullName,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(38.dp))
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(student.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    if (student.nickName.isNotBlank()) {
                        Text("Panggilan: ${student.nickName}")
                    }
                    Text("Lahir: ${student.birthDate}")
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Tinggi: ${student.heightCm.ifBlank { "-" }} cm")
            Text("Berat: ${student.weightKg.ifBlank { "-" }} kg")
            if (student.bio.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(student.bio)
            }
        }
    }
}

@Composable
private fun StudentCard(
    student: Student,
    onClick: () -> Unit,
    onAttendance: (AttendanceStatus) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (student.photoUri.isNotBlank()) {
                    AsyncImage(
                        model = student.photoUri,
                        contentDescription = student.fullName,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Groups, contentDescription = null)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(student.fullName, fontWeight = FontWeight.Bold)
                    Text(
                        student.nickName.ifBlank { "Nama panggilan belum diisi" },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AttendanceAction("H") { onAttendance(AttendanceStatus.HADIR) }
                AttendanceAction("I") { onAttendance(AttendanceStatus.IZIN) }
                AttendanceAction("S") { onAttendance(AttendanceStatus.SAKIT) }
                AttendanceAction("A") { onAttendance(AttendanceStatus.ALPA) }
            }
        }
    }
}

@Composable
private fun AttendanceAction(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(label, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SportTimerCard() {
    var timerSeconds by rememberSaveable { mutableLongStateOf(60L) }
    var remainingSeconds by rememberSaveable { mutableLongStateOf(60L) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds -= 1
        } else if (remainingSeconds == 0L) {
            isRunning = false
        }
    }

    InputCard(title = "Timer guru penjas") {
        Text("Cocok untuk lari, stretching, circuit training, atau permainan estafet.")
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(30L, 60L, 120L).forEach { value ->
                FilterChip(
                    selected = timerSeconds == value,
                    onClick = {
                        timerSeconds = value
                        remainingSeconds = value
                        isRunning = false
                    },
                    label = { Text(if (value < 60) "$value dtk" else "${value / 60} mnt") },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = formatSeconds(remainingSeconds),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isRunning = true }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Timer, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Mulai")
            }
            TextButton(
                onClick = {
                    isRunning = false
                    remainingSeconds = timerSeconds
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("Reset")
            }
        }
    }
}

private fun formatSeconds(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

@Composable
private fun HistoryCard(
    title: String,
    emptyText: String,
    rows: List<String>,
) {
    InputCard(title = title) {
        if (rows.isEmpty()) {
            Text(emptyText)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rows.take(10).forEachIndexed { index, row ->
                    Text(row)
                    if (index != rows.take(10).lastIndex) {
                        Divider()
                    }
                }
            }
        }
    }
}
