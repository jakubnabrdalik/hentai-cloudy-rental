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

