### Do before the workshop
Before the workshops please start up the docker compose in order to download container images and initialize the cluster. 
This way we will save time during the training. You can stop the cluster afterwards. 

#### How to

In order to start please run this command from the root of this repository
```
docker-compose -f src/main/docker/docker-compose.yml up -d
```
To check the logs, please run
```
docker-compose -f src/main/docker/docker-compose.yml logs -f
```
If you want to stop, please run
```
docker-compose -f  src/main/docker/docker-compose.yml stop
```
If you want to start it again, please run
```
docker-compose -f  src/main/docker/docker-compose.yml start
```
And if you want to clean up, please run
```
docker-compose -f  src/main/docker/docker-compose.yml down
```
### In case of problems

 * If you want to start the cluster after it was stopped it might happen that due to parts of cluster not being yet ready
to serve the traffic some other services will start shutting down. In such case please try starting (the start command)
the cluster again (without stopping it). 
* If you are running Docker in a VM, make sure it has available at least 4 GB of RAM.
