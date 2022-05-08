package com.brytech.seamathrevisiontnt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Symmetry extends Fragment {

    PDFView pdfView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Symmetry");
        View view = inflater.inflate(R.layout.fragment_inflate_particular_pdf, container, false);
        AdView bannerAd = (AdView) view.findViewById(R.id.adViewBannerWholeNos);

        pdfView = (PDFView) view.findViewById(R.id.wholeNumbersPdfView);
        pdfView.fromAsset("symmetry.pdf").load();

        AdRequest adRequest1 = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest1);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent goback = new Intent(Symmetry.this.getActivity(), MainActivity.class);
                startActivity(goback);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }
}
