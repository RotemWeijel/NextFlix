FROM gcc:latest

# Install Node.js and npm
RUN apt-get update && apt-get install -y \
    cmake \
    nodejs \
    npm \
    && rm -rf /var/lib/apt/lists/*

# Copy the entire project
COPY . /usr/src/netflix

# Set working directory
WORKDIR /usr/src/netflix

# Build the C++ recommendation server
RUN mkdir -p build
WORKDIR /usr/src/netflix/build
RUN cmake .. -DCMAKE_BUILD_TYPE=Debug && make

# Create data directory
RUN mkdir -p /usr/src/netflix/data

# Copy recommendation server executable
RUN cp NetflixServer /usr/local/bin/

# Set up Node.js web server
WORKDIR /usr/src/netflix/src/web-server
RUN npm install

# Copy the run script
COPY run.sh /usr/local/bin/run.sh
RUN sed -i 's/\r$//' /usr/local/bin/run.sh
RUN chmod +x /usr/local/bin/run.sh

WORKDIR /usr/src/netflix

ENTRYPOINT ["/usr/local/bin/run.sh"]
CMD []