package com.adzani.penjasapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PenjasViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = PenjasDatabase.getInstance(application).penjasDao()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val classes: StateFlow<List<SchoolClass>> = dao.observeClasses().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )

    fun studentsForClass(classId: Long): StateFlow<List<Student>> {
        return dao.observeStudentsForClass(classId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList(),
        )
    }

    fun studentDetail(studentId: Long): StateFlow<StudentDetailBundle?> {
        return dao.observeStudent(studentId).flatMapLatest { student ->
            if (student == null) {
                flowOf(null)
            } else {
                dao.observeAttendance(studentId).flatMapLatest { attendance ->
                    dao.observeAssessments(studentId).flatMapLatest { assessments ->
                        dao.observeNotes(studentId).map { notes ->
                            StudentDetailBundle(
                                student = student,
                                attendance = attendance,
                                assessments = assessments,
                                notes = notes,
                            )
                        }
                    }
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )
    }

    fun addClass(name: String, description: String) {
        viewModelScope.launch {
            if (name.isBlank()) return@launch
            dao.insertClass(SchoolClass(name = name.trim(), description = description.trim()))
        }
    }

    fun addStudent(
        classId: Long,
        fullName: String,
        nickName: String,
        birthDate: String,
        heightCm: String,
        weightKg: String,
        bio: String,
        photoUri: String,
    ) {
        viewModelScope.launch {
            if (fullName.isBlank() || birthDate.isBlank()) return@launch
            dao.insertStudent(
                Student(
                    classId = classId,
                    fullName = fullName.trim(),
                    nickName = nickName.trim(),
                    birthDate = birthDate.trim(),
                    heightCm = heightCm.trim(),
                    weightKg = weightKg.trim(),
                    bio = bio.trim(),
                    photoUri = photoUri,
                ),
            )
        }
    }

    fun saveAttendance(studentId: Long, status: AttendanceStatus, date: String = today()) {
        viewModelScope.launch {
            val existing = dao.attendanceByDate(studentId, date)
            dao.upsertAttendance(
                AttendanceRecord(
                    id = existing?.id ?: 0,
                    studentId = studentId,
                    date = date,
                    status = status,
                ),
            )
        }
    }

    fun saveAssessment(studentId: Long, score: String, note: String, date: String = today()) {
        viewModelScope.launch {
            if (score.isBlank()) return@launch
            val existing = dao.assessmentByDate(studentId, date)
            dao.upsertAssessment(
                DailyAssessment(
                    id = existing?.id ?: 0,
                    studentId = studentId,
                    date = date,
                    score = score.trim(),
                    note = note.trim(),
                ),
            )
        }
    }

    fun saveDailyNote(studentId: Long, content: String, date: String = today()) {
        viewModelScope.launch {
            if (content.isBlank()) return@launch
            val existing = dao.noteByDate(studentId, date)
            dao.upsertNote(
                DailyStudentNote(
                    id = existing?.id ?: 0,
                    studentId = studentId,
                    date = date,
                    content = content.trim(),
                ),
            )
        }
    }

    fun today(): String = LocalDate.now().format(dateFormatter)
}
