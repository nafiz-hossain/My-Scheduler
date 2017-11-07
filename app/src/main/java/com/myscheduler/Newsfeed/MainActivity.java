package com.myscheduler.Newsfeed;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myscheduler.Login.UserProfile;
import com.myscheduler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Fragment
{

    private Button feedButton;
    private Button notification;
    private List<ContactInfo> list;
    private RecyclerView recList;
    private ContactAdapter ca;
    private EditText subEditText;
    private TextView surNameText;
    private TextView firstNameText;
    private Calendar calendar;

    UserProfile profile;


    private DatabaseReference mdatabase;
    private StorageReference mStorage;
    private FirebaseUser user;
    private DatabaseReference reference;

    UserProfile userPro;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.newsfeed_activity_main, container, false);
        reference = FirebaseDatabase.getInstance().getReference().child("Feed");
        //Make an array list for the content of the Recyclerview
        list = new ArrayList<>();

        //This is the calendar to get the time of your device.=
        calendar = Calendar.getInstance();

        //Set the onclick listener for the 'post feed' button
        feedButton = (Button) v.findViewById(R.id.postFeedButton);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openDialog();

            }
        });
        //notification = (Button) v.findViewById(R.id.notification);



        //Declares the view for your feed
        ContactInfo contactInfo = new ContactInfo();

        //Set the layout and the RecyclerView
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new ContactAdapter(list, getContext());
        //Set the adapter for the recyclerlist
        recList.setAdapter(ca);
       /* v.findViewById(R.id.b_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        */
//        v.findViewById(R.id.b_profilePage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),ProfilePageActivity.class);
//                startActivity(intent);
//            }
//        });



//        mAuth = FirebaseAuth.getInstance();
        //Bundle data = getIntent().getExtras();
        //profile = (UserProfile) data.getParcelable("Profile");



        user = FirebaseAuth.getInstance().getCurrentUser();

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        loadData();
        load_data_firebase();



        return v;




    }
    public void load_data_firebase()
    {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    ContactInfo tourEvent = dataSnapshot1.getValue(ContactInfo.class);
                    list.add(tourEvent);
                }

                ca = new ContactAdapter(list,getContext());
                recList.setAdapter(ca);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    void loadData()
    {
        DatabaseReference mUserReference;
        String refKey = user.getUid();
        mUserReference = mdatabase.child("users").child(refKey);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                if(userProfile==null)
                {
                    //Toast.makeText(ProfilePageActivity.this,"You have no info to show, Edit Profile",Toast.LENGTH_SHORT).show();
                    return;
                }

                //System.err.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX got it");

                StorageReference filePath = FirebaseStorage.getInstance().getReference()
                        .child("photos")
                        .child(user.getUid().toString())
                        .child("ProfilePic");

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        userProfile.setProfilePicUrl(uri.toString());
                       /* if(uri!=null)
                            Picasso.with().load(uri).fit().centerCrop().into(profilePic);*/
                    }
                });

                userPro = userProfile;
                /*
                mFullname.setText(userProfile.getName().toString());


                mEmail.setText(userProfile.getEmail().toString());
                //mEmail.setText(user.getEmail());

                mPhone.setText(userProfile.getPhone().toString());
                mDOB.setText(userProfile.getDOB().toString());
                mAddr.setText(userProfile.getAddr().toString());
                mInst.setText(userProfile.getInst().toString());*/

                // [END EXCLUDE]
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("noob", "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getActivity(), "Failed to load User Profile.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });
    }


    /*

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_activity_main);
        //Make an array list for the content of the Recyclerview
        list = new ArrayList<>();

        //This is the calendar to get the time of your device.=
        calendar = Calendar.getInstance();

        //Set the onclick listener for the 'post feed' button
        feedButton = (Button) findViewById(R.id.postFeedButton);
        feedButton.setOnClickListener(this);
        notification = (Button) findViewById(R.id.notification);
        notification.setOnClickListener(this);


        //Declares the view for your feed
        ContactInfo contactInfo = new ContactInfo();

        //Set the layout and the RecyclerView
        recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new ContactAdapter(list);
        //Set the adapter for the recyclerlist
        recList.setAdapter(ca);
        findViewById(R.id.b_signOut).setOnClickListener(this);
        findViewById(R.id.b_profilePage).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //Bundle data = getIntent().getExtras();
        //profile = (UserProfile) data.getParcelable("Profile");

    }*/
    /*

    @Override
    public void onStart()
    {
        super.onStart();
        //Bundle data = getIntent().getExtras();
        //profile = (UserProfile) data.getParcelable("Profile");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"CurrentUser email: "+currentUser.getEmail().toString());
    }*/
/*
    private void signOut()
    {
        mAuth.signOut();
        // end activity, go back to sign in page;
        getActivity().finish();
    }*/
/*
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i== R.id.b_signOut)
        {
            signOut();
        }
        else if(i== R.id.b_profilePage)
        {
            //Intent intent = new Intent(NewsFeedActivity.this,databaseTestActivity.class);
            Intent intent = new Intent(MainActivity.this,ProfilePageActivity.class);
            startActivity(intent);
            //this.finish();
        }
        else if(i==R.id.postFeedButton)
                openDialog();
        else if (i==R.id.notification)
        {
            Intent intent = new Intent(MainActivity.this, com.myscheduler.Notification.MainActivity.class);
            startActivity(intent);
        }

    }*/

    //Method to open the dialog to post a feed
    private void openDialog(){
        //LayoutInflater inflater = LayoutInflater.from(Dashboard.this);
        View subView = getActivity().getLayoutInflater().inflate(R.layout.newsfeed_dialog_layout, null);
        subEditText = (EditText)subView.findViewById(R.id.dialogEditText);
        // = (EditText)subView.findViewById(R.id.surNameEditText);
        //firstNameText = (EditText)subView.findViewById(R.id.nameEditText);

        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);
       // Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
       // subImageView.setImageDrawable(drawable);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Social media feed");
//        builder.setMessage("New Feed");
        builder.setView(subView);

        //Build the AlertDialog.
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ContactInfo ci = new ContactInfo();
                ci.feed = subEditText.getText().toString();
                ci.firstName = userPro.getName();
                ci.feedDate = "Date value";
                ci.surName = "";
                ci.path = userPro.getProfilePicUrl();

                /*
                UserProfile n = new UserProfile();

                String name = n.returnName();*/


                //mFullname.setText(name.toString());
                //ci.surName = surNameText.getText().toString();
                //ci.firstName = firstNameText.getText().toString();
                //Add data to the list
                list.add(ci);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                DatabaseReference usersRef = ref.child("Feed");
                String pushKey = usersRef.getKey();
                usersRef.push().setValue(ci);
                Log.e("pushkey: ", pushKey);

                //Notify the Adapter so that you can see the changes.
                ca.notifyDataSetChanged();
                //Scroll the RecyclerView to the bottom.
                recList.smoothScrollToPosition(ca.getItemCount());

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //This will close the Dialog
            }
        });

        builder.show();
    }

    /*@Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to Sign Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }*/


}