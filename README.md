# Bird Tracking App

A bird tracking Android application that connects to the [eBird API](https://documenter.getpostman.com/view/664302/ebird-api-20/2HTbHW) to display bird sightings on an interactive map component. The app is built in Android Studio, making it easy for bird enthusiasts to track and visualize bird sightings in real time.

## Features

- **eBird API Integration:** Fetch real-time bird sightings data from the eBird API.
- **Map Integration:** Display sightings on a map with custom markers for each bird species.
- **Search Functionality:** Search sightings based on location or bird species.
- **Interactive UI:** Zoom and pan features on the map for better exploration.
- **Real-time Data:** Get the latest bird sightings from around the world.

## Installation

1. Clone this repository:
    ```bash
    git clone https://github.com/your-username/bird-tracking-app.git
    ```

2. Open the project in Android Studio.

3. Get an API key from eBird by signing up at [eBird](https://ebird.org/).

5. Build and run the project on your Android device or emulator.

## Usage

1. Upon launching the app, the map will show bird sightings around your current location.
2. Use the search bar to filter sightings based on species or location.
3. Tap on a marker to view detailed information about the bird species.

## Dependencies

- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/overview)
- [eBird API](https://documenter.getpostman.com/view/664302/ebird-api-20/2HTbHW)
- [Retrofit](https://square.github.io/retrofit/) - For API calls.
- [Glide](https://github.com/bumptech/glide) - For image loading.
- [ViewModel and LiveData](https://developer.android.com/topic/libraries/architecture/viewmodel) - For lifecycle management.
