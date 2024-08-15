package com.example.myapplication1.AppFirebase.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.AppFirebase.LoginFirebase;
import com.example.myapplication1.AppFirebase.MainMenuNavigation;
import com.example.myapplication1.Models.Note;
import com.example.myapplication1.Models.User;
import com.example.myapplication1.R;
import com.example.myapplication1.databinding.FragmentProfileFirebaseBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ProfileFragmentFirebase extends Fragment {

    private FragmentProfileFirebaseBinding binding;

    TextView usernameTextView, onlineTextView, aboutMeTextView;
    Button changeImageButton, changeUsernameButton, changePasswordButton, deleteAllNoteButton, logOutButton;
    ImageView imageView;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    Activity activity;
    Context context;

    Boolean changeUsernameSession = false;
    Boolean changePasswordSession = false;

    public ProfileFragmentFirebase(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public ProfileFragmentFirebase(Activity activity, Context context, Uri filePath) {
        this.activity = activity;
        this.context = context;
        this.filePath = filePath;
    }

    public ProfileFragmentFirebase() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileFirebaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        usernameTextView = binding.usernameTextView;
        aboutMeTextView = binding.aboutMeTextView;
        imageView = binding.imageView;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://todolistandroidproject-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference("Users");

        TextPaint paint = binding.usernameTextView.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, binding.usernameTextView.getTextSize(),
                new int[]{
                        Color.parseColor("#7490BB"),
                        Color.parseColor("#2D538C"),
                }, null, Shader.TileMode.CLAMP);
        binding.usernameTextView.getPaint().setShader(textShader);

        storage = FirebaseStorage.getInstance("gs://todolistandroidproject.appspot.com");
        storageReference = storage.getReference("Profile picture");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(auth.getCurrentUser().getUid())) {
                        User user = dataSnapshot.getValue(User.class);
                        usernameTextView.setText(user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getAboutMe();
        uploadImage();
        download();

        imageView.setImageResource(R.drawable.image_profile);
        storageReference.getFile(new File(auth.getCurrentUser().getUid())).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                      imageView.setImageURI(Uri.fromFile(new File(auth.getCurrentUser().getUid())));
            }
        });

        changeImageButton = binding.changeButton;
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });



        changeUsernameButton = binding.changeUsernameButton;
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!changeUsernameSession) changeUsername();
                else Toast.makeText(activity, "You have already changed your name in this session", Toast.LENGTH_LONG).show();

            }
        });

        changePasswordButton = binding.changePasswordButton;
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!changePasswordSession) changePassword();
                else Toast.makeText(activity, "You have already changed your password in this session", Toast.LENGTH_LONG).show();
            }
        });

        deleteAllNoteButton = binding.deleteAllNoteButton;
        deleteAllNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        logOutButton = binding.logOutButton;
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "User Signed Out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(activity, LoginFirebase.class));
                            }
                        });
            }
        });

        binding.changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor prefEditor = activity.getSharedPreferences("themeApp", Context.MODE_PRIVATE).edit();
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    prefEditor.putString("themeApp", "light");
                    prefEditor.apply();
                    startActivity(new Intent(getActivity(), MainMenuNavigation.class));
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    prefEditor.putString("themeApp", "dark");
                    prefEditor.apply();
                    startActivity(new Intent(getActivity(), MainMenuNavigation.class));
                }

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, 71);
    }

    private void uploadImage() {
        if(filePath != null) {
            storageReference.child(auth.getCurrentUser().getUid()).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(activity, "Uploaded image", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void download() {
        try {
            StorageReference picture = storage.getReference("Profile picture/" + auth.getCurrentUser().getUid());
            final File localFile = File.createTempFile("userProfilePicture", ".jpg");
            picture.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(
                                FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(activity, "Download complete", Toast.LENGTH_LONG).show();
                            imageView.setImageURI(Uri.fromFile(localFile));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(activity, "Download failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity,
                    "Failed to create temp file: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    void getAboutMe()
    {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey().equals(auth.getCurrentUser().getUid()))
                    {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            aboutMeTextView.setText("О себе: " + user.getAboutMe());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void changeUsername() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(activity);

        alert.setMessage("Enter your new username");
        alert.setTitle("Change username");

        alert.setView(edittext);

        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                if(edittext.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                }
                else {
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if(dataSnapshot.getKey().equals(auth.getCurrentUser().getUid()))
                                {
                                    User user = dataSnapshot.getValue(User.class);
                                    User user1 = new User(edittext.getText().toString().trim(), user.getEmail(), user.getPassword(), user.getAboutMe(), user.getAccessRights());
                                    users.child(auth.getCurrentUser().getUid()).setValue(user1);
                                    Toast.makeText(activity, "Successfully change username", Toast.LENGTH_LONG).show();
                                    changeUsernameSession = true;
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    void changePassword() {
        AlertDialog.Builder alertOld = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(activity);

        alertOld.setMessage("Enter old password");
        alertOld.setTitle("Change password");

        alertOld.setView(edittext);

        alertOld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if(dataSnapshot.getKey().equals(auth.getCurrentUser().getUid())) {
                                User user = dataSnapshot.getValue(User.class);
                                if(user.getPassword().equals(edittext.getText().toString().trim())) {
                                    AlertDialog.Builder alertNew = new AlertDialog.Builder(context);
                                    final EditText edittextNew = new EditText(activity);

                                    alertNew.setMessage("Enter new password");
                                    alertNew.setTitle("Change password");

                                    alertNew.setView(edittextNew);

                                    alertNew.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton)
                                        {
                                            if(edittextNew.getText().toString().trim().isEmpty()) {
                                                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                users.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            if(dataSnapshot.getKey().equals(auth.getCurrentUser().getUid()))
                                                            {
                                                                User user = dataSnapshot.getValue(User.class);
                                                                User user1 = new User(user.getUsername(), user.getEmail(), edittextNew.getText().toString(), user.getAboutMe(), user.getAccessRights());
                                                                users.child(auth.getCurrentUser().getUid()).setValue(user1);
                                                                Toast.makeText(activity, "Successfully change password", Toast.LENGTH_LONG).show();
                                                                changePasswordSession = true;
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                    alertNew.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // what ever you want to do with No option.
                                        }
                                    });

                                    alertNew.show();
                                    break;
                                }
                                else Toast.makeText(activity, "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        alertOld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alertOld.show();
    }



    public void deleteAll() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to delete all notes?");
        alert.setTitle("Delete all notes");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference notes = db.getReference("Notes");
                notes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Note note = dataSnapshot.getValue(Note.class);
                            if(note.getIdUser().equals(auth.getCurrentUser().getUid())) {
                                String keyData = dataSnapshot.getKey();
                                notes.child(keyData).removeValue();
                            }
                        }
                        Toast.makeText(activity, "All notes deleted", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

}