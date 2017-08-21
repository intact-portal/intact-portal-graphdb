# IntAct GraphDB

```
docker run --publish=7474:7474  --publish=7687:7687 --volume=/Users/ntoro/Downloads/root/neo4j/data:/data --ulimit=nofile=40000:40000 --env=NEO4J_CACHE_MEMORY=4G  neo4j:3.1.0
```