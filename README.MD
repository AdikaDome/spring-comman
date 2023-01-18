This microservice is upgraded to 2.0.0 as this contains major updates.

1. The following modules in this service prior to 2.0.0 upgrade are removed as this microservice was decommissioned
    1. spring-common-client
    2. spring-common-core

2. Following updates are made in 2.0.0 version
    1. Added spring-common-utils module. This contains business logic validators, which were part of [spring-rest](https://bitbucket.nextgen.com/projects/MFPLAT/repos/spring-rest/browse) initially
    2. Added spring-common-web module. This contains RequestID Generator, Logging Filters and Global Exception Handlers that any web service could use