package com.example.cs2340_firstproject.ui.dashboard;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs2340_firstproject.R;
import com.example.cs2340_firstproject.ui.home.ClassItem;

import java.util.ArrayList;
import java.util.List;

public class AssignExamAdapt extends ArrayAdapter<AssignExamItem> {
    private boolean deleteMode = false;
    private List<AssignExamItem> assignItems; // Keep a reference to the list
    public AssignExamAdapt(Context context, List<AssignExamItem> assigns) {
        super(context, 0, assigns);
        this.assignItems = assigns; // Initialize your class items list
    }

    public List<AssignExamItem> getSelectedItems() {
        List<AssignExamItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            AssignExamItem item = getItem(i);
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_item, parent, false);
        }
        // Get the data item for this position
        AssignExamItem assignItem = getItem(position);

        // Populate the data into the template view using the data object
        ImageView iconView = convertView.findViewById(R.id.classIcon);
        TextView courseNameTextView = convertView.findViewById(R.id.courseName);
        TextView timeTextView = convertView.findViewById(R.id.time);
        TextView daysTextView = convertView.findViewById(R.id.daysTextView);
        TextView instructorTextView = convertView.findViewById(R.id.textViewInstructor);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        // Set the image resource for the ImageView
        iconView.setImageResource(R.drawable.gt_logo);
        int imageSize = dpToPx(getContext(), 48);
        ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
        layoutParams.width = imageSize;
        layoutParams.height = imageSize;
        iconView.setLayoutParams(layoutParams);

        // Set the text for TextViews
        courseNameTextView.setText(assignItem.getAssignName());
        timeTextView.setText(assignItem.getDueDate());
        instructorTextView.setText(assignItem.getClassName());

        // Convert the days of the week Set to a comma-separated String
        String days = TextUtils.join(", ", assignItem.getDaysOfWeek());
        daysTextView.setText(days); // Set this text to the TextView in your layout

        // Set the visibility and checked state of the checkbox
        checkBox.setVisibility(deleteMode ? View.VISIBLE : View.GONE);
        checkBox.setChecked(assignItem.isSelected()); // Assuming there is a isSelected() method in ClassItem
        checkBox.setOnCheckedChangeListener(null); // Null out the listener before changing checked state to prevent unwanted callbacks
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                assignItem.setSelected(isChecked); // Assuming there is a setSelected(boolean) method in ClassItem
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public void deleteSelectedItems() {
        // Get the current items in a modifiable list
        List<AssignExamItem> currentItems = new ArrayList<>(assignItems);

        // Create a new list to hold the items that are not deleted
        List<AssignExamItem> itemsToDelete = new ArrayList<>();

        // Identify the items that should be deleted
        for (int i = 0; i < getCount(); i++) {
            AssignExamItem item = getItem(i);
            if (item.isSelected()) {
                itemsToDelete.add(item);
            }
        }

        // Actually remove the items from the currentItems list
        currentItems.removeAll(itemsToDelete);

        // Now update the adapter with the remaining items
        clear();
        addAll(currentItems);
        notifyDataSetChanged();
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        notifyDataSetChanged(); // Notify the ListView to refresh and show/hide checkboxes
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
