# CommunityPlus
Community Plus is a web application where users can post their problems or their need of any service or assistance.  The users will have to select a domain (ex. Medical, Automobile, Household, etc.) in which they are willing to provide help while registering themselves to Community Plus. When a user publishes a new post, the backend logic of Community Plus sends an email about this new post as a notification to all other users who has the same help domain preference with that of the domain of the subject of the post. Community Plus uses a chronological feed to display all the posts with the most recent posts being at the top.

Steps to be followed to run Community Plus on your system:
•	Download this repository.

•	Create a new folder in your NetbeansProject workspace and name it CommunityPlus

•	Import all sub folders and files in this newly created CommunityPlus folder and open netbeans IDE.

•	Resolve the jar files dependencies by adding them in library, if required.

•	Configure the database service. Create two tables using below queries:

o	To create the userinfo table:

create table "userinfo" 
(
id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
username varchar(32) NOT NULL,
email varchar(32) primary key NOT NULL,
helpdomain varchar(32) NOT NULL,
city varchar(32) NOT NULL,
password varchar(32) NOT NULL,
mobile BIGINT NOT NULL UNIQUE,
age INT NOT NULL
);




o	To create the postinfo table:

create table "postinfo" 
(
id int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
username varchar(32) NOT NULL,
email varchar(32) NOT NULL,
helpdomain varchar(32) NOT NULL,
location varchar(32) NOT NULL,
subject varchar(300) NOT NULL,
mobile BIGINT NOT NULL,
pdate date NOT NULL
);

•	Update the values of servlet context parameters in deployment descriptor (web.xml).

•	Run the project (Community Plus).


Note: This project has been made on Windows system and expected to run in any other windows system.  
