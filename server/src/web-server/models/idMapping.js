const mongoose = require('mongoose');

const mappingSchema = new mongoose.Schema({
    mongoId: { 
        type: mongoose.Schema.Types.ObjectId, 
        required: true 
    },
    intId: { 
        type: Number, 
        required: true 
    },
    type: { 
        type: String, 
        enum: ['user', 'movie'], 
        required: true 
    }
});

// Ensure unique indexes
mappingSchema.index({ mongoId: 1, type: 1 }, { unique: true });
mappingSchema.index({ intId: 1, type: 1 }, { unique: true });

const IdMapping = mongoose.model('IdMapping', mappingSchema);

module.exports = IdMapping;