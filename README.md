# NextFlix

https://github.com/RotemWeijel/Ex4.git

NextFlix is an educational project that simulates the Netflix streaming platform, demonstrating modern software architecture and best practices across web and mobile platforms.

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

### Setup & Installation

The setup instructions have been moved to the project wiki for detailed steps on:
- Environment setup
- Building the project
- Running the applications
- Testing procedures

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