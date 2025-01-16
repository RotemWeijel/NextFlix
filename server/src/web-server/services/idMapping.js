const mongoose = require('mongoose');

const IdMapping = require('../models/idMapping');
const Movie = require('../models/movies');

class IdMappingService {
    async getOrCreateIntId(mongoId, type) {
        let mapping = await IdMapping.findOne({ mongoId, type });
        if (!mapping) {
            // Find max intId and increment
            const maxMapping = await IdMapping.findOne({ type })
                .sort({ intId: -1 });
            const nextId = maxMapping ? maxMapping.intId + 1 : 1;
            
            mapping = await IdMapping.create({
                mongoId,
                intId: nextId,
                type
            });
        }
        return mapping.intId;
    }

    async getMongoId(intId, type) {
        const mapping = await IdMapping.findOne({ intId, type });
        return mapping ? mapping.mongoId : null;
    }
}

module.exports = new IdMappingService();