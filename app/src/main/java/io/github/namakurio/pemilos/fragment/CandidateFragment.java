package io.github.namakurio.pemilos.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.namakurio.pemilos.R;
import io.github.namakurio.pemilos.activity.MainActivity;
import io.github.namakurio.pemilos.adapter.CandidateAdapter;
import io.github.namakurio.pemilos.connection.API;
import io.github.namakurio.pemilos.connection.RestAdapter;
import io.github.namakurio.pemilos.connection.callbacks.CallbackGetCandidate;
import io.github.namakurio.pemilos.model.Candidate;
import io.github.namakurio.pemilos.utils.NetworkCheck;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidateFragment extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;
    private Call<CallbackGetCandidate> callbackCall;
    private CandidateAdapter adapter;

    public CandidateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_candidate, container, false);
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        //set data and list adapter
        adapter = new CandidateAdapter(getActivity(), new ArrayList<Candidate>());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

//        adapter.setOnItemClickListener(new CandidateAdapter().OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, Candidate obj) {
//                Snackbar.make(root_view, obj.name, Snackbar.LENGTH_SHORT).show();
//                ActivityCategoryDetails.navigate(getActivity(), obj);
//            }
//        });
    }


    private void requestListCategory() {
        API api = RestAdapter.createAPI();
        callbackCall = api.getCandidate();
        callbackCall.enqueue(new Callback<CallbackGetCandidate>() {
            @Override
            public void onResponse(Call<CallbackGetCandidate> call, Response<CallbackGetCandidate> response) {
                CallbackGetCandidate resp = response.body();
//                if (resp != null && resp.status.equals("success")) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    adapter.setItems(resp.categories);
//                    ActivityMain.getInstance().category_load = true;
//                    ActivityMain.getInstance().showDataLoaded();
//                } else {
//                    onFailRequest();
//                }
            }

            @Override
            public void onFailure(Call<CallbackGetCandidate> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                if (!call.isCanceled()) onFailRequest();
            }

        });
    }

    private void onFailRequest() {
        if (NetworkCheck.isConnect(getActivity())) {
//            showFailedView(R.string.msg_failed_load_data);
        } else {
//            showFailedView(R.string.no_internet_text);
        }
    }

    private void showFailedView(@StringRes int message) {
//        MainActivity.getInstance().showDialogFailed(message);
    }


}
