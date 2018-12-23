package com.jaxws.series.td.spring.hibernate.dao;

import com.jaxws.series.td.spring.hibernate.model.Music;

import java.util.List;

public interface MusicDAO {

    public List<Music> getAllMoviesByComposer(String composerName);
}