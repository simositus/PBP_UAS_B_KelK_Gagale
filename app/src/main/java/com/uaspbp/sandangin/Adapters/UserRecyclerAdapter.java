package com.uaspbp.sandangin.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uaspbp.sandangin.R;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.RoomViewHolder> implements Filterable
{
    private List<com.uaspbp.sandangin.UserDAO> dataList;
    private List<com.uaspbp.sandangin.UserDAO> filteredDataList;
    private Context context;

    public UserRecyclerAdapter(Context context, List<com.uaspbp.sandangin.UserDAO> dataList)
    {
        this.context = context;
        this.dataList = dataList;
        this.filteredDataList = dataList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_adapter_sandang, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.RoomViewHolder holder, int position)
    {
        final com.uaspbp.sandangin.UserDAO brg = filteredDataList.get(position);
        holder.twNama.setText(brg.getNama());
        holder.twNim.setText(brg.getNim());
        holder.mParent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                com.uaspbp.sandangin.DetailUserFragment dialog = new com.uaspbp.sandangin.DetailUserFragment();
                dialog.show(manager, "dialog");

                Bundle args = new Bundle();
                args.putString("id", brg.getId());
                dialog.setArguments(args);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return filteredDataList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder
    {
        private TextView twNama, twNim;
        private LinearLayout mParent;

        public RoomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            twNama = itemView.findViewById(R.id.twNama);
            twNim = itemView.findViewById(R.id.twNim);
            mParent = itemView.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                if(charSequenceString.isEmpty())
                {
                    filteredDataList = dataList;
                }
                else
                {
                    List<com.uaspbp.sandangin.UserDAO> filteredList = new ArrayList<>();
                    for(com.uaspbp.sandangin.UserDAO UserDAO : dataList)
                    {
                        if(UserDAO.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filteredList.add(UserDAO);
                        }
                        filteredDataList = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredDataList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredDataList = (List<com.uaspbp.sandangin.UserDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}