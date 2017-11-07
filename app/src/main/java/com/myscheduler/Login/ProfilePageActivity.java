package com.myscheduler.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.myscheduler.R;
import com.squareup.picasso.Picasso;
public class ProfilePageActivity extends AppCompatActivity {

    public final static String TAG = "UserInfoActivity";

    private static final int GALLERY_INTENT = 2;

    private DatabaseReference mdatabase;
    private StorageReference mStorage;
    private FirebaseUser user;
    private ValueEventListener mUserModelListener;

    private Button updateProf_btn,uploadImg_btn,editProf_btn;
    private EditText mFullname, mEmail, mDOB, mAddr, mInst, mPhone;
    private ImageView profilePic;

    private String profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blah);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        profilePic = (ImageView) findViewById(R.id.img_profilepic);
        mFullname = (EditText) findViewById(R.id.nameET);
        mEmail = (EditText) findViewById(R.id.emailET);
        mPhone = (EditText) findViewById(R.id.phoneET);
        mDOB = (EditText) findViewById(R.id.dobET);
        mAddr = (EditText) findViewById(R.id.addrET);
        mInst = (EditText) findViewById(R.id.instET);

        updateProf_btn = (Button) findViewById(R.id.b_profUpdate);
        uploadImg_btn = (Button) findViewById(R.id.b_uploadImg);
        editProf_btn = (Button) findViewById(R.id.b_editProf);

        uploadImg_btn.setVisibility(View.INVISIBLE);
        updateProf_btn.setVisibility(View.INVISIBLE);
        setEditingEnabled(false);
        editProf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProf_btn.setVisibility(View.GONE);
                uploadImg_btn.setVisibility(View.VISIBLE);
                updateProf_btn.setVisibility(View.VISIBLE);
                setEditingEnabled(true);
            }
        });

        uploadImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        updateProf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
                setEditingEnabled(false);
                uploadImg_btn.setVisibility(View.INVISIBLE);
                updateProf_btn.setVisibility(View.INVISIBLE);
                editProf_btn.setVisibility(View.VISIBLE);
            }
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            String uID = user.getUid();

            final StorageReference filePath = mStorage.child("photos").child(uID).child("ProfilePic");

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProfilePageActivity.this, "Upload done",Toast.LENGTH_SHORT).show();

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            profilePicUrl = uri.toString();
                            Picasso.with(ProfilePageActivity.this).load(uri).fit().centerCrop().into(profilePic);

                        }
                    });

                }
            });
        }
    }




    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mUserReference;
        String refKey = user.getUid();
        mUserReference = mdatabase.child("users").child(refKey);
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

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
                    public void onSuccess(Uri uri) {
                        if(uri!=null) Picasso.with(ProfilePageActivity.this).load(uri).fit().centerCrop().into(profilePic);
                    }
                });

                mFullname.setText(userProfile.getName().toString());


                mEmail.setText(userProfile.getEmail().toString());
                //mEmail.setText(user.getEmail());

                mPhone.setText(userProfile.getPhone().toString());
                mDOB.setText(userProfile.getDOB().toString());
                mAddr.setText(userProfile.getAddr().toString());
                mInst.setText(userProfile.getInst().toString());

                // [END EXCLUDE]
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ProfilePageActivity.this, "Failed to load User Profile.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });
    }

    private void saveToDB()
    {
        setEditingEnabled(false);
        Toast.makeText(this, "Saving to Database...", Toast.LENGTH_SHORT).show();
        mdatabase.child("users")
                .child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(mEmail.getText().toString().equals("")) mEmail.setText(user.getEmail());

                        final String name = mFullname.getText().toString();
                        final String email = mEmail.getText().toString();
                        final String phone = mPhone.getText().toString();
                        final String addr = mAddr.getText().toString();
                        final String DOB = mDOB.getText().toString();
                        final String inst = mInst.getText().toString();
                        final String userID = user.getUid().toString();

                        final StorageReference filePath = mStorage.child("photos").child(userID).child("ProfilePic");
                        String profilePicUrl = filePath.getDownloadUrl().toString();
                        UserProfile user1 = new UserProfile(name,  email,phone,   userID,   profilePicUrl,  DOB,  addr,  inst);

                        mdatabase.child("users")
                                .child(userID)
                                .setValue(user1);

                        setEditingEnabled(true);
                        //finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ProfilePageActivity.this, "data change cancelled", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {



        mFullname.setEnabled(enabled);
        mEmail.setEnabled(enabled);
        mDOB.setEnabled(enabled);
        mAddr.setEnabled(enabled);
        mInst.setEnabled(enabled);
        mPhone.setEnabled(enabled);

        if (enabled) {
            updateProf_btn.setVisibility(View.VISIBLE);
        } else {
            updateProf_btn.setVisibility(View.GONE);
        }
    }
}