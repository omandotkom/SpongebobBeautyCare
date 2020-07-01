package com.example.spongebobbeautycare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spongebobbeautycare.CreateActivity;
import com.example.spongebobbeautycare.DetilActivity;
import com.example.spongebobbeautycare.R;
import com.example.spongebobbeautycare.data.VARS;
import com.example.spongebobbeautycare.data.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private ArrayList<Product> dataList;
    private Context context;

    public ProductsAdapter(ArrayList<Product> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onViewRecycled(@NonNull ProductViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ProductViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ProductViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ProductViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private ProductViewHolder phv;
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
phv = holder;
                Glide.with(context).load(dataList.get(position).getImage())
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.makeup).into(holder.productImage);

        holder.txtNama.setText(dataList.get(position).getName());
           holder.contentFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetilActivity.class);
                intent.putExtra(VARS.PRODUCTID, dataList.get(position).getId());
                intent.putExtra(VARS.PRODUCTIMAGEURL,dataList.get(position).getImage());
                context.startActivity(intent);
            }
        });
    holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CreateActivity.class);
            intent.putExtra(VARS.PRODUCTID, dataList.get(position).getId());
            intent.putExtra(VARS.PRODUCTIMAGEURL,dataList.get(position).getImage());
            intent.putExtra(VARS.CATEGORY,dataList.get(position).getCategory());
            intent.putExtra(VARS.EDITSTATUS,true);
            context.startActivity(intent);
        }
    });
holder.buttonMore.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        phv.contentFocus.callOnClick();
    }
});

    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder{
        private TextView txtNama;
        private ImageView productImage;
        private LinearLayout contentFocus;
        private Button buttonMore;
        private Button buttonEdit;
        public ProductViewHolder(@NonNull View itemView) {
          super(itemView);
          txtNama = (TextView) itemView.findViewById(R.id.namaproduct);
          productImage = (ImageView) itemView.findViewById(R.id.productimage);
          buttonMore = (Button) itemView.findViewById(R.id.buttonMore);
          buttonEdit = (Button) itemView.findViewById(R.id.buttonEdit);
            contentFocus = (LinearLayout) itemView.findViewById(R.id.contentFocus);
          if (VARS.ISADMIN){
              buttonEdit.setVisibility(View.VISIBLE);
              buttonMore.setVisibility(View.GONE);
             }else{
              buttonEdit.setVisibility(View.GONE);
              buttonMore.setVisibility(View.VISIBLE);

          }
      }
  }
}
