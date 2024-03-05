# Centralized Docker Compose Setup

This repository provides a centralized Docker Compose setup for running all of our microservices and portals.


## Getting Started
### 1. Clone the Repository

Clone this repository to your local machine using the `--recurse-submodules` flag to ensure all submodules are also cloned:

```bash
git clone --recurse-submodules <repository-url>
````

If you cloned without `--recurse-submodules` run the following commands:
```bash
cd centralized-docker-compose
git submodule update --init --recursive
```


### 2. Running the docker-compose
Navigate to the root directory and run the following command to
build and start all services:
```docker-compose up```
or if you made changes to images:
```docker-compose up --build```

### 3. Updating Submodules

To update the submodules to their latest commits, run:
```bash
git submodule update --remote
```


# Contributers
@Nicholas Csaszar