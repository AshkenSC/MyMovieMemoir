/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws.service;

import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
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
import restws.Person;

/**
 *
 * @author ASUS
 */
@Stateless
@Path("restws.person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "Assignment1PU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Person entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Person entity) {
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
    public Person find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Person> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findByFirstName/{firstName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByFirstName(@PathParam("firstName") String firstName) {
        Query query = em.createNamedQuery("Person.findByFirstName");
        query.setParameter("firstName", firstName);
        return query.getResultList();
    }
    
    @GET
    @Path("findBySurname/{surname}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findBySurname(@PathParam("surname") String surname) {
        Query query = em.createNamedQuery("Person.findBySurname");
        query.setParameter("surname", surname);
        return query.getResultList();
    }
    
    @GET
    @Path("findByDob/{dob}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByDob(@PathParam("dob") Date dob) {
        Query query = em.createNamedQuery("Person.findByDob");
        query.setParameter("dob", dob);
        return query.getResultList();
    }
    
    @GET
    @Path("findByAddress/{address}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByAddress(@PathParam("address") String address) {
        Query query = em.createNamedQuery("Person.findByAddress");
        query.setParameter("address", address);
        return query.getResultList();
    }
    
    @GET
    @Path("findByStateName/{stateName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByStateName(@PathParam("stateName") String stateName) {
        Query query = em.createNamedQuery("Person.findByStateName");
        query.setParameter("stateName", stateName);
        return query.getResultList();
    }
    
    @GET
    @Path("findByPostcode/{postcode}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByPostcode(@PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Person.findByPostcode");
        query.setParameter("postcode", postcode);
        return query.getResultList();
    }
    
    // task 3(b) dynamic query
    @GET
    @Path("findByNameANDDoB/{firstName}/{surname}/{dob}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Person> findByFullNameANDDoB(@PathParam("firstName") String firstName, @PathParam("surname") String surname, @PathParam("dob") Date dob) {
        TypedQuery<Person> query = em.createQuery("select m from Person m where m.firstName = :firstName AND m.surname = :surname AND m.dob = :dob", Person.class);
        query.setParameter("firstName", firstName);
        query.setParameter("surname", surname);
        query.setParameter("dob", dob);
        return query.getResultList();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
