package com.adzani.penjasapp.data

import android.app.Application
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

class AttendanceConverters {
    @TypeConverter
    fun fromAttendanceStatus(value: AttendanceStatus): String = value.name

    @TypeConverter
    fun toAttendanceStatus(value: String): AttendanceStatus = AttendanceStatus.valueOf(value)
}

data class StudentDetailBundle(
    val student: Student,
    val attendance: List<AttendanceRecord>,
    val assessments: List<DailyAssessment>,
    val notes: List<DailyStudentNote>,
)

@Dao
interface PenjasDao {
    @Query("SELECT * FROM classes ORDER BY name ASC")
    fun observeClasses(): Flow<List<SchoolClass>>

    @Insert
    suspend fun insertClass(item: SchoolClass)

    @Query("SELECT * FROM students WHERE classId = :classId ORDER BY fullName ASC")
    fun observeStudentsForClass(classId: Long): Flow<List<Student>>

    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
    fun observeStudent(studentId: Long): Flow<Student?>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun observeAttendance(studentId: Long): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendance(record: AttendanceRecord)

    @Query("SELECT * FROM assessments WHERE studentId = :studentId ORDER BY date DESC")
    fun observeAssessments(studentId: Long): Flow<List<DailyAssessment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAssessment(item: DailyAssessment)

    @Query("SELECT * FROM notes WHERE studentId = :studentId ORDER BY date DESC")
    fun observeNotes(studentId: Long): Flow<List<DailyStudentNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(item: DailyStudentNote)

    @Query("SELECT * FROM attendance WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun attendanceByDate(studentId: Long, date: String): AttendanceRecord?

    @Query("SELECT * FROM assessments WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun assessmentByDate(studentId: Long, date: String): DailyAssessment?

    @Query("SELECT * FROM notes WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun noteByDate(studentId: Long, date: String): DailyStudentNote?
}

@Database(
    entities = [
        SchoolClass::class,
        Student::class,
        AttendanceRecord::class,
        DailyAssessment::class,
        DailyStudentNote::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(AttendanceConverters::class)
abstract class PenjasDatabase : RoomDatabase() {
    abstract fun penjasDao(): PenjasDao

    companion object {
        @Volatile
        private var instance: PenjasDatabase? = null

        fun getInstance(application: Application): PenjasDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    application,
                    PenjasDatabase::class.java,
                    "penjas-db",
                ).build().also { instance = it }
            }
        }
    }
}
