 Bits-Eon is a project to get details about latest events and thus subscribe to them. In its different branches you'll find the same architecture, MVVM with Data binding applicable in most cases.

In the main branch you'll find:

A single-activity architecture, using the Navigation component to manage fragment operations.
A presentation layer that contains a fragment (View) and a ViewModel per screen (or feature).
Reactive UIs using LiveData observables and Data Binding.
A data layer with a repository and  remote data sources that are queried with one-shot operations.
Two product flavors, TESTING and PRODUCTION, to ease development and testing.


 Libraries that we are using:

Android libraries:

Android-X: AndroidX is a major improvement to the original Android Support Library, which is no longer maintained.
Androidx packages fully replace the Support Library by providing feature parity and new libraries.

core-ktx:

Android KTX is a set of Kotlin extensions that are included with Android Jetpack and other Android libraries. KTX extensions provide concise, idiomatic Kotlin to Jetpack, Android platform, and other APIs. 

Jetpack Navigation:Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app. Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer. The Navigation component also ensures a consistent and predictable user experience by adhering to an established set of principles.

Viewmodel: The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way. The ViewModel class allows data to survive configuration changes such as screen rotations.


Other libraries:

Retrofit2: Type-safe HTTP client for Android and Java by Square, Inc.

Stepview: A simple animated step view for Android. Backward and forward animations is supported.

Facebook stetho: Stetho is a sophisticated debug bridge for Android applications. When enabled, developers have access to the Chrome Developer Tools feature natively part of the Chrome desktop browser. We are using this in development environment only.

florent37 ShapeOfView: a custom shape to any android view, Material Design 2 ready

zxing: barcode scanning library for Java, Android


hellocharts: Charts/graphs library for Android compatible with API 8+, several chart types with support for scaling, scrolling and animations


highcharts: The most popular, robust and battle-tested JavaScript Charting library is now available for Android with our new Java wrapper. Get gorgeous, multi-touch charts with minimal effort.




Folder/package Structure:

In Project mode : 
Eon Mobile: consists the entire project and all packages

App module: Our very own app/root module for the project.

Inside app->src->Main:

 Api: Consists of The app service interface and all the common response models

 Event Organiser: It contains the following packages:
Models: It constains all the model classes used in organiser module 
UI: Contains all the fragment and views to display organiser related data such as events, graphs, invitee list etc 
Viewmodels: Contains all viewmodels used in organiser to make api calls and retain data over the lifecycle of the related view

Event Subscriber: Contrary to Organiser,subscriber has  folder structure that is feature based :
Models: Consist common models being used in subscriber
Subscriber: This packagae consists all the feature packages such as subscriber event detail, summary, payment etc with their views and viewmodel 
in same package. 
 
Login: Login packagae contains all the files related to login and signing up a user. It consists of two packages, data and ui.
 Now, contrary to organiser, since this has quite less viewmodels, viewmodels are used in same package as view i.e. in ui.

Direct/Global Files inside Main:

Baseviewmodel: A viewmodel class that is being used as super viewmodel class by all the viewmodels regardless of subscriber package or organiser 
in order to show progressbars and errors.

BitsEonActivity: A single-activity architecture, using the Navigation component to manage fragment operations.

EonViewModelFactory: A viewmodel factory used in generating viewmodel instances as per parameter passed in entire application.


 



