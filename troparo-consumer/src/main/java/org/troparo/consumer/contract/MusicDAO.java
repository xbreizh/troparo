package org.troparo.consumer.contract;





import org.troparo.model.Music;

import java.util.List;

public interface MusicDAO {

    public List<Music> getAllMoviesByComposer(String composerName);
}