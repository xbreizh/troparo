package com.jaxws.series.td.spring.hibernate.dao;

import com.jaxws.series.td.spring.hibernate.model.Music;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("musicDAO")
public class MusicDAOImpl implements MusicDAO {

    public static final String MUSIC_COMPOSER = "AR Rahman";

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(value="transactionManager")
    public List<Music> getAllMoviesByComposer(String composerName) {

        // local variables
        List<Music> lstMusic = null;

        if(null != composerName && composerName.equalsIgnoreCase(MUSIC_COMPOSER)){

            // get all books info from database
            lstMusic = sessionFactory.getCurrentSession().createCriteria(Music.class).list();
        }
        return lstMusic;
    }
}