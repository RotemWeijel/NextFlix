const net = require('net');
const mongoose = require('mongoose');
const idMappingService = require('./idMapping');
const Movie = require('../models/movies');
const User = require('../models/user');

class RecommendationClient {
    constructor(host, port) {
        this.host = (host === 'localhost' || host === 'host.docker.internal') 
            ? '0.0.0.0'  // Listen on all network interfaces
            : host;
        this.port = port;
        this.existingUsers = new Set();
    }

    async sendCommand(command, ...args) {
        return new Promise((resolve, reject) => {

            const client = new net.Socket();
            let timeout = setTimeout(() => {
                client.destroy();

                reject(new Error('Connection timeout'));
            }, 5000);
        
            client.connect(this.port, this.host, () => {        
                const cmdString = `${command} ${args.join(' ')}\n`;
                client.write(cmdString);
            });
        
            let data = '';
            client.on('data', (chunk) => {
                data += chunk;
                clearTimeout(timeout);
                client.end();
                    
                const responseData = data.trim();
                if (data.includes('200') || data.includes('201') || data.includes('204')) {
                    resolve({ success: true, data: responseData });
                } else {
                    resolve({ success: false, error: responseData });
                }
            });
        
            client.on('error', (err) => {
                clearTimeout(timeout);
                reject(err);
            });
        });
    }
    
    async addWatchedMovie(userId, movieId) {
        try {
            const movie = await Movie.findById(movieId);
            if (!movie) throw new Error("Movie not found");
    
            const user = await User.findById(userId);
            if (!user) throw new Error("User not found");
                
            const intUserId = await idMappingService.getOrCreateIntId(userId, 'user');
            const intMovieId = await idMappingService.getOrCreateIntId(movieId, 'movie');
                
            const command = this.existingUsers.has(intUserId) ? 'PATCH' : 'POST';
            const response = await this.sendCommand(command, intUserId, intMovieId);
                
            if (response.success || (command === 'POST' && response.error.includes('404'))) {
                if (command === 'POST') {
                    this.existingUsers.add(intUserId);
                }
    
                await Promise.all([
                    Movie.findByIdAndUpdate(movieId, 
                        { $push: { watchedBy: { userId, watchedAt: new Date() } } }
                    ),
                    User.findByIdAndUpdate(userId, 
                        { $push: { watchedMovies: movieId } }
                    )
                ]);
                    
                return { success: true };
            }
                
            return { success: false, error: response.error };
        } catch (error) {
            return { success: false, error: error.message };
        }
    }
    
    async getRecommendations(userId, movieId) {
        try {
            const intUserId = await idMappingService.getOrCreateIntId(userId, 'user');
            const intMovieId = await idMappingService.getOrCreateIntId(movieId, 'movie');
                
            const response = await this.sendCommand('GET', intUserId, intMovieId);
            if (!response.success) {
                return { success: false, data: [], error: response.error };
            }
    
            if (response.data.trim() === '200 Ok') {
                return { success: true, data: [] };
            }
    
            const recommendationStr = response.data.split('\n\n')[1];
            if (!recommendationStr) {
                return { success: true, data: [] };
            }
    
            const recommendations = recommendationStr
                .trim()
                .split(' ')
                .filter(id => id)
                .map(Number);
    
            const mongoIds = await Promise.all(
                recommendations.map(recId => 
                    idMappingService.getMongoId(recId, 'movie')
                )
            );
                
            return { success: true, data: mongoIds };
        } catch (error) {
            return { success: false, error: error.message, data: [] };
        }
    }
}

module.exports = RecommendationClient;