package com.dicoding.courseschedule.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListActivity
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddCourseViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                val nama = findViewById<TextInputEditText>(R.id.ed_course).text.toString()
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                val startTime = findViewById<TextView>(R.id.tv_pick_start).text.toString()
                val endTime = findViewById<TextView>(R.id.tv_pick_end).text.toString()
                if (nama.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(this, getString(R.string.input_empty_message), Toast.LENGTH_SHORT).show()
                }
                viewModel.insertCourse(courseName = nama, lecturer = lecturer, note = note, day = day, startTime = startTime, endTime = endTime)
                viewModel.saved.observe(this, {
                    it.getContentIfNotHandled() ?: return@observe
                    Toast.makeText(this, getString(R.string.add_success_message), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ListActivity::class.java))
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        when (view.id) {
            R.id.img_btn_start -> dialogFragment.show(supportFragmentManager, "timePickerStart")
            R.id.img_btn_end -> dialogFragment.show(supportFragmentManager, "timePickerEnd")
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            "timePickerStart" -> findViewById<TextView>(R.id.tv_pick_start).text = dateFormat.format(calendar.time)
            "timePickerEnd" -> findViewById<TextView>(R.id.tv_pick_end).text = dateFormat.format(calendar.time)
        }
    }
}