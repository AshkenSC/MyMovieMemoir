/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "MEMOIR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memoir.findAll", query = "SELECT m FROM Memoir m")
    , @NamedQuery(name = "Memoir.findByMemoirId", query = "SELECT m FROM Memoir m WHERE m.memoirId = :memoirId")
    , @NamedQuery(name = "Memoir.findByMovieName", query = "SELECT m FROM Memoir m WHERE m.movieName = :movieName")
    , @NamedQuery(name = "Memoir.findByMovieReleaseDate", query = "SELECT m FROM Memoir m WHERE m.movieReleaseDate = :movieReleaseDate")
    , @NamedQuery(name = "Memoir.findByWatchDate", query = "SELECT m FROM Memoir m WHERE m.watchDate = :watchDate")
    , @NamedQuery(name = "Memoir.findByWatchTime", query = "SELECT m FROM Memoir m WHERE m.watchTime = :watchTime")
    , @NamedQuery(name = "Memoir.findByComment", query = "SELECT m FROM Memoir m WHERE m.comment like :comment")
    , @NamedQuery(name = "Memoir.findByScore", query = "SELECT m FROM Memoir m WHERE m.score = :score")
    , @NamedQuery(name = "Memoir.findByPersonId", query = "SELECT m FROM Memoir m WHERE m.personId.personId = :personId")
    , @NamedQuery(name = "Memoir.findByCinemaId", query = "SELECT m FROM Memoir m WHERE m.cinemaId.cinemaId = :cinemaId")
    , @NamedQuery(name = "Memoir.findMemoir2020", query = "SELECT m FROM Memoir m WHERE FUNC('YEAR', m.watchDate) = 2020 and m.personId.personId = :personId")
    , @NamedQuery(name = "Memoir.findByMovieNameANDCinemaName_Static", query = "SELECT m FROM Memoir m WHERE m.movieName = :movieName AND m.cinemaId.cinemaName = :cinemaName")
})
public class Memoir implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMOIR_ID")
    private Integer memoirId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MOVIE_NAME")
    private String movieName;
    @Column(name = "MOVIE_RELEASE_DATE")
    @Temporal(TemporalType.DATE)
    private Date movieReleaseDate;
    @Column(name = "WATCH_DATE")
    @Temporal(TemporalType.DATE)
    private Date watchDate;
    @Column(name = "WATCH_TIME")
    @Temporal(TemporalType.TIME)
    private Date watchTime;
    @Lob
    @Size(max = 32700)
    @Column(name = "COMMENT")
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SCORE")
    private int score;
    @JoinColumn(name = "CINEMA_ID", referencedColumnName = "CINEMA_ID")
    @ManyToOne(optional = false)
    private Cinema cinemaId;
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID")
    @ManyToOne(optional = false)
    private Person personId;

    @Size(max = 50)
    @Column(name = "CINEMA_POSTCODE")
    private String cinemaPostcode;

    @Size(max = 50)
    @Column(name = "IMDB_ID")
    private String imdbId;

    public Memoir() {
    }

    public Memoir(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public Memoir(Integer memoirId, String movieName, int score) {
        this.memoirId = memoirId;
        this.movieName = movieName;
        this.score = score;
    }

    public Integer getMemoirId() {
        return memoirId;
    }

    public void setMemoirId(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public Date getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(Date watchDate) {
        this.watchDate = watchDate;
    }

    public Date getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(Date watchTime) {
        this.watchTime = watchTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }
    
    public void setCinemaPostcode(String cinemaPostcode) {
        this.cinemaPostcode = cinemaPostcode;
    }
    
    public String getCinemaPostcode() {
        return this.cinemaPostcode;
    }
    
    public void setImdbId(String ImdbId) {
        this.imdbId = imdbId;
    }
    
    public String getImdbId() {
        return this.imdbId;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memoirId != null ? memoirId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoir)) {
            return false;
        }
        Memoir other = (Memoir) object;
        if ((this.memoirId == null && other.memoirId != null) || (this.memoirId != null && !this.memoirId.equals(other.memoirId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restws.Memoir[ memoirId=" + memoirId + " ]";
    }

}
