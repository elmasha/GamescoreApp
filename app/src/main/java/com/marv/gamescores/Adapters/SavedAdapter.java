package com.marv.gamescores.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.marv.gamescores.Models.SavedNews;
import com.marv.gamescores.Models.Stories;
import com.marv.gamescores.R;
import com.marv.gamescores.TimeAgo;
import com.squareup.picasso.Picasso;


public class SavedAdapter extends FirestoreRecyclerAdapter<SavedNews, SavedAdapter.StoriesViewHolder> {

    private OnItemCickListener listener;
    public Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public SavedAdapter(@NonNull FirestoreRecyclerOptions<SavedNews> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull StoriesViewHolder holder, int position, @NonNull SavedNews model) {
        holder.headline.setText(model.getTitle());

        String subtt = model.getTitle();
        if (subtt == null){
            holder.story.setText(model.getDescription());
        }else {
            holder.story.setText(model.getDescription());
        }
        Picasso.get().load(model.getImage()).fit().into(holder.homeNewsImage);

       long milisecond = model.getTimestamp().getTime();
//        String date = DateFormat.format("dd-MMM-yyyy | hh:mm a",new Date(milisecond)).toString();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        holder.date.setText(TimeAgo.getTimeAgo(milisecond)+"");

//        if(model.getViewsCount() >= 1000){
//            double div = model.getViewsCount() /1000;
//            DecimalFormat precision = new DecimalFormat("0.0");
//            holder.views.setText(precision.format(div)+"K ");
//        }else if(model.getLikesCount() >= 1000) {
//            double divlike = model.getLikesCount() /1000;
//            DecimalFormat precision = new DecimalFormat("0.0");
//            holder.likes.setText(precision.format(divlike)+"K ");
//        }else if (model.getCommentCount() >=1000){
//            double divcomment = model.getCommentCount() /1000;
//            DecimalFormat precision = new DecimalFormat("0.0");
//            holder.comment.setText(precision.format(divcomment)+"K ");
//        }else {
//            holder.views.setText(model.getViewsCount()+"");
//            holder.comment.setText(model.getCommentCount()+"");
//            holder.likes.setText(model.getLikesCount()+"");
//        }

    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_row,parent,false);

        return new StoriesViewHolder(v);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class StoriesViewHolder extends RecyclerView.ViewHolder{
       private TextView headline,story,likes,views,comment,date;
       private ImageView homeNewsImage;
       private RelativeLayout ord_layout;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            headline = itemView.findViewById(R.id.row_headline2);
            story = itemView.findViewById(R.id.row_story2);
            homeNewsImage  = itemView.findViewById(R.id.row_image2);
            likes = itemView.findViewById(R.id.row_likes2);
            views = itemView.findViewById(R.id.row_views2);
            date = itemView.findViewById(R.id.row_date2);
            comment = itemView.findViewById(R.id.row_comment2);



            likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
//                        News news = documentSnapshot.toObject(News.class);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface  OnItemCickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemCickListener listener){
        this.listener = listener;

    }


    //----Views count
    private void likesCount(String doc_Id){

        final DocumentReference sfDocRef = db.collection("News").document(doc_Id);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                double newPopulation = snapshot.getLong("likesCount") + 1;
                transaction.update(sfDocRef, "likesCount", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    ///___end likes






}
