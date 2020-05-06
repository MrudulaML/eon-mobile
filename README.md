================================================================================
SET UP STARTS HERE

Installation and Setup:

Following are the steps to install necessary tools and sdks to start with:

1) git clone https://github.com/bits-pgp-fse/eon-mobile.git

2) The installation links below consist of installation steps based on your operating system. Please select your system’s operating system in it to get steps accordingly.

## Install java
 https://java.com/en/download/help/download_options.xml

## Install Android studio
 https://developer.android.com/studio 

## Set JAVA_HOME environment variable in linux
https://www.cyberciti.biz/faq/linux-unix-set-java_home-path-variable/

## Set JAVA_HOME environment variable in Windows
https://mkyong.com/java/how-to-set-java_home-on-windows-10/

After installing and configuring these steps, open the cloned repository -> eon-mobile (While Opening android studio it will ask you to start new project or open existing one), please sync your gradle files in android studio and download the api sdks if asked in android studio.

Running the application:

Before running the application please connect an actual device using usb to your system with developer options on or create a Virtual device on your Android emulator.

For creating a device on android emulator refer:  
https://developer.android.com/studio/run/managing-avds



In order to run the application 
Select build from toolbar -> Build variant -> select TestingDebug -> tap on Play/Run icon on the toolbar. 
Before running the application, make sure you have selected one of the connected or virtual devices in the toolbar under the running device option.


Generating apk:
To generate an apk t, please select TestingDebug build variant and tap on build from toolbar -> Build Bundle/Apks -> Build Apk. You will see a pop up on bottom right  of the
screen, after android studio successfully rebuilds. Tap on locate button of that pop up, it will take you to the generated apk.
 
SET UP ENDS HERE
================================================================================

Overview of architecture:
Architecture components: MVVM with Data binding applicable in most cases.

A single-activity architecture, using the Navigation component to manage fragment operations.
A presentation layer that contains a fragment (View) and a ViewModel per screen (or feature).
Reactive UIs using LiveData observables and Data Binding.
A data layer with a repository and  remote data sources that are queried with one-shot operations.
Two product flavors, TESTING and PRODUCTION, to ease development and testing.

Libraries: 

Android libraries:

Android-X: 
Android-x replaces conventional support libraries, so we are using it over here.
For more info: https://developer.android.com/jetpack/androidx

Core-ktx:
 Ktx provides kotlin extensions which are also part of android jetpack.
 For more info:https://developer.android.com/kotlin/ktx

Jetpack Navigation:
We are using this library to navigate from one fragment to another, using the same base activity.
For more info: https://developer.android.com/guide/navigation

Viewmodel: 
Viewmodel helps us to retain data  even when our screen is rotated and new instance of activity is created, also viewmodel is lifecycle aware component. 
For more info: https://developer.android.com/topic/libraries/architecture/viewmodel

Other libraries:

Retrofit2: 
We have used retrofit to make api calls over the network
For more info: https://square.github.io/retrofit/

Stepview: 
Used for Steps UI  
For more info: https://github.com/shuhart/StepView

Facebook stetho: 
This library is integrated so that we can check all the network calls over the network and their exact responses in chrome browser. 
For more info: http://facebook.github.io/stetho/

Facebook-share: 
We are using this library to share posts on facebook about events. 
For more info: https://developers.facebook.com/docs/sharing/android/

florent37 ShapeOfView: 
Used to create custom shapes
For more info: https://github.com/florent37/ShapeOfView

Zxing: 
Barcode scanning library that helps us generate QR code
For more info: https://github.com/zxing/zxing

hellocharts: 
Charts library with several chart options for making custom charts.
For more info: https://github.com/lecho/hellocharts-android

Highcharts: 
Also used to create charts and graphs
For more info: https://www.highcharts.com/blog/products/android/


Folder/package Structure:

On the left side of your screen, in android studio, you will be able to see a tilted button with the name project. Tap on it and a small menu will show up. On top of that menu, usually by default, Android mode is selected, please tap on that dropdown and select project, to view project in project mode.

Now, Inside, Project mode : 
Eon Mobile: consists the entire project and all packages

App module:  App/root module for the project.

Inside app -> src -> Main

Api: 
Consists of The app service interface and all the common response models

Event Organiser: 
It contains the following packages:
Models: It contains all the model classes used in organiser module 
UI: Contains all the fragment and views to display organiser related data such as events, graphs, invitee list etc 
Viewmodels: Contains all viewmodels used in organiser to make api calls and retain data over the lifecycle of the related view

Event Subscriber: 
Models: Consist common models being used in subscriber
Subscriber: This package consists of all the feature packages such as subscriber event detail, summary, payment etc with their views and viewmodel in the same package. 
 
Login:
Login package contains all the files related to login and signing up a user. It consists of two packages, data and ui.

Direct/Global Files inside Main:

Baseviewmodel: 
A viewmodel class that is being used as super viewmodel class by all the viewmodels regardless of subscriber package or organiser 
in order to show progress bars and errors.

BitsEonActivity: 
A single-activity architecture, using the Navigation component to manage fragment operations.

EonViewModelFactory: 
A viewmodel factory used in generating view model instances as per parameter passed in the entire application.

