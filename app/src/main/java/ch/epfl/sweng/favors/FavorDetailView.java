package ch.epfl.sweng.favors;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.databinding.FragmentEditProfileBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavorDetailView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorDetailView extends android.support.v4.app.Fragment  {

    private static final String TAG = "FAVOR_DETAIL_FRAGMENT";

    public ObservableField<String> title;
    public ObservableField<String> description;
    public ObservableField<Integer> tokenCost;


    FragmentEditProfileBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FAVOR_ID = "favorID";

    private Favor currentFavor;


    public FavorDetailView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param favorID ID of the favor that will be used to display details about.
     * @return A new instance of fragment FavorDetailView.
     */
    // TODO: Rename and change types and number of parameters
    public static FavorDetailView newInstance(String favorID) {
        FavorDetailView fragment = new FavorDetailView();
        Bundle args = new Bundle();
        args.putString(FAVOR_ID, favorID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewFavor model = ViewModelProviders.of(getActivity()).get(SharedViewFavor.class);
        model.getfavor().observe(this, newFavor -> {
            title = newFavor.getObservableStringObject(Favor.StringFields.title);
            description = newFavor.getObservableStringObject(Favor.StringFields.description);
            //TODO add token cost binding with new database implementation
            //tokenCost = new ObservableField<>(newFavor.get(Favor.IntegerFields.))
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_detail_view,container,false);
        binding.setElements(this);

        return binding.getRoot();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
