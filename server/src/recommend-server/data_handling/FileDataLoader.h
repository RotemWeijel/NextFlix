#ifndef FILE_DATA_LOADER_H
#define FILE_DATA_LOADER_H

#include "IDataLoader.h"
#include <string>

// A class that implements IDataLoader, for reading from files
class FileDataLoader : public IDataLoader {
public:
    explicit FileDataLoader(const std::string& directoryPath);
    void load(MovieDatabase& database) const override;

private:
    std::string directoryPath_;

    // Add these private methods to the header
    void loadUserFile(const std::string& filepath, MovieDatabase& database) const;
    void loadMovieFile(const std::string& filepath, MovieDatabase& database) const;
};

#endif // FILE_DATA_LOADER_H