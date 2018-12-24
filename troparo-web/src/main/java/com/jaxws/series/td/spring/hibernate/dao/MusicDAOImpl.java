package com.jaxws.series.td.spring.hibernate.dao;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.model.Music;

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

        if(composerName !=null && composerName.equalsIgnoreCase(MUSIC_COMPOSER)){
            System.out.println("composer is correct");
            // get all books info from database
            lstMusic = sessionFactory.getCurrentSession().createQuery("from Music").getResultList();
            System.out.println("size of list: "+lstMusic.size());
        }
        return lstMusic;
    }
}