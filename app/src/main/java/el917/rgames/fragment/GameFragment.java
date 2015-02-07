package el917.rgames.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dexafree.materialList.view.MaterialListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import el917.rgames.R;
import el917.rgames.material.ProgressBarCircular;
import el917.rgames.utils.UtilsHelper;
import me.drakeet.materialdialog.MaterialDialog;


public class GameFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private static final String ARG_CURRENT_URL = "selectedUrl";
    private static final String ARG_POSITION = "positionConsole";
    private String url;

    private List<GameItem> gameList;
    private int positionConsole;


    public static GameFragment newInstance(String url, int position) {
        GameFragment mFragment = new GameFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(ARG_CURRENT_URL, url);
        mBundle.putInt(ARG_POSITION, position);

        mFragment.setArguments(mBundle);
        return mFragment;
    }

    TextView textView;
    private MaterialListView listView;
    private View view;

    private ProgressBarCircular progressBarCircular;
    private String color[];
    private String console;
   RelativeLayout rootLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = getArguments().getString(ARG_CURRENT_URL);
        positionConsole = getArguments().getInt(ARG_POSITION);
        color = getResources().getStringArray(R.array.console_color);

        view = inflater.inflate(R.layout.lv_fragment, container, false);
        progressBarCircular = (ProgressBarCircular) view.findViewById(R.id.progress);

        switch (positionConsole) {
            case 0:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 1:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 2:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 3:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 4:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 5:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
            case 6:
                console = getResources().getStringArray(R.array.console_name)[positionConsole];
                progressBarCircular.setBackgroundColor(Color.parseColor(color[positionConsole]));
                break;
        }


        listView = (MaterialListView) view.findViewById(R.id.material_listview);
        listView.setOnItemClickListener(this);


        if (UtilsHelper.isOnline(activity)) {
            new LoadHtml().execute();
        }

        return view;
    }

    FragmentActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;


    }


    String titleGame;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        titleGame = gameList.get(position).getTitleGame();

        final MaterialDialog materialDialog = new MaterialDialog(activity);

        View viewDialog = LayoutInflater.from(activity).inflate(R.layout.dialog, null);

        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.setView(viewDialog);
        materialDialog.show();

        View btn1 = viewDialog.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        View btn2 = viewDialog.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        View btn3 = viewDialog.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        View btn4 = viewDialog.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);


        MaterialRippleLayout.on(btn1).rippleColor(Color.parseColor("#212B60"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
        MaterialRippleLayout.on(btn2).rippleColor(Color.parseColor("#212B60"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
        MaterialRippleLayout.on(btn3).rippleColor(Color.parseColor("#212B60"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
        MaterialRippleLayout.on(btn4).rippleColor(Color.parseColor("#212B60"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();


    }


    @Override
    public void onClick(View v) {
        Uri uri;
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                uri = Uri.parse("http://www.google.com/#q=" + titleGame + " " + console + "&lr=lang_ru");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btn2:
                uri = Uri.parse("http://www.google.it/m/search?tbm=nws&q=" + titleGame + " " + console + "&lr=lang_ru");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btn3:
                uri = Uri.parse("https://www.google.com/search?hl=ru&site=imghp&tbm=isch&source=hp&q=" + titleGame + " " + console);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.btn4:
                uri = Uri.parse("https://m.youtube.com/results?q=" + titleGame + " " + console);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;


        }

    }


    class LoadHtml extends AsyncTask<Void, Void, Void> {

        String[] thumbnailUrls;
        String[] titleGames;
        String[] typeGames;
        String[] releasedData;
        int countGames;

        Document document;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (textView != null) {
                textView.setText("");
            }


            progressBarCircular.setVisibility(View.VISIBLE);

        }


        @Override

        protected Void doInBackground(Void... params) {
            if (UtilsHelper.isOnline(activity)) {

                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elTitleGames = document.select("table [class=\"list_item_table\"]  div[class*=\"title\"]");
                Elements elTypeGames = document.select("table [class=\"list_item_table\"]  div[class=\"game_list_genre\"]");
                Elements elReleasedData = document.select("table [class=\"list_item_table\"]  span[class=\"value noactive\"]");
                Elements elThumbnailUrls = document.select("table [class=\"list_item_table\"]  img[src*=/screenshots_copy/]");

                countGames = Math.min(elTitleGames.size(), elTypeGames.size());

                titleGames = new String[elTitleGames.size()];
                for (int i = 0; i < elTitleGames.size(); i++) {
                    titleGames[i] = elTitleGames.get(i).text();
                }
                typeGames = new String[elTypeGames.size()];
                for (int i = 0; i < elTypeGames.size(); i++) {
                    typeGames[i] = elTypeGames.get(i).text();
                }
                releasedData = new String[elReleasedData.size()];
                for (int i = 0; i < elReleasedData.size(); i++) {
                    releasedData[i] = elReleasedData.get(i).text();
                }
                thumbnailUrls = new String[elThumbnailUrls.size()];
                for (int i = 0; i < elThumbnailUrls.size(); i++) {
                    thumbnailUrls[i] = elThumbnailUrls.get(i).attr("src");
                }
            }
            //  UtilsHelper.stopThread(5);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (UtilsHelper.isOnline(activity)) {
                gameList = new ArrayList<>();
                if (countGames > 0) {
                    for (int i = 0; i < countGames; i++) {
                        GameItem gameItem = new GameItem(titleGames[i], typeGames[i], releasedData[i], "http://www.metagames.ru" + thumbnailUrls[i]);
                        gameList.add(gameItem);
                    }
                    GameAdapter gameAdapter = new GameAdapter(color[positionConsole], gameList, activity);
                    listView.setAdapter(gameAdapter);
                } else {
                    RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    textView = new TextView(activity);
                    if (rootLayout != null) {
                        rootLayout.addView(textView);
                    }
                    textView.setLayoutParams(params);
                    textView.setText("No Data");

                }

                if (progressBarCircular != null) {
                    progressBarCircular.setVisibility(View.GONE);


                }

            }
        }
    }


}
