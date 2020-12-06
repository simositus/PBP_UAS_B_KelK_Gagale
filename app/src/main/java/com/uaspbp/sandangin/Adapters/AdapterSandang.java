package com.uaspbp.sandangin.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uaspbp.sandangin.AddEditActivity;
import com.uaspbp.sandangin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static java.nio.file.attribute.AclEntryPermission.DELETE;

public class AdapterSandang extends RecyclerView.Adapter<AdapterSandang.adapterSandangViewHolder> {

    private List<Sandang> sandangList;
    private List<Sandang> sandangListFiltered;
    private Context context;
    private View view;
    private AdapterSandang.deleteItemListener mListener;

    public AdapterSandang(Context context, List<Sandnag> sandangList,
                       AdapterSandang.deleteItemListener mListener) {
        this.context            = context;
        this.sandangList        = sandangList;
        this.sandangListFiltered   = sandangList;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public AdapterSandang.adapterSandangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_sandang, parent, false);
        return new AdapterSandang.adapterSandangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSandang.adapterSandangViewHolder holder, int position) {
        final Sandang sandang = sandangListFiltered.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(sandang.getNamaSandang());
        holder.txtHarga.setText("Rp "+ formatter.format(sandang.getHarga()));
        holder.txtStok.setText(sandang.getStok());
        Glide.with(context)
                .load(SandangPI.URL_IMAGE+sandang.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle data = new Bundle();
                data.putSerializable("sandang", sandang);
                data.putString("status", "edit");
                AddEditActivity addEditActivity = new AddEditActivity();
                addEditActivity.setArguments(data);
                loadFragment(addEditActivity);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus item ini ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSandang(sandang.getIdSandang());
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (sandangListFiltered != null) ? sandangListFiltered.size() : 0;
    }

    public class adapterSandangViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga, txtStok, ivEdit, ivHapus;;
        private ImageView ivGambar;
        private CardView cardSandang;

        public adapterSandangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama         = itemView.findViewById(R.id.tvNamaSandang);
            txtHarga        = itemView.findViewById(R.id.tvHarga);
            txtStok         = itemView.findViewById(R.id.tvStok);
            ivGambar        = itemView.findViewById(R.id.ivGambar);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
            cardSandang     = itemView.findViewById(R.id.cardSandang);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    sandangListFiltered = sandangList;
                }
                else {
                    List<Sandang> filteredList = new ArrayList<>();
                    for(Sandang sandang : sandangList) {
                        if(String.valueOf(sandang.getNamaSandang()).toLowerCase().contains(userInput) ||
                                sandang.getStok().toLowerCase().contains(userInput)) {
                            filteredList.add(sandang);
                        }
                    }
                    sandangListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sandangListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sandangListFiltered = (ArrayList<Sandang>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.list_view_sandang,fragment)
                .commit();
    }

    public void deleteSandang(int id){
        //Tambahkan hapus item disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data item");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, SandangAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    mListener.deleteItem(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);

    }
}
