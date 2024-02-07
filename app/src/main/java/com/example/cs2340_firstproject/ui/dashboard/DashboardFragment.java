package com.example.cs2340_firstproject.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340_firstproject.R;
import com.example.cs2340_firstproject.ui.home.ClassAdapter;
import com.example.cs2340_firstproject.ui.home.ClassItem;
import com.example.cs2340_firstproject.ui.home.ClassListViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private ListView listView;
    private ClassAdapter adapter;
    private ArrayList<ClassItem> arrayOfClasses;
    private ClassListViewModel viewModel;
    private boolean deleteMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ClassListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Construct the data source
        arrayOfClasses = new ArrayList<>();
        // Create the adapter to convert the array to views
        adapter = new ClassAdapter(getActivity(), new ArrayList<ClassItem>());
        // Attach the adapter to a ListView
        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Set up an item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassItem selectedClass = adapter.getItem(position); // Retrieve the selected item
                showEditDialog(selectedClass, position); // Show dialog with selected item's details
            }
        });

        // Observe the LiveData from the ViewModel
        viewModel.getClassItemList().observe(getViewLifecycleOwner(), new Observer<List<ClassItem>>() {
            @Override
            public void onChanged(List<ClassItem> classItems) {
                // Update the adapter with the new class items
                adapter.clear();
                adapter.addAll(classItems);
                adapter.notifyDataSetChanged();
            }
        });

        // Set up the add and delete buttons
        Button addButton = rootView.findViewById(R.id.button_add);
        Button deleteButton = rootView.findViewById(R.id.button_delete);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode) {
                    // Retrieve the items to delete from the adapter
                    List<ClassItem> itemsToDelete = adapter.getSelectedItems();

                    // Call the ViewModel to remove the items
                    viewModel.removeClassItems(itemsToDelete); // Implement this method in your ViewModel

                    // Clear selected items in the adapter
                    adapter.deleteSelectedItems(); // This method should also update the classItems list

                    deleteMode = false;
                } else {
                    // Enter delete mode and show checkboxes
                    deleteMode = true;
                }

                // Update the adapter state and UI
                adapter.setDeleteMode(deleteMode);
            }
        });

        return rootView;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_assign, null);


        final EditText editTextClassName = dialogView.findViewById(R.id.editTextClassName);
        final EditText editTextInstructorName = dialogView.findViewById(R.id.editTextInstructorName);

        // TimePicker initialization
        TimePicker timePickerStartTime = dialogView.findViewById(R.id.timePickerStartTime);
        TimePicker timePickerEndTime = dialogView.findViewById(R.id.timePickerEndTime);
        timePickerStartTime.setIs24HourView(true);
        timePickerEndTime.setIs24HourView(true);
        final CheckBox checkBoxMonday = dialogView.findViewById(R.id.checkbox_monday);
        final CheckBox checkBoxTuesday = dialogView.findViewById(R.id.checkbox_tuesday);
        final CheckBox checkBoxWednesday = dialogView.findViewById(R.id.checkbox_wednesday);
        final CheckBox checkBoxThursday = dialogView.findViewById(R.id.checkbox_thursday);
        final CheckBox checkBoxFriday = dialogView.findViewById(R.id.checkbox_friday);

        builder.setView(dialogView)
                .setTitle("Add Assignments and Exams")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the entered information
                        String className = editTextClassName.getText().toString().trim();
                        int startHour = timePickerStartTime.getCurrentHour();
                        int startMinute = timePickerStartTime.getCurrentMinute();
                        int endHour = timePickerEndTime.getCurrentHour();
                        int endMinute = timePickerEndTime.getCurrentMinute();

                        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                        String classTimeRange = startTime + " - " + endTime;
                        String instructorName = editTextInstructorName.getText().toString().trim();

                        LinkedHashSet<String> daysOfWeek = new LinkedHashSet<>();
                        if (checkBoxMonday.isChecked()) daysOfWeek.add("Monday");
                        if (checkBoxTuesday.isChecked()) daysOfWeek.add("Tuesday");
                        if (checkBoxWednesday.isChecked()) daysOfWeek.add("Wednesday");
                        if (checkBoxThursday.isChecked()) daysOfWeek.add("Thursday");
                        if (checkBoxFriday.isChecked()) daysOfWeek.add("Friday");

                        // Validate the input
                        if (!className.isEmpty() && !instructorName.isEmpty() && !daysOfWeek.isEmpty()) {
                            // Add the new class to the ViewModel
                            ClassItem newClass = new ClassItem(className, classTimeRange, instructorName, daysOfWeek);
                            viewModel.addClassItem(newClass);

                            //Update the adapter and ListView
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            showDialog();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(ClassItem classItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_assign, null);

        final EditText editTextClassName = dialogView.findViewById(R.id.editTextClassName);
        final EditText editTextInstructorName = dialogView.findViewById(R.id.editTextInstructorName);
        final TimePicker timePickerStartTime = dialogView.findViewById(R.id.timePickerStartTime);
        final TimePicker timePickerEndTime = dialogView.findViewById(R.id.timePickerEndTime);
        final CheckBox checkBoxMonday = dialogView.findViewById(R.id.checkbox_monday);
        final CheckBox checkBoxTuesday = dialogView.findViewById(R.id.checkbox_tuesday);
        final CheckBox checkBoxWednesday = dialogView.findViewById(R.id.checkbox_wednesday);
        final CheckBox checkBoxThursday = dialogView.findViewById(R.id.checkbox_thursday);
        final CheckBox checkBoxFriday = dialogView.findViewById(R.id.checkbox_friday);

        // Pre-populate dialog with class details
        editTextClassName.setText(classItem.getCourseName());
        editTextInstructorName.setText(classItem.getInstructor());
        // You'll need to parse the start and end times from classItem.getClassTime() to set on TimePickers

        // Parse the class start and end times and set them in the TimePickers
        String[] times = classItem.getTime().split(" - ");
        String[] startTime = times[0].split(":");
        String[] endTime = times[1].split(":");

        timePickerStartTime.setCurrentHour(Integer.parseInt(startTime[0]));
        timePickerStartTime.setCurrentMinute(Integer.parseInt(startTime[1]));
        timePickerEndTime.setCurrentHour(Integer.parseInt(endTime[0]));
        timePickerEndTime.setCurrentMinute(Integer.parseInt(endTime[1]));

        // Set the checkboxes based on the days of the week for the class
        LinkedHashSet<String> daysOfWeek = classItem.getDaysOfWeek();
        checkBoxMonday.setChecked(daysOfWeek.contains("Monday"));
        checkBoxTuesday.setChecked(daysOfWeek.contains("Tuesday"));
        checkBoxWednesday.setChecked(daysOfWeek.contains("Wednesday"));
        checkBoxThursday.setChecked(daysOfWeek.contains("Thursday"));
        checkBoxFriday.setChecked(daysOfWeek.contains("Friday"));

        builder.setView(dialogView)
                .setTitle("Edit Class")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the edited information and update the ViewModel
                        String className = editTextClassName.getText().toString().trim();
                        // Extract times from TimePickers
                        int startHour = timePickerStartTime.getCurrentHour();
                        int startMinute = timePickerStartTime.getCurrentMinute();
                        int endHour = timePickerEndTime.getCurrentHour();
                        int endMinute = timePickerEndTime.getCurrentMinute();

                        // Declare and initialize startTime and endTime variables
                        String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
                        String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
                        String classTimeRange = startTime + " - " + endTime;
                        String instructorName = editTextInstructorName.getText().toString().trim();

                        daysOfWeek.clear();
                        if (checkBoxMonday.isChecked()) daysOfWeek.add("Monday");
                        if (checkBoxTuesday.isChecked()) daysOfWeek.add("Tuesday");
                        if (checkBoxWednesday.isChecked()) daysOfWeek.add("Wednesday");
                        if (checkBoxThursday.isChecked()) daysOfWeek.add("Thursday");
                        if (checkBoxFriday.isChecked()) daysOfWeek.add("Friday");

                        if (!className.isEmpty() && !instructorName.isEmpty() && !daysOfWeek.isEmpty()) {
                            // Create an updated ClassItem object
                            ClassItem updatedClass = new ClassItem(className, classTimeRange, instructorName, daysOfWeek);
                            viewModel.updateClassItem(position, updatedClass); // This updates the item in the LiveData list
                        } else {
                            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            showEditDialog(classItem, position); // Reshow the dialog
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}