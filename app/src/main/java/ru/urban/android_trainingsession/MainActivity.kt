package ru.urban.android_trainingsession

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    val exercises = ExerciseDataBase.exercises

    private lateinit var titleTV: TextView
    private lateinit var startButton: Button
    private lateinit var exerciseTV: TextView
    private lateinit var descriptionTV: TextView
    private lateinit var timerTV: TextView
    private lateinit var completedButton: Button
    private lateinit var imageViewIV: ImageView

    private var exerciseIndex = 0
    private lateinit var currentExercise: Exercise
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titleTV = findViewById(R.id.titleTV)
        startButton = findViewById(R.id.startButton)
        exerciseTV = findViewById(R.id.exerciseTV)
        descriptionTV = findViewById(R.id.descriptionTV)
        timerTV = findViewById(R.id.timerTV)
        completedButton = findViewById(R.id.completedButton)
        imageViewIV = findViewById(R.id.imageViewIV)

        startButton.setOnClickListener{
            startWorkout()
        }

        completedButton.setOnClickListener {
            timer.cancel()
            completedButton.isEnabled = false
            startNextExercise()
        }
    }

    private fun startWorkout() {
        exerciseIndex = 0
        titleTV.text = "Начало тренировки"
        startButton.isEnabled = false
        startButton.text = "Процесс тренировки"
        startNextExercise()
    }

    private fun startNextExercise() {
        if (exerciseIndex < exercises.size){
            currentExercise = exercises[exerciseIndex]
            exerciseTV.text = currentExercise.name
            descriptionTV.text = currentExercise.description
            imageViewIV.setImageResource(currentExercise.gifImage)
            timerTV.text = formatTimer(currentExercise.durationInSeconds)
            timer = object : CountDownTimer(currentExercise.durationInSeconds * 1000L, 1000){
                override fun onTick(millisUntilFinished: Long) {
                    timerTV.text = formatTimer((millisUntilFinished / 1000).toInt())
                }

                override fun onFinish() {
                    timerTV.text = "Упражнение завершено"
                    imageViewIV.visibility = View.VISIBLE
                    completedButton.isEnabled = true
                    imageViewIV.setImageResource(0)
                }

            }.start()
            exerciseIndex++
        } else{
            exerciseTV.text = "Тренировка завершена"
            descriptionTV.text = ""
            timerTV.text = ""
            completedButton.isEnabled = false
            startButton.isEnabled = true
            startButton.text = "Начать снова"
        }
    }

    private fun formatTimer(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes,remainingSeconds)
    }
}