package com.zengyicalvin.homework9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tab2Artist extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2artist, container, false);
        JSONObject json = ((DetailActivity)getActivity()).getTab2JSON();
        JSONArray artists = null;
        JSONArray imgs = null;
        try {
            artists = json.getJSONArray("artists");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            imgs = json.getJSONArray("images");
            Log.i("jsonImageTest", imgs.length() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2 && i < imgs.length(); i++) {
            JSONObject jsonArtistTmp = null;
            try {
                jsonArtistTmp = artists.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonArtistTmp == null) {
                if (i == 0) {
                    LinearLayout layone= (LinearLayout) rootView.findViewById(R.id.not_music1_layout);// change id here
                    layone.setVisibility(View.VISIBLE);
                    JSONObject imgJson = null;
                    try {
                        imgJson = new JSONObject(imgs.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TextView not_artist1_name = (TextView) rootView.findViewById(R.id.not_artist1_name);
                    try {
                        not_artist1_name.setText(imgJson.getJSONObject("queries").getJSONArray("request").getJSONObject(0).getString("searchTerms"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ImageView[] imgViewArr = new ImageView[8];
                    imgViewArr[0] = (ImageView) rootView.findViewById(R.id.na1p1);
                    imgViewArr[1] = (ImageView) rootView.findViewById(R.id.na1p2);
                    imgViewArr[2] = (ImageView) rootView.findViewById(R.id.na1p3);
                    imgViewArr[3] = (ImageView) rootView.findViewById(R.id.na1p4);
                    imgViewArr[4] = (ImageView) rootView.findViewById(R.id.na1p5);
                    imgViewArr[5] = (ImageView) rootView.findViewById(R.id.na1p6);
                    imgViewArr[6] = (ImageView) rootView.findViewById(R.id.na1p7);
                    imgViewArr[7] = (ImageView) rootView.findViewById(R.id.na1p8);
                    try {
                        for (int j = 0; j < 8 && j < imgJson.getJSONArray("items").length(); j++) {
                            String imageUri = imgJson.getJSONArray("items").getJSONObject(j).getString("link");
                            Picasso.with(getActivity()).load(imageUri).into(imgViewArr[j]);
                        }
                    }catch (JSONException err) {
                        err.printStackTrace();
                    }
                } else {
                    LinearLayout layone= (LinearLayout) rootView.findViewById(R.id.not_music2_layout);// change id here
                    layone.setVisibility(View.VISIBLE);
                    JSONObject imgJson = null;
                    try {
                        imgJson = new JSONObject(imgs.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TextView not_artist2_name = (TextView) rootView.findViewById(R.id.not_artist2_name);
                    try {
                        not_artist2_name.setText(imgJson.getJSONObject("queries").getJSONArray("request").getJSONObject(0).getString("searchTerms"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ImageView[] imgViewArr = new ImageView[8];
                    imgViewArr[0] = (ImageView) rootView.findViewById(R.id.na2p1);
                    imgViewArr[1] = (ImageView) rootView.findViewById(R.id.na2p2);
                    imgViewArr[2] = (ImageView) rootView.findViewById(R.id.na2p3);
                    imgViewArr[3] = (ImageView) rootView.findViewById(R.id.na2p4);
                    imgViewArr[4] = (ImageView) rootView.findViewById(R.id.na2p5);
                    imgViewArr[5] = (ImageView) rootView.findViewById(R.id.na2p6);
                    imgViewArr[6] = (ImageView) rootView.findViewById(R.id.na2p7);
                    imgViewArr[7] = (ImageView) rootView.findViewById(R.id.na2p8);
                    try {
                        for (int j = 0; j < 8 && j < imgJson.getJSONArray("items").length(); j++) {
                            String imageUri = imgJson.getJSONArray("items").getJSONObject(j).getString("link");
                            Picasso.with(getActivity()).load(imageUri).into(imgViewArr[j]);
                        }
                    }catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
            } else {
                if (i == 0) {
                    LinearLayout layone= (LinearLayout) rootView.findViewById(R.id.a1_layout);// change id here
                    layone.setVisibility(View.VISIBLE);

                    TextView artist1_name = (TextView) rootView.findViewById(R.id.artist1_name);
                    TextView a1_name = (TextView) rootView.findViewById(R.id.a1_name);
                    TextView a1_followers = (TextView) rootView.findViewById(R.id.a1_followers);
                    TextView a1_popularity = (TextView) rootView.findViewById(R.id.a1_popularity);
                    TextView a1_url = (TextView) rootView.findViewById(R.id.a1_url);

                    try {
                        artist1_name.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a1_name.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a1_followers.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a1_popularity.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("popularity"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ImageView[] imgViewArr = new ImageView[8];
                    imgViewArr[0] = (ImageView) rootView.findViewById(R.id.a1p1);
                    imgViewArr[1] = (ImageView) rootView.findViewById(R.id.a1p2);
                    imgViewArr[2] = (ImageView) rootView.findViewById(R.id.a1p3);
                    imgViewArr[3] = (ImageView) rootView.findViewById(R.id.a1p4);
                    imgViewArr[4] = (ImageView) rootView.findViewById(R.id.a1p5);
                    imgViewArr[5] = (ImageView) rootView.findViewById(R.id.a1p6);
                    imgViewArr[6] = (ImageView) rootView.findViewById(R.id.a1p7);
                    imgViewArr[7] = (ImageView) rootView.findViewById(R.id.a1p8);

                    try {
                        JSONObject imgJson = new JSONObject(imgs.getString(i));
                        JSONArray imgArt = imgJson.getJSONArray("items");
                        for (int j = 0; j < imgArt.length() && j < 8; j++) {
                            String imageUri = imgArt.getJSONObject(j).getString("link");
                            Picasso.with(getActivity()).load(imageUri).into(imgViewArr[j]);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String text = null;
                    try {
                        text = "<a href='" +  artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify") + "'> Spotify </a>";
                        a1_url.setClickable(true);
                        a1_url.setMovementMethod(LinkMovementMethod.getInstance());
                        a1_url.setText(Html.fromHtml(text));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    LinearLayout layone= (LinearLayout) rootView.findViewById(R.id.a2_layout);// change id here
                    layone.setVisibility(View.VISIBLE);

                    TextView artist2_name = (TextView) rootView.findViewById(R.id.artist2_name);
                    TextView a2_name = (TextView) rootView.findViewById(R.id.a2_name);
                    TextView a2_followers = (TextView) rootView.findViewById(R.id.a2_followers);
                    TextView a2_popularity = (TextView) rootView.findViewById(R.id.a2_popularity);
                    TextView a2_url = (TextView) rootView.findViewById(R.id.a2_url);

                    try {
                        artist2_name.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a2_name.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a2_followers.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        a2_popularity.setText(artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("popularity"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ImageView[] imgViewArr = new ImageView[8];
                    imgViewArr[0] = (ImageView) rootView.findViewById(R.id.a2p1);
                    imgViewArr[1] = (ImageView) rootView.findViewById(R.id.a2p2);
                    imgViewArr[2] = (ImageView) rootView.findViewById(R.id.a2p3);
                    imgViewArr[3] = (ImageView) rootView.findViewById(R.id.a2p4);
                    imgViewArr[4] = (ImageView) rootView.findViewById(R.id.a2p5);
                    imgViewArr[5] = (ImageView) rootView.findViewById(R.id.a2p6);
                    imgViewArr[6] = (ImageView) rootView.findViewById(R.id.a2p7);
                    imgViewArr[7] = (ImageView) rootView.findViewById(R.id.a2p8);

                    try {
                        JSONObject imgJson = new JSONObject(imgs.getString(i));
                        JSONArray imgArt = imgJson.getJSONArray("items");
                        for (int j = 0; j < imgArt.length() && j < 8; j++) {
                            String imageUri = imgArt.getJSONObject(j).getString("link");
                            Picasso.with(getActivity()).load(imageUri).into(imgViewArr[j]);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String text = null;
                    try {
                        text = "<a href='" +  artists.getJSONObject(i).getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify") + "'> Spotify </a>";
                        Log.i("spotifyLinktest", text);
                        a2_url.setClickable(true);
                        a2_url.setMovementMethod(LinkMovementMethod.getInstance());
                        a2_url.setText(Html.fromHtml(text));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return rootView;
    }
}
