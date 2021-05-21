package com.example.my_coach.sport.SportsProperties;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_coach.Model.AbouteSportModel;
import com.example.my_coach.R;
import com.example.my_coach.adapter.AbouteSportAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class AboutSportFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AbouteSportAdapter adapter;
    private List<AbouteSportModel> list;
    private FirebaseFirestore firestore;
    private String GetSportBriefID;



    public AboutSportFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_sport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_Aboute_sport);
        progressBar=view.findViewById(R.id.progres_About_Sorte);
        Toast.makeText(getActivity(), SportsSpecificationsActivity.UID, Toast.LENGTH_LONG).show();
        firestore=FirebaseFirestore.getInstance();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list=new ArrayList<>();
        adapter=new AbouteSportAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        getSportsData();

    }

    private void getSportsData() {
        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("aboute_sport").whereEqualTo("sport_id",SportsSpecificationsActivity.UID)
                .orderBy("sport_id", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error==null){

                        if (value==null){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "value is null", Toast.LENGTH_SHORT).show();
                        }else {


                            for (DocumentChange documentChange:value.getDocumentChanges()){
                                String SportID =documentChange.getDocument().getString("sport_id");

                                AbouteSportModel model=documentChange.getDocument().toObject(AbouteSportModel.class);
                                list.add(model);
                                adapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                        }


                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
    }


}