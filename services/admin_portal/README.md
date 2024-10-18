# SecureSentinelBankAngularFrontend

## Description
Secure Sentinel Bank's Administrator Portal.  Accessible only by Users with Admin authorization.

## Table of Contents

- [Badges](#badges)
- [Visuals](#visuals)
- [Installation](#installation)
- [Usage](#usage)
- [Support](#support)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Authors and Acknowledgment](#authors-and-acknowledgment)
- [License](#license)
- [Project Status](#project-status)

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

```md
### Dockerfile.jenkins Commands:

 BUILD THE IMAGE
 docker build -t jenkins-server -f Dockerfile.jenkins .

 RUN AS ADMIN
 docker run -d --name jenkins -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock jenkins-server
 OR
 docker run -it --name ssb-jenkins-server -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v jenkins_home:/var/jenkins_home jenkins-server

 RUN WITH SONARQUBE 
 docker run -it --name ssb-jenkins-server -p 8080:8080 -p 50000:50000 \
     -v /var/run/docker.sock:/var/run/docker.sock \
     -v jenkins_home:/var/jenkins_home \
     -e SONAR_HOST_URL="http://localhost:9000" \
     -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=YOUR_PROJECT_KEY" \
     -e SONAR_TOKEN="myAuthenticationToken" \
     -v "/path/to/your/repo:/usr/src" \
     jenkins-server
```

```md
### Dockerfile.sonarqube Commands:

 docker build --tag=sonarqube-custom .
 docker run -ti sonarqube-custom

 DOCKER PULL
 (by default, the image will use an embedded H2 database that is not suited for production)
 docker pull sonarqube

 SONARQUBE SERVER FROM DOCKER
 docker volume create --name sonarqube_data
 docker volume create --name sonarqube_logs
 docker volume create --name sonarqube_extensions

 START THE SERVER CONTAINER WITH DB ENV VARIABLES
 docker run -d --name ssb-sonarqube-server \
   -p 9000:9000 \
   -e SONAR_JDBC_URL=... \
   -e SONAR_JDBC_USERNAME=... \
   -e SONAR_JDBC_PASSWORD=... \
   -v sonarqube_data:/opt/sonarqube/data \
   -v sonarqube_extensions:/opt/sonarqube/extensions \
   -v sonarqube_logs:/opt/sonarqube/logs \
   sonarqube

 RUN SONARQUBE WITH H2 DB
 docker run -d --name ssb-sonarqube-server \
   -p 9000:9000 \
   -v sonarqube_data:/opt/sonarqube/data \
   -v sonarqube_extensions:/opt/sonarqube/extensions \
   -v sonarqube_logs:/opt/sonarqube/logs \
   sonarqube

 SonarQube needs a Personal Access Token to report the Quality Gate status on Merge Requests in GitLab. To create this token, we recommend using a dedicated GitLab account with Reporter permission to all target projects. The token itself needs the api scope. 

 SONARQUBE
 docker run \
     --rm \
     -e SONAR_HOST_URL="http://${SONARQUBE_URL}" \
     -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=${YOUR_PROJECT_KEY}" \
     -e SONAR_TOKEN="myAuthenticationToken" \
     -v "${YOUR_REPO}:/usr/src" \
     sonarsource/sonar-scanner-cli
```

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and Acknowledgment

### The Ledger Legends:
- [Eric Kirberger](https://ekirbs.github.io/react-portfolio/ 'The portfolio of Eric Kirberger.  Made with React.js.')

## License
For open source projects, say how it is licensed.

## Project Status
We are still in acvtive development.


