package com.pierr.rockyouth.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pierr.rockyouth.model.Album;
import com.pierr.rockyouth.R;

import java.util.List;

/**
 * Created by Pierr on 13-9-21.
 */
public class ReadItFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_album_detail_read, container, false);
        assert rootView != null;
        TextView songListView = (TextView) rootView.findViewById(R.id.song_list);

        Album album = getArguments().getParcelable("album");
        assert album != null;
        List<Album.Song> songs = album.songs;
        StringBuilder sb = new StringBuilder();

        for(Album.Song s : songs){
            sb.append(s.title).append("\n");
        }

        songListView.setText(sb.toString());

        return rootView;
    }
}
