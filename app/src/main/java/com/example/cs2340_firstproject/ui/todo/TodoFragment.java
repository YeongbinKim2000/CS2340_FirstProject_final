package com.example.cs2340_firstproject.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.cs2340_firstproject.R;
import com.example.cs2340_firstproject.databinding.FragmentNotificationsBinding;
import com.example.cs2340_firstproject.databinding.FragmentTodoBinding;
import com.example.cs2340_firstproject.ui.notifications.NotificationsViewModel;

import java.util.ArrayList;

public class TodoFragment extends Fragment {

    private @NonNull FragmentTodoBinding binding;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView list;
    private Button button;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TodoViewModel todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addItem(view);
            }
        });
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        binding.listView.setAdapter(itemsAdapter);

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItem(position);
                return true;
            }
        });
        return root;
    }

    private void removeItem(int position) {
        itemsAdapter.remove(itemsAdapter.getItem(position));
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(requireContext(), "Item removed", Toast.LENGTH_SHORT).show();
    }

    private void addItem(View view) {
        EditText input = root.findViewById(R.id.text_notifications);
        String itemText = input.getText().toString();

        if (!(itemText.equals(""))) {
            itemsAdapter.add(itemText);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
