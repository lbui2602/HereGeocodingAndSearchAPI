package com.example.heregeocodingandsearchapi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heregeocodingandsearchapi.IClickItem;
import com.example.heregeocodingandsearchapi.R;
import com.example.heregeocodingandsearchapi.models.Item;

import java.text.Normalizer;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> list;
    IClickItem iClickItem;
    String textSearch;

    public ItemAdapter(List<Item> list, IClickItem iClickItem, String textSearch) {
        this.list = list;
        this.iClickItem = iClickItem;
        this.textSearch = textSearch;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = list.get(position);
        if (item == null) {
            return;
        }
        textSearch = textSearch.replaceAll("\\s+", " ");
        holder.tvTitle.setText(makeBold(item.getTitle(), textSearch.trim()));
        holder.imgDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItem.onClickItem(item);
            }
        });
    }

    private SpannableString makeBold(String a, String b) {
        String converta = convertVietnameseString(a).toLowerCase().trim();
        String convertb = convertVietnameseString(b).toLowerCase().trim();
        SpannableString spannableString = new SpannableString(a);
        int startIndex = converta.indexOf(convertb);
        while (startIndex != -1) {
            int endIndex = startIndex + convertb.length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex = converta.indexOf(convertb, endIndex);
        }
        return spannableString;
    }

    public static String convertVietnameseString(String str) {
        String normalizedStr = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        normalizedStr = normalizedStr.replaceAll("[đĐ]", "d");
        return normalizedStr.toLowerCase();
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLocation, imgDirection;
        TextView tvTitle;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLocation = itemView.findViewById(R.id.imgLocation);
            imgDirection = itemView.findViewById(R.id.imgDirection);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
