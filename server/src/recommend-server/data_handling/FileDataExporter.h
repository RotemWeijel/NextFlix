#ifndef FILE_DATA_EXPORTER_H
#define FILE_DATA_EXPORTER_H

#include "IDataExporter.h"
#include "../main_body/MovieDatabase.h"
#include <string>

// A class that implements IDataExporter, for writing to files
class FileDataExporter : public IDataExporter {
public:
    explicit FileDataExporter(const std::string& filePath);
    void addUser (int userId, std::unordered_set<int> movies) override;
    void addMoviesToUser(int userId, std::unordered_set<int> movies) override;
    void deleteMoviesFromUser(int userId, std::unordered_set<int> movieIds) override;
    
private:
    std::string filePath_;
};

#endif // FILE_DATA_EXPORTER_H
