package best.Aveiro.EBEC.ui.best;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import best.Aveiro.EBEC.R;

public class best extends Fragment {

    private BestViewModel mViewModel;
    private Button fb_btn, tt_btn, web_btn, lk_btn, flr_btn, insta_btn;
    public static best newInstance() {
        return new best();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.best_fragment, container, false);
        fb_btn = root.findViewById(R.id.button_fb);
        flr_btn = root.findViewById(R.id.button_flr);
        insta_btn = root.findViewById(R.id.button_insta);
        lk_btn = root.findViewById(R.id.button_lk);
        tt_btn = root.findViewById(R.id.button_tt);
        web_btn = root.findViewById(R.id.button_web);

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + "EBEC.aveiro/"));

                startActivity(intent);
            }
        });
        tt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/bestaveiro"));

                startActivity(intent);
            }
        });
        insta_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/best.aveiro/"));

                startActivity(intent);
            }
        });
        web_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("http://bestaveiro.web.ua.pt/#"));

                startActivity(intent);
            }
        });
        flr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flickr.com/photos/bestaveiro/with/37313883721/"));

                startActivity(intent);
            }
        });
        lk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/best-aveiro/"));

                startActivity(intent);
            }
        });




        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BestViewModel.class);
        // TODO: Use the ViewModel
    }

}