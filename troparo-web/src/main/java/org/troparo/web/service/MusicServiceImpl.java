package org.troparo.web.service;




import org.troparo.consumer.contract.MusicDAO;
import org.troparo.entities.music.*;
import org.troparo.model.Music;
import org.troparo.services.musicservice.BusinessException;
import org.troparo.services.musicservice.IMusicService;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName="MusicService", endpointInterface="org.troparo.services.musicservice.IMusicService",
        targetNamespace="http://troparo.org/services/MusicService/", portName="MusicServicePort", name="MusicServiceImpl")
public class MusicServiceImpl implements IMusicService {

    @Inject
    private MusicDAO musicDAO;


    @Override
    public MusicListResponseType getAllMovieDetailByComposer(MusicListRequestType parameters) throws BusinessException {

        // local variables
        List<Music> lstMusic = null;
        MovieType movieType = null;
        MovieListType movieListType = null;
        MusicListResponseType musicListResponseType = null;
        BusinessFaultType businessFaultType = null;

        try{
            if(parameters != null && !parameters.getComposerName().equalsIgnoreCase("")){

                // invoke dao to get values
                lstMusic = musicDAO.getAllMoviesByComposer(parameters.getComposerName());

                // create musicListType to set return/response values
                musicListResponseType = new MusicListResponseType();
                musicListResponseType.setComposer(parameters.getComposerName());

                // create movieListType and add movie type retrieved from DAO layer
                movieListType = new MovieListType();

                // iterate through lstMusic and set values in the movieType
                for(Music music : lstMusic){

                    // set values retrieved from DAO class
                    movieType = new MovieType();
                    movieType.setMovieName(music.getMovieName());
                    movieType.setDirector(music.getDirector());
                    movieType.setYear(music.getYearOfRelease());
                    movieType.setComments(music.getComments());
                    movieListType.getMovieType().add(movieType); // add movieType to the movieListType
                }

                // finally set movieListType in the response object i.e. musicListResponseType
                musicListResponseType.setMovieListType(movieListType);
            }
        }
        catch(Exception ex){
            // dummy setting for business exception, we can set more meaningful error depending on the business requirements
            businessFaultType = new BusinessFaultType();
            businessFaultType.setErrorCode(16359);
            businessFaultType.setErrorMessage("Error in invoking Music Service " + ex.getMessage());
            businessFaultType.setErrorDescription(ex.getStackTrace().toString());
        }
        finally{
            // close resources, if any
        }
        return musicListResponseType;
    }
}
