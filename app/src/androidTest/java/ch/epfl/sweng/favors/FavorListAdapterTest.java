//package ch.epfl.sweng.favors;
//
//import android.content.Context;
//import android.content.Intent;
//import android.databinding.ObservableArrayList;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//
//import ch.epfl.sweng.favors.database.Favor;
//import ch.epfl.sweng.favors.database.FavorRequest;
//import ch.epfl.sweng.favors.database.User;
//
//import static java.util.Arrays.*;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(AndroidJUnit4.class)
//public class FavorListAdapterTest {
//
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
//    private FavorListAdapter adapter = null;
//    private FavorListAdapter.FavorViewHolder holder;
//    private View mockView = mock(View.class);
//    private Fragment mockFragment = mock(Fragment.class);
//    @Mock private FirebaseAuth fakeAuth;
//    @Mock private FirebaseFirestore fakeDb;
//    @Mock private CollectionReference fakeCollection;
//
//    @Mock private DocumentReference fakeDoc;
//    @Mock private DocumentSnapshot fakeDocSnap;
//
//        private Task<DocumentSnapshot> fakeTask;
//        private Task<Void> fakeSetTask;
//        private HashMap<String,Object> data;
//
//        private final String FAKE_ID = "sklfklalsdj";
//        private final String FAKE_DESCRIPTION = "thisisatestemail@email.com";
//        private final String FAKE_TITLE = "title";
//        private final String FAKE_LAST_NAME = "foo";
//        private final String FAKE_SEX = "M";
//        private final Integer FAKE_TIMESTAMP = 343354;
//
//        @Before
//        public void Before(){
//            ExecutionMode.getInstance().setTest(true);
//            fakeTask = Tasks.forResult(fakeDocSnap);
//            fakeSetTask = Tasks.forResult(null);
//            data = new HashMap<>();
//            data.put(Favor.StringFields.title.toString(), FAKE_TITLE);
//            data.put(Favor.StringFields.description.toString(), FAKE_DESCRIPTION);
//            data.put(Favor.IntegerFields.creationTimestamp.toString(), FAKE_TIMESTAMP);
//            when(fakeAuth.getUid()).thenReturn(FAKE_ID);
//            when(fakeDb.collection("favors")).thenReturn(fakeCollection);
//            when(fakeCollection.document(FAKE_ID)).thenReturn(fakeDoc);
//            when(fakeDoc.get()).thenReturn(fakeTask);
//            when(fakeDoc.set(any())).thenReturn(fakeSetTask);
//            when(fakeDocSnap.getData()).thenReturn(data);
//        }
//
//    @Test
//    public void getItemCountTest(){
//        Favor favor = new Favor(FAKE_ID, fakeDb);
//        favor.updateOnDb();
//        adapter = new FavorListAdapter(mockFragment.getActivity(), FavorRequest.all(null, null));
//        assertEquals(adapter.getItemCount(), 1);
//    }
//
////    @Test
////    public void getItemAtPosition() {
////        Favor firstCandy = new Favor();
////        Favor secondCandy = new Favor();
////        adapter.setCandies(asList(firstCandy, secondCandy));
////        assertThat(adapter.getItemAtPosition(0)).isEqualTo(firstCandy);
////        assertThat(adapter.getItemAtPosition(1)).isEqualTo(secondCandy);
////    }
////
////    @Test
////    public void onBindViewHolder_setsTextAndClickEventForCandyView() {
////        Favor favor = new Favor();
////        favor.set(Favor.StringFields.title, "Title");
////        favor.set(Favor.StringFields.description, "description");
////
////        adapter.set
////        adapter.setContext(mockFragment);
////        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        //We have a layout especially for the items in our recycler view. We will see it in a moment.
////        View listItemView = inflater.inflate(R.layout.list_adapter_candies_item, null, false);
////        holder = new FavorListAdapter.CandyViewHolder(listItemView);
////        adapter.onBindViewHolder(holder, 0);
////        assertThat(holder.title.getText().toString()).isEqualTo("Title");
////        assertThat(holder.descriptionView.getText().toString()).isEqualTo("Don't leave these sticky treats in a car during the summer.");
////        holder.itemView.performClick();
////        Intent intent = new Intent(mockFragment.getActivity(), CandyDetailActivity.class);
////        intent.putExtra("favor", candy);
////        verify(mockFragment).startActivity(intent);
////    }
////
////    @Test
////    public void onCreateViewHolder_returnsNewFavorViewHolderOfCorrectLayout() {
////        TestableFavorListAdapter testableAdapter = new TestableFavorListAdapter();
////        testableAdapter.setMockView(mockView);
////        FavorListAdapter.FavorViewHolder favorViewHolder = testableAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
////        assertThat((RecyclerView) favorViewHolder.itemView).isSameAs(mockView);
////    }
////
////    //Here we subclass and override the test subject again so we can use a mock view for testing, instead of the real one.
////    static class TestableFavorListAdapter extends FavorListAdapter {
////        public View mockView;
////
////        public TestableFavorListAdapter(FragmentActivity fragActivity, ObservableArrayList<Favor> favorList) {
////            super(fragActivity, favorList);
////        }
////
////        public void setMockView(View mockView) {
////            this.mockView = mockView;
////        }
////
////        @Override
////        public View getLayout(ViewGroup parent) {
////            return mockView;
////        }
////    }
//}