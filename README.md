# inzynierka

<h1> Running locally.</h1>

1. Run `docker-compose up` in directory `infrastructure/apartmanager-db/docker`.
2. Go [keyloak admin console](http://localhost:8090/admin) and login whith these credentials: 
   - username: `admin` 
   - password: `password`
3. Click 'master' on top left corner and choose <b>ApartManager</b>.
4. Go to Users option and Add user with following username: `admin` and select <i><b>Email verified</b></i>. 
5. Click on new admin user and go to <i><b>Credentials</b></i>. Create password: `admin`.
6. Go to <i><b>Role mapping</b></i> -> <i><b>Assign role</b></i>. Assign all roles to this user. (change filtering).

<h1> Test Backend from postman</h1>
To make request from postman(or any other tool) you need to pass token in authorization header.

<h3> Import keycloak APIs collection </h3>

1. [Download keycloak collection](./infrastructure/postman-collections/keycloak-server/Keycloak_Apartmanager.postman_collection.json)
2. Open postman and import downloaded collection.

<h3> Obtain token </h3>

1. Make token request(ensure that user in request body exists in ApartManager realm. if no - add one).
2. Copy token from response. 
3. Add this token for every request to backend you make. Authorization -> Type: Bearer Token