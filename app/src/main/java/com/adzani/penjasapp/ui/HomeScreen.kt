package com.adzani.penjasapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.calculateTopPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adzani.penjasapp.data.PenjasViewModel
import com.adzani.penjasapp.data.SchoolClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PenjasViewModel,
    onOpenClass: (SchoolClass) -> Unit,
) {
    val classes by viewModel.classes.collectAsState()
    var className by rememberSaveable { mutableStateOf("") }
    var classDescription by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Guru Penjas") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addClass(className, classDescription)
                className = ""
                classDescription = ""
            }) {
                Icon(Icons.Default.Class, contentDescription = "Tambah kelas")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = padding.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { HeroCard() }
            item {
                InputCard(title = "Tambah kelas") {
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Nama kelas") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = classDescription,
                        onValueChange = { classDescription = it },
                        label = { Text("Catatan kelas") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.addClass(className, classDescription)
                            className = ""
                            classDescription = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Simpan kelas")
                    }
                }
            }
            item { FeatureStrip() }
            item {
                Text(
                    text = "Daftar kelas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (classes.isEmpty()) {
                item {
                    EmptyState("Belum ada kelas. Tambahkan satu kelas untuk mulai menyimpan data siswa.")
                }
            } else {
                items(classes, key = { it.id }) { schoolClass ->
                    ClassCard(schoolClass = schoolClass, onClick = { onOpenClass(schoolClass) })
                }
            }
        }
    }
}

@Composable
private fun HeroCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.tertiaryContainer,
                        ),
                    ),
                )
                .padding(20.dp),
        ) {
            Text(
                "Aplikasi Data Siswa Penjas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Simpan biodata, foto, absensi, nilai harian, catatan, dan timer olahraga dalam satu aplikasi Android.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeatureStrip() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf(
            Icons.Default.Groups to "Data siswa",
            Icons.Default.CheckCircle to "Absensi",
            Icons.Default.EditNote to "Penilaian",
            Icons.Default.AccessAlarm to "Timer penjas",
        ).forEach { (icon, label) ->
            AssistChip(
                onClick = {},
                label = { Text(label) },
                leadingIcon = { Icon(icon, contentDescription = null) },
            )
        }
    }
}

@Composable
private fun ClassCard(
    schoolClass: SchoolClass,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Class, contentDescription = null)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schoolClass.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (schoolClass.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(schoolClass.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
