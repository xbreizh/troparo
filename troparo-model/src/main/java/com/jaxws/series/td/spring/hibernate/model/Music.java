package com.jaxws.series.td.spring.hibernate.model;


import javax.persistence.*;

@Entity
@Table(name = "MUSICO")
public class Music {

    // member variables
    @Id
    @GeneratedValue
    @Column(name = "MUSIC_ID")
    private int musicId;

    @Column(name= "MOVIE_NAME")
    private String movieName;

    @Column(name= "MOVIE_DIRECTOR")
    private String director;

    @Column(name= "YEAR_OF_RELEASE")
    private String yearOfRelease;

    @Column(name= "COMMENTS")
    private String comments;

    // getters & setters
    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(String yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}