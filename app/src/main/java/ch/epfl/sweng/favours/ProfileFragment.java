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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private static final String LOG_TAG = "PROFILE_FRAGMENT";


    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert userAuth != null;
        DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/"+userAuth.getUid());

        rootView = inflater.inflate(R.layout.fragment_profile_layout, container, false);

        Log.d(LOG_TAG, "onCreateViewCalled");
       mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(LOG_TAG, "retrievedDocument");

                    User user = documentSnapshot.toObject(User.class);
                    assert (user != null);
                    Log.d(LOG_TAG, user.getFirstName());
                    Log.d(LOG_TAG, user.getLastName());
                    Log.d(LOG_TAG, user.getBasedLocation());
                    Log.d(LOG_TAG, user.getEmail());
                    Log.d(LOG_TAG, user.getSex());

                    /*String[] usrInfo =  user.userInfoList();
                    ListAdapter userAdapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_list_item_1, usrInfo);
                    ListView listProfileElements = (ListView)container.findViewById(R.id.drawer_layout);
                    listProfileElements.setAdapter(userAdapter);*/

                    TextView firstName = container.findViewById(R.id.profFirstName);
                    TextView lastName = container.findViewById(R.id.profLastName);
                    TextView email = container.findViewById(R.id.profEmail);
                    TextView sex = container.findViewById(R.id.profSex);
                    TextView city = container.findViewById(R.id.profCity);

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    email.setText(user.getEmail());
                    sex.setText(user.getSex());
                    city.setText(user.getBasedLocation());
                }
            }
        });

        return inflater.inflate(R.layout.fragment_profile_layout, container, false);

    }


}
