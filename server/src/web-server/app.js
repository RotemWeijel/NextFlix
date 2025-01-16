const express = require('express');
const mongoose = require('mongoose');
const userRoutes = require('./routes/users');
const tokenRoutes = require('./routes/tokens');
const movieRoutes = require('./routes/movies');
const categoryRoutes = require('./routes/categories');

// Initialize express app
const app = express();

// Middleware
app.use(express.json());  // Parse JSON bodies
app.use(express.urlencoded({ extended: true }));  // Parse URL-encoded bodies

// CORS middleware
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, PATCH');
    if (req.method === 'OPTIONS') {
        return res.sendStatus(200);
    }
    next();
});

// Connect to MongoDB
mongoose.connect('mongodb://host.docker.internal:27017/netflix-server', {
    serverSelectionTimeoutMS: 5000
})
.then(() => {
    //console.log('Connected to MongoDB');
})
.catch(err => {
    console.error('MongoDB connection error:', err);
    process.exit(1);
});

// Routes
app.use('/api/users', userRoutes);
app.use('/api/tokens', tokenRoutes);
app.use('/api/movies', movieRoutes);
app.use('/api/categories', categoryRoutes);


// Error handling middleware
app.use((err, req, res, next) => {
    console.error('Error occurred:', err);
    console.error('Stack trace:', err.stack);
    res.status(500).json({
        success: false,
        error: 'Internal Server Error'
    });
});

// 404 handler
app.use((req, res) => {
    res.status(404).json({
        success: false,
        error: 'Not Found'
    });
});

// Start server
// const PORT = process.env.PORT || 3000;
// app.listen(PORT, () => {
// });

module.exports = app;