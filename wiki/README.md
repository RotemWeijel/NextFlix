# NextFlix User Guide

# Table of Contents
1. [Introduction](#introduction)
2. [Running the Server](#running-the-server)
3. [Viewing the Database](#viewing-the-database)
4. [Running the Web Client](#running-the-web-client)
5. [User Registration](#user-registration)
6. [Granting Admin Privileges](#granting-admin-privileges)
7. [Managing Categories](#managing-categories)
8. [Adding Movies](#adding-movies)
9. [Editing and Managing Movies](#editing-and-managing-movies)
10. [Logging Out and New User Login](#logging-out-and-new-user-login)
11. [Additional Features](#additional-features)
    - Theme Toggle
    - Search Function
12. [Running the Android Client](#running-the-android-client)
13. [User Registration (Android)](#user-registration-android)
14. [Granting Admin Privileges (Android)](#granting-admin-privileges-android)
15. [Managing Categories (Android)](#managing-categories-android)
16. [Adding Movies (Android)](#adding-movies-android)
17. [Editing and Managing Movies (Android)](#editing-and-managing-movies-android)
18. [Additional Features (Android)](#additional-features-android)
    - Theme Toggle
    - Search Function
19. [Conclusion](#conclusion)

## Introduction
Welcome to the NextFlix user guide! This document will walk you through setting up the NextFlix server, using the web client, and exploring the Android app. By the end, you'll be able to register users, manage movies, and enjoy the full NextFlix experience.

---

## Running the Server
To run the server, navigate to the server directory and open a terminal. Run the following command:

```
$env:WEB_PORT="4000"; $env:RECOMMEND_PORT="7000"; docker-compose up --build
```
![alt text](<צילום מסך 2025-02-06 162551.png>)   

Docker will build and start the server.

![alt text](<צילום מסך 2025-02-06 162640.png>)   
---

## Viewing the Database   

Once the server is running, open MongoDB Compass to view the database. Initially, it will be empty.

![alt text](<צילום מסך 2025-02-06 162821.png>)   

---

## Running the Web Client
To run the web client, navigate to the project's root directory, then go to the `web-client` folder. Open a terminal there and run:

```
npm start
```
![alt text](<צילום מסך 2025-02-06 174136.png>)   

The application's landing page will open.

![alt text](<צילום מסך 2025-02-06 163146.png>)   
---

## User Registration
1. Click **Sign Up** on the landing page.
2. Fill in your details and choose an avatar.

![alt text](<צילום מסך 2025-02-06 163343.png>)   

3. After submitting, a success screen will confirm registration.

![alt text](<צילום מסך 2025-02-06 163423.png>)   

4. The main screen will now appear, but it will be empty until movies are added.

![alt text](<צילום מסך 2025-02-06 163510.png>)   

To add movies, the user must be granted admin privileges.

---

## Granting Admin Privileges
1. Click on the avatar icon and select **Sign Out**.

![alt text](<צילום מסך 2025-02-06 163608.png>)   

2. Open MongoDB Compass and find the newly created user.

![alt text](<צילום מסך 2025-02-06 163710.png>)   

3. Modify the `isAdmin` field to `true`.

![alt text](<צילום מסך 2025-02-06 163729.png>)   

4. Log back into the application. The **Admin** button will now be visible in the top menu.

![alt text](<צילום מסך 2025-02-06 163908.png>)   

---

## Managing Categories
1. Click the **Admin** button.

![alt text](<צילום מסך 2025-02-06 164004.png>)   

2. In the **Category Management** screen, click **Add New Category**.

![alt text](<צילום מסך 2025-02-06 164049.png>)   

3. Enter category details and click **Create Category**.

![alt text](<צילום מסך 2025-02-06 164138-1.png>)   

4. Verify that the category has been successfully added.

![alt text](<צילום מסך 2025-02-06 164223-1.png>)   

![alt text](<צילום מסך 2025-02-06 164343.png>)   

![alt text](<צילום מסך 2025-02-06 164423.png>)   

5. Edit categories by clicking **Edit**, modifying the details, and clicking **Save**.

![alt text](<צילום מסך 2025-02-06 164550.png>)   

![alt text](<צילום מסך 2025-02-06 164636.png>)   

![alt text](<צילום מסך 2025-02-06 164722.png>)   

6. Delete categories by selecting **Delete**.

![alt text](<צילום מסך 2025-02-06 164755.png>)   

![alt text](<צילום מסך 2025-02-06 164839.png>)   

Changes should be reflected both in the app and in MongoDB Compass.

---

## Adding Movies
1. Navigate to an existing category and click **Edit**.

![alt text](<צילום מסך 2025-02-06 164920.png>)   

2. Click **Add Movie**.

![alt text](<צילום מסך 2025-02-06 165021.png>)   

3. Enter the movie details.

![alt text](<צילום מסך 2025-02-06 165202.png>)   

4. Upload the movie poster, trailer video, and the movie file.

![alt text](<צילום מסך 2025-02-06 165356.png>)   

5. Click **Add Movie**.
6. Ensure the movie appears in the assigned categories and in MongoDB Compass.

![alt text](<צילום מסך 2025-02-06 165453.png>)   

![alt text](<צילום מסך 2025-02-06 165543.png>)   

7. Repeat the process to add more movies.

Once movies are added, the home screen will display content, including a random trailer player and categorized movies.

![alt text](<צילום מסך 2025-02-06 171102.png>)   

![alt text](<צילום מסך 2025-02-06 171151.png>)   

---

## Editing and Managing Movies
1. Click on a movie to view its details.

![alt text](<צילום מסך 2025-02-06 171447.png>)   

2. Admins can click the **pencil icon** to edit the movie.
3. Modify movie details and click **Save**.
4. Movies can also be deleted from this screen.
5. Click **Play** to watch a movie.

![alt text](<צילום מסך 2025-02-06 171854.png>)   

![alt text](<צילום מסך 2025-02-06 171915.png>)   

---

## Logging Out and New User Login
1. Click on the avatar icon and select **Sign Out**.

![alt text](<צילום מסך 2025-02-06 171939.png>)   

2. Register a new user and log in.

![alt text](<צילום מסך 2025-02-06 172132.png>)   

3. The home screen will now display all available movies.

![alt text](<צילום מסך 2025-02-06 172151.png>)   

4. Clicking on a movie will show recommended movies based on the recommendation system.

![alt text](<צילום מסך 2025-02-06 172315.png>)   

---

## Additional Features
- **Theme Toggle:** Click the **sun/moon icon** to switch between light and dark mode.

![alt text](<צילום מסך 2025-02-06 172454.png>)   

- **Search Function:** Click the **search icon** to open a text field.
  - You can search for movies, actors, languages, and more.

  ![alt text](<צילום מסך 2025-02-06 172619.png>)   

  - The app will return all matching results.
  
  ![alt text](<צילום מסך 2025-02-06 172625.png>)   
  
---


## Running the Android Client
1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to and select the `android-client` folder
4. Wait for Gradle sync to complete
5. Run the application by clicking the "Run" button (green play icon) or pressing Shift + F10
6. Select a deployment target:
   - Choose an existing Android Virtual Device (AVD)
   - Or connect a physical Android device with USB debugging enabled

The application's landing page will open.

![alt text](<צילום מסך 2025-02-10 103828.png>)   

---

## User Registration (Android)
1. Click **Get Started** on the landing page.
2. Fill in your details and choose an avatar.

![alt text](<צילום מסך 2025-02-10 103905.png>)   

3. After submitting, a success screen will confirm registration.

![alt text](<צילום מסך 2025-02-10 103916.png>)    

4. The main screen will now appear.

![alt text](<צילום מסך 2025-02-10 103929.png>)    
![alt text](<צילום מסך 2025-02-10 103941.png>)   

To add movies, the user must be granted admin privileges.

---

## Granting Admin Privileges (Android)
1. Click on the avatar icon and select **Sign Out**.

![alt text](<צילום מסך 2025-02-10 104001.png>)   

2. Open MongoDB Compass and find the newly created user.

![alt text](<צילום מסך 2025-02-10 104014.png>)    

3. Modify the `isAdmin` field to `true`.

![alt text](<צילום מסך 2025-02-10 104025.png>)   

4. Log back into the application.

![alt text](<צילום מסך 2025-02-10 104043.png>)   

---

## Managing Categories (Android)
1. Click the **Admin** button in the buttom.

![alt text](<צילום מסך 2025-02-10 104053.png>)

2. In the **Category Management** screen, click **Add New Category**.

![alt text](<צילום מסך 2025-02-10 104107.png>)

3. Enter category details and click **Create Category**.

![alt text](<צילום מסך 2025-02-10 104156.png>)

4. Verify that the category has been successfully added.

![alt text](<צילום מסך 2025-02-10 104211.png>)
![alt text](<צילום מסך 2025-02-10 104226.png>)

5. Edit categories by clicking **Edit**, modifying the details, and clicking **Save**.

![alt text](<צילום מסך 2025-02-10 104250.png>)
![alt text](<צילום מסך 2025-02-10 104303.png>)

6. Delete categories by selecting **Delete**.

![alt text](<צילום מסך 2025-02-10 104315.png>)
![alt text](<צילום מסך 2025-02-10 104324.png>)

Changes should be reflected both in the app and in MongoDB Compass.

---

## Adding Movies (Android)
1. Navigate to an existing category and click **Edit**.
2. Click **Add Movie**.
3. Enter the movie details.

![alt text](<צילום מסך 2025-02-10 104606.png>)

4. Upload the movie poster, trailer video, and the movie file.

![alt text](<צילום מסך 2025-02-10 105938.png>)

5. Click **Add Movie**.
6. Ensure the movie appears in the assigned categories and in MongoDB Compass.

![alt text](<צילום מסך 2025-02-10 110012.png>)
![alt text](<צילום מסך 2025-02-10 111316.png>)

7. Repeat the process to add more movies.

---

## Editing and Managing Movies (Android)
1. Click on a movie to view its details.

![alt text](<צילום מסך 2025-02-10 110021.png>)

2. Admins can click the **pencil icon** to edit the movie.
3. Modify movie details and click **Save**.

![alt text](<צילום מסך 2025-02-10 110122.png>)
![alt text](<צילום מסך 2025-02-10 110133.png>)

4. Movies can also be deleted from this screen.
5. Click **Play** to watch a movie.

![alt text](<צילום מסך 2025-02-10 110148.png>)

5. Clicking on a movie will show recommended movies based on the recommendation system.

![alt text](<צילום מסך 2025-02-10 110221.png>)

---

## Additional Features (Android)
- **Theme Toggle:** Click the **sun/moon icon** to switch between light and dark mode.

![alt text](<צילום מסך 2025-02-10 110227.png>)

- **Search Function:** Click the **search icon** to open a text field.

![alt text](<צילום מסך 2025-02-10 110238.png>)

  - You can search for movies, actors, languages, and more.

![alt text](<צילום מסך 2025-02-10 110254.png>)

  - The app will return all matching results.

  ![alt text](<צילום מסך 2025-02-10 110308.png>)

---

## Conclusion
You are now ready to explore NextFlix! Set up the server, manage users and movies, and enjoy your streaming experience.

