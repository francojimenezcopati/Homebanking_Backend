package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity  // JPA crea una tabla en la base de datos con todas las variables que haya en la clase "Client"
// (Columnas: cada variable //Filas: Lo que contiene cada variable)
public class Client {
    @Id // Primary key (que no se pueda repetir un ID en diferentes objetos)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // Genera valor numerico irrepetible de manera ordenada para el id
    @GenericGenerator(name = "native", strategy = "native") // Se asigna el id generado a la variable id
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy = "client" // esto busca el client que se le pasa a la clase Account, para poner los datos en el set
            , fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
