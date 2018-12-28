package org.troparo.consumer.impl;


import org.hibernate.SessionFactory;
import org.troparo.consumer.contract.MusicDAO;
import org.troparo.model.Music;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("musicDAO")
public class MusicDAOImpl implements MusicDAO {

    public static final String MUSIC_COMPOSER = "AR Rahman";

    @Inject
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<Music> getAllMoviesByComposer(String composerName) {

        // local variables
        List<Music> lstMusic = null;

        if (composerName != null && composerName.equalsIgnoreCase(MUSIC_COMPOSER)) {
            System.out.println("composer is correct");
            // get all books info from database
            lstMusic = sessionFactory.getCurrentSession().createQuery("from Music").getResultList();
            System.out.println("size of list: " + lstMusic.size());
        }
        return lstMusic;
    }
}