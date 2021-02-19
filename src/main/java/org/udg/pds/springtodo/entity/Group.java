package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity(name = "usergroup")
public class Group implements Serializable {
    public Group() {
    }

    public Group(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<User> members;

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public void addMember(User user) { members.add(user); }

    public Collection<User> getMembers() {
        members.size();
        return members;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }
}
