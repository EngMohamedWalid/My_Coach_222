package com.example.my_coach.Fragment.SprtsType;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.my_coach.Model.CategoriesModel;
import com.example.my_coach.R;
import com.example.my_coach.adapter.SportsCategoriesAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SportsCategoriesAdapter adapter;
    private List<CategoriesModel> list;
    private FirebaseFirestore firestore;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean isFirstOpen=true;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_sports_categories);
        progressBar=view.findViewById(R.id.progres_sports_categories);
        swipeRefreshLayout=view.findViewById(R.id.SwipCategories);

        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list=new ArrayList<>();
        adapter=new SportsCategoriesAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(
            getResources().getColor( R.color.color_button),
            getResources().getColor( R.color.blue),
            getResources().getColor( R.color.white)
        );

        getSportsData();
        swipeRefreshLayout.setOnRefreshListener(() -> {


            getSportsData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getSportsData(){

        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("sports_Categories")
                .orderBy("Time",Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error==null){

                        if (value==null){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "value is null", Toast.LENGTH_SHORT).show();
                        }else {

                            for (DocumentChange documentChange:value.getDocumentChanges()){
                                if (!isFirstOpen)
                                    list.clear();

                                CategoriesModel model=documentChange.getDocument().toObject(CategoriesModel.class);
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