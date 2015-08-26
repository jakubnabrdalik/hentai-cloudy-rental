package base

import eu.solidcraft.hentai.rentals.dtos.Film
import eu.solidcraft.hentai.rentals.price.FilmType

class TestData {
    static Film film0 = new Film(id: 1, filmType: FilmType.NEW_RELEASE, title: "Pirate Ninja Tentacle attacks", description: "Classic Japan family movie")
    static Film film1 = new Film(id: 2, filmType: FilmType.REGULAR, title: "Day of the Tentacle", description: "LucasArts penetrates Japan market with huuuge impact!")
    static Film film2 = new Film(id: 3, filmType: FilmType.OLD, title: "It came in the deep", description: "Enigma tentacle grabs its market")
    static ArrayList<Film> films = [film0, film1, film2]
}
