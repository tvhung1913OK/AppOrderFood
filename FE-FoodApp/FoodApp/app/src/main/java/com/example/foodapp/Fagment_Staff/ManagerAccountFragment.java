package com.example.foodapp.Fagment_Staff;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.foodapp.Model.Staff;
import com.example.foodapp.R;
import com.example.foodapp.adapter_staff.StaffAdapter;
import com.example.foodapp.config.Config;
import com.example.foodapp.config.VolleySingleton;
import com.example.foodapp.databinding.FragmentManagerAccountBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ManagerAccountFragment extends Fragment {
    private static final String TAG = "ManagerAccountFragment";
    public FragmentManagerAccountBinding binding;
    public ArrayList<Staff> staffData;
    public List<Staff> newStaff;
    public StaffAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManagerAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MoreFragment_Staff()).commit());
        binding.fabCreate.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AddAccountSatff_Fragment()).commit());

        staffData = new ArrayList<>();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        binding.rcvStaff.addItemDecoration(itemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rcvStaff);

        getStaffData();
    }

    public void getStaffData() {
        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Config.IP + "staff", response -> {
            try {
                String jsonArray = String.valueOf(new JSONObject(response).getJSONArray("listStaff"));
                staffData = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<Staff>>() {
                }.getType());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    newStaff = staffData.stream().filter(staff -> !"admin".equals(staff.getRole())).collect(Collectors.toList());
                    adapter = new StaffAdapter((ArrayList<Staff>) newStaff);
                    binding.rcvStaff.setAdapter(adapter);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Log.d(TAG, "onSwiped: " + newStaff.get(position).getUser().getId());
                    VolleySingleton.getInstance(requireActivity()).addToRequestQueue(new StringRequest(Request.Method.DELETE, Config.IP + "staff/deleteStaff/" + newStaff.get(position).getUser().getId(), response -> {
                        Toast.makeText(requireActivity(), "onSuccess!", Toast.LENGTH_SHORT).show();
                        newStaff.remove(position);
                        adapter.notifyItemRemoved(position);
                    }, error -> {
                        Toast.makeText(requireActivity(), "onFailure: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }));
                    break;
                case ItemTouchHelper.RIGHT:
                    Fragment fragment = new AddAccountSatff_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("staff", new Gson().toJson(newStaff.get(position)));
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive);

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_24)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
                    .addSwipeRightActionIcon(R.drawable.archive_24)
                    .addSwipeRightLabel("Update")
                    .setSwipeRightLabelColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    .create()
                    .decorate();
        }
    };

}