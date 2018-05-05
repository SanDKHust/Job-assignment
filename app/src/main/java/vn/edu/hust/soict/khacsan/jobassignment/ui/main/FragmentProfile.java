package vn.edu.hust.soict.khacsan.jobassignment.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.valdesekamdem.library.mdtoast.MDToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import vn.edu.hust.soict.khacsan.jobassignment.R;
import vn.edu.hust.soict.khacsan.jobassignment.ui.login.LoginActivity;

import static android.app.Activity.RESULT_OK;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.CAMERA_REQUEST_CODE;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.PICK_FROM_GALLERY;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.REQUESTCODE;
import static vn.edu.hust.soict.khacsan.jobassignment.untils.Constant.STATUS;

/**
 * Created by San on 02/27/2018.
 */

public class FragmentProfile extends Fragment {
    private ImageView imageView;
    private TextView tvName, tvStatus;
    private RecyclerView recyclerView;
    private boolean mPermissionCamera = false, mPermissionImg = false;
    private View mViewSheetBottom = null;
    private BottomSheetDialog mBottomSheetDialog;
    private StorageReference mImageStorage;
    private AVLoadingIndicatorView avi;

    private String mCurrentPhotoPath;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = layout.findViewById(R.id.img_avatar);
        tvName = layout.findViewById(R.id.tv_username);
        tvStatus = layout.findViewById(R.id.tv_status);
        recyclerView = layout.findViewById(R.id.info_recycler_view);
        avi = layout.findViewById(R.id.loading_indicator_info);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        layout.findViewById(R.id.btn_change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewSheetBottom == null) {
                    mViewSheetBottom = getLayoutInflater().inflate(R.layout.buttom_sheet_dialog_change_avatar, null);
                    mViewSheetBottom.findViewById(R.id.action_camera_edit_avatar).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setupButtonCamera();
                        }
                    });
                    mViewSheetBottom.findViewById(R.id.action_chon_anh_edit_avatar).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setupButtonChooseImg();
                        }
                    });
                    mBottomSheetDialog = new BottomSheetDialog(getContext());
                    mBottomSheetDialog.setContentView(mViewSheetBottom);
                }

                mBottomSheetDialog.show();
            }
        });


        layout.findViewById(R.id.btn_edit_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEdit = new Intent(getContext(), EditStatusActivity.class);
                intentEdit.putExtra(STATUS, tvStatus.getText().toString());
                startActivityForResult(intentEdit, REQUESTCODE);
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        updateUI(FirebaseAuth.getInstance().getCurrentUser());

        return layout;
    }

    private void setupButtonChooseImg() {
        if (mBottomSheetDialog.isShowing()) mBottomSheetDialog.dismiss();
        if (!mPermissionImg) {
            Dexter.withActivity(getActivity())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mPermissionImg = true;
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            mPermissionImg = false;
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
        }
    }


    private void setupButtonCamera() {
        if (mBottomSheetDialog.isShowing()) mBottomSheetDialog.dismiss();
        if (!mPermissionCamera) {
//            PermissionListener snackbarPermissionListener =
//                    SnackbarOnDeniedPermissionListener.Builder
//                            .with(FragmentProfile.this.getView(), "Camera access is needed to take pictures of your")
//                            .withOpenSettingsButton("Settings")
//                            .withCallback(new Snackbar.Callback() {
//                                @Override
//                                public void onShown(Snackbar snackbar) {
//
//                                }
//                                @Override
//                                public void onDismissed(Snackbar snackbar, int event) {
//
//                                }
//                            }).build();
            Dexter.withActivity(getActivity())
                    .withPermissions( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.getGrantedPermissionResponses().size() == 2) {
                                mPermissionCamera = true;
                                dispatchTakePictureIntent();
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        }else  {
            dispatchTakePictureIntent();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public void updateUI(final FirebaseUser current_user) {
        avi.show();
        tvName.setText(current_user.getDisplayName());
        if (current_user.getPhotoUrl() != null) {
            Picasso.with(getContext()).load(current_user.getPhotoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.ic_account_circle)
                    .error(R.drawable.ic_error)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            avi.hide();
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(current_user.getPhotoUrl())
                                    .placeholder(R.drawable.ic_account_circle)
                                    .error(R.drawable.ic_error)
                                    .into(imageView);
                            avi.hide();
                        }
                    });
        }else avi.hide();
        List<Info> infoList = new ArrayList<>();
        infoList.add(new Info(R.drawable.ic_account_circle, "Display name: ", current_user.getDisplayName()));
        infoList.add(new Info(R.drawable.ic_email, "Email: ", current_user.getEmail()));
        infoList.add(new Info(R.drawable.ic_restore, "Change password", ""));
        infoList.add(new Info(R.drawable.ic_signout, "Sign out", ""));
        AdapterRVInfo adapterRVInfo = new AdapterRVInfo(R.layout.item_profile, infoList);
        adapterRVInfo.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        break;
                    }
                    case 2: {

                        break;
                    }
                    case 3: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        recyclerView.setAdapter(adapterRVInfo);


        final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvStatus.setText(dataSnapshot.child("status").getValue().toString());
                //String image = dataSnapshot.child("image").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                tvStatus.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uploadImage(uri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                uploadImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                MDToast.makeText(getActivity(), "Error: " + error.getMessage(),
                        MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//upload avartar
    private void uploadImage(final Uri resultUri) {
        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();

        StorageReference filePath = mImageStorage.child("profile_images").child(uid + ".jpg");
        final AlertDialog dialog = new SpotsDialog(getContext(),R.style.CustomDialogUploadImg);

        dialog.show();
        File thumb_filePath = new File(resultUri.getPath());
        try {
            Bitmap thumb_bitmap = new Compressor(getContext())
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(thumb_filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumb_byte = baos.toByteArray();

            final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(uid + ".jpg");
            filePath.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot thumb_task) {
                                    String thumb_downloadUrl = thumb_task.getDownloadUrl().toString();
                                    DatabaseReference mCurrentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                                    mCurrentUserDatabase.child("thumb_image").setValue(thumb_downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(downloadUrl).build();
                                            current_user.updateProfile(profileUpdates);
                                            imageView.setImageURI(resultUri);
                                            dialog.dismiss();
                                            MDToast.makeText(getActivity(), "Successful!",
                                                    MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            MDToast.makeText(getActivity(), "Error: " + e.getMessage(),
                                                    MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    MDToast.makeText(getActivity(), "Error: " + e.getMessage(),
                                            MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            MDToast.makeText(getActivity(), "Error: " + e.getMessage(),
                                    MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                        }
                    });
        } catch (IOException e) {
            dialog.dismiss();
            MDToast.makeText(getActivity(), "Error: " + e.getMessage(),
                    MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            e.printStackTrace();
        }
    }

    class AdapterRVInfo extends BaseQuickAdapter<Info, BaseViewHolder> {

        public AdapterRVInfo(int layoutResId, @Nullable List<Info> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Info item) {
            helper.setText(R.id.item_type_info, item.getTypeInfo())
                    .setText(R.id.item_info, item.getInfo())
                    .setImageResource(R.id.item_ic_type_info, item.getIc());
        }
    }

    class Info {
        int ic;
        String typeInfo, info;

        public Info(int ic, String typeInfo, String info) {
            this.ic = ic;
            this.typeInfo = typeInfo;
            this.info = info;
        }

        public int getIc() {
            return ic;
        }

        public void setIc(int ic) {
            this.ic = ic;
        }

        public String getTypeInfo() {
            return typeInfo;
        }

        public void setTypeInfo(String typeInfo) {
            this.typeInfo = typeInfo;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
