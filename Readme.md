<a href="https://www.linkedin.com/pub/jill-heske/13/836/635">
                <img src="https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png" width="160" height="33" border="0" alt="View Jill Heske's profile on LinkedIn"></a>
                
               
## Synopsis

![TerrierTime icon](https://github.com/jheske/TerrierTime/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true)

This repository contains **TerrierTime**, a sample Android/Kotlin app that allows users to guess terrier breeds based on an image and fun fact.

Built on the latest Android features in Kotlin, listed below.

## Features

This project is designed to be a sample, non-production, Android/Kotlin app. Features may or may not be completed and include, but are not limited to:

Material Design conforms to Google's latest UI/UX standards. 

Features the latest Jetpack UI, Architecture, and Foundation components:

Views and Widgets:

* CoordinatorLayout
* AppBarLayout
* CollapsingToolbarLayout
* Toolbar
* NestedScrollView
* RecyclerView
* CardView

Jetpack:

* Navigation
* Databinding
* ViewModels
* LiveData
* Room Database
* Android KTX
* Fragments
* AppCompat


Retrofit 2.0 and Gson to retrieve and parse breed summaries from wikipedia.org and images from flickr.com.

Room database for persistence.

A Splash activity loads a preset list of terriers and facts into the database.  If summaries and image links are not in the database (and the network is available), it attempts to dowload and store summaries from Wiki and a list of image file paths from Flickr.

If the network is unavailable on startup, the Splash activity still makes sure the local list of terrier data is in the database.  Along with an images for each breed in assets, this is all the information the user needs to make guesses.  If the app is able to access the network on startup at any point, it will download Wiki summaries and Flickr image paths and store them in the database for future runs.  User will be able to see summaries on the detail screen, but Flickr images cannot be downloaded without a working network connection. 

The main screen displays the list of terriers, along with a useful fact, and image (from assets) for each to help the user guess the breed.  

The user enters a guess and clicks a button for feedback.  A correct guess reveals a "More" button allowing them to click through to a detail activity.  If the user gives up, they can click a button to go directly to the detail activity.

The detail screen displays a picture of the breed and a brief Wiki summary.  User can click through to see additional flickr images of the breed. 

The Flickr Pix activity downloads and displays a list of breed-specific images.

Handles config changes, but a future version should include a landscape Master/Detail layout for tablets, and a better landscape layout for phones.


## Devices

This project has been tested on and is optimized for:

* Samsung Galaxy Tab S2, API 24
* Galaxy Nexus, API 26

## FUTURE ENHANCEMENTS

* Integrate Dagger-2 and related patterns to provide a more convenient framework for testing advanced features such as ViewModels, LiveData, and Retrofit.

* Integrate RxAndroid. The Observable pattern is to be a great way to develop cleaner and more testable code.

* Introduce Null Object pattern, particularly to the Breed class, in order to further isolate business logic and facilitate testing.   












