# inzynierka

<h1> Running keycloak locally.</h1>

<h3> Before you start </h3>
Make sure you have OpenJDK 17 installed.

<h3> Download Keycloak </h3>

1. Download keycloak server: https://www.keycloak.org/downloads . Server -> Keycloak -> ZIP
2. Go to Downloads on your machine and extract downloaded zip somewhere.

<h3> Set keycloak server port to 8090 </h3>

1. Go to extracted keycloak directory. Go to /conf directory and edit keycloak.conf
2. Add this property <b>http-port=8090</b> inside keycloak.conf

<h3> Start Keycloak </h3>

1. From a terminal, navigate to the keycloak-22.0.5 directory.
2. Enter the following command:
   - On Linux, run: <br>`bin/kc.sh start-dev`
   - On Windows, run: <br>`bin\kc.bat start-dev`

<h3> Create an admin user </h3>

1. Open 
2. Fill in the form with your preferred username and password.

<h3> Log in to the Admin Console </h3>
   
1. Go to the [Keycloak Admin Console.](http://localhost:8090/admin)
2. Log in with the username and password you created earlier.

<h3> Create a realm </h3>

1. [Download ApartManager realm config](./infrastructure/keycloak/realm-export.json)
2. Go to the [Keycloak Admin Console.](http://localhost:8090/admin)
3. Click the word <b>master</b> in the top-left corner, then click <b>Create Realm.</b>
4. Upload downloaded json file from step 1 and click create. 
