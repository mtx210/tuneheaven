# Prerequisites
While developing this project I realised I should never have used Hibernate here,
for future notice - JDBC all the way.
Anyway it's far from finished. There are a lot of things to clarify when it comes 
to business logic:
- would import file dir contain only files to import?
- would there be only one at a time? the correct one to import?
- are these files supposed to be archived in some way?
- would import file's structure and data be guaranteed to be correct?
- are the ratings unique per user, can user change rating, can he rate 
multiple times?

Nonetheless, project still lacks any unit or integration tests !!!
Core functionalities still require work due to Hibernate's nature - it
should all be deleted in redone in JDBC with all the queries and result mapping.
There is no proper validation for import file, there is no docker compose setup
for easy local running with database setup with its structure and data to 
play with. Probably could add swagger api documentation and some db structure
graph just for the sake of it. Probably more things to come up with when
doing finishing touches.