# Hentai rental: your porn on somebody else's computer (cloud)

# requirements

## The problem – Video rental store

For a video rental store we want to create a system for managing the rental administration.

We want three primary functions.

- Have an inventory of films
- Calculate the price for rentals
- Keep track of the customers “bonus” points

## Price

The price of rentals is based type of film rented and how many days the film is rented for.

The customers say when renting for how many days they want to rent for and pay up front. If

the film is returned late, then rent for the extra days is charged when returning.

The store has three types of films.

- New releases – Price is <premium price> times number of days rented.
- Regular films – Price is <basic price> for the fist 3 days and then <basic price> times the number of days over 3.
- Old film - Price is <basic price> for the fist 5 days and then <basic price> times the number of days over 5

premium price is 40 SEK

basic price is 30 SEK

The program should expose a rest-ish HTTP API. (hint; unless you have any personal

preferences, http://www.dropwizard.io is a really simple way of getting that)

The API should (at least) expose operations for

- Renting one or several films and calculating the price.
- Returning films and calculating possible surcharges.

## Examples of price calculations

Matrix 11 (New release) 1 days 40 SEK

Spider Man (Regular rental) 5 days 90 SEK

Spider Man 2 (Regular rental) 2 days 30 SEK

Out of Africa (Old film) 7 days 90 SEK

Total price: 250 SEK

When returning films late

Matrix 11 (New release) 2 extra days 80 SEK

Spider Man (Regular rental) 1 days 30 SEK

Total late charge: 110 SEK

## Bonus points

Customers get bonus points when renting films. A new release gives 2 points and other films

give one point per rental (regardless of the time rented).

--

# Hentai rental: decision log

### javac params
 
I've added -parameters to javac, so I can use some simplifications in Spring MVC, thanks to parameter names in methods. 

### Authentication/Authorization

Since this is a RESTFULL API, I have a few options. I could:

Basic auth

For development:
user = test
password = test

(all sample data are in data.sql)

### Logging

Logback. With vintage XML.

## Functional

### Film catalogue

Url: http://localhost:8080/films{?page,size,sort}

Since this is REST, I will use Spring Data Rest to handle the catalogue with paging and HATEOAS.

For testing this I need some Films in the DB, so I have a few options. I cold for example create a JpaRepository (CRUD) 
and save it directly, but I'd like to treat Film as immutable and save() method is updating them. I could populate the 
database like in dev profile, so that I can work on immutable database (rollbacks for tests give me that benefit),
but I would either have to violate DRY in static fields (for 'where' part AST of Groovy requires static or @Shared), or
read the data directly from DB (without Spring Data JPA). Every option has its benefits and disadvantages. 
I've decided to have a add save() method on a repo but only in test profile.

You could ask, why would I write tests for a code produced by Spring with @RepositoryRestResource annotation. 
I wouldn't write an integration test for that, but I need to store the requirements, and the best way to do it is with 
automated acceptance tests. This way the code communicates its requirements itself.

There are a few gotchas of using Spring Data Rest, though. 

While hibernate is completely fine with composed natural primary 
key (in case of our films: year + title), Spring Data Rest isn't. This, however, is actually not that bad, because the 
URL created for composed natural primary key, would look a bit ugly.

Second problem is that I cannot use HTTP PATH on /rents to make a return, because it clashes with defaults from Spring 
Data Rest. So instead, I went with /returns 

### Viewing your rentals

Url: http://localhost:8080/rents/{rentId}

Warning: you have to be logged as the same user who's rental it is. 

### Renting

If your username doesn't match the logged user, you'll not be allowed.

Sample Post:
 
POST /rents HTTP/1.1
Host: localhost:8080
Cache-Control: no-cache
Content-Disposition: form-data; name="filmId"
1
Content-Disposition: form-data; name="numberOfDays"
2
Content-Disposition: form-data; name="username"
seba

### Returning

I assumed everyone can return the film, not only the person who rented (happened a lot back in the VHS days, with
my sisters/dad returning my films)

Sample Post:

POST /returns HTTP/1.1
Host: localhost:8080
Cache-Control: no-cache
Content-Disposition: form-data; name="rentId"
3

