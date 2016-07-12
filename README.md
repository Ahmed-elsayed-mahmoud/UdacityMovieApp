# Popular Movies App

## Overview

An android mobile application. The purpose of this project was to built an app, optimized for tablets, to help users discover popular and highly rated movies on the web. 
It displays a scrolling grid of movie trailers, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews. 
This app utilizes core Android user interface components and fetches movie information using themoviedb.org web API.

# Features

* Sort movies by popularity and rating
* View a particular movie detail.
* View movie trailers, reviews.
* Content provider for storing already viewed content this way app works offline.
* glide library for image caching

## Prerequisites

### [Api key](https://themoviedb.org)

In order to access themoviedb.org API an API key is required. 
It is required to modify server-api-key.xml in order to add a valid api key. 
Otherwise the server shall reject requests as unauthorized. 

### [Android Studio](https://developer.android.com/studio/index.html)

Android Studio provides the fastest tools for building apps on every type of Android device.

World-class code editing, debugging, performance tooling, a flexible build system, and an instant build/deploy system all allow you to focus on building unique and high quality apps.


## Running Tests
<img width="80%" src="http://ahmed-elsayed.890m.com/assets/images/works/movieApp.png" />

# How to Run

To make server calls, we use the API from [themoviedb.org](https://www.themoviedb.org/) which requires an API Key. To run this project, you need to add the API Key mentioned in build.gradle.
* To request an API key from [themoviedb.org](https://www.themoviedb.org/), you need to create an account on the site.
* In the request for a key, you have to state that our usage will be for educational/non-commercial use. You also need to provide some personal information to complete the request. Once you submit the request, you should receive your key via email shortly after.

## Libraries Used 

* [Picasso](http://square.github.io/picasso/) - A powerful library that handles image loading and caching in the app
* [Retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
* [Butterknife](http://jakewharton.github.io/butterknife/) - Field and method binding for Android views


## License
 
CopyRight 2016 Ahmed Elsayed Mahmoud
=======

