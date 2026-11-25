# Find & Dine
 
## Group Members
 
| Name | Student # |
| --- | --- |
| Tyson Howell | 8920479 |
| Paulo Fonseca | 8903202 |
| Ethan Keating | 8927657 |

## Course
 
PROG3211, Programming: Mobile Applications II

## Description
 
Find & Dine is a mobile app for discovering new restaurants near you and keeping track of the ones you've been to. Users can browse nearby restaurants on a map, record their visits, give each restaurant a star rating and a private note, and arrange their visited restaurants into a personal ranking. Users can also generate a shareable "Top Restaurants" list to show off their favourites.
 
## Features
 
- Map of nearby restaurants (Google Maps / Places API)
- Search for restaurants or locations
- Mark restaurants as visited
- Star ratings and private notes
- Personal numerical ranking, separate from star ratings
- Filter and sort rated restaurants
- Shareable top restaurants list

## Data Model

The data classes live in `data/model`.

| Class | Purpose |
| --- | --- |
| `Restaurant` | A restaurant from the map. Its `id` is the Google Places place ID. A restaurant is saved locally once the user marks it as visited. |
| `Rating` | The user's personal rating of a visited restaurant: stars (1 to 5), visit date, private note and personal rank. |
| `RatedRestaurant` | A `Restaurant` paired with its `Rating`, used by the My Ratings list, Detail screen and Top Restaurants screen. |
| `RatingSortOption` | The sort options for the My Ratings list. |

### Restaurant and Rating relationship

A `Restaurant` has **zero or one** `Rating` (one-to-one, optional):

- A visited restaurant can have no rating yet, or exactly one rating.
- Every `Rating` belongs to exactly one `Restaurant`, linked by `Rating.restaurantId` → `Restaurant.id`.
- Visiting a restaurant again updates its existing rating instead of adding a second one.

The star rating and the personal rank are separate. Stars describe how good the restaurant was, while the rank is its position in the user's own list (#1 is their favourite). Two restaurants can have the same number of stars, but never the same rank.

## Tech Stack
 
- Kotlin
- Jetpack Compose
- Android Studio
- Google Maps / Places API
- Google Firebase
