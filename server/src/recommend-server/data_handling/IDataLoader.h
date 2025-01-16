#ifndef I_DATA_LOADER_H
#define I_DATA_LOADER_H

class MovieDatabase;

// Interface for loading information and writing it into the Movie Database
class IDataLoader {
public:
    virtual ~IDataLoader() = default;
    virtual void load(MovieDatabase& database) const = 0;
};

#endif // I_DATA_LOADER_H
