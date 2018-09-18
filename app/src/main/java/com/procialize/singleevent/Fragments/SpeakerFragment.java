package com.procialize.singleevent.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.singleevent.Activity.SpeakerDetailsActivity;
import com.procialize.singleevent.Adapter.SpeakerAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.FetchSpeaker;
import com.procialize.singleevent.GetterSetter.SpeakerList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;


import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpeakerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeakerFragment extends Fragment implements SpeakerAdapter.SpeakerAdapterListner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private APIService mAPIService;
    SwipeRefreshLayout speakerfeedrefresh;
    RecyclerView speakerrecycler;
    EditText searchEt;
    SpeakerAdapter speakerAdapter;
    private ProgressBar progressBar;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<SpeakerList> speakerList;
    private List<SpeakerList> speakersDBList;
    private DBHelper dbHelper;

    String eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
     String token;


    public SpeakerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeakerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeakerFragment newInstance(String param1, String param2) {
        SpeakerFragment fragment = new SpeakerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speaker, container, false);
        speakerrecycler = view.findViewById(R.id.speakerrecycler);

        speakerfeedrefresh = view.findViewById(R.id.speakerfeedrefresh);

        searchEt = view.findViewById(R.id.searchEt);

        progressBar = view.findViewById(R.id.progressBar);

        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();


//        speakerrecycler.setHasFixedSize(true);


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        speakerrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        speakerrecycler.setLayoutAnimation(animation);

        if (cd.isConnectingToInternet()) {
            fetchSpeaker(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            speakersDBList = dbHelper.getSpeakerDetails();

            speakerAdapter = new SpeakerAdapter(getActivity(), speakersDBList, this);
            speakerAdapter.notifyDataSetChanged();
            speakerrecycler.setAdapter(speakerAdapter);
            speakerrecycler.scheduleLayoutAnimation();
        }

        speakerfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // fetchSpeaker(token,evntid);
                if (cd.isConnectingToInternet()) {
                    fetchSpeaker(token, eventid);
                } else {
                    db = procializeDB.getReadableDatabase();

                    speakersDBList = dbHelper.getSpeakerDetails();

                    speakerAdapter = new SpeakerAdapter(getActivity(), speakersDBList, SpeakerFragment.this);
                    speakerAdapter.notifyDataSetChanged();
                    speakerrecycler.setAdapter(speakerAdapter);
                    speakerrecycler.scheduleLayoutAnimation();
                }
            }
        });


        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    speakerAdapter.getFilter().filter(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }


    public void fetchSpeaker(String token, String eventid) {

        showProgress();
        mAPIService.SpeakerFetchPost(token, eventid).enqueue(new Callback<FetchSpeaker>() {
            @Override
            public void onResponse(Call<FetchSpeaker> call, Response<FetchSpeaker> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    hideProgress();
                    if (speakerfeedrefresh.isRefreshing()) {
                        speakerfeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                hideProgress();
                if (speakerfeedrefresh.isRefreshing()) {
                    speakerfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchSpeaker> response) {

        speakerList = response.body().getSpeakerList();
        procializeDB.clearSpeakersTable();
        procializeDB.insertSpeakersInfo(speakerList, db);

        // specify an adapter (see also next example)
        speakerAdapter = new SpeakerAdapter(getActivity(), response.body().getSpeakerList(), this);
        speakerAdapter.notifyDataSetChanged();
        speakerrecycler.setAdapter(speakerAdapter);
        speakerrecycler.scheduleLayoutAnimation();

        SubmitAnalytics(token, eventid, "", "", "speaker");
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onContactSelected(SpeakerList speaker) {
        Intent speakeretail = new Intent(getContext(), SpeakerDetailsActivity.class);
        speakeretail.putExtra("id", speaker.getAttendeeId());
        speakeretail.putExtra("name", speaker.getFirstName() + " " + speaker.getLastName());
        speakeretail.putExtra("city", speaker.getCity());
        speakeretail.putExtra("country", speaker.getCountry());
        speakeretail.putExtra("company", speaker.getCompany());
        speakeretail.putExtra("designation", speaker.getDesignation());
        speakeretail.putExtra("description", speaker.getDescription());
        speakeretail.putExtra("totalrate", speaker.getTotalRating());
        speakeretail.putExtra("profile", speaker.getProfilePic());
        startActivity(speakeretail);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
