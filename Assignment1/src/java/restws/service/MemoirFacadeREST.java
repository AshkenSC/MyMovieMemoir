/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import restws.Memoir;

/**
 *
 * @author ASUS
 */
@Stateless
@Path("restws.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "Assignment1PU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memoir entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Memoir find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByMovieName/{movieName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByMovieName(@PathParam("movieName") String movieName) {
        Query query = em.createNamedQuery("Memoir.findByMovieName");
        query.setParameter("movieName", movieName);
        return query.getResultList();
    }
    
    @GET
    @Path("findByMovieReleaseDate/{movieReleaseDate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByMovieReleaseDate(@PathParam("movieReleaseDate") Date movieReleaseDate) {
        Query query = em.createNamedQuery("Memoir.findByMovieReleaseDate");
        query.setParameter("movieReleaseDate", movieReleaseDate);
        return query.getResultList();
    }
    
    @GET
    @Path("findByWatchDate/{watchDate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByWatchDate(@PathParam("watchDate") Date watchDate) {
        Query query = em.createNamedQuery("Memoir.findByWatchDate");
        query.setParameter("watchDate", watchDate);
        return query.getResultList();
    }
    
    @GET
    @Path("findByWatchTime/{watchTime}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByWatchTime(@PathParam("watchTime") Time watchTime) {
        Query query = em.createNamedQuery("Memoir.findByWatchTime");
        query.setParameter("watchTime", watchTime);
        return query.getResultList();
    }
    
    @GET
    @Path("findByComment/{comment}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByComment(@PathParam("comment") String comment) {
        Query query = em.createNamedQuery("Memoir.findByComment");
        query.setParameter("comment", comment);
        return query.getResultList();
    }
    
    @GET
    @Path("findByScore/{score}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByScore(@PathParam("score") int score) {
        Query query = em.createNamedQuery("Memoir.findByScore");
        query.setParameter("score", score);
        return query.getResultList();
    }
    
    @GET
    @Path("findByPersonId/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByPersonId(@PathParam("personId") int personId) {
        Query query = em.createNamedQuery("Memoir.findByPersonId");
        query.setParameter("personId", personId);
        return query.getResultList();
    }
    
    @GET
    @Path("findByCinemaId/{cinemaId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByCinemaId(@PathParam("cinemaId") int cinemaId) {
        Query query = em.createNamedQuery("Memoir.findByCinemaId");
        query.setParameter("cinemaId", cinemaId);
        return query.getResultList();
    }
    
    // task 3(c) dynamic query implicit join
    @GET
    @Path("findByMovieNameANDCinemaName_Dynamic/{movieName}/{cinemaName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByMovieNameANDCinemaName_Dynamic(@PathParam("movieName") String movieName, @PathParam("cinemaName") String cinemaName) {
        TypedQuery<Memoir> query = em.createQuery("select m from Memoir m where m.movieName = :movieName AND m.cinemaId.cinemaName = :cinemaName", Memoir.class);
        query.setParameter("movieName", movieName);
        query.setParameter("cinemaName", cinemaName);
        return query.getResultList();
    }
    
    // task 3(c) static query implicit join
    @GET
    @Path("findByMovieNameANDCinemaName_Static/{movieName}/{cinemaName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findByMovieNameANDCinemaName_Static(@PathParam("movieName") String movieName, @PathParam("cinemaName") String cinemaName) {
        Query query = em.createNamedQuery("Memoir.findByMovieNameANDCinemaName_Static");
        query.setParameter("movieName", movieName);
        query.setParameter("cinemaName", cinemaName);
        return query.getResultList();
    }
    
    // task 4(a)
    @GET
    @Path("task4a/{personId}/{startDate}/{endDate}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4a(@PathParam("personId") int personId, @PathParam("startDate") Date startDate, @PathParam("endDate") Date endDate) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.cinemaId.cinemaPostcode,count(m.cinemaId.cinemaPostcode) FROM Memoir m WHERE m.personId.personId =:personId AND m.watchDate BETWEEN :startDate AND :endDate GROUP BY m.cinemaId.cinemaPostcode", Object[].class);
        query.setParameter("personId", personId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Object[]> queryList = query.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
                    add("cinemaPostcode", (String)row[0])
                    .add("movieWatchedPerSuburb", (Long)row[1]).build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // task 4(b)
    @GET
    @Path("task4b/{personId}/{year}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4b(@PathParam("personId") int personId, @PathParam("year") int year) {
        String monthNames[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        TypedQuery<Object[]> query = em.createQuery("SELECT FUNCTION('MONTH',m.watchDate), count(m.movieName) FROM Memoir m WHERE m.personId.personId =:personId AND FUNCTION('YEAR',m.watchDate) =:year GROUP BY FUNCTION('MONTH', m.watchDate) ", Object[].class);
        query.setParameter("personId", personId);
        query.setParameter("year", year);
        List<Object[]> queryList = query.getResultList();               
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
                    add("month", monthNames[(Integer)row[0]])
                    .add("movieWatchedPerMonth", (String)row[1].toString()).build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // task 4(c)
    @GET
    @Path("task4c/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4c(@PathParam("personId") int personId) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.personId.firstName, m.personId.surname, m.score, m.movieReleaseDate, m.movieName FROM Memoir AS m WHERE m.personId.personId = :personId AND m.score = (SELECT FUNCTION('max', m.score) FROM Memoir m WHERE m.personId.personId =:personId GROUP BY m.personId.personId)", Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
                    add("personName", (String)row[0] + " " + (String)row[1])
                    .add("movieName", (String)row[4])
                    .add("releaseDate", row[3].toString().replace("00:00:00 CST ", ""))
                    .add("highestScore", (Integer)row[2])
                    .build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // task 4(d)
    @GET
    @Path("task4d/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4d(@PathParam("personId") int personId) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.movieName, FUNCTION('year', m.movieReleaseDate) FROM Memoir m WHERE m.personId.personId = :personId AND FUNCTION('year', m.watchDate) = FUNCTION('year', m.movieReleaseDate)", Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList();    
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
                    add("movieName", (String)row[0])
                    .add("releaseYear", (Integer)row[1]).build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // task 4(e)
    @GET
    @Path("task4e/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4e(@PathParam("personId") int personId) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.movieName, FUNCTION('year',m.movieReleaseDate) FROM Memoir m WHERE m.personId.personId=:personId AND m.movieName IN (SELECT m.movieName FROM Memoir m WHERE m.personId.personId = :personId GROUP BY m.movieName HAVING count(DISTINCT m.movieReleaseDate) > 1)", Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject personObject = Json.createObjectBuilder().
                    add("movieName", (String)row[0])
                    .add("releaseYear", (Integer)row[1]).build();
            arrayBuilder.add(personObject);
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // task 4(f)
    @GET
    @Path("task4f/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Object task4f(@PathParam("personId") int personId) {
        TypedQuery<Object[]> query = em.createQuery("SELECT m.movieName, m.movieReleaseDate, m.score FROM Memoir m WHERE m.personId.personId = :personId AND FUNCTION('year',m.movieReleaseDate) BETWEEN FUNCTION('year', current_Date) - 5 AND FUNCTION('year', current_Date) ORDER BY m.score DESC", Object[].class);
        query.setParameter("personId", personId);
        List<Object[]> queryList = query.getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            int i = 0;
            JsonObject personObject = Json.createObjectBuilder().
                    add("movieName", (String)row[0])
                    .add("releaseDate", (String)row[1].toString().replace("00:00:00 CST ", ""))
                    .add("score", (Integer)row[2]).build();
            arrayBuilder.add(personObject);
            if(i > 5)
                break;
            i++;
        }
        JsonArray jArray = arrayBuilder.build();
        return jArray;
    }
    
    // find memoir in 2020 to show on homepage
    @GET
    @Path("findMemoir2020/{personId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memoir> findMemoir2020(@PathParam("personId") int personId) {
        Query query = em.createNamedQuery("Memoir.findMemoir2020");
        query.setParameter("personId", personId);
        return query.getResultList();
    }
  
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
