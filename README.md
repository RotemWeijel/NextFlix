# NextFlix

https://github.com/RotemWeijel/Ex4.git

NextFlix is an educational project that simulates the Netflix streaming platform, demonstrating modern software architecture and best practices across web and mobile platforms.

## Quick Start
```bash
# Clone the repository
git clone https://github.com/RotemWeijel/Ex4.git
cd Ex4
```

## Project Overview

This project implements a fully-functional streaming platform simulation with:
- A secure backend server infrastructure
- A responsive React-based web client
- A native Android mobile application
- An intelligent recommendation system

### Exercise Requirements Fulfillment

#### Architecture & Development
- Follows MVVM architecture in Android client using Room and Repository pattern
- Implements clean architecture principles across all platforms
- Uses feature branches and proper Git workflow with pull request reviews
- JIRA integration for project management and sprint tracking

#### Authentication & Security
- JWT-based authentication system
- Role-based access control (user/admin)
- Secure login and registration with input validation
- Protected routes and API endpoints

#### Core Features

##### Web Client (React)
- Built with React, JavaScript, HTML5, CSS3, and Bootstrap
- Component-based architecture following React best practices
- Responsive design with platform-specific UI/UX considerations
- Theme switching capability (dark/light mode)
- Client-side routing without page refreshes

##### Android Client
- Native Java implementation
- MVVM architecture with LiveData and ViewModel
- Room database for local storage
- Repository pattern for data management
- Custom UI components matching platform guidelines

##### Common Features Across Platforms
1. User Management
   - Registration with profile picture
   - Login/logout functionality
   - User profile management
   - Role-based access control

2. Content Management
   - Category-based movie browsing
   - Search functionality
   - Movie details view
   - Video playback
   - Admin panel for content management

3. Movie Recommendations
   - Personalized recommendations
   - Category-based suggestions
   - Watch history tracking

### Technical Implementation

#### Backend Integration
- RESTful API consumption
- JWT token management
- Asynchronous data loading
- Error handling and user feedback

#### Data Management
- Local data caching
- Offline capability
- Synchronization mechanisms
- Efficient data loading patterns

#### Security Features
- Input validation
- Secure token storage
- Protected routes
- Role-based access control

### Development Process

#### Version Control
- Feature branch workflow
- Pull request reviews
- Protected main branch
- Proper Git commit messages

#### Project Management
- JIRA for task tracking
- Sprint planning and reviews
- Story point estimation
- Regular status updates

#### Code Quality
- Component-based architecture
- Clean code principles
- Code review process
- Proper error handling

### Project Structure

```
Ex4/
├── server/          # Backend server files
├── web-client/      # React web application
├── android-client/  # Android application
└── wiki/            # Project documentation
```
   
```
server/
├── .env
├── CMakeLists.txt
├── Dockerfile
├── docker-compose.yml
├── run.sh
├── .vscode/
│   └── settings.json
├── src/
│   ├── recommend-client/
│   │   └── client.py
│   ├── recommend-server/
│   │   ├── IO_handling/
│   │   │   ├── ConsoleInput.cpp
│   │   │   ├── ConsoleInput.h
│   │   │   ├── ConsoleOutput.cpp
│   │   │   ├── ConsoleOutput.h
│   │   │   ├── IInputHandler.h
│   │   │   ├── IOutputHandler.h
│   │   │   ├── SocketInput.cpp
│   │   │   ├── SocketInput.h
│   │   │   ├── SocketOutput.cpp
│   │   │   └── SocketOutput.h
│   │   ├── commands/
│   │   │   ├── DeleteCommand.cpp
│   │   │   ├── DeleteCommand.h
│   │   │   ├── GetCommand.cpp
│   │   │   ├── GetCommand.h
│   │   │   ├── HelpCommand.cpp
│   │   │   ├── HelpCommand.h
│   │   │   ├── ICommand.h
│   │   │   ├── PatchCommand.cpp
│   │   │   ├── PatchCommand.h
│   │   │   ├── PostCommand.cpp
│   │   │   └── PostCommand.h
│   │   ├── data_handling/
│   │   │   ├── FileDataExporter.cpp
│   │   │   ├── FileDataExporter.h
│   │   │   ├── FileDataLoader.cpp
│   │   │   ├── FileDataLoader.h
│   │   │   ├── IDataExporter.h
│   │   │   └── IDataLoader.h
│   │   ├── main_body/
│   │   │   ├── Application.cpp
│   │   │   ├── Application.h
│   │   │   ├── ClientHandler.cpp
│   │   │   ├── ClientHandler.h
│   │   │   ├── MovieDatabase.cpp
│   │   │   ├── MovieDatabase.h
│   │   │   └── Server.cpp
│   │   └── thread_handling/
│   │       ├── DetachedThreadHandler.cpp
│   │       ├── DetachedThreadHandler.h
│   │       ├── IThreadHandler.h
│   │       ├── ThreadPool.cpp
│   │       └── ThreadPool.h
│   └── web-server/
│       ├── app.js
│       ├── .gitignore
│       ├── package.json
│       ├── server.js
│       ├── controllers/
│       │   ├── categories.js
│       │   ├── movies.js
│       │   ├── tokens.js
│       │   └── users.js
│       ├── middleware/
│       │   ├── authMiddleware.js
│       │   └── errorHandler.js
│       ├── models/
│       │   ├── categories.js
│       │   ├── idMapping.js
│       │   ├── movies.js
│       │   ├── token.js
│       │   └── user.js
│       ├── routes/
│       │   ├── categories.js
│       │   ├── movies.js
│       │   ├── tokens.js
│       │   └── users.js
│       └── services/
│           ├── categories.js
│           ├── idMapping.js
│           ├── movies.js
│           ├── password.js
│           ├── recommend.js
│           ├── token.js
│           └── user.js
```
   
```
web-client/
├── public/
│   ├── images/
│   ├── NextFlix_icon.png
│   ├── NextFlix_logo.png
│   ├── favicon.ico
│   ├── index.html
│   ├── logo192.png
│   ├── logo512.png
│   ├── manifest.json
│   ├── netflix-logo.png
│   ├── netflix.png
│   ├── profile.png
│   └── robots.txt
└── src/
    ├── components/
    │   ├── admin/
    │   │   ├── categories/
    │   │   │   ├── CategoryForm.css
    │   │   │   ├── CategoryForm.js
    │   │   │   ├── CategoryList.css
    │   │   │   └── CategoryList.js
    │   │   └── movies/
    │   │       ├── MovieForm.css
    │   │       └── MovieForm.js
    │   ├── common/
    │   │   ├── Button/
    │   │   │   ├── Button.js
    │   │   │   └── Button.module.css
    │   │   ├── Footer/
    │   │   │   ├── Footer.js
    │   │   │   └── Footer.module.css
    │   │   ├── Input/
    │   │   │   ├── Input.js
    │   │   │   └── Input.module.css
    │   │   ├── LoadingSpinner/
    │   │   │   ├── LoadingSpinner.js
    │   │   │   └── LoadingSpinner.module.css
    │   │   ├── MovieCard/
    │   │   │   ├── MovieCard.css
    │   │   │   └── MovieCard.js
    │   │   ├── MovieList/
    │   │   │   ├── MovieList.css
    │   │   │   └── MovieList.js
    │   │   ├── Navbar/
    │   │   │   ├── Navbar.js
    │   │   │   └── Navbar.module.css
    │   │   └── player/
    │   │       ├── VideoPlayer.css
    │   │       └── VideoPlayer.js
    │   └── features/
    │       ├── DetailsMovie/
    │       │   ├── DetailsMovie.css
    │       │   └── DetailsMovie.js
    │       ├── FooterDetailsMovie/
    │       │   ├── MovieFooter.css
    │       │   └── MovieFooter.js
    │       ├── MoviesByCategory/
    │       │   ├── MoviesPage.css
    │       │   └── MoviesPage.js
    │       ├── RecommendSeries/
    │       │   ├── MovieWrap.css
    │       │   ├── MovieWrap.js
    │       │   ├── RecommendSeries.css
    │       │   └── RecommendSeries.js
    │       ├── VideoPlayerDetails/
    │       │   ├── VideoPlayerDetails.css
    │       │   └── VideoPlayerDetails.js
    │       ├── VideoPlayerHome/
    │       │   ├── PlayerHome.css
    │       │   └── PlayerHome.js
    │       └── searchBar/
    │           └── SearchResults.js
    ├── contexts/
    │   └── AuthContext.js
    ├── hooks/
    │   ├── useAuthFetch.js
    │   └── useTheme.js
    ├── routing/
    │   └── ProtectedRoutes.js
    ├── screens/
    │   ├── admin/
    │   │   ├── CategoryManagement.css
    │   │   ├── CategoryManagement.js
    │   │   ├── MovieManagement.css
    │   │   └── MovieManagement.js
    │   ├── auth/
    │   │   ├── Landing/
    │   │   │   ├── Landing.js
    │   │   │   └── Landing.module.css
    │   │   ├── Login/
    │   │   │   ├── Login.js
    │   │   │   └── Login.module.css
    │   │   └── Register/
    │   │       ├── AvatarSelector.js
    │   │       ├── AvatarSelector.module.css
    │   │       ├── CharacterDisplay.js
    │   │       ├── CharacterDisplay.module.css
    │   │       ├── Register.js
    │   │       ├── Register.module.css
    │   │       ├── RegistrationSuccess.js
    │   │       └── RegistrationSuccess.module.css
    │   └── main/
    │       ├── BrowseScreen.css
    │       ├── BrowseScreen.js
    │       ├── MovieDetailsScreen.js
    │       ├── PlayerScreen.css
    │       └── PlayerScreen.js
    ├── styles/theme/
    │   ├── colors.js
    │   └── theme.js
    ├── utils/
    │   └── auth.js
    ├── App.css
    ├── App.js
    ├── App.test.js
    ├── TestComponents.js
    ├── index.css
    ├── index.js
    ├── reportWebVitals.js
    └── setupTests.js
```
   
```
android-client/app/src/main/java/com/app/nextflix
├── MainActivity.java
├── MyAppGlideModule.java
├── data
│   ├── api
│   │   ├── AuthApi.java
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   └── RegistrationRequest.java
│   ├── local
│   │   ├── AppDatabase.java
│   │   ├── ImageUtils.java
│   │   ├── UserPreferences.java
│   │   ├── converter
│   │   │   └── Converters.java
│   │   ├── dao
│   │   │   ├── CategoryDao.java
│   │   │   ├── MovieDao.java
│   │   │   ├── TokenDao.java
│   │   │   └── UserDao.java
│   │   └── entity
│   │       ├── CategoryEntity.java
│   │       ├── MovieEntity.java
│   │       ├── TokenEntity.java
│   │       └── UserEntity.java
│   ├── remote
│   │   └── api
│   │       ├── AuthApi.java
│   │       ├── AuthInterceptor.java
│   │       ├── CategoryApi.java
│   │       ├── MovieApi.java
│   │       ├── RetrofitClient.java
│   │       └── WebServiceApi.java
│   └── repositories
│       ├── AuthRepository.java
│       ├── CategoryRepository.java
│       ├── MovieRepository.java
│       └── UserRepository.java
├── models
│   ├── Category.java
│   ├── CategoryResponse.java
│   ├── Movie.java
│   ├── MovieCategory.java
│   ├── Token.java
│   └── User.java
├── security
│   ├── SecurityConfig.java
│   └── TokenManager.java
├── ui
│   ├── admin
│   │   ├── adapters
│   │   │   ├── CategoryAdapter.java
│   │   │   ├── MovieCategoryAdapter.java
│   │   │   └── RecommendedMoviesAdapter.java
│   │   ├── categories
│   │   │   ├── CategoryFormHelper.java
│   │   │   ├── CategoryManagementActivity.java
│   │   │   ├── CategoryManagementViewModel.java
│   │   │   └── CategoryViewModelFactory.java
│   │   └── movies
│   │       ├── MovieFormDialog.java
│   │       └── UploadResponse.java
│   ├── auth
│   │   ├── landing
│   │   │   ├── FeatureAdapter.java
│   │   │   ├── GridSpaceItemDecoration.java
│   │   │   ├── LandingActivity.java
│   │   │   └── LandingViewModel.java
│   │   ├── login
│   │   │   ├── LoginActivity.java
│   │   │   ├── LoginViewModel.java
│   │   │   └── LoginViewModelFactory.java
│   │   └── register
│   │       ├── AvatarAdapter.java
│   │       ├── RegistrationActivity.java
│   │       ├── RegistrationSuccessActivity.java
│   │       ├── RegistrationViewModel.java
│   │       └── RegistrationViewModelFactory.java
│   ├── common
│   │   ├── CategoryMoviesViewModel.java
│   │   ├── MovieViewModel.java
│   │   ├── NavBarManager.java
│   │   └── PlayerViewModel.java
│   └── main
│       ├── browse
│       │   └── BrowseActivity.java
│       ├── details
│       │   └── DetailsMovie.java
│       ├── player
│       │   └── PlayerActivity.java
│       └── search
│           ├── SearchResultsActivity.java
│           ├── SearchViewModel.java
│           └── SearchViewModelFactory.java
└── utils
    ├── ImageUtils.java
    ├── SecurityUtils.java
    └── UrlUtils.java
```

---   

### Running the Project

Follow these steps to run each component of the project. Make sure to start the server first, then run the clients.

#### 1. Running the Server

1. Open a terminal in the `server` directory:
   ```bash
   cd server
   ```

2. Run the server using Docker Compose:
   ```powershell
   # On Windows PowerShell
   $env:WEB_PORT="4000"; $env:RECOMMEND_PORT="7000"; docker-compose up --build
   ```
   ```bash
   # On Linux/Mac
   export WEB_PORT=4000 export RECOMMEND_PORT=7000 docker-compose up --build
   ```

The server will start on:
- Web server: http://localhost:4000
- Recommendation server: http://localhost:7000

#### 2. Running the Web Client

1. Open a new terminal in the `web-client` directory:
   ```bash
   cd web-client
   ```

2. Install dependencies (first time only):
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The web client will automatically open in your default browser at http://localhost:3000

#### 3. Running the Android Client

1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to and select the `android-client` folder
4. Wait for Gradle sync to complete
5. Run the application by clicking the "Run" button (green play icon) or pressing Shift + F10
6. Select a deployment target:
   - Choose an existing Android Virtual Device (AVD)
   - Or connect a physical Android device with USB debugging enabled

### Running Order
1. Always start the server first
2. Then run either or both clients
3. Ensure the server is accessible from your Android emulator/device

## Troubleshooting

### Common Issues
1. Docker Container Won't Start
   - Ensure no other services are using ports 4000 and 7000
   - Check Docker daemon is running
   - Try `docker-compose down` then retry

2. Web Client Build Fails
   - Clear npm cache: `npm cache clean --force`
   - Delete node_modules and run `npm install` again

3. Android Build Issues
   - Sync Gradle project
   - Check Android SDK installation
   - Ensure correct JDK version

### Network Configuration
- Required open ports:
  - 4000 (Web Server)
  - 7000 (Recommendation Server)
  - 3000 (React Development Server)


### Contributing

This is an educational project focused on demonstrating:
- Modern software architecture
- Best development practices
- Cross-platform development
- Team collaboration

## License

This project is created for educational purposes. All rights reserved.

---

*Note: This is an educational simulation and is not affiliated with Netflix.*