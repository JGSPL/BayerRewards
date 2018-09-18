package com.procialize.singleevent.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.procialize.singleevent.Activity.AgendaDetailActivity;
import com.procialize.singleevent.Adapter.AgendaAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.AgendaList;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.FetchAgenda;
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
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements AgendaAdapter.AgendaAdapterListner{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView agendarecycler;
    SwipeRefreshLayout agendafeedrefresh;
    private APIService mAPIService;
    private ProgressBar progressBar;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AgendaList> agendaList;
    private List<AgendaList> agendaDBList;
    private DBHelper dbHelper;
    String eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    String token;

    public AgendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        agendarecycler =  view.findViewById(R.id.agendarecycler);
        agendafeedrefresh = view.findViewById(R.id.agendafeedrefresh);
        progressBar = view.findViewById(R.id.progressBar);

        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        agendarecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        agendarecycler.setLayoutAnimation(animation);


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");



        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
         token = user.get(SessionManager.KEY_TOKEN);

        if (cd.isConnectingToInternet()) {

            fetchAgenda(token, eventid);
        }else{
            db = procializeDB.getReadableDatabase();

            agendaDBList = dbHelper.getAgendaDetails();
            // specify an adapter (see also next example)
            AgendaAdapter agendaAdapter = new AgendaAdapter(getActivity(),agendaDBList,this);
            agendaAdapter.notifyDataSetChanged();
            agendarecycler.setAdapter(agendaAdapter);
            agendarecycler.scheduleLayoutAnimation();
            progressBar.setVisibility(View.GONE);

            if (agendafeedrefresh.isRefreshing()) {
                agendafeedrefresh.setRefreshing(false);
            }
        }

        agendafeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {

                    fetchAgenda(token, eventid);
                }else{
                    db = procializeDB.getReadableDatabase();

                    agendaDBList = dbHelper.getAgendaDetails();
                    // specify an adapter (see also next example)
                    AgendaAdapter agendaAdapter = new AgendaAdapter(getActivity(),agendaDBList, AgendaFragment.this);
                    agendaAdapter.notifyDataSetChanged();
                    agendarecycler.setAdapter(agendaAdapter);
                    agendarecycler.scheduleLayoutAnimation();
                    progressBar.setVisibility(View.GONE);

                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                }
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onContactSelected(AgendaList agenda) {
        Intent agendadetail = new Intent(getContext(), AgendaDetailActivity.class);

        agendadetail.putExtra("id",agenda.getSessionId());
        agendadetail.putExtra("date",agenda.getSessionDate());
        agendadetail.putExtra("name",agenda.getSessionName());
        agendadetail.putExtra("description",agenda.getSessionDescription());
        agendadetail.putExtra("starttime",agenda.getSessionStartTime());
        agendadetail.putExtra("endtime",agenda.getSessionEndTime());

        startActivity(agendadetail);

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


    public void fetchAgenda(String token, String eventid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.AgendaFetchPost(token,eventid).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {

                if(response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);

                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                }else
                {
                    progressBar.setVisibility(View.GONE);
                    if (agendafeedrefresh.isRefreshing()) {
                        agendafeedrefresh.setRefreshing(false);
                    }
                    Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressBar.setVisibility(View.GONE);
                if (agendafeedrefresh.isRefreshing()) {
                    agendafeedrefresh.setRefreshing(false);
                }
                Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<FetchAgenda> response) {


        agendaList = response.body().getAgendaList();
        procializeDB.clearAgendaTable();
        procializeDB.insertAgendaInfo(agendaList, db);

        // specify an adapter (see also next example)
        AgendaAdapter agendaAdapter = new AgendaAdapter(getActivity(),response.body().getAgendaList(),this);
        agendaAdapter.notifyDataSetChanged();
        agendarecycler.setAdapter(agendaAdapter);
        agendarecycler.scheduleLayoutAnimation();

        SubmitAnalytics(token, eventid, "", "", "agenda");
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
