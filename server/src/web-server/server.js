require('dotenv').config();  // Load environment variables
const app = require('./app');
const RecommendationClient = require('./services/recommend');

// Get port from command line arguments or environment variables
const port = process.argv[2] || process.env.PORT || 3000;
const recommendPort = process.argv[3] || process.env.RECOMMENDATION_SERVER_PORT || 6000;

// Explicitly override the host if it's localhost
if (process.env.RECOMMENDATION_SERVER_HOST === 'localhost') {
    process.env.RECOMMENDATION_SERVER_HOST = 'host.docker.internal';
}

// Recommendation server configuration
global.recommendationClient = new RecommendationClient(
    process.env.RECOMMENDATION_SERVER_HOST || 'host.docker.internal',
    parseInt(recommendPort)
);

// Start server
app.listen(port, '0.0.0.0');

// Uncaught exception handler
process.on('uncaughtException', (err) => {
    console.error('Uncaught Exception:', err);
    process.exit(1);
});

// Unhandled rejection handler
process.on('unhandledRejection', (err) => {
    console.error('Unhandled Rejection:', err);
    process.exit(1);
});