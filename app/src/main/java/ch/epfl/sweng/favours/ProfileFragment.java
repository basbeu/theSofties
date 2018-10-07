package ch.epfl.sweng.favours;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/FDlmugRDbsGw5J4x5avN");
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile_layout, container, false);


       /*mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    TextView title = rootView.findViewById(R.id.profileTitle);
                    title.setText(documentSnapshot.getString("firstName"));


                    assert (user != null);
                    String[] usrInfo =  user.userInfoList();
                    ListAdapter userAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, usrInfo);
                    ListView listProfileElements = (ListView)rootView.findViewById(R.id.drawer_layout);
                    listProfileElements.setAdapter(userAdapter);
                }
            }
        });*/







        return inflater.inflate(R.layout.fragment_profile_layout, container, false);

    }

    public void getUser(final View v){
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    TextView title = v.findViewById(R.id.profileTitle);
                    title.setText(documentSnapshot.getString("firstName"));
                    /*

                    assert (user != null);
                    String[] usrInfo =  user.userInfoList();
                    ListAdapter userAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, usrInfo);
                    ListView listProfileElements = (ListView)rootView.findViewById(R.id.drawer_layout);
                    listProfileElements.setAdapter(userAdapter);*/
                }
            }
        });
    }


}
