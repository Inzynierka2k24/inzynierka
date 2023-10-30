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
