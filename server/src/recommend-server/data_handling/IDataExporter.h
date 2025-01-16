#ifndef I_DATA_EXPORTER_H
#define I_DATA_EXPORTER_H

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <unordered_set>

class MovieDatabase;

// Interface for exporting information from the Movie Database
class IDataExporter {
public:
    virtual ~IDataExporter() = default;
    virtual void addUser (int userId, std::unordered_set<int> movies) = 0;
    virtual void addMoviesToUser(int userId, std::unordered_set<int> movies) = 0;
    virtual void deleteMoviesFromUser(int userId, std::unordered_set<int> movieIds) = 0;
};

#endif // I_DATA_EXPORTER_H