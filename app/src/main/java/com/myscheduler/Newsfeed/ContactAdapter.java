package com.myscheduler.Newsfeed;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myscheduler.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private DatabaseReference mdatabase;
    private StorageReference mStorage;
    private FirebaseUser user;
    private ValueEventListener mUserModelListener;
    private List<ContactInfo> contactList;
    Context context;

    public ContactAdapter(List<ContactInfo> contactList, Context context)
    {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Calendar calendar = Calendar.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        ContactInfo ci = contactList.get(i);
        //Set the text of the feed with your data
        contactViewHolder.feedText.setText(ci.feed);
        contactViewHolder.surNameText.setText(ci.surName);
        contactViewHolder.nameText.setText(ci.firstName);
        contactViewHolder.feedDate.setText(calendar.getTime().toString());
        Picasso.with(context).load(Uri.parse(ci.path)).fit().centerCrop().into(contactViewHolder.propic);
        //contactViewHolder.propic.setImageResource();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.newsfeed_card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView feedText;
        protected TextView nameText;
        protected TextView surNameText;
        protected TextView feedDate;
        private TextView mFullname;
        private ImageView propic;

        public ContactViewHolder(View v)
        {
            super(v);
            mFullname = (TextView) v.findViewById(R.id.name);
            feedText =  (TextView) v.findViewById(R.id.feedText);
            surNameText =  (TextView) v.findViewById(R.id.surName);
            nameText =  (TextView) v.findViewById(R.id.name);
            feedDate =  (TextView) v.findViewById(R.id.date);
            propic = (ImageView) v.findViewById(R.id.profileImageView);
        }
    }

}


