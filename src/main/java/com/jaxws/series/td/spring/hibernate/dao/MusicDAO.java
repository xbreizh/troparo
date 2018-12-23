package com.jaxws.series.td.spring.hibernate.dao;

import java.util.List;

import com.jaxws.series.td.spring.hibernate.model.Music;

public interface MusicDAO {

    public List<Music> getAllMoviesByComposer(String composerName);
}