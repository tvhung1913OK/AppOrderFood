package com.example.foodapp.adapter_staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.Model.Category;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryDropDownAdapter extends ArrayAdapter<Category> {

    private List<Category> categoryListFull;

    public CategoryDropDownAdapter(@NonNull Context context, @NonNull List<Category> categoryList) {
        super(context, 0, categoryList);
        categoryListFull = new ArrayList<>(categoryList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropdown, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tvCategoryDropDownName);

        Category category = getItem(position);

        if(category != null) {
            textView.setText(category.getName());
        }
        return convertView;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Category> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(categoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Category category :
                        categoryListFull) {
                    if (category.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(category);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Category) resultValue).getId();
        }
    };
}
